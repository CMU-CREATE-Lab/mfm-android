package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Group implements Sender {

    private static final Sender.Type senderType = Sender.Type.Group;

    // class attributes
    private int id;
    private long databaseId;
    private String name;
    private String photoUrl;
    private String updatedAt;
    private ArrayList<Student> students = new ArrayList<>();

    // getters/setters
    public long getDatabaseId() { return databaseId; }
    public String getPhotoUrl() { return photoUrl; }
    public String getUpdatedAt() { return updatedAt; }
    public ArrayList<Student> getStudents() { return students; }
    public void setDatabaseId(long databaseId) { this.databaseId = databaseId; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setStudents(ArrayList<Student> students) { this.students = students; }


    // Sender implementation


    @Override
    public int getId() {
        return this.id;
    }


    @Override
    public Sender.Type getSenderType() {
        return senderType;
    }


    @Override
    public String getName() {
        return this.name;
    }

}
