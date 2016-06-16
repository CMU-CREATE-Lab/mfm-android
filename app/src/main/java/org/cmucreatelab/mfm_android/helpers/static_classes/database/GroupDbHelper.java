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
 *
 * GroupDbHelper
 *
 * A helper class to simplify group database operations.
 *
 */
public class GroupDbHelper {


    private static Group generateGroupFromRecord(Context context, Cursor cursor, ArrayList<Student> students) {
        Group result = new Group();

        int id;
        String name,groupId, photoURL, updatedAt;

        try {
            // read record
            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            name = cursor.getString(cursor.getColumnIndexOrThrow(GroupContract.COLUMN_NAME));
            groupId = cursor.getString(cursor.getColumnIndexOrThrow(GroupContract.COLUMN_GROUP_ID));
            photoURL = cursor.getString(cursor.getColumnIndexOrThrow(GroupContract.COLUMN_PHOTO_URL));
            updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(GroupContract.COLUMN_UPDATED_AT));
            Log.v(Constants.LOG_TAG, "Read group record _id=" + id);

            // add to data structure
            result.setDatabaseId(id);
            result.setName(name);
            result.setId(Integer.parseInt(groupId));
            result.setPhotoUrl(photoURL);
            result.setUpdatedAt(updatedAt);

            // make request for the group's students
            StudentGroupDbHelper.populateGroupFromDb(context, result, students);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to read from cursor! cursor.toString()=" + cursor.toString());
            throw e;
        }

        return result;
    }


    protected static boolean destroy(Context context, Group group) {
        boolean result = false;

        if (group.getDatabaseId() < 0) {
            return false;
        }

        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = "_id LIKE ?";
        String[] selectionArgs = { String.valueOf(group.getDatabaseId()) };
        int resultInt;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(GroupContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt == 1) {
            Log.i(Constants.LOG_TAG, "deleted group with _id=" + group.getDatabaseId());
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete group with _id=" +
                group.getDatabaseId() + " but deleted " + resultInt + " items.");
        }
        result = (resultInt > 0);

        // delete ALL student associations
        StudentGroupDbHelper.destroyAllFromGroup(context, group.getId());

        return result;
    }


    protected static void addToDatabase(Context context, Group group) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newId;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(GroupContract.COLUMN_NAME, group.getName());
        values.put(GroupContract.COLUMN_GROUP_ID, String.valueOf(group.getId()));
        values.put(GroupContract.COLUMN_PHOTO_URL, group.getPhotoUrl());
        values.put(GroupContract.COLUMN_UPDATED_AT, group.getUpdatedAt());
        newId = db.insert(GroupContract.TABLE_NAME, "null", values);

        group.setDatabaseId(newId);
        Log.i(Constants.LOG_TAG, "inserted new group _id=" + newId);

        // double-check that there aren't already students in the DB sharing groupId (there shouldn't be)
        StudentGroupDbHelper.destroyAllFromGroup(context, group.getId());
        // add student_groups to DB
        for (Student student : group.getStudents()) {
            StudentGroupDbHelper.addToDatabase(context, student, group);
        }
    }


    protected static void update(Context context, Group group) {
        if (group.getDatabaseId() >= 0) {
            MessageFromMeSQLLiteOpenHelper mDbHelper;
            SQLiteDatabase db;
            String selection = "_id LIKE ?";
            String[] selectionArgs = { String.valueOf(group.getDatabaseId()) };
            int result;
            ContentValues contentValues;

            mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
            db = mDbHelper.getWritableDatabase();

            // find values to be updated
            contentValues = new ContentValues();
            contentValues.put(GroupContract.COLUMN_NAME, group.getName());
            contentValues.put(GroupContract.COLUMN_GROUP_ID, String.valueOf(group.getId()));
            contentValues.put(GroupContract.COLUMN_PHOTO_URL, group.getPhotoUrl());
            contentValues.put(GroupContract.COLUMN_UPDATED_AT, group.getUpdatedAt());

            // perform update
            result = db.update(GroupContract.TABLE_NAME, contentValues, selection, selectionArgs);
            if (result == 1) {
                Log.i(Constants.LOG_TAG, "updated group _id=" + group.getDatabaseId());
            } else {
                Log.w(Constants.LOG_TAG, "Attempted to update group _id=" +
                        group.getDatabaseId() + " but updated " + result + " items.");
            }

            // delete ALL students in the DB sharing groupId
            StudentGroupDbHelper.destroyAllFromGroup(context, group.getId());
            // (re-)add student_groups to DB
            for (Student student : group.getStudents()) {
                StudentGroupDbHelper.addToDatabase(context, student, group);
            }
        }
    }


    protected static ArrayList<Group> fetchFromDatabaseWithStudents(Context context, ArrayList<Student> students) {
        ArrayList<Group> result = new ArrayList<>();

        String[] projection = {
                "_id",
                GroupContract.COLUMN_NAME, GroupContract.COLUMN_GROUP_ID,
                GroupContract.COLUMN_PHOTO_URL, GroupContract.COLUMN_UPDATED_AT
        };
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(GroupContract.TABLE_NAME, projection,
                null, null, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                GroupContract.COLUMN_GROUP_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                result.add(generateGroupFromRecord(context, cursor, students));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "fetchGroupFromDatabase is returning an empty list.");
        }

        return result;
    }

}
