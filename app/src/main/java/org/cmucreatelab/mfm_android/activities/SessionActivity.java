package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.CameraFragment;
import org.cmucreatelab.mfm_android.activities.fragments.SessionInfoFragment;
import org.cmucreatelab.mfm_android.activities.fragments.UserFragment;
import org.cmucreatelab.mfm_android.classes.AudioRecorder;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.OnButtonClickAudio;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


// TODO - may add interfaces to the fragments that use this activity....similar to SelectionActivity
public class SessionActivity extends OnButtonClickAudio implements UserFragment.UserListener,
                                                                SessionInfoFragment.SessionInfoListener{

    private final String STUDENT_KEY = "student";
    private final String GROUP_KEY = "group";

    private GlobalHandler globalHandler;
    private Timer timer;    // used to delay the display of the camera fragment
    private TimerTask task;
    private Fragment camera;
    private Fragment sessionInfo;
    private Fragment users;
    private ArrayList<User> selectedUsers;
    private Sender mSender;
    private Student mStudent;
    private Group mGroup;
    private AudioRecorder audioRecorder;
    private AudioPlayer audioPlayer;

    // class methods


    private void replaceFragment(int id, Fragment fragment) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.show(fragment);
        ft.commit();
    }


    private void hideFragment(Fragment fragment) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    private void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }


    private void startTimer() {
        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                onPhoto();
            }
        };

        timer.schedule(task, 500);
    }


    // TODO listener
    public void pictureTakenOrCancelled() {
        hideFragment(camera);
        sessionInfo = SessionInfoFragment.newInstance();
        replaceFragment(R.id.session_info, sessionInfo);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        audioPlayer = AudioPlayer.newInstance(globalHandler.appContext);

        if (savedInstanceState == null) {
            mSender = globalHandler.sessionHandler.getMessageSender();

            if (mSender.getSenderType() == Sender.Type.student) {
                mStudent = (Student) mSender;
                onRecipients();
            } else {
                mGroup = (Group) mSender;
                startTimer();
            }
            audioRecorder = new AudioRecorder(globalHandler.appContext);
        } else {
            selectedUsers = new ArrayList<>();
            mStudent = (Student) savedInstanceState.getSerializable(STUDENT_KEY);
            mGroup = (Group) savedInstanceState.getSerializable(GROUP_KEY);
            camera = CameraFragment.newInstance();
            sessionInfo = SessionInfoFragment.newInstance();
            replaceFragment(R.id.session_info, sessionInfo);
            if (mStudent != null) {
                mSender = (Sender) mStudent;
            }
            audioRecorder = new AudioRecorder(globalHandler.appContext);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle out) {
        out.putSerializable(STUDENT_KEY, mStudent);
        out.putSerializable(GROUP_KEY, mGroup);
        audioPlayer.release();
    }


    @Override
    public void onUserSelected(User user, boolean isChecked, View v) {
        super.onButtonClick(globalHandler.appContext);
        ImageView chooseButton = (ImageView) findViewById(R.id.selection_done_selecting_users);
        if (isChecked) {
            selectedUsers.add(user);
            Log.i(Constants.LOG_TAG, "Selected " + user.getId() + " to be added to the recipients list.");
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.GREEN);
            Log.i(Constants.LOG_TAG, ((ViewGroup) v).getChildAt(0).toString());
            ((ViewGroup) v).getChildAt(0).setBackgroundDrawable(drawable);
        } else {
            selectedUsers.remove(user);
            Log.i(Constants.LOG_TAG, "Deselected " + user.getId() + " to be added to the recipients list.");
            ((ViewGroup) v).getChildAt(0).setBackgroundColor(Color.alpha(0));
        }

        if (!selectedUsers.isEmpty()) {
            chooseButton.setImageResource(R.drawable.choose_up);
        } else {
            chooseButton.setImageResource(R.drawable.choose_disabled);
        }
    }

    @Override
    public void onDoneSelectingUsers() {
        if (!selectedUsers.isEmpty()) {
            super.onButtonClick(globalHandler.appContext);
            globalHandler.sessionHandler.setMessageRecipients(selectedUsers);
            sessionInfo = SessionInfoFragment.newInstance();
            hideFragment(users);
            replaceFragment(R.id.session_info, sessionInfo);
            if (globalHandler.sessionHandler.getMessagePhoto() == null)
                startTimer();
        }
    }

    @Override
    public void onRecipients() {
        if (mSender.getSenderType() == Sender.Type.student) {
            selectedUsers = new ArrayList<>();
            if (sessionInfo != null)
                hideFragment(sessionInfo);
            users = UserFragment.newInstance(mStudent.getUsers());
            replaceFragment(R.id.session_users, users);

            // reset the message and audio
            globalHandler.sessionHandler.setMessagePhoto(null);
            globalHandler.sessionHandler.setMessageAudio(null);
        }
    }

    @Override
    public void onPhoto() {
        super.onButtonClick(globalHandler.appContext);
        camera = new CameraFragment().newInstance();
        if (sessionInfo != null)
            hideFragment(sessionInfo);
        replaceFragment(R.id.session_camera, camera);
    }

    @Override
    public void onAudio(AudioPlayer audioPlayer) {
        super.onButtonClick(globalHandler.appContext);
        if (!audioRecorder.isRecording) {
            audioPlayer.stop();
            ((ImageView) findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.button_up_talkstop);
            audioRecorder.startRecording();
        } else {
            ((ImageView) findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.soundwave_final);
            ((ImageView) findViewById(R.id.f_session_info_send)).setImageResource(R.drawable.send_up);
            audioRecorder.stopRecording();
            audioPlayer.addAudio(R.raw.press_the_green_button);
            audioPlayer.playAudio();
        }
    }

    @Override
    public void onSend() {
        super.onButtonClick(globalHandler.appContext);
        if (!audioRecorder.isRecording)
            globalHandler.sessionHandler.sendMessage();
    }
}
