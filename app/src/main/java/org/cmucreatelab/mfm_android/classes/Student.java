package org.cmucreatelab.mfm_android.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by mike on 1/28/16.
 *
 * Students
 *
 * Represents a Student that belongs to a School and/or Group.
 *
 */
public class Student implements Sender, Serializable {

    private static final Sender.Type senderType = Sender.Type.student;

    // class attributes
    private int id;
    private long databaseId;
    private String firstName = new String(), lastName = new String();
    private String photoUrl = new String();
    private String updatedAt = new String();
    private ArrayList<User> users = new ArrayList<>(); // users connected to the student (message recipients)

    // getters/setters
    public long getDatabaseId() { return databaseId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhotoUrl() { return photoUrl; }
    public String getUpdatedAt() { return updatedAt; }
    public ArrayList<User> getUsers() { return users; }
    public void setDatabaseId(long databaseId) { this.databaseId = databaseId; }
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setUsers(ArrayList<User> users) { this.users = users; }


    // Sender implementation


    @Override
    public Sender.Type getSenderType() {
        return Sender.Type.student;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.firstName+" "+this.lastName;
    }

}
