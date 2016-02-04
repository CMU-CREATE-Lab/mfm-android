package org.cmucreatelab.mfm_android.helpers.static_classes;

import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
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

}
