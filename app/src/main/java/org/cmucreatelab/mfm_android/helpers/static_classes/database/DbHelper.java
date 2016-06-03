package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.MfmLoginHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 4/11/16.
 */
public class DbHelper {


    public static void clearAll(GlobalHandler globalHandler) {
        ArrayList<Student> students = globalHandler.mfmLoginHandler.getSchool().getStudents();
        ArrayList<Group> groups = globalHandler.mfmLoginHandler.getSchool().getGroups();

        for (Student student : students) {
            UserDbHelper.destroyAllFromStudent(globalHandler.appContext, student.getId());
            StudentGroupDbHelper.destroyAllFromStudent(globalHandler.appContext, student.getId());
            StudentDbHelper.destroy(globalHandler.appContext, student);
        }

        for (Group group : groups) {
            StudentGroupDbHelper.destroyAllFromStudent(globalHandler.appContext, group.getId());
            GroupDbHelper.destroy(globalHandler.appContext, group);
        }
    }


    public static void addToDatabase(Context context, Sender sender) {
        switch(sender.getSenderType()) {
            case group:
                GroupDbHelper.addToDatabase(context, (Group) sender);
                break;
            case student:
                StudentDbHelper.addToDatabase(context, (Student) sender);
                break;
            default:
                Log.e(Constants.LOG_TAG, "Invalid SenderType in DbHelper.addToDatabase");
        }
    }


    public static void update(Context context, Sender sender) {
        switch(sender.getSenderType()) {
            case group:
                GroupDbHelper.update(context, (Group) sender);
                break;
            case student:
                StudentDbHelper.update(context, (Student) sender);
                break;
            default:
                Log.e(Constants.LOG_TAG, "Invalid SenderType in DbHelper.update");
        }
    }


    public static void loadFromDb(Context context) {
        MfmLoginHandler mfmLoginHandler = GlobalHandler.getInstance(context).mfmLoginHandler;
        if (mfmLoginHandler.kioskIsLoggedIn) {
            // ASSERT: the students/groups list in school is empty
            School school = mfmLoginHandler.getSchool();
            ArrayList<Student> dbStudents;
            ArrayList<Group> dbGroups;

            dbStudents = StudentDbHelper.fetchFromDatabase(context);
            for (Student student : dbStudents) {
                school.addStudent(student);
            }
            dbGroups = GroupDbHelper.fetchFromDatabaseWithStudents(context, dbStudents);
            for (Group group : dbGroups) {
                school.addGroup(group);
            }
        } else {
            Log.e(Constants.LOG_TAG, "trying to load from database but Kiosk is not logged in.");
        }
    }

}
