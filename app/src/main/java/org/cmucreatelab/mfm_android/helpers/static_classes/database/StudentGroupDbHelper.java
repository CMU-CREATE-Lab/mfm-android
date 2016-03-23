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


    public static ArrayList<Integer> fetchStudentsFromGroup(Context context, Group group) {
        ArrayList<Integer> result = new ArrayList<>();

        //TODO fetch students from Group

        /*String[] projection = {
                "_id",
                StudentGroupContract.COLUMN_GROUP_ID, StudentGroupContract.COLUMN_STUDENT_ID,
        };
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(GroupContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                StudentGroupContract.COLUMN_GROUP_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateGroupFromRecord(cursor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchGroupFromDatabase is returning an empty list.");
        }*/

        return result;
    }

}
