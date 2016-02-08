package org.cmucreatelab.mfm_android.helpers.static_classes.database;

import android.content.Context;
import android.database.Cursor;
import org.cmucreatelab.mfm_android.classes.User;
import java.util.ArrayList;

/**
 * Created by mike on 2/8/16.
 */
public class UserDbHelper {


    public static boolean destroy(Context context, User user) {
        boolean result = false;

        // TODO destroy User in database

        return result;
    }


    public static void addToDatabase(Context context, User user) {

        // TODO add User to database
        // TODO user.setDatabaseId()
    }


    public static void update(Context context, User user) {

        // TODO update User in database
    }


    public static User generateUserFromRecord(Cursor cursor) {
        User result = new User();

        // TODO generate User from record

        return result;
    }


    public static ArrayList<User> fetchFromDatabase(Context context) {
        ArrayList<User> result = new ArrayList<>();

        // TODO fetch all Users from database

        return result;
    }

}
