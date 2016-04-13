package org.cmucreatelab.mfm_android.helpers.static_classes;

import android.util.Log;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import java.util.ArrayList;

/**
 * Created by mike on 4/8/16.
 *
 * ListHelper
 *
 * Performs helpful operations on commonly-used lists.
 *
 */
public class ListHelper {


    public static Student findStudentWithId(ArrayList<Student> students, int studentId) throws Exception {
        for (Student student : students) {
            if (student.getId() == studentId) {
                return student;
            }
        }
        // TODO is this how throwing exceptions should look?
        Log.e(Constants.LOG_TAG, "could not find studentId="+studentId+" in findStudentWithId");
        throw new Exception();
    }

    public static Group findGroupWithId(ArrayList<Group> groups, int groupId) throws Exception {
        for (Group group : groups) {
            if (group.getId() == groupId) {
                return group;
            }
        }
        // TODO is this how throwing exceptions should look?
        Log.e(Constants.LOG_TAG, "could not find groupId="+groupId+" in findGroupWithId");
        throw new Exception();
    }

}
