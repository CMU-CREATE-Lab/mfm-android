package org.cmucreatelab.mfm_android.helpers.readings;

import android.util.Log;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.GroupDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.StudentGroupDbHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.UserDbHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Steve on 4/1/2016.
 */
public class ReadingsHandler{

    private final HashMap<String,ArrayList> hashMap = new HashMap<>();
    private final ArrayList<Group> groups = new ArrayList<>();
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<ArrayList<Student>> studentsInGroups = new ArrayList<>();

    public void addReading(Readable readable){
        Readable.Type type = readable.getReadableType();
        switch (type){
            case GROUP:
                this.groups.add((Group) readable);
                this.studentsInGroups.add(((Group) readable).getStudents()); // students in a group
                break;
            case STUDENT:
                this.students.add((Student) readable);
                break;
            case USER:
                this.users.add((User) readable);
                break;
            default:
                Log.e(Constants.LOG_TAG, "Tried to add Readable of unknown Type in ReadingsHandler ");
        }

        refreshHash();
    }

    public void updateGroups(){
        for (Group group : this.groups) {
            globalHandler.mfmRequestHandler.updateGroup(group);
        }
    }

    public void updateStudents(){
        for (Student student : this.students) {
            globalHandler.mfmRequestHandler.updateStudent(student);
        }
    }

    public void populateDatabase(){
        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            // Populate the list of students and the list of groups
            globalHandler.mfmRequestHandler.requestListStudents();
            globalHandler.mfmRequestHandler.requestListGroups();
            // Populate students and users in the database
            for (Student student : students) {
                if (student.getDatabaseId() <= 0) {
                    StudentDbHelper.addToDatabase(globalHandler.appContext, student);
                    for (User user : student.getUsers()) {
                        UserDbHelper.addToDatabase(globalHandler.appContext, user);
                    }
                }
            }
            // Populate groups and students in group in the database
            for (Group group : groups) {
                GroupDbHelper.addToDatabase(globalHandler.appContext, group);
                for (Student student: group.getStudents()) {
                    StudentGroupDbHelper.addToDatabase(globalHandler.appContext, student, group);
                }
            }
        }

        refreshHash();
    }

    public void refreshHash(){
        this.hashMap.clear();
        this.hashMap.put(Constants.HEADER_TITLES[0], this.groups);
        this.hashMap.put(Constants.HEADER_TITLES[3], this.studentsInGroups);
        this.hashMap.put(Constants.HEADER_TITLES[1], this.students);
        this.hashMap.put(Constants.HEADER_TITLES[2], this.users);
    }

    // Singleton Implementation

    private static ReadingsHandler classInstance;
    private GlobalHandler globalHandler;

    public static ReadingsHandler newInstance(GlobalHandler globalHandler){
        if (classInstance == null) {
            classInstance = new ReadingsHandler(globalHandler);
        }

        return  classInstance;
    }

    private ReadingsHandler(GlobalHandler globalHandler){
        this.globalHandler = globalHandler;
    }

}
