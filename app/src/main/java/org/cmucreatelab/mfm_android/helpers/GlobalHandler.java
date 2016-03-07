package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;

import org.cmucreatelab.mfm_android.classes.Kiosk;
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
    private ArrayList<Student> mStudents = new ArrayList<>();
    private Student mIndividualStudent;

    public ArrayList<Student> getStudentData(){
        return this.mStudents;
    }
    public void setIndividualStudentData(Student st){
        this.mIndividualStudent = st;
    }
    public Student getIndividualStudentData() {
        return this.mIndividualStudent;
    }


    public void refreshStudentsAndGroups() {
        mfmRequestHandler.requestListStudents();
        // TODO groups
        //mfmRequestHandler.requestListGroups();
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
        // TODO just a placeholder for testing
        kiosk.login(Constants.schoolId, Constants.schoolName, Constants.kioskID);
    }

}
