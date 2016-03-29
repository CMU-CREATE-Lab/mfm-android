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

import java.io.File;
import java.lang.reflect.Array;
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

    private static GlobalHandler classInstance;
    protected Context appContext;
    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    public MfmRequestHandler mfmRequestHandler;
    public SharedPreferencesHandler sharedPreferencesHandler;
    // the "kiosk" that this application represents
    final public Kiosk kiosk;

    private ArrayList<School> schools;
    private ArrayList<Student> mStudents;
    private Student mIndividualStudent;
    private ArrayList<Group> mGroups;
    private Group mIndividualGroup;

    private File mImage;
    private File mAudio;

    public ArrayList<School> getSchools(){
        return this.schools;
    }
    public void setSchools(ArrayList<School> schools){
        this.schools = schools;
    }

    public ArrayList<Student> getStudentData(){
        return this.mStudents;
    }
    public void setIndividualStudentData(Student st){
        this.mIndividualStudent = st;
    }
    public Student getIndividualStudentData() {
        return this.mIndividualStudent;
    }

    public ArrayList<Group> getGroupData(){
        return this.mGroups;
    }
    public void setIndividualGroup(Group gp){
        this.mIndividualGroup = gp;
    }
    public Group getIndividualGroup(){
        return this.mIndividualGroup;
    }

    public File getCurrentImage(){
        return this.mImage;
    }
    public void setCurrentImage(File image){
        this.mImage = image;
    }
    public File getCurrentAudio(){
        return this.mAudio;
    }
    public void setCurrentAudio(File audio){
        this.mAudio = audio;
    }

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
        mStudents.clear();
        mStudents.addAll(students);
        for (Student student : students) {
            mfmRequestHandler.updateStudent(student);
        }
    }

    public void checkAndUpdateGroups(ArrayList<Group> groups){
        mGroups.clear();
        mGroups.addAll(groups);
        for(Group group : groups){
            mfmRequestHandler.updateGroup(group);
        }
    }

    public Student getStudentByID(int id){
        Student result = null;

        for(int i = 0; i < this.mStudents.size(); i++){
            Student test = this.mStudents.get(i);
            if(id == test.getId()){
                result = test;
            }
        }

        return result;
    }

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
        this.sharedPreferencesHandler = new SharedPreferencesHandler(this);
        this.kiosk = new Kiosk();
        this.mStudents = new ArrayList<>();
        this.mGroups = new ArrayList<>();
        // TODO just a placeholder for testing

        // load from database
        ArrayList<Group> dbGroups = GroupDbHelper.fetchFromDatabase(ctx);
        ArrayList<Student> dbStudents = StudentDbHelper.fetchFromDatabase(ctx);
        ArrayList<User> dbUsers = UserDbHelper.fetchFromDatabase(ctx);
        // I am not sure if this is how we want to access elements in the database
        ArrayList<ArrayList<Integer>> dbStudentsInGroups = new ArrayList<>();

        for(Group group : dbGroups){
            // add the student ids for each group
            ArrayList<Integer> dbStudentIdsFromGroup = StudentGroupDbHelper.fetchStudentsFromGroup(ctx, group);
            dbStudentsInGroups.add(dbStudentIdsFromGroup);
            // TODO add group readings
        }
        for(Student student : dbStudents){
            // TODO add student readings
        }
        for(User user : dbUsers){
            // TODO add user readings
        }
        for(ArrayList<Integer> studentIds : dbStudentsInGroups){
            // TODO add studentId readings
            Log.i(Constants.LOG_TAG, studentIds.get(0).toString());
        }

    }

}
