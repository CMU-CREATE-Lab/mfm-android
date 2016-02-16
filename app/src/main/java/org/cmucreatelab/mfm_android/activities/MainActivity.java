package org.cmucreatelab.mfm_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

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

        final Student[] mStudent = new Student[1];
        Group mGroup;

        String kioskID = "f6321b67d68cd8092806094f1d1f16c5";
        String studentsURL = "http://dev.messagefromme.org/api/v2/students?kiosk_uid=" + kioskID;
        String groupURL = "http://dev.messagefromme.org/api/v2/students?kiosk_uid=" + kioskID;


        Log.v(TAG, "before if statement");
        if (isNetworkAvailable()) {
            Log.v(TAG, "inside if conditional");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(studentsURL).build();
            Request request1 = new Request.Builder()
                    .url(groupURL).build();

            Call call = client.newCall(request);
            Call call1 = client.newCall(request1);

            Log.v(TAG, "api call made");
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
                            mStudent[0] = getStudentDetails(jsonStudentData);
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

    private Student getStudentDetails(String jsonStudentData) throws JSONException {
        JSONObject student = new JSONObject(jsonStudentData);
        String studentList = student.getString("rows");
        Log.i(TAG, "From JSON: " + studentList);

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
