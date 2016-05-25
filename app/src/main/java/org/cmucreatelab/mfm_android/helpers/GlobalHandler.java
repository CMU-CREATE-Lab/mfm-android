package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.cmucreatelab.mfm_android.activities.LoginActivity;
import org.cmucreatelab.mfm_android.classes.FormFile;
import org.cmucreatelab.mfm_android.classes.FormValue;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.Refreshable;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.ListHelper;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.DbHelper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by mike on 1/28/16.
 *
 * GlobalHandler
 *
 * Singleton object for storing application data structures.
 *
 */
public class GlobalHandler {

    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    public MfmRequestHandler mfmRequestHandler;
    public MfmLoginHandler mfmLoginHandler;
    public SessionHandler sessionHandler;
    public AppState appState;


    public void sendPost(byte[] photo, byte[] audio) {
        int requestMethod = Request.Method.POST;
        String requestUrl = Constants.MFM_API_URL + "/api/v2/message";

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("GlobalHandler", "Got response: "+response.toString());
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GlobalHandler", "Got ERROR: "+error.toString());
            }
        };

        String senderType = sessionHandler.getMessageSender().getSenderType().toString();
        Log.i(Constants.LOG_TAG, senderType);
        HashMap<String,FormValue> formElements = new HashMap<>();
        formElements.put("photo", new FormFile("photo.jpg","image/jpeg",photo));
        formElements.put("audio", new FormFile("audio.amr-nb","audio/amr-nb",audio));
        formElements.put("message_type",new FormValue(senderType));
        formElements.put("sender_id",new FormValue(Integer.toString(sessionHandler.getMessageSender().getId())));
        if (sessionHandler.getMessageSender().getSenderType() == Sender.Type.student) {
            formElements.put("recipients", new FormValue(Arrays.toString(sessionHandler.getRecipientsIds()).replace("[", "").replace("]", "").replace(" ", "")));
        }
        formElements.put("kiosk_uid", new FormValue(mfmLoginHandler.getKioskUid()));

        FormRequestHandler request = new FormRequestHandler(requestMethod,requestUrl,formElements,response,error);
        Volley.newRequestQueue(appContext).add(request);
    }


    // refresh the list of students and groups in a school
    public void refreshStudentsAndGroups(final Refreshable activity) {
        mfmRequestHandler.requestListStudents(activity);
        mfmRequestHandler.requestListGroups(activity);
    }


    public void checkAndUpdateStudents(ArrayList<Student> studentsFromMfmRequest, final Refreshable activity) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            ArrayList<Student> studentsFromDB = school.getStudents();

            // for ever student in the list of students from - mfmRequestHandler.requestListStudents();
            for (Student mfmStudent : studentsFromMfmRequest) {
                try {
                    Student dbStudent = ListHelper.findStudentWithId(studentsFromDB, mfmStudent.getId());
                    // If the student is not updated, then update the student.
                    if (!dbStudent.getUpdatedAt().equals(mfmStudent.getUpdatedAt())) {
                        Log.i(Constants.LOG_TAG, "dbStudent not up to date in checkAndUpdateStudents...updating.");
                        mfmRequestHandler.updateStudent(dbStudent);
                    }
                    // If the student was not found in the database.
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "No student found in the database that matched the mfmRequest. Adding to database");
                    school.addStudent(mfmStudent);
                    DbHelper.addToDatabase(appContext, mfmStudent);
                    mfmRequestHandler.updateStudent(mfmStudent);
                }
            }
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateStudents with Kiosk not logged in");
        }
        // To make sure the list of students and groups are populated before displaying them.
        // I would get empty lists every so often and would have to refresh the activity.
        if (activity.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName()))
            ((LoginActivity)activity).isStudentsDone = true;
        activity.populatedGroupsAndStudentsList();
    }


    public void checkAndUpdateGroups(ArrayList<Group> groupsFromMfmRequest, final Refreshable activity) {
        if (mfmLoginHandler.kioskIsLoggedIn) {
            School school = mfmLoginHandler.getSchool();
            ArrayList<Group> groupsFromDB = school.getGroups();

            // for ever group in the list of groups from - mfmRequestHandler.requestListGroups();
            for (Group mfmGroup : groupsFromMfmRequest) {
                try {
                    Group dbGroup = ListHelper.findGroupWithId(groupsFromDB, mfmGroup.getId());
                    // If the group is not updated, then update the group.
                    if (!dbGroup.getUpdatedAt().equals(mfmGroup.getUpdatedAt())) {
                        Log.i(Constants.LOG_TAG, "dbGroup not up to date in checkAndUpdateGroups...updating.");
                        mfmRequestHandler.updateGroup(dbGroup);
                    }
                    // If the group was not found in the database.
                } catch (Exception e) {
                    Log.i(Constants.LOG_TAG, "No group found in the database that matched the mfmRequest. Adding to database");
                    school.addGroup(mfmGroup);
                    DbHelper.addToDatabase(appContext, mfmGroup);
                    mfmRequestHandler.updateGroup(mfmGroup);
                }
            }
        } else {
            Log.e(Constants.LOG_TAG, "Tried to checkAndUpdateGroups with Kiosk not logged in");
        }
        // To make sure the list of students and groups are populated before displaying them.
        // I would get empty lists every so often and would have to refresh the activity.
        // I hate how I'm doing this...
        if (activity.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName()))
            ((LoginActivity)activity).isGroupsDone = true;
        activity.populatedGroupsAndStudentsList();
    }


    // Singleton Implementation


    private static GlobalHandler classInstance;
    public Context appContext;


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.httpRequestHandler = new HttpRequestHandler(this);
        this.mfmLoginHandler = new MfmLoginHandler(this);
        this.mfmRequestHandler = new MfmRequestHandler(this);
        this.sessionHandler = new SessionHandler(this);
        this.appState = AppState.SELECTION_SHOW_ALL;
        Kiosk.ioSVersion = Build.VERSION.RELEASE;
    }

}
