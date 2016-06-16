package org.cmucreatelab.mfm_android.helpers.static_classes.database;

/**
 * Created by mike on 2/5/16.
 *
 * UserContract
 *
 * The contract information for a User.
 *
 */
public class UserContract {

    public static final String TABLE_NAME = "users";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_STUDENT_USER_ROLE = "student_user_role";
    public static final String COLUMN_PHOTO_URL = "photo_url";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_USER_ID + " INTEGER" +
            ", " + COLUMN_STUDENT_ID + " INTEGER" +
            ", " + COLUMN_FIRST_NAME + " TEXT" +
            ", " + COLUMN_LAST_NAME + " TEXT" +
            ", " + COLUMN_STUDENT_USER_ROLE + " TEXT" +
            ", " + COLUMN_PHOTO_URL + " TEXT" +
            ", " + COLUMN_UPDATED_AT + " TEXT" +
            // ...
            ") ";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

}
