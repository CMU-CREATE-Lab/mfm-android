package org.cmucreatelab.mfm_android.helpers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;

import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.json.JSONObject;

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
                // TODO response handling
                Log.i(Constants.LOG_TAG, "requestListStudents onResponse");
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void requestStudent(Student student) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;

        requestMethod = Request.Method.GET;
        requestUrl = Constants.MFM_API_URL + "/api/v2/students/" + student.getId() + "?kiosk_uid=" + globalHandler.kiosk.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
                Log.i(Constants.LOG_TAG, "requestStudent onResponse");
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

        requestMethod = Request.Method.GET;
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

        requestMethod = Request.Method.GET;
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
