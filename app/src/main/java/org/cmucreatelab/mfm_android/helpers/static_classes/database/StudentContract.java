package org.cmucreatelab.mfm_android.helpers.static_classes.database;

/**
 * Created by mike on 2/5/16.
 *
 * SutdentContract
 *
 * The contract information for a Student.
 *
 */
public class StudentContract {

    public static final String TABLE_NAME = "students";

    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHOTO_URL = "photo_url";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_STUDENT_ID + " INTEGER" +
            ", " + COLUMN_FIRST_NAME + " TEXT" +
            ", " + COLUMN_LAST_NAME + " TEXT" +
            ", " + COLUMN_PHOTO_URL + " TEXT" +
            ", " + COLUMN_UPDATED_AT + " TEXT" +
            // ...
            ") ";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

}
