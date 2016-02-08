package org.cmucreatelab.mfm_android.helpers.static_classes.database;

/**
 * Created by mike on 2/5/16.
 */
public class StudentGroupContract {

    public static final String TABLE_NAME = "student_groups";

    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_GROUP_ID = "group_id";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            ", " + COLUMN_STUDENT_ID + " INTEGER" +
            ", " + COLUMN_GROUP_ID + " INTEGER" +
            // ...
            ") ";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

}
