package org.cmucreatelab.mfm_android.classes;

import java.io.Serializable;

/**
 * Created by mohaknahta on 2/20/16.
 */
public class StudentList implements Serializable {

    private Student[] mStudent;

    public Student[] getStudentList() {
        return mStudent;
    }
    public void setStudentList(Student[] studentList) {
        mStudent = studentList;
    }

}
