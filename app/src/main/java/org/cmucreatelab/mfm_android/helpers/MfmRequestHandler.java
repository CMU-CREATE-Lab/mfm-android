package org.cmucreatelab.mfm_android.helpers;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mike on 2/3/16.
 *
 * MfmRequestHandler
 *
 * Helper class for http requests to the Message from Me API
 *
 */
public class MfmRequestHandler {

    private GlobalHandler globalHandler;


    public MfmRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void requestListStudents() {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/students?kiosk_uid=" + globalHandler.kiosk.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Student> students = JSONParser.parseStudentsFromJson(response);
                    globalHandler.checkAndUpdateStudents(students);
                } catch (JSONException e) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for requestListStudents");
                }
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void updateStudent(final Student student) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/students/" + student.getId() + "?kiosk_uid=" + globalHandler.kiosk.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // parse values
                    JSONObject studentJson = response.getJSONObject("student");
                    String firstName = studentJson.getString("first_name");
                    String lastName = studentJson.getString("last_name");
                    String updatedAt = studentJson.getString("updated_at");
                    String thumbPhotoUrl = studentJson.getString("thumb_photo_url");
                    ArrayList<User> users = JSONParser.parseUsersFromJson(studentJson.getJSONArray("users"));

                    // update object
                    student.setFirstName(firstName);
                    student.setLastName(lastName);
                    student.setUpdatedAt(updatedAt);
                    student.setPhotoUrl(thumbPhotoUrl);
                    student.setUsers(users);
                } catch (JSONException e) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for updateStudent");
                }
                Log.i(Constants.LOG_TAG, "updateStudent onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void requestListGroups() {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/groups?kiosk_uid="+globalHandler.kiosk.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
                Log.i(Constants.LOG_TAG, "requestListGroups onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void ping() {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL +
                "/api/v2/ping?kiosk_uid=" + globalHandler.kiosk.getKioskUid() +
                "&app_version=" + globalHandler.kiosk.getAppVersion() +
                "&os_version=" + globalHandler.kiosk.getIosVersion();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
                Log.i(Constants.LOG_TAG, "ping onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void requestListSchools(String username, String password) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/schools?username="+username+"&password="+password;
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
                Log.i(Constants.LOG_TAG, "requestListSchools onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void login(String username, String password, String schoolId) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.POST;
        requestUrl = Constants.MFM_API_URL +
                "/api/v2/schools?username=" + username +
                "&password=" + password +
                "&with_school=" + schoolId +
                "&app_version=" + globalHandler.kiosk.getAppVersion() +
                "&os_version=" + globalHandler.kiosk.getIosVersion();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
                Log.i(Constants.LOG_TAG, "login onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void logout() {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.DELETE;
        requestUrl = Constants.MFM_API_URL + "/api/v2/logout?kiosk_uid="+globalHandler.kiosk.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
                Log.i(Constants.LOG_TAG, "logout onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }

}
