package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import android.database.Cursor;
import org.cmucreatelab.mfm_android.classes.Group;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class GroupDbHelper {


    public static boolean destroy(Context context, Group group) {
        boolean result = false;

        // TODO destroy Group in database
        // TODO destroy relational StudentGroup

        return result;
    }


    public static void addToDatabase(Context context, Group group) {

        // TODO add Group to database
        // TODO group.setDatabaseId()
    }


    public static void update(Context context, Group group) {

        // TODO update Group in database
    }


    public static Group generateGroupFromRecord(Cursor cursor) {
        Group result = new Group();

        // TODO generate Group from record

        return result;
    }


    public static ArrayList<Group> fetchFromDatabase(Context context) {
        ArrayList<Group> result = new ArrayList<>();

        // TODO fetch all Groups from database

        return result;
    }

}
