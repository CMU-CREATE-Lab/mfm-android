package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class UserDbHelper {


    public static boolean destroy(Context context, User user) {
        boolean result = false;

        if (user.getDatabaseId() < 0) {
            return false;
        }

        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = "_id LIKE ?";
        String[] selectionArgs = { String.valueOf(user.getId()) };
        int resultInt;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(UserContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt == 1) {
            Log.i(Constants.LOG_TAG, "deleted user _id=" + user.getId());
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete user _id=" +
                    user.getId() + " but deleted " + resultInt + " items.");
        }
        result = (resultInt > 0);

        return result;
    }


    public static void addToDatabase(Context context, User user) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
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


    public static void update(Context context, User user) {
        if (user.getDatabaseId() >= 0) {
            MessageFromMeSQLLiteOpenHelper mDbHelper;
            SQLiteDatabase db;
            String selection = "_id LIKE ?";
            String[] selectionArgs = { String.valueOf(user.getDatabaseId()) };
            int result;
            ContentValues contentValues;

            mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
            db = mDbHelper.getWritableDatabase();

            // find values to be updated
            contentValues = new ContentValues();
            contentValues.put(UserContract.COLUMN_FIRST_NAME, user.getFirstName());
            contentValues.put(UserContract.COLUMN_LAST_NAME, user.getLastName());
            contentValues.put(UserContract.COLUMN_STUDENT_USER_ROLE, user.getStudentUserRole());
            contentValues.put(UserContract.COLUMN_USER_ID, user.getId());
            contentValues.put(UserContract.COLUMN_STUDENT_ID, user.getStudent().getId());
            contentValues.put(UserContract.COLUMN_PHOTO_URL, user.getPhotoUrl());
            contentValues.put(UserContract.COLUMN_UPDATED_AT, user.getUpdatedAt());

            // perform update
            result = db.update(UserContract.TABLE_NAME, contentValues, selection, selectionArgs);
            if (result == 1) {
                Log.i(Constants.LOG_TAG, "updated user _id=" + user.getDatabaseId());
            } else {
                Log.w(Constants.LOG_TAG, "Attempted to update user _id=" +
                        user.getDatabaseId() + " but updated " + result + " items.");
            }
        }
    }


    private static User generateUserFromRecord(Cursor cursor) {
        User result = new User();

        int id;
        String firstName, lastName, studentUserRole, userId, studentId, photoURL, updatedAt;

        try {
            // read record
            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            firstName = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_FIRST_NAME));
            lastName = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_LAST_NAME));
            studentUserRole = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_STUDENT_USER_ROLE));
            userId = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_USER_ID));
            studentId = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_STUDENT_ID));
            photoURL = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_PHOTO_URL));
            updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_UPDATED_AT));
            Log.v(Constants.LOG_TAG, "Read user record _id=" + id);

            // add to data structure
            result.setFirstName(firstName);
            result.setLastName(lastName);
            result.setStudentUserRole(studentUserRole);
            result.setId(Integer.parseInt(userId));
            result.getStudent().setId(Integer.parseInt(studentId));
            result.setPhotoUrl(photoURL);
            result.setUpdatedAt(updatedAt);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to read from cursor! cursor.toString()=" + cursor.toString());
            throw e;
        }

        return result;
    }


    public static ArrayList<User> fetchFromDatabase(Context context) {
        ArrayList<User> result = new ArrayList<>();

        String[] projection = {
                "_id",
                UserContract.COLUMN_FIRST_NAME, UserContract.COLUMN_LAST_NAME,
                UserContract.COLUMN_STUDENT_USER_ROLE, UserContract.COLUMN_USER_ID,
                UserContract.COLUMN_PHOTO_URL, UserContract.COLUMN_UPDATED_AT,
                UserContract.COLUMN_STUDENT_ID
        };
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = new MessageFromMeSQLLiteOpenHelper(context);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(UserContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                UserContract.COLUMN_USER_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateUserFromRecord(cursor));
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
