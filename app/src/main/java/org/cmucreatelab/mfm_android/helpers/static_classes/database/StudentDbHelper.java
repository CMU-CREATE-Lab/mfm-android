package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import android.database.Cursor;
import org.cmucreatelab.mfm_android.classes.Student;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class StudentDbHelper {


    public static boolean destroy(Context context, Student student) {
        boolean result = false;

        // TODO destroy Student in database
        // TODO destroy relational StudentGroup

        return result;
    }


    public static void addToDatabase(Context context, Student student) {

        // TODO add Student to database
        // TODO student.setDatabaseId()
    }


    public static void update(Context context, Student student) {

        // TODO update Student in database
    }


    public static Student generateGroupFromRecord(Cursor cursor) {
        Student result = new Student();

        // TODO generate Student from record

        return result;
    }


    public static ArrayList<Student> fetchFromDatabase(Context context) {
        ArrayList<Student> result = new ArrayList<>();

        // TODO fetch all Students from database

        return result;
    }

}
