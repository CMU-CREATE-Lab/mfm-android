package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.School;
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


    // TODO this is just the test code I wrote to test out the database; it will probably be deleted in the next refactor
    public static void testDatabase(Context context) {
        // test data
        Group group = new Group();
        group.setId(1);
        group.setName("Group A");
        Student student1 = new Student();
        student1.setId(101);
        student1.setFirstName("Alice");
        student1.setLastName("Smith");
        Student student2 = new Student();
        student2.setId(102);
        student2.setFirstName("Bob");
        student2.setLastName("Smith");
        Student student3 = new Student();
        student3.setId(103);
        student3.setFirstName("Charlie");
        student3.setLastName("Smith");
        User user = new User();
        user.setStudent(student1);
        user.setFirstName("Some");
        user.setLastName("User");
        user.setStudentUserRole("Alice's Parent");
        user.setId(201);
        ArrayList<Student> groupStudents = new ArrayList<>();
        groupStudents.add(student1);
        groupStudents.add(student2);
        group.setStudents(groupStudents);
        ArrayList<User> studentUsers = new ArrayList<>();
        studentUsers.add(user);
        student1.setUsers(studentUsers);
        ArrayList<Group> flatGroups = new ArrayList<>();
        ArrayList<Student> flatStudents= new ArrayList<>();
        flatGroups.add(group);
        flatStudents.add(student1);
        flatStudents.add(student2);
        flatStudents.add(student3);

        // database calls
        ArrayList<Student> dbStudents;
        ArrayList<Group> dbGroups;
        dbStudents = StudentDbHelper.fetchFromDatabase(context);
        dbGroups = GroupDbHelper.fetchFromDatabaseWithStudents(context, dbStudents);

        // clear and add to database
        for (Group mGroup : dbGroups) {
            GroupDbHelper.destroy(context, mGroup);
        }
        for (Student mStudent : dbStudents) {
            StudentDbHelper.destroy(context, mStudent);
        }
        for (Group mGroup : flatGroups) {
            GroupDbHelper.addToDatabase(context, mGroup);
        }
        for (Student mStudent : flatStudents) {
            StudentDbHelper.addToDatabase(context, mStudent);
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
