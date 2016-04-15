package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import org.cmucreatelab.mfm_android.activities.LoginActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.ListHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.DbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.GroupDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.MessageFromMeSQLLiteOpenHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentContract;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentGroupDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.UserDbHelper;
import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 *
 * GlobalHandler
 *
 * Singleton object for storing application data structures.
 *
 */
public class GlobalHandler {

    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    public MfmRequestHandler mfmRequestHandler;
    public MfmLoginHandler mfmLoginHandler;
    public SessionHandler sessionHandler;


    public void refreshStudents(final LoginActivity login) {
        mfmRequestHandler.requestListStudents(login);
    }

    public void refreshGroups(final LoginActivity login) {
        mfmRequestHandler.requestListGroups(login);
    }


    public void checkAndUpdateStudents(final LoginActivity login, ArrayList<Student> studentsFromMfmRequest) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            ArrayList<Student> studentsFromDB = school.getStudents();

            for (Student mfmStudent : studentsFromMfmRequest) {
                try {
                    Student dbStudent = ListHelper.findStudentWithId(studentsFromDB, mfmStudent.getId());
                    // should we compare based off of some threshold?
                    // does the api only update at a fixed interval of time?
                    if (!dbStudent.getUpdatedAt().equals(mfmStudent.getUpdatedAt())) {
                        mfmRequestHandler.updateStudent(dbStudent);
                    }
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "No student found in the database that matched the mfmRequest. Adding to database");
                    school.addStudent(mfmStudent);
                    DbHelper.addToDatabase(appContext, mfmStudent);
                    mfmRequestHandler.updateStudent(mfmStudent); // still calling this to populate the rest of the attributes
                }
            }
            login.populateStudentsSuccess();
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateStudents with Kiosk not logged in");
        }
    }


    public void checkAndUpdateGroups(final LoginActivity login, ArrayList<Group> groupsFromMfmRequest) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            ArrayList<Group> groupsFromDB = school.getGroups();
            for (Group mfmGroup : groupsFromMfmRequest) {
                try {
                    Group dbGroup = ListHelper.findGroupWithId(groupsFromDB, mfmGroup.getId());
                    if (!dbGroup.getUpdatedAt().equals(mfmGroup.getUpdatedAt())) {
                        mfmRequestHandler.updateGroup(dbGroup);
                    }
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "No student found in the database that matched the mfmRequest. Adding to database");
                    school.addGroup(mfmGroup);
                    DbHelper.addToDatabase(appContext, mfmGroup);
                    mfmRequestHandler.updateGroup(mfmGroup);
                }
            }
            login.populateGroupsSuccess();
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateGroups with Kiosk not logged in");
        }
    }


    // We probably do not need this method anymore
    public void addStudentsAndGroupsToDatabase() {
        Log.i(Constants.LOG_TAG, mfmLoginHandler.getSchool().toString());
        ArrayList<Group> groups = mfmLoginHandler.getSchool().getGroups();
        ArrayList<Student> students = mfmLoginHandler.getSchool().getStudents();

        Log.v(Constants.LOG_TAG, groups.toString());
        Log.v(Constants.LOG_TAG, students.toString());

        // groups and students in each of those groups
        for (Group group : groups) {
            DbHelper.addToDatabase(appContext, group);
        }
        // students and users for each of those students
        for (Student student : students) {
            DbHelper.addToDatabase(appContext, student);
        }

    }


    // Singleton Implementation


    private static GlobalHandler classInstance;
    protected Context appContext;


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.httpRequestHandler = new HttpRequestHandler(this);
        this.mfmLoginHandler = new MfmLoginHandler(this);
        this.mfmRequestHandler = new MfmRequestHandler(this);
        this.sessionHandler = new SessionHandler(this);
        Kiosk.ioSVersion = Build.VERSION.RELEASE;
        // TODO sessions will need to be created when you select a student or group; for now this is just created to avoid null pointer
        this.sessionHandler.startSession(new Student());
    }

}
