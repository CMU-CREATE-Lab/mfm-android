package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Group implements Sender {
    int id;
    String name;
    String photoUrl;
    String updatedAt;
    ArrayList<Student> students;

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
