package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by Steve on 3/14/2016.
 */
public class School {

    // class attributes
    private int id;
    private String name;
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Group> groups = new ArrayList<>();

    // getters/setters
    public int getId() { return id; }
    public String getName() { return name; }
    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Group> getGroups() { return groups; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }


    public School(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
