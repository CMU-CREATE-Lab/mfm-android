package org.cmucreatelab.mfm_android.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Student implements Sender, Serializable {

    private int id;
    private long databaseId;
    private String firstName,lastName;
    private String photoUrl;
    private String updatedAt;
    // users connected to the student (message recipients)
    private ArrayList<User> users;

    public long getDatabaseId() {
        return databaseId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


    // methods for Sender interface


    @Override
    public Type getSenderType() {
        return Type.Student;
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
