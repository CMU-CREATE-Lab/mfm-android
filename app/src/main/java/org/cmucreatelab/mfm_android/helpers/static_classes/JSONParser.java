package org.cmucreatelab.mfm_android.helpers.static_classes;

import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
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
        Log.i(Constants.LOG_TAG, "parseGroupFromJson");
        // parse values
        Integer id = row.getInt("id");
        String updatedAt = row.getString("updated_at");
        String mediumPhotoUrl = row.getString("medium_photo_url");
        String thumbPhotoUrl = row.getString("thumb_photo_url");
        String mediumPolaroidUrl = row.getString("medium_polaroid_url");
        String thumbPolaroidUrl = row.getString("thumb_polaroid_url");

        // create object
        Group group = new Group();
        group.setId(id);
        group.setUpdatedAt(updatedAt);
        group.setPhotoUrl(thumbPhotoUrl);

        // students, studentids and name do not get taken care of here

        return group;
    }


    private static Student parseStudentFromJson(JSONObject row) throws JSONException {
        Log.i(Constants.LOG_TAG, "parseStudentFromJson");
        // parse values
        Integer id = row.getInt("id");
        String updatedAt = row.getString("updated_at");
        String mediumPhotoUrl = row.getString("medium_photo_url");
        String thumbPhotoUrl = row.getString("thumb_photo_url");
        String mediumPolaroidUrl = row.getString("medium_polaroid_url");
        String thumbPolaroidUrl = row.getString("thumb_polaroid_url");

        // create object
        Student student = new Student();
        student.setId(id);
        student.setUpdatedAt(updatedAt);
        student.setPhotoUrl(thumbPhotoUrl);

        // firstname, lastname and users do not get taken care of here

        return student;
    }


    private static User parseUserFromJson(JSONObject row) throws JSONException {
        Log.i(Constants.LOG_TAG, "parseUserFromJson");
        // parse values
        Integer id = row.getInt("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String updatedAt = row.getString("updated_at");
        String studentUserRole = row.getString("student_user_role");
        String mediumPhotoUrl = row.getString("medium_photo_url");
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


    private static ArrayList<Integer> parseStudentIdsInGroup(JSONObject row) throws JSONException {
        Log.i(Constants.LOG_TAG, "parseStudentIdsInGroup");
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
                Log.i(Constants.LOG_TAG, "parseGroupsFromJson");
                JSONArray rows = response.getJSONArray("rows");
                int size = rows.length();
                for (int i = 0; i < size; i++) {
                    results.add(parseStudentFromJson(rows.getJSONObject(i)));
                }
            }
        catch (JSONException e) {
            throw e;
        }

        return results;
    }


    public static ArrayList<Group> parseGroupsFromJson(JSONObject response) throws JSONException {
        ArrayList<Group> groups = new ArrayList<>();

        try {
            Log.i(Constants.LOG_TAG, "parseGroupsFromJson");
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
        Log.i(Constants.LOG_TAG, "parseUsersFromJson");
        ArrayList<User> results = new ArrayList<>();

        int size = users.length();
        for (int i = 0; i < size; i++) {
            JSONObject row = users.getJSONObject(i);
            results.add(parseUserFromJson(row));
        }

        return results;
    }


    public static Group parseGroupBasedOffId(JSONObject row, GlobalHandler globalHandler) throws JSONException {
        // parse values
        Log.i(Constants.LOG_TAG, "parseGroupBasedOffId");
        JSONObject groupJson = row.getJSONObject("student");
        Integer id = groupJson.getInt("id");
        String name = groupJson.getString("name");
        String updatedAt = groupJson.getString("updated_at");
        String thumbPhotoUrl = groupJson.getString("thumb_photo_url");
        ArrayList<Integer> studentIds = JSONParser.parseStudentIdsInGroup(groupJson);

        // create group
        Group result = new Group();
        result.setId(id);
        result.setName(name);
        result.setUpdatedAt(updatedAt);
        result.setPhotoUrl(thumbPhotoUrl);

        ArrayList<Student> groupStudents = new ArrayList<>();
        for (int i = 0; i < studentIds.size(); i++) {
            groupStudents.add(globalHandler.mfmLoginHandler.getSchool().checkForStudent(studentIds.get(i),globalHandler));
        }
        result.setStudents(groupStudents);

        return result;
    }


    public static Student parseStudentBasedOffId(JSONObject row) throws JSONException {
        // parse values
        Log.i(Constants.LOG_TAG, "parseStudentBasedOffId");
        JSONObject studentJson = row.getJSONObject("student");
        Integer id = studentJson.getInt("id");
        String firstName = studentJson.getString("first_name");
        String lastName = studentJson.getString("last_name");
        String updatedAt = studentJson.getString("updated_at");
        String mediumPhotoUrl = studentJson.getString("medium_photo_url");
        String thumbPhotoUrl = studentJson.getString("thumb_photo_url");
        String mediumPolaroidUrl = studentJson.getString("medium_polaroid_url");
        String thumbPolaroidUrl = studentJson.getString("thumb_polaroid_url");
        ArrayList<User> users = JSONParser.parseUsersFromJson(studentJson.getJSONArray("users"));

        // create student
        Student result = new Student();
        result.setId(id);
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setUpdatedAt(updatedAt);
        result.setPhotoUrl(thumbPhotoUrl);
        // set the student that belongs to each of the users
        for (User user : users) {
            user.setStudent(result);
        }
        result.setUsers(users);

        return result;
    }


    public static ArrayList<School> parseSchoolsFromJSON(JSONObject row) throws JSONException {
        Log.i(Constants.LOG_TAG, "parseSchoolsFromJSON");
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

        // students and groups are not added at this point

        return result;
    }

}