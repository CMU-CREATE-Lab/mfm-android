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


    public void refreshStudentsAndGroups() {
        mfmRequestHandler.requestListStudents();
        mfmRequestHandler.requestListGroups();
    }


    public void checkAndUpdateStudents(ArrayList<Student> studentsFromMfmRequest) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            ArrayList<Student> studentsFromDB = school.getStudents();

            for (Student mfmStudent : studentsFromMfmRequest) {
                try {
                    Student dbStudent = ListHelper.findStudentWithId(studentsFromDB, mfmStudent.getId());
                    if (!dbStudent.getUpdatedAt().equals(mfmStudent.getUpdatedAt())) {
                        mfmRequestHandler.updateStudent(dbStudent);
                    }
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "No student found in the database that matched the mfmRequest. Adding to database");
                    school.addStudent(mfmStudent);
                    DbHelper.addToDatabase(appContext, mfmStudent);
                    mfmRequestHandler.updateStudent(mfmStudent);
                }
            }
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateStudents with Kiosk not logged in");
        }
    }


    public void checkAndUpdateGroups(ArrayList<Group> groupsFromMfmRequest) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            ArrayList<Group> groupsFromDB = school.getGroups();
            for (Group mfmGroup : groupsFromMfmRequest) {
                try {
                    Group dbGroup = ListHelper.findGroupWithId(groupsFromDB, mfmGroup.getId());
                    Log.i(Constants.LOG_TAG, dbGroup.getUpdatedAt() + " " + mfmGroup.getUpdatedAt());
                    if (!dbGroup.getUpdatedAt().equals(mfmGroup.getUpdatedAt())) {
                        mfmRequestHandler.updateGroup(dbGroup);
                    }
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "No group found in the database that matched the mfmRequest. Adding to database");
                    school.addGroup(mfmGroup);
                    DbHelper.addToDatabase(appContext, mfmGroup);
                    mfmRequestHandler.updateGroup(mfmGroup);
                }
            }
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateGroups with Kiosk not logged in");
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
