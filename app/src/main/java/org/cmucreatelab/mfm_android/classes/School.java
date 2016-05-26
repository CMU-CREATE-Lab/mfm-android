package org.cmucreatelab.mfm_android.classes;

import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.DbHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Steve on 3/14/2016.
 */
public class School implements Serializable{

    // class attributes
    private int id;
    private String name;
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Group> groups = new ArrayList<>();

    // getters/setters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Group> getGroups() { return groups; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }


    public void addStudent(Student student) {
        this.students.add(student);
    }


    public void addGroup(Group group) {
        this.groups.add(group);
    }


    // This checks for a student in the school. If the student DNE, it will update it and add it to the DB.
    public synchronized Student checkForStudent(int studentId, GlobalHandler globalHandler) {
        for (Student student : students) {
            if (student.getId() == studentId) {
                return student;
            }
        }

        Student student = new Student();
        student.setId(studentId);
        DbHelper.addToDatabase(globalHandler.appContext, student);
        globalHandler.mfmRequestHandler.updateStudent(student);
        return student;
    }


    public School(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
