package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

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


    public void refreshStudentsAndGroups() {
        mfmRequestHandler.requestListSchools("stevefulton", "stevefulton");
        mfmRequestHandler.requestListGroups();
        mfmRequestHandler.requestListStudents();
        mfmRequestHandler.ping();
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
        kiosk.login(Constants.schoolId, Constants.schoolName, Constants.kioskID);
    }

}
