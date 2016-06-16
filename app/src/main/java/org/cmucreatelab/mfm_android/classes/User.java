package org.cmucreatelab.mfm_android.classes;

import java.io.Serializable;

/**
 * Created by mike on 1/28/16.
 *
 * User
 *
 * Represents a User who has a Student belonging to a School.
 *
 */
public class User implements Serializable {

    // class attributes
    private int id;
    private long databaseId;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String updatedAt;
    private Student student;
    private String studentUserRole;

    // getters/setters
    public long getDatabaseId() { return databaseId; }
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStudentUserRole() { return studentUserRole; }
    public String getUpdatedAt() { return updatedAt; }
    public String getPhotoUrl() { return photoUrl; }
    public Student getStudent() { return student; }
    public void setDatabaseId(long databaseId) { this.databaseId = databaseId; }
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setStudentUserRole(String studentUserRole) { this.studentUserRole = studentUserRole; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setStudent(Student student) { this.student = student; }

}
