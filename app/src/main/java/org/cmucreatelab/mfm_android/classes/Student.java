package org.cmucreatelab.mfm_android.classes;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Student implements Sender {

    private int id;
    private long databaseId;
    private String firstName,lastName;
    private String photoUrl;
    private String udpatedAt;
    private String thumb_photoUrl;
    // users connected to the student (message recipients)
    private ArrayList<User> users;
    public static final String STUDENT_TAG = Student.class.getSimpleName();

    //change databaseId to Id
    public long getDatabaseId() { return databaseId; }

    public String getFirstName() { return firstName; }
    public String getLastName() {
        return lastName;
    }

    public String getThumbPhotoUrl() {
        return thumb_photoUrl;
    }

    public String getPhotoUrl() { return photoUrl; }

    public String getUdpatedAt(JSONObject studentNode) { return udpatedAt; }

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
    public void setUdpatedAt(String udpatedAt) {
        this.udpatedAt = udpatedAt;
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
