package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class StudentGroupDbHelper {


    public static boolean destroy(Context context, long databaseId) {
        boolean result = false;

        // TODO destroy record in database

        return result;
    }


    public static void addToDatabase(Context context, Student student, Group group) {

        // TODO add StudentGroup to database
    }


    public static ArrayList<Integer> fetchStudentsFromGroup(Context context, Group group) {
        ArrayList<Integer> result = new ArrayList<>();

        // TODO fetch all StudentIds from database with Group

        return result;
    }

}
