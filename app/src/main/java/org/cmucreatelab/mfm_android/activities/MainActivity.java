package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.StudentList;
import org.cmucreatelab.mfm_android.helpers.CameraPreview;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private StudentList mStudentList;


    // to handle cases when there is no network available
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        if (isNetworkAvailable()) {
            globalHandler.mfmRequestHandler.login("stevefulton", "stevefulton", "17");
            globalHandler.refreshStudentsAndGroups();

        } else {
            Toast.makeText(this, "Network Is Unavailable!", Toast.LENGTH_LONG).show();
        }

    }


    @OnClick(R.id.studentListButton)
    public void startStudentActivity(View view){
        Intent intent = new Intent(this, ViewStudentsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.groupListButton)
    public void startGroupActivity(View view){
        Intent intent = new Intent(this, ViewGroupsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cameraButton)
    public void startCameraActivity(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.audioButton)
    public void startAudioActivity(View view){
        Intent intent = new Intent(this, AudioActivity.class);
        startActivity(intent);
    }

}