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
    private Fragment audio;

    // class methods


    private void startTimer(final FragmentActivity activity) {
        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                FragmentManager fm = activity.getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(sessionInfo);
                ft.add(R.id.cameraFragment, camera, "camera");
                ft.commit();
            }
        };

        timer.schedule(task, 500);
    }


    public void pictureTaken() {
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(camera);
        ft.add(R.id.sessionInfoFragment, sessionInfo, "session_info");
        ft.commit();
    }


    public void recordAudio() {
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(sessionInfo);
        ft.add(R.id.audioFragment, audio, "audio");
        ft.commit();
    }


    public void audioRecorded() {
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(audio);
        ft.add(R.id.sessionInfoFragment, sessionInfo, "session_info");
        ft.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        sessionInfo = SessionInfoFragment.newInstance();
        camera = CameraFragment.newInstance();
        audio = AudioFragment.newInstance();
        ft.add(R.id.sessionInfoFragment, sessionInfo, "session_info");
        ft.commit();

        startTimer(this);
    }

}
