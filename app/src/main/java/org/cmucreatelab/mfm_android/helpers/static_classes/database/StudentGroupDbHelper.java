package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class StudentGroupDbHelper {


    public static boolean destroy(Context context, long databaseId) {
        boolean result = false;

        if (databaseId < 0) {
            return false;
        }

        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = "_id LIKE ?";
        String[] selectionArgs = { String.valueOf(databaseId) };
        int resultInt;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(StudentGroupContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt == 1) {
            Log.i(Constants.LOG_TAG, "deleted studentGroup _id=" + databaseId);
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete studentGroup _id=" +
                    databaseId + " but deleted " + result + " items.");
        }
        result = (resultInt > 0);

        return result;
    }


    public static void addToDatabase(Context context, Student student, Group group) {

        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newGroupId, newStudentId;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(StudentGroupContract.COLUMN_GROUP_ID, String.valueOf(group.getId()));
        values.put(StudentGroupContract.COLUMN_STUDENT_ID, String.valueOf(student.getId()));
        newGroupId = db.insert(StudentGroupContract.TABLE_NAME, "null", values);
        newStudentId = db.insert(StudentGroupContract.TABLE_NAME, "null", values);

        group.setDatabaseId(newGroupId);
        Log.i(Constants.LOG_TAG, "inserted new group _id=" + newGroupId);

        student.setDatabaseId(newStudentId);
        Log.i(Constants.LOG_TAG, "inserted new student _id=" + newStudentId);
    }

    // I am not sure if this is how we want to fetch the Students, but it makes sense to me
    // since the group that was passed in was fetched from the database earlier
    public static ArrayList<Integer> fetchStudentsFromGroup(Context context, Group group) {
        ArrayList<Integer> result = new ArrayList<>();

        try {
            result = group.getStudentIds();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchGroupFromDatabase is returning an empty list.");
        }

        return result;
    }

}
