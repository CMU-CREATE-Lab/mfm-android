package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class StudentDbHelper {


    public static boolean destroy(Context context, Student student) {
        boolean result = false;

        if (student.getDatabaseId() < 0) {
            return false;
        }

        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = "_id LIKE ?";
        String[] selectionArgs = { String.valueOf(student.getId()) };
        int resultInt;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(StudentContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt == 1) {
            Log.i(Constants.LOG_TAG, "deleted student _id=" + student.getId());
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete student _id=" +
                    student.getId() + " but deleted " + resultInt + " items.");
        }
        result = (resultInt > 0);

        return result;
    }


    public static void addToDatabase(Context context, Student student) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(StudentContract.COLUMN_FIRST_NAME, student.getFirstName());
        values.put(StudentContract.COLUMN_LAST_NAME, student.getLastName());
        values.put(StudentContract.COLUMN_STUDENT_ID, String.valueOf(student.getId()));
        values.put(StudentContract.COLUMN_PHOTO_URL, student.getPhotoUrl());
        values.put(StudentContract.COLUMN_UPDATED_AT, student.getUpdatedAt());
        newId = db.insert(StudentContract.TABLE_NAME, "null", values);

        student.setDatabaseId(newId);
        Log.i(Constants.LOG_TAG, "inserted new student _id=" + newId);
    }


    public static void update(Context context, Student student) {
        if (student.getDatabaseId() >= 0) {
            MessageFromMeSQLLiteOpenHelper mDbHelper;
            SQLiteDatabase db;
            String selection = "_id LIKE ?";
            String[] selectionArgs = { String.valueOf(student.getDatabaseId()) };
            int result;
            ContentValues contentValues;

            mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
            db = mDbHelper.getWritableDatabase();

            // find values to be updated
            contentValues = new ContentValues();
            contentValues.put(StudentContract.COLUMN_FIRST_NAME, student.getFirstName());
            contentValues.put(StudentContract.COLUMN_LAST_NAME, student.getLastName());
            contentValues.put(StudentContract.COLUMN_STUDENT_ID, String.valueOf(student.getId()));
            contentValues.put(StudentContract.COLUMN_PHOTO_URL, student.getPhotoUrl());
            contentValues.put(StudentContract.COLUMN_UPDATED_AT, student.getUpdatedAt());

            // perform update
            result = db.update(GroupContract.TABLE_NAME, contentValues, selection, selectionArgs);
            if (result == 1) {
                Log.i(Constants.LOG_TAG, "updated student _id=" + student.getDatabaseId());
            } else {
                Log.w(Constants.LOG_TAG, "Attempted to update student _id=" +
                        student.getDatabaseId() + " but updated " + result + " items.");
            }
        }
    }


    private static Student generateStudentFromRecord(Cursor cursor) {
        Student result = new Student();

        int id;
        String firstName, lastName,studentID, photoURL, updatedAt;

        try {
            // read record
            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            firstName = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.COLUMN_FIRST_NAME));
            lastName = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.COLUMN_LAST_NAME));
            studentID = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.COLUMN_STUDENT_ID));
            photoURL = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.COLUMN_PHOTO_URL));
            updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.COLUMN_UPDATED_AT));
            Log.v(Constants.LOG_TAG, "Read student record _id=" + id);

            // add to data structure
            result.setFirstName(firstName);
            result.setLastName(lastName);
            result.setId(Integer.parseInt(studentID));
            result.setPhotoUrl(photoURL);
            result.setUpdatedAt(updatedAt);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to read from cursor! cursor.toString()=" + cursor.toString());
            throw e;
        }

        return result;
    }


    public static ArrayList<Student> fetchFromDatabase(Context context) {
        ArrayList<Student> result = new ArrayList<>();

        String[] projection = {
                "_id",
                StudentContract.COLUMN_FIRST_NAME, StudentContract.COLUMN_LAST_NAME,
                StudentContract.COLUMN_STUDENT_ID, StudentContract.COLUMN_PHOTO_URL,
                StudentContract.COLUMN_UPDATED_AT,
        };
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(StudentContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                StudentContract.COLUMN_STUDENT_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateStudentFromRecord(cursor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchStudentFromDatabase is returning an empty list.");
        }

        return result;
    }

}
