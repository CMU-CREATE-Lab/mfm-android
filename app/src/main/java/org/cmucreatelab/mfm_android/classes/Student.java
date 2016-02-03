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
