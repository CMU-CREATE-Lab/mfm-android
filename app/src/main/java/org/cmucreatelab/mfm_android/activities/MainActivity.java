package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.StudentList;
import org.cmucreatelab.mfm_android.classes.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Bundle mSavedInstanceState;
    private StudentList mStudentList;
    Group mGroup;
    public static final String STUDENT_LIST = "STUDENT_FORECAST";

//    @Bind(R.id.timeLabel) TextView mTimeLabel;
//    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
//    @Bind(R.id.humidityValue) TextView mHumidityValue;
//    @Bind(R.id.precipValue) TextView mPrecipValue;
//    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
//    @Bind(R.id.iconImageView) ImageView mIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        final Student[] mStudents = new Student[1];



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

                        mStudentList = parseStudentDetails(jsonStudentData);
                        /*
                        parseStudentDetails
                             |
                         StudentList[] list of Students
                               |
                               getStudentDetails - updates detail of each student
                               and populates StudentList
                                   |
                                   getAdditionalStudentDetails gets more details like name
                                      |
                                      getUsers() parses and updates the Userlist in Student class
                        */

                    } catch (Exception e) {
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
                    } catch (Exception e) {
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
//            Picasso.with(this)
//                    .load("https://cms-assets.tutsplus.com/uploads/users/21/posts/19431/featured_image/CodeFeature.jpg")
//                    .into(imageView);
//
        }

        else {
            Toast.makeText(this, "Network Is Unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    //StudentList is a list of students. Each item in the list contains a Student (see Student class
    //for more details).
    private StudentList parseStudentDetails(String jsonStudentData) throws JSONException {
        StudentList studentList = new StudentList();
        studentList.setStudentList(getStudentsDetails(jsonStudentData));

        return studentList;
    }

    private Student[] getStudentsDetails(String jsonData) throws JSONException {

        JSONObject data = new JSONObject(jsonData);
        JSONArray studentData = data.optJSONArray("rows");
        Student[] allStudents = new Student[studentData.length()];

        for (int i =0; i < studentData.length(); i++){

            JSONObject jsonStudent = studentData.getJSONObject(i);
            Student student = new Student();

            student.setId(Integer.valueOf(jsonStudent.optString("id")));
            student.setPhotoUrl(jsonStudent.optString("thumb_photo_url"));
            student.setUpdatedAt(jsonStudent.optString("updated_at"));
            getAdditionalStudentDetails(student);
            allStudents[i] = student;
        }

        return allStudents;
    }

    //Call for getting student names and additional details
    private void getAdditionalStudentDetails(final Student student) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(this);

        //will change when login screen implemented to have dynamic kiosk_id
        String studentDetailsURL = "http://dev.messagefromme.org/api/v2/students/" +
                Integer.toString(student.getId()) +
                "?kiosk_uid=f6321b67d68cd8092806094f1d1f16c5";

        StringRequest studentAddDetailsRequest = new StringRequest(Request.Method.GET,
                    studentDetailsURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) throws IOException {
                    try {
                        String jsonData = response.toString();
                        parseAdditionalStudentDetails(student, jsonData);

                    } catch (Exception e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

        queue.add(studentAddDetailsRequest);
    }

    private void parseAdditionalStudentDetails(Student student, String jsonData) throws JSONException {
        JSONObject data = new JSONObject(jsonData);
        JSONObject studentData = data.optJSONObject("student");
        student.setFirstName(studentData.optString("first_name"));
        student.setLastName(studentData.optString("last_name"));
        JSONArray jsonUsers = studentData.optJSONArray("users");
//        student.setUsers(getUsers(jsonUsers));
        //#############################################################################
        //Change to get and set Users instead of test
        student.setTest(getUsers(jsonUsers));
//        Log.v(TAG, "testing array type " + getUsers(jsonUsers));
    }


    private ArrayList<User> getUsers(JSONArray jsonUsers) throws JSONException {
       ArrayList<User> userArrayList = new ArrayList<>(jsonUsers.length());

        for (int i = 0; i<jsonUsers.length(); i++){
            JSONObject jsonUser  = jsonUsers.getJSONObject(i);
            User user = new User();
            user.setId(Integer.valueOf(jsonUser.optString("id")));
            user.setFirstName(jsonUser.optString("first_name"));
            user.setLastName(jsonUser.optString("last_name"));
            user.setUpdatedAt(jsonUser.optString("updated_at"));
            user.setStudentUserRole(jsonUser.optString("student_user_role"));
            user.setPhotoUrl(jsonUser.optString("medium_photo_url")); //MN: Note that setting medium photo here in photo url

            userArrayList.add(i, user);
        }
        return userArrayList;
    }
//    private User[] getUsers(JSONArray jsonUsers) throws JSONException {
//        User[] userArrayList = new User[jsonUsers.length()];
//
//        for (int i = 0; i<jsonUsers.length(); i++){
//            JSONObject jsonUser  = jsonUsers.getJSONObject(i);
//            User user = new User();
//            user.setId(Integer.valueOf(jsonUser.optString("id")));
//            user.setFirstName(jsonUser.optString("first_name"));
//            user.setLastName(jsonUser.optString("last_name"));
//            user.setUpdatedAt(jsonUser.optString("updated_at"));
//            user.setStudentUserRole(jsonUser.optString("student_user_role"));
//            user.setPhotoUrl(jsonUser.optString("medium_photo_url")); //MN: Note that setting medium photo here in photo url
//            userArrayList[i] = user;
//        }
//        return userArrayList;
//    }

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

    @OnClick(R.id.studentListButton)
    public void startStudentActivity(View view){
        Intent intent = new Intent(this, ViewStudentsActivity.class);
        intent.putExtra(STUDENT_LIST, mStudentList.getStudentList());
        startActivity(intent);
    }
}
