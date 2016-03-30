package org.cmucreatelab.mfm_android.helpers;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.mfm_android.activities.LoginActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
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
        requestUrl = Constants.MFM_API_URL + "/api/v2/students?kiosk_uid=" + globalHandler.mfmLoginHandler.getKioskUid();
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


    public void requestListGroups() {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/groups?kiosk_uid=" + globalHandler.mfmLoginHandler.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(Constants.LOG_TAG, "requestListGroups onResponse");
                try {
                    ArrayList<Group> groups = JSONParser.parseGroupsFromJson(response);
                    globalHandler.checkAndUpdateGroups(groups);
                } catch (JSONException ex) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for requestListGroups");
                }
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod,requestUrl,null,response);
    }


    public void updateStudent(final Student student) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/students/" + student.getId() + "?kiosk_uid=" + globalHandler.mfmLoginHandler.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(Constants.LOG_TAG, "updateStudent onResponse");
                try {
                    // create a temp student
                    Student temp = JSONParser.parseStudentBasedOffId(response);

                    // update object
                    student.setFirstName(temp.getFirstName());
                    student.setLastName(temp.getLastName());
                    student.setUpdatedAt(temp.getUpdatedAt());
                    student.setPhotoUrl(temp.getPhotoUrl());
                    student.setUsers(temp.getUsers());
                } catch (JSONException e) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for updateStudent");
                }
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void updateGroup(final Group group) {
        int requestMethod;
        String requestURL;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestURL = Constants.MFM_API_URL + "/api/v2/groups/" + group.getId() + "?kiosk_uid=" + globalHandler.mfmLoginHandler.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(Constants.LOG_TAG, "updateGroup onResponse");
                try {
                    // create temporary object
                    Group temp = JSONParser.parseGroupBasedOffId(response);

                    // set values
                    group.setName(temp.getName());
                    group.setStudentIds(temp.getStudentIds());
                    group.setPhotoUrl(temp.getPhotoUrl());
                    group.setUpdatedAt(temp.getUpdatedAt());
                    group.setStudentIds(temp.getStudentIds());

                    ArrayList<Student> result = new ArrayList<Student>();
                    ArrayList<Integer> ids = group.getStudentIds();
                    for (int i = 0; i < temp.getStudentIds().size(); i++) {
                        Student student = globalHandler.getStudentByID(ids.get(i));
                        result.add(student);
                    }
                    group.setStudents(result);
                } catch (JSONException e) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for updateGroup");
                }
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestURL, null, response);
    }


    public void ping() {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL +
                "/api/v2/ping?kiosk_uid=" + globalHandler.mfmLoginHandler.getKioskUid() +
                "&app_version=" + Kiosk.appVersion +
                "&os_version=" + Kiosk.ioSVersion;
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(Constants.LOG_TAG, "ping onResponse");
                try {
                    String success = response.getString("success");
                    String statusMessage = response.getString("status_message");
                    // TODO Do something with these variables
                } catch (JSONException e) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for ping.");
                }
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
                Log.i(Constants.LOG_TAG, "requestListSchools onResponse");
                try {
                    ArrayList<School> schools = JSONParser.parseSchoolsFromJSON(response);
                    // TODO we probably will send this to an Activity to display, rather than storing in GH
                    //globalHandler.schools = schools;
                } catch (JSONException e) {
                    Log.e(Constants.LOG_TAG, "JSONException in response for requestListSchools.");
                }
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void login(final LoginActivity login, String username, String password, String schoolId) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.POST;
        requestUrl = Constants.MFM_API_URL +
                "/api/v2/login?username=" + username +
                "&password=" + password +
                "&with_school=" + schoolId +
                "&app_version=" + Kiosk.appVersion +
                "&os_version=" + Kiosk.ioSVersion;
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(Constants.LOG_TAG, response.toString());
                try {
                    String success = response.getString("success");

                    if (success.equals("true")) {
                        int schoolId = response.getInt("school_id");
                        String schoolName = response.getString("school_name");
                        String kioskId = response.getString("kiosk_id");
                        School school = new School(schoolId,schoolName);

                        globalHandler.mfmLoginHandler.login(school, kioskId);
                        login.loginSuccess();
                    } else{
                        globalHandler.mfmLoginHandler.logout();
                        login.loginFailure();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        requestUrl = Constants.MFM_API_URL + "/api/v2/logout?kiosk_uid="+globalHandler.mfmLoginHandler.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(Constants.LOG_TAG, response.toString());
                globalHandler.mfmLoginHandler.logout();
                Log.i(Constants.LOG_TAG, "logout onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }

}