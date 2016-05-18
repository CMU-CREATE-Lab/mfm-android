package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.AudioFragment;
import org.cmucreatelab.mfm_android.activities.fragments.CameraFragment;
import org.cmucreatelab.mfm_android.activities.fragments.SessionInfoFragment;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.Timer;
import java.util.TimerTask;


// TODO make the audio and camera activities fragments
// TODO display the picture after the picture was taken
public class SessionActivity extends FragmentActivity {

    // used to delay the display of the camera fragment
    private Timer timer;
    private TimerTask task;
    private GlobalHandler globalHandler;
    private Fragment camera;
    private Fragment sessionInfo;

    // class methods


    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.session_fragment, fragment);
        ft.commit();
    }


    private void startTimer() {
        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                startCamera();
            }
        };

        timer.schedule(task, 100);
    }


    public void startCamera() {
        camera = new CameraFragment().newInstance();
        replaceFragment(camera);
    }



    public void pictureTakenOrCancelled() {
        sessionInfo = SessionInfoFragment.newInstance();
        replaceFragment(sessionInfo);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        if (savedInstanceState == null) {
            sessionInfo = SessionInfoFragment.newInstance();
            replaceFragment(sessionInfo);
            startTimer();
        }
    }

}
