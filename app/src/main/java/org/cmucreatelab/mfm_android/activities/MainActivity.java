package org.cmucreatelab.mfm_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Student[] mStudents = new Student[1];
        Group mGroup;

        String kioskID = "f6321b67d68cd8092806094f1d1f16c5";
        String studentsURL = "http://dev.messagefromme.org/api/v2/students?kiosk_uid=" + kioskID;
        String groupURL = "http://dev.messagefromme.org/api/v2/groups?kiosk_uid=" + kioskID;


        if (isNetworkAvailable()) {

            RequestQueue queue = Volley.newRequestQueue(this);
            //Call for students
            StringRequest studentRequest = new StringRequest(Request.Method.GET, studentsURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) throws IOException {
                    try {
                        String jsonStudentData = response.toString();
                        Log.v(TAG, "Students: ");
                        Log.v(TAG, jsonStudentData);

                        mStudents[0] = getStudentsDetails(jsonStudentData);
                    }
                    catch (Exception e){
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            queue.add(studentRequest);

            //Call for Groups
            StringRequest groupRequest = new StringRequest(Request.Method.GET, groupURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) throws IOException {
                    try {
                        String jsonGroupData = response.toString();
                        Log.v(TAG, "Groups: ");
                        Log.v(TAG, jsonGroupData);
                    }
                    catch (Exception e){
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(groupRequest);

//            ImageView imageView = (ImageView) findViewById(R.id.imageView);
//
//            Picasso.with(this)
//                    .load("https://cms-assets.tutsplus.com/uploads/users/21/posts/19431/featured_image/CodeFeature.jpg")
//                    .into(imageView);
//

        }

        else {
            Toast.makeText(this, "Network Is Unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    private Student getStudentsDetails(String jsonStudentData) throws JSONException {

        JSONObject students = new JSONObject(jsonStudentData);
        JSONArray studentList = students.optJSONArray("rows");
        Student student = new Student();

        //setup for layout
//        LinearLayout linearlayout = new LinearLayout(this);
//        setContentView(linearlayout);
//        linearlayout.setOrientation(LinearLayout.VERTICAL);


        for (int i =0; i < studentList.length(); i++){

            JSONObject studentNode = studentList.getJSONObject(i);
            student.setId(Integer.valueOf(studentNode.optString("id")));
            student.setPhotoUrl(studentNode.optString("thumb_photo_url"));
            student.setUdpatedAt(studentNode.optString("updated_at"));


            //UI
//            TextView textview = new TextView(this);
//            textview.setText(Integer.toString(student.getId()));
//            linearlayout.addView(textview);

        }


        return new Student();
    }

    //to handle cases when there is no network available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo  = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
