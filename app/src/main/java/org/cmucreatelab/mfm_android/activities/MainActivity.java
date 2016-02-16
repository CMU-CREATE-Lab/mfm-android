package org.cmucreatelab.mfm_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Student[] mStudents = new Student[1];
        Group mGroup;

        String kioskID = "f6321b67d68cd8092806094f1d1f16c5";
        String studentsURL = "http://dev.messagefromme.org/api/v2/students?kiosk_uid=" + kioskID;
        String groupURL = "http://dev.messagefromme.org/api/v2/students?kiosk_uid=" + kioskID;


        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(studentsURL).build();
            Request request1 = new Request.Builder()
                    .url(groupURL).build();

            Call call = client.newCall(request);
            Call call1 = client.newCall(request1);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {
                        String jsonStudentData = response.body().string();
                        Log.v(TAG, jsonStudentData);
                        if (response.isSuccessful()) {
                            Log.v(TAG, "Students: ");
                            mStudents[0] = getStudentsDetails(jsonStudentData);
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });

            call1.enqueue(new Callback() {
                @Override
                public void onFailure(Call call1, IOException e) {
                }

                @Override
                public void onResponse(Call call1, Response response) throws IOException {

                    try {

                        if (response.isSuccessful()) {
                            Log.v(TAG, "Groups: ");
                            Log.v(TAG, response.body().string());
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Network Is Unavailable!", Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Main UI code is running!");

    }

    private Student getStudentsDetails(String jsonStudentData) throws JSONException {

        JSONObject students = new JSONObject(jsonStudentData);
        JSONArray studentList = students.optJSONArray("rows");
        Student student = new Student();

        LinearLayout linearlayout = new LinearLayout(this);
        setContentView(linearlayout);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        for (int i =0; i < studentList.length(); i++){

            JSONObject studentNode = studentList.getJSONObject(i);
            long ID = student.getDatabaseId(studentNode);
            String photoUrl = student.getPhotoUrl(studentNode);
            student.setDatabaseId(ID);
            student.setPhotoUrl(photoUrl);
            student.setUdpatedAt(student.getUdpatedAt(studentNode));

            TextView textview = new TextView(this);
            textview.setText(Long.toString(ID));
            linearlayout.addView(textview);
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
