package org.cmucreatelab.mfm_android.helpers.static_classes.database;

/**
 * Created by mike on 2/5/16.
 *
 * GroupContract
 *
 * The contract information for a Group.
 */
public class GroupContract {

    public static final String TABLE_NAME = "groups";

    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHOTO_URL = "photo_url";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_GROUP_ID + " INTEGER" +
            ", " + COLUMN_NAME + " TEXT" +
            ", " + COLUMN_PHOTO_URL + " TEXT" +
            ", " + COLUMN_UPDATED_AT + " TEXT" +
            // ...
            ") ";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

}
