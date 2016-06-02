package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.ListHelper;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class StudentGroupDbHelper {


    protected static void destroyAllFromStudent(Context context, int studentId) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = StudentGroupContract.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};
        int resultInt;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(StudentGroupContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt > 0) {
            Log.i(Constants.LOG_TAG, "deleted " + resultInt + " student_groups.");
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete student_groups with studentId=" + studentId + " but returned " + resultInt);
        }
    }


    protected static void destroyAllFromGroup(Context context, int groupId) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        String selection = StudentGroupContract.COLUMN_GROUP_ID + " = ?";
        String[] selectionArgs = {String.valueOf(groupId)};
        int resultInt;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        resultInt = db.delete(StudentGroupContract.TABLE_NAME, selection, selectionArgs);
        if (resultInt > 0) {
            Log.i(Constants.LOG_TAG, "deleted " + resultInt + " student_groups.");
        } else {
            Log.w(Constants.LOG_TAG, "Attempted to delete student_groups with groupId=" + groupId + " but returned " + resultInt);
        }
    }


    protected static void addToDatabase(Context context, Student student, Group group) {
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        ContentValues values;
        long newGroupId, newStudentId;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(StudentGroupContract.COLUMN_GROUP_ID, String.valueOf(group.getId()));
        values.put(StudentGroupContract.COLUMN_STUDENT_ID, String.valueOf(student.getId()));
        // construct in database (we don't care about the returned databaseId)
        db.insert(StudentGroupContract.TABLE_NAME, "null", values);
    }


    protected static void populateGroupFromDb(Context context, Group group, ArrayList<Student> students) {
        ArrayList<Student> result = new ArrayList<>();

        String[] projection = {
                "_id",
                StudentGroupContract.COLUMN_GROUP_ID, StudentGroupContract.COLUMN_STUDENT_ID
        };
        String selection = "group_id = ?";
        String[] selectionArgs = {
                String.valueOf(group.getId())
        };
        MessageFromMeSQLLiteOpenHelper mDbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        mDbHelper = MessageFromMeSQLLiteOpenHelper.getInstance(context);
        db = mDbHelper.getWritableDatabase();
        cursor = db.query(StudentGroupContract.TABLE_NAME, projection,
                selection, selectionArgs, // columns and values for WHERE clause
                null, null, // group rows, filter row groups
                StudentGroupContract.COLUMN_STUDENT_ID + " ASC" // sort order
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(StudentGroupContract.COLUMN_STUDENT_ID));
                result.add(ListHelper.findStudentWithId(students, studentId));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // iterate
            cursor.moveToNext();
        }

        if (result.size() == 0) {
            Log.w(Constants.LOG_TAG, "populateGroupFromDb is returning an empty list.");
        }

        // set group's list of students
        // TODO order the list of students (by name, most likely)
        group.setStudents(result);
    }

}