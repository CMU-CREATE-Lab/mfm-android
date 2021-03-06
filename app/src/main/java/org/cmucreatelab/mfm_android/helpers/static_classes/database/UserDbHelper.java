package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 *
 * UserDbHelper
 *
 * A helper class to simplify User database operations.
 *
 */
public class UserDbHelper {


    private static User generateUserFromRecord(Cursor cursor, Student student) {
        User result = new User();

        int id, studentId;
        String firstName, lastName, studentUserRole, userId, photoURL, updatedAt;

        try {
            // read record
            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            firstName = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_FIRST_NAME));
            lastName = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_LAST_NAME));
            studentUserRole = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_STUDENT_USER_ROLE));
            userId = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_USER_ID));
            studentId = cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.COLUMN_STUDENT_ID));
            photoURL = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_PHOTO_URL));
            updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_UPDATED_AT));
            Log.v(Constants.LOG_TAG, "Read user record _id=" + id);
            if (student.getId() != studentId) {
                Log.wtf(Constants.LOG_TAG, "WTF? Passed in studentId="+student.getId()+" to generate user but found studentId="+studentId+" in DB.");
                return result;
            }

            // add to data structure
            result.setDatabaseId(id);
            result.setFirstName(firstName);
            result.setLastName(lastName);
            result.setStudentUserRole(studentUserRole);
            result.setId(Integer.parseInt(userId));
            result.setPhotoUrl(photoURL);
            result.setUpdatedAt(updatedAt);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to read from cursor! cursor.toString()=" + cursor.toString());
            throw e;
        }

        return result;
    }


    protected static void destroyAllFromStudent(Context context, int studentId) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = UserContract.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};
        int resultInt;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(UserContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt > 0) {
            Log.i(Constants.LOG_TAG, "deleted " + resultInt + " users.");
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete users with studentId=" + studentId + " but returned " + resultInt);
        }
    }


    protected static void addToDatabase(Context context, User user) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(UserContract.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserContract.COLUMN_LAST_NAME, String.valueOf(user.getLastName()));
        values.put(UserContract.COLUMN_STUDENT_ID, user.getStudent().getId());
        values.put(UserContract.COLUMN_USER_ID, user.getId());
        values.put(UserContract.COLUMN_STUDENT_USER_ROLE, user.getStudentUserRole());
        values.put(UserContract.COLUMN_PHOTO_URL, user.getPhotoUrl());
        values.put(UserContract.COLUMN_UPDATED_AT, user.getUpdatedAt());
        newId = db.insert(UserContract.TABLE_NAME, "null", values);

        user.setDatabaseId(newId);
        Log.i(Constants.LOG_TAG, "inserted new user _id=" + newId);
    }


    // NOTE: this fetches from the database ONLY for a specific studentId
    protected static ArrayList<User> fetchFromDatabaseWithStudent(Context context, Student student) {
        ArrayList<User> result = new ArrayList<>();

        String[] projection = {
                "_id",
                UserContract.COLUMN_FIRST_NAME, UserContract.COLUMN_LAST_NAME,
                UserContract.COLUMN_STUDENT_USER_ROLE, UserContract.COLUMN_USER_ID,
                UserContract.COLUMN_PHOTO_URL, UserContract.COLUMN_UPDATED_AT,
                UserContract.COLUMN_STUDENT_ID
        };
        String selection = "student_id = ?";
        String[] selectionArgs = {
                String.valueOf(student.getId())
        };
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(UserContract.TABLE_NAME, projection,
                selection, selectionArgs, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                UserContract.COLUMN_USER_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateUserFromRecord(cursor, student));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchUserFromDatabase is returning an empty list.");
        }

        return result;
    }

}
