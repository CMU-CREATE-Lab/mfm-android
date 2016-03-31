package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.util.Log;
import org.cmucreatelab.mfm_android.activities.LoginActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
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
        this.mfmRequestHandler = new MfmRequestHandler(this);
        this.mfmLoginHandler = new MfmLoginHandler(this);
        this.sessionHandler = new SessionHandler(this);
        // TODO request OS version, and append it to kiosk attribute
        Kiosk.ioSVersion += "4.5";
        // TODO sessions will need to be created when you select a student or group; for now this is just created to avoid null pointer
        this.sessionHandler.startSession(new Student());

        // load from database
        ArrayList<Group> dbGroups = GroupDbHelper.fetchFromDatabase(ctx);
        ArrayList<Student> dbStudents = StudentDbHelper.fetchFromDatabase(ctx);
        ArrayList<User> dbUsers = UserDbHelper.fetchFromDatabase(ctx);
        // I am not sure if this is how we want to access elements in the database
        ArrayList<ArrayList<Integer>> dbStudentsInGroups = new ArrayList<>();

        for (Group group : dbGroups) {
            // add the student ids for each group
            ArrayList<Integer> dbStudentIdsFromGroup = StudentGroupDbHelper.fetchStudentsFromGroup(ctx, group);
            dbStudentsInGroups.add(dbStudentIdsFromGroup);
            // TODO add group readings
        }
        for (Student student : dbStudents) {
            // TODO add student readings
        }
        for (User user : dbUsers) {
            // TODO add user readings
        }
        for (ArrayList<Integer> studentIds : dbStudentsInGroups) {
            // TODO add studentId readings
            Log.i(Constants.LOG_TAG, studentIds.get(0).toString());
        }

    }


    // Handler attributes and methods


    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    public MfmRequestHandler mfmRequestHandler;
    public MfmLoginHandler mfmLoginHandler;
    public SessionHandler sessionHandler;


    public void refreshStudentsAndGroups(LoginActivity login) {
        // TODO change how username and password is entered
        mfmRequestHandler.requestListSchools("stevefulton", "stevefulton");
        mfmRequestHandler.requestListGroups();
        mfmRequestHandler.requestListStudents();
        mfmRequestHandler.ping();
        login.populateStudentAndGroupSuccess();
    }


    public void checkAndUpdateStudents(ArrayList<Student> students) {
        // TODO we want to compare updatedAt string with objects sharing IDs. If they need updated, add/update them.
        // For now, just clear and add all
        // TODO handle when school is null
        School school = mfmLoginHandler.getSchool();
        school.getStudents().clear();
        school.getStudents().addAll(students);
        for (Student student : students) {
            mfmRequestHandler.updateStudent(student);
        }
    }


    public void checkAndUpdateGroups(ArrayList<Group> groups) {
        // TODO handle when school is null
        School school = mfmLoginHandler.getSchool();
        school.getGroups().clear();
        school.getGroups().addAll(groups);
        for (Group group : groups) {
            mfmRequestHandler.updateGroup(group);
        }
    }


    public Student getStudentByID(int id) {
        Student result = null;

        // TODO handle when school is null
        ArrayList<Student> students = mfmLoginHandler.getSchool().getStudents();
        for (int i = 0; i < students.size(); i++) {
            Student test = students.get(i);
            if (id == test.getId()) {
                result = test;
            }
        }

        return result;
    }

}
