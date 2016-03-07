package org.cmucreatelab.mfm_android.helpers.static_classes;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mike on 1/28/16.
 *
 * JSONParser
 *
 * Responsible for parsing JSON strings into relevant classes/models.
 *
 */
public class JSONParser {


    private static Group parseGroupFromJson(JSONObject row) throws JSONException {
        Group group = new Group();

        // TODO parse fields: id, name, updated_at, medium_photo_url, thumb_photo_url, student_ids_in_group

        return group;
    }


    private static User parseUserFromJson(JSONObject row) throws JSONException {
        // parse values
        Integer id = row.getInt("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String updatedAt = row.getString("updated_at");
        String studentUserRole = row.getString("student_user_role");
        String thumbPhotoUrl = row.getString("thumb_photo_url");

        // create object
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUpdatedAt(updatedAt);
        user.setStudentUserRole(studentUserRole);
        user.setPhotoUrl(thumbPhotoUrl);
        return user;
    }


    public static ArrayList<Student> parseStudentsFromJson(JSONObject response) throws JSONException {
        ArrayList<Student> results = new ArrayList<>();

        try {
            JSONArray rows = response.getJSONArray("rows");
            int size = rows.length();
            for (int i = 0; i < size; i++) {
                // parse values
                JSONObject row = rows.getJSONObject(i);
                Integer id = row.getInt("id");
                String thumbPhotoUrl = row.getString("thumb_photo_url");
                String updatedAt = row.getString("updated_at");

                // create object
                Student student = new Student();
                student.setId(id);
                student.setPhotoUrl(thumbPhotoUrl);
                student.setUpdatedAt(updatedAt);

                // add to results
                results.add(student);
            }
        } catch (JSONException e) {
            throw e;
        }

        return results;
    }


    public static ArrayList<Group> parseGroupsFromJson(JSONObject response) throws JSONException {
        ArrayList<Group> groups = new ArrayList<>();

        try {
            JSONArray rows = response.getJSONArray("rows");
            int size = rows.length();
            for (int i = 0; i < size; i++) {
                groups.add(parseGroupFromJson(rows.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw e;
        }

        return groups;
    }


    public static ArrayList<User> parseUsersFromJson(JSONArray users) throws JSONException {
        ArrayList<User> results = new ArrayList<>();

        int size = users.length();
        for (int i = 0; i < size; i++) {
            JSONObject row = users.getJSONObject(i);
            results.add(parseUserFromJson(row));
        }

        return results;
    }

}
