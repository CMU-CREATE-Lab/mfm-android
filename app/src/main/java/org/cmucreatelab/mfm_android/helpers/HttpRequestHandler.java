package org.cmucreatelab.mfm_android.helpers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.json.JSONObject;

/**
 * Created by mike on 2/3/16.
 *
 * HttpRequestHandler
 *
 * Helper class for calls to the Volley library.
 *
 */
public class HttpRequestHandler implements Response.ErrorListener {

    private GlobalHandler globalHandler;
    private RequestQueue queue;


    public HttpRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.queue = Volley.newRequestQueue(globalHandler.appContext);
    }


    // helper method which assumes that you want to use this instance as the default ErrorListener
    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        sendJsonRequest(requestMethod, requestUrl, requestParams, response, this);
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
        if (requestParams != null) {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        // TODO - I couldn't use a dialog because of issues with getting a valid instance of an Activity and its context.  It shouldn't be hard to get working but for now I am just using a toast.
        Toast.makeText(globalHandler.appContext, "Check internet connection", Toast.LENGTH_LONG).show();
        /*AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(globalHandler.appContext, R.style.AppTheme));
        builder.setTitle("Http Request Error");
        builder.setMessage(error.getMessage() + "\n\nCheck connection status");
        dialog = builder.create();
        dialog.show();*/
    }

}
