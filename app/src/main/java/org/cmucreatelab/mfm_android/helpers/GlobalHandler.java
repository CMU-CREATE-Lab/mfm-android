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
import org.cmucreatelab.mfm_android.helpers.readings.ReadingsHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.GroupDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentGroupDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.UserDbHelper;
import java.util.ArrayList;
import java.util.Date;

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
    public ReadingsHandler readingsHandler;

    // This previously was a protected field but I changed it to public because I needed access to it so frequently
    // and saw no reason why it shouldn't be public. I may be missing though.
    public Context appContext;


    public void refreshStudentsAndGroups(LoginActivity login) {
        this.readingsHandler.populateDatabase();
        login.populateStudentAndGroupSuccess();
    }


    public void checkAndUpdateStudents(ArrayList<Student> students) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            // TODO we want to compare updatedAt string with objects sharing IDs. If they need updated, add/update them.
            // For now, just clear and add all
            School school = mfmLoginHandler.getSchool();
            school.getStudents().clear();
            school.getStudents().addAll(students);
            for (Student student : students) {
                mfmRequestHandler.updateStudent(student);
            }
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateStudents with Kiosk not logged in");
        }
    }


    public void checkAndUpdateGroups(ArrayList<Group> groups) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            school.getGroups().clear();
            school.getGroups().addAll(groups);
            for (Group group : groups) {
                mfmRequestHandler.updateGroup(group);
            }
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

    public void updateReadings(){
        readingsHandler.updateGroups();
        readingsHandler.updateStudents();
    }


    // Singleton Implementation


    private static GlobalHandler classInstance;


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
        this.mfmRequestHandler = new MfmRequestHandler(this);
        this.mfmLoginHandler = new MfmLoginHandler(this);
        this.sessionHandler = new SessionHandler(this);
        Kiosk.ioSVersion = Build.VERSION.RELEASE;
        // TODO sessions will need to be created when you select a student or group; for now this is just created to avoid null pointer
        this.sessionHandler.startSession(new Student());
        this.readingsHandler = ReadingsHandler.newInstance(this);

        // load from database
        ArrayList<Group> dbGroups = GroupDbHelper.fetchFromDatabase(ctx);
        ArrayList<Student> dbStudents = StudentDbHelper.fetchFromDatabase(ctx);
        ArrayList<User> dbUsers = UserDbHelper.fetchFromDatabase(ctx);

        Log.v(Constants.LOG_TAG, dbGroups.toString());
        Log.v(Constants.LOG_TAG, dbStudents.toString());
        Log.v(Constants.LOG_TAG, dbUsers.toString());

        // I left out students in groups because adding a reading of a group takes care of that.
        for (Group group : dbGroups) {
            // TODO add group readings
            readingsHandler.addReading(group);
        }
        for (Student student : dbStudents) {
            // TODO add student readings
            readingsHandler.addReading(student);
        }
        for (User user : dbUsers) {
            // TODO add user readings
            readingsHandler.addReading(user);
        }
    }

}
