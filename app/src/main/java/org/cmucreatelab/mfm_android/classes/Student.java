package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Student implements Sender {

    int id;
    String firstName,lastName;
    String photoUrl;
    String udpatedAt;
    ArrayList<User> users;

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getUdpatedAt() {
        return udpatedAt;
    }
    public ArrayList<User> getUsers() {
        return users;
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
