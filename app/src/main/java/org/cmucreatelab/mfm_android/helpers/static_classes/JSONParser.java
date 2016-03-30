package org.cmucreatelab.mfm_android.helpers.static_classes;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.School;
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
        // parse values
        Integer id = row.getInt("id");
        String updatedAt = row.getString("updated_at");
        String thumbPhotoUrl = row.getString("thumb_photo_url");

        // create object
        Group group = new Group();
        group.setId(id);
        group.setUpdatedAt(updatedAt);
        group.setPhotoUrl(thumbPhotoUrl);

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

    public static ArrayList<Integer> parseStudentIdsInGroup(JSONObject row) throws JSONException {
        ArrayList<Integer> ids = new ArrayList<>();

        // parse values
        JSONArray values = row.getJSONArray("student_ids_in_group");
        int size = values.length();
        for (int i = 0; i < size; i++) {
            ids.add(values.getInt(i));
        }

        return ids;
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

    public static Group parseGroupBasedOffId(JSONObject row) throws JSONException {
        // parse values
        // should get "group"
        JSONObject groupJson = row.getJSONObject("student");
        String name = groupJson.getString("name");
        String updatedAt = groupJson.getString("updated_at");
        String thumbPhotoUrl = groupJson.getString("thumb_photo_url");
        ArrayList<Integer> studentIds = JSONParser.parseStudentIdsInGroup(groupJson);

        // create group
        Group result = new Group();
        result.setName(name);
        result.setStudentIds(studentIds);
        result.setUpdatedAt(updatedAt);
        result.setPhotoUrl(thumbPhotoUrl);
        result.setStudentIds(studentIds);

        return result;
    }

    public static Student parseStudentBasedOffId(JSONObject row) throws JSONException {
        // parse values
        JSONObject studentJson = row.getJSONObject("student");
        String firstName = studentJson.getString("first_name");
        String lastName = studentJson.getString("last_name");
        String updatedAt = studentJson.getString("updated_at");
        String thumbPhotoUrl = studentJson.getString("thumb_photo_url");
        ArrayList<User> users = JSONParser.parseUsersFromJson(studentJson.getJSONArray("users"));

        // create student
        Student result = new Student();
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setUpdatedAt(updatedAt);
        result.setPhotoUrl(thumbPhotoUrl);
        result.setUsers(users);

        return result;
    }

    public static ArrayList<School> parseSchoolsFromJSON(JSONObject row) throws JSONException {
        ArrayList<School> result = new ArrayList<>();

        JSONArray schools = row.getJSONArray("schools");

        // parse the separate schools
        for (int i = 0; i < schools.length(); i++) {
            JSONObject json = schools.getJSONObject(i);
            int id = json.getInt("id");
            String name = json.getString("name");
            School school = new School(id,name);
            result.add(school);
        }

        return result;
    }

}