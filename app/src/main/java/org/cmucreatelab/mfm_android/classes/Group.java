package org.cmucreatelab.mfm_android.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mike on 1/28/16.
 *
 * Group
 *
 * Represents a Group that belongs to a School.
 *
 */
public class Group implements Sender, Serializable {

    private static final Sender.Type senderType = Type.group;

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
