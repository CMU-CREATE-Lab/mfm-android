package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.CameraFragment;
import org.cmucreatelab.mfm_android.activities.fragments.SessionInfoFragment;

import java.util.Timer;
import java.util.TimerTask;


// TODO - may add interfaces to the fragments that use this activity....similar to SelectionActivity
public class SessionActivity extends FragmentActivity {

    private Timer timer;    // used to delay the display of the camera fragment
    private TimerTask task;
    private Fragment camera;
    private Fragment sessionInfo;

    // class methods


    private void startTimer() {
        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                startCamera();
            }
        };

        timer.schedule(task, 250);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.session_fragment, fragment);
        ft.commit();
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

        if (savedInstanceState == null) {
            sessionInfo = SessionInfoFragment.newInstance();
            replaceFragment(sessionInfo);
            startTimer();
        }
    }

}
