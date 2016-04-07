package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 3/21/2016.
 */
public class MessageFromMeSQLLiteOpenHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    // I do not think the database has even been made yet so I changed the version from 6 to 1.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MessageFromMe.db";


    public MessageFromMeSQLLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        Log.i(Constants.LOG_TAG, "(SQL COMMAND): " + GroupContract.SQL_CREATE_TABLE);
        db.execSQL(GroupContract.SQL_CREATE_TABLE);
        Log.i(Constants.LOG_TAG, "(SQL COMMAND): " + StudentContract.SQL_CREATE_TABLE);
        db.execSQL(StudentContract.SQL_CREATE_TABLE);
        Log.i(Constants.LOG_TAG, "(SQL COMMAND): " + StudentGroupContract.SQL_CREATE_TABLE);
        db.execSQL(StudentGroupContract.SQL_CREATE_TABLE);
        Log.i(Constants.LOG_TAG, "(SQL COMMAND): " + UserContract.SQL_CREATE_TABLE);
        db.execSQL(UserContract.SQL_CREATE_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(GroupContract.SQL_DROP_TABLE);
        db.execSQL(StudentContract.SQL_DROP_TABLE);
        db.execSQL(StudentGroupContract.SQL_DROP_TABLE);
        db.execSQL(UserContract.SQL_DROP_TABLE);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
