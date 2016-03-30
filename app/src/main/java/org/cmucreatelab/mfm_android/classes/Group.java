package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Group implements Sender {

    // class attributes
    private int id;
    private long databaseId;
    private String name;
    private String photoUrl;
    private String updatedAt;
    private ArrayList<Student> students;
    private ArrayList<Integer> studentIds;

    // getters/setters
    public long getDatabaseId() { return databaseId; }
    public String getPhotoUrl() { return photoUrl; }
    public String getUpdatedAt() { return updatedAt; }
    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<Integer> getStudentIds() { return studentIds; }
    public void setDatabaseId(long databaseId) { this.databaseId = databaseId; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setStudents(ArrayList<Student> students) { this.students = students; }
    public void setStudentIds(ArrayList<Integer> studentIds) { this.studentIds = studentIds; }


    // Sender implementation


    @Override
    public Type getSenderType() {
        return Type.Group;
    }


    @Override
    public int getId() {
        return this.id;
    }


    @Override
    public String getName() {
        return this.name;
    }

}
