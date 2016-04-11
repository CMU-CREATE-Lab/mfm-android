package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import org.cmucreatelab.mfm_android.activities.LoginActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.DbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.GroupDbHelper;
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
        //mfmRequestHandler.requestListGroups();
        mfmRequestHandler.requestListStudents(login);
        //login.populateStudentAndGroupSuccess();
    }

    public void refreshGroups(final LoginActivity login) {
        mfmRequestHandler.requestListGroups(login);
    }


    public void checkAndUpdateStudents(final LoginActivity login, ArrayList<Student> students) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            // TODO we want to compare updatedAt string with objects sharing IDs. If they need updated, add/update them.
            // For now, just clear and add all
            School school = mfmLoginHandler.getSchool();
            school.getStudents().clear();
            //school.getStudents().addAll(students);
            for (Student student : students) {
                mfmRequestHandler.updateStudent(student);
            }
            login.populateStudentsSuccess();
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateStudents with Kiosk not logged in");
        }
    }


    public void checkAndUpdateGroups(final LoginActivity login, ArrayList<Group> groups) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            school.getGroups().clear();
            //school.getGroups().addAll(groups);
            for (Group group : groups) {
                mfmRequestHandler.updateGroup(group);
            }
            login.populateGroupsSuccess();
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateGroups with Kiosk not logged in");
        }
    }


    public Student getStudentByID(int id) {
        Student result = null;

        if (mfmLoginHandler.kioskIsLoggedIn) {
            ArrayList<Student> students = mfmLoginHandler.getSchool().getStudents();
            for (int i = 0; i < students.size(); i++) {
                Student test = students.get(i);
                if (id == test.getId()) {
                    result = test;
                }
            }
        } else {
            Log.e(Constants.LOG_TAG, "Tried to getStudentByID with Kiosk not logged in");
        }

        return result;
    }


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
        // TODO load from database, then compare with HTTP request and perform necessary updates
        //DbHelper.loadFromDb(appContext);
    }

}
