package org.cmucreatelab.mfm_android.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 */
public class Student implements Parcelable {

    private int id;
    private long databaseId;
    private String firstName,lastName;
    private String photoUrl;
    private String updatedAt;
    // users connected to the student (message recipients)
    private User[] users;

    //change databaseId to Id
    public long getDatabaseId() { return databaseId; }
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName;}
    public String getPhotoUrl() { return photoUrl; }
    public String getUpdatedAt() { return updatedAt; }
    public User[] getUsers() {
        return users;
    }

    public void setDatabaseId(long databaseId) { this.databaseId = databaseId; }
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
    public void setUsers(User[] users) {
        this.users = users;
    }


    // methods for Sender interface

    //@Override
////    public Type getSenderType() {
////        return Type.Student;
////    }
//
//    @Override
//    public int getId() {
//        return this.id;
//    }
//
//    @Override
//    public String getName() {
//        return this.firstName+" "+this.lastName;
//    }
//
    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(databaseId);
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(photoUrl);
        dest.writeString(updatedAt);
//        dest.writeArray(users);
    }

    private Student(Parcel in){
        databaseId = in.readLong();
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        photoUrl = in.readString();
        updatedAt = in.readString();
//        users = (User[]) in.readArray(getClass().getClassLoader());
    }

    public Student() { }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel parcel) {
            return new Student(parcel);
        }

        @Override
        public Student[] newArray(int i) {
            return new Student[i]; //i is the size
        }
    };
}
