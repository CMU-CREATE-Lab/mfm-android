package org.cmucreatelab.mfm_android.helpers;

import com.android.volley.Request;
import com.android.volley.Response;

import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.json.JSONObject;

/**
 * Created by mike on 2/3/16.
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
        requestUrl = Constants.MFM_API_URL + "/api/v2/students?kiosk_uid="+globalHandler.kiosk.getKioskUid();
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO response handling
            }
        };

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    public void requestStudent() {
        // TODO /api/v2/students/$1?kiosk_uid=$KIOSK_UID
    }


    public void requestListGroups() {
        // TODO /api/v2/groups?kiosk_uid=$KIOSK_UID
    }


    public void ping() {
        // TODO /api/v2/ping?kiosk_uid=$KIOSK_UID&app_version=1.2.10&os_version=android_4.5
    }


    public void requestListSchools() {
        // TODO /api/v2/schools?username=$MFM_USERNAME&password=$MFM_PASSWORD
    }


    public void login() {
        // TODO /api/v2/login?username=$MFM_USERNAME&password=$MFM_PASSWORD&with_school=$MFM_SCHOOL_ID&app_version=1.2.10&os_version=android_4.5
    }


    public void logout() {
        //TODO /api/v2/logout?kiosk_uid=$KIOSK_UID
    }

}
