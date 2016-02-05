package org.cmucreatelab.mfm_android.classes;

/**
 * Created by mike on 1/28/16.
 */
public class User {

    private int id;
    private String firstName,lastName,studentUserRole;
    private String updatedAt;

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

}
