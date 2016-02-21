package org.cmucreatelab.mfm_android.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mike on 1/28/16.
 */
public class User implements Parcelable {

    private int id;
    private long databaseId;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String updatedAt;
    private Student student;
    private String studentUserRole;

    public long getDatabaseId() {
        return databaseId;
    }
    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getStudentUserRole() {
        return studentUserRole;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public Student getStudent() {
        return student;
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
    public void setStudentUserRole(String studentUserRole) {
        this.studentUserRole = studentUserRole;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(databaseId);
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(photoUrl);
        dest.writeString(updatedAt);
        dest.writeString(studentUserRole);
    }

    private User(Parcel in) {
        databaseId = in.readLong();
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        photoUrl = in.readString();
        updatedAt = in.readString();
    }

    public User() { }
    private static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
}
