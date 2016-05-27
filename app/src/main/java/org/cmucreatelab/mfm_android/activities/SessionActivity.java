package org.cmucreatelab.mfm_android.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.CameraFragment;
import org.cmucreatelab.mfm_android.activities.fragments.SessionInfoFragment;
import org.cmucreatelab.mfm_android.activities.fragments.UserFragment;
import org.cmucreatelab.mfm_android.helpers.AppState;
import org.cmucreatelab.mfm_android.helpers.AudioRecorder;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.OnButtonClickAudio;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.FragmentHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


// TODO - may add interfaces to the fragments that use this activity....similar to SelectionActivity
public class SessionActivity extends OnButtonClickAudio implements UserFragment.UserListener,
                                                                SessionInfoFragment.SessionInfoListener{

    private final String STUDENT_TAG = "student";
    private final String GROUP_TAG = "group";
    private final String CAMERA_TAG = "camera";
    private final String INFO_TAG = "info";
    private final String USER_TAG = "user";

    private Activity activity;
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

    public boolean isSent;

    // class methods


    private void showUsers() {
        globalHandler.appState = AppState.SESSION_USER;

        FragmentManager fm = this.getFragmentManager();
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(INFO_TAG));
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(CAMERA_TAG));
        users = UserFragment.newInstance(mStudent.getUsers());
        FragmentHandler.replaceFragment(this, R.id.session_users, users, USER_TAG);
    }


    private void showInfo() {
        globalHandler.appState = AppState.SESSION_INFO;

        FragmentManager fm = this.getFragmentManager();
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(USER_TAG));
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(CAMERA_TAG));
        sessionInfo = SessionInfoFragment.newInstance();
        FragmentHandler.replaceFragment(this, R.id.session_info, sessionInfo, INFO_TAG);
    }


    private void showCamera() {
        globalHandler.appState = AppState.SESSION_CAMERA;

        FragmentManager fm = this.getFragmentManager();
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(USER_TAG));
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(INFO_TAG));
        camera = CameraFragment.newInstance();
        FragmentHandler.replaceFragment(this, R.id.session_camera, camera, CAMERA_TAG);
    }


    public void pictureTaken() {
        audioPlayer.addAudio(R.raw.what_did_take);
        audioPlayer.playAudio();
        showInfo();
    }


    public void onRecipients() {
        if (mSender.getSenderType() == Sender.Type.student) {
            audioPlayer.stop();
            audioPlayer.addAudio(R.raw.send_your_message_to_short);
            audioPlayer.playAudio();
            showUsers();

            // reset the message and audio
            globalHandler.sessionHandler.setMessagePhoto(null);
            globalHandler.sessionHandler.setMessageAudio(null);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        activity = this;
        audioRecorder = new AudioRecorder(globalHandler.appContext);
        audioPlayer = AudioPlayer.newInstance(globalHandler.appContext);
        selectedUsers = new ArrayList<>();
        isSent = false;

        if (savedInstanceState == null) {
            mSender = globalHandler.sessionHandler.getMessageSender();

            sessionInfo = SessionInfoFragment.newInstance();
            camera = CameraFragment.newInstance();
            FragmentManager fm = this.getFragmentManager();
            FragmentHandler.addFragment(this, R.id.session_info, sessionInfo, INFO_TAG);
            FragmentHandler.addFragment(this, R.id.session_camera, camera, CAMERA_TAG);
            FragmentHandler.hideFragment(this, sessionInfo);
            FragmentHandler.hideFragment(this, camera);

            if (mSender.getSenderType() == Sender.Type.student) {
                mStudent = (Student) mSender;
                users = UserFragment.newInstance(mStudent.getUsers());
                FragmentHandler.addFragment(this, R.id.session_users, users, USER_TAG);
                FragmentHandler.hideFragment(this, users);
                onRecipients();
            } else {
                mGroup = (Group) mSender;
                showCamera();
            }
        } else {
            mStudent = (Student) savedInstanceState.getSerializable(STUDENT_TAG);
            mGroup = (Group) savedInstanceState.getSerializable(GROUP_TAG);
            switch (globalHandler.appState) {
                case SESSION_USER:
                    showUsers();
                    break;
                case SESSION_INFO:
                    showInfo();
                    break;
                case SESSION_CAMERA:
                    showCamera();
                    break;
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putSerializable(STUDENT_TAG, mStudent);
        out.putSerializable(GROUP_TAG, mGroup);
        audioPlayer.stop();
    }


    @Override
    public void onUserSelected(User user, boolean isChecked, View v) {
        super.onButtonClick(globalHandler.appContext);
        if (audioPlayer.playedBlueButton == false) {
            audioPlayer.stop();
            audioPlayer.addAudio(R.raw.press_the_blue_button);
            audioPlayer.playAudio();
            audioPlayer.playedBlueButton = true;
        }
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
            ((ImageView) findViewById(R.id.selection_done_selecting_users)).setImageResource(R.drawable.choose_down);
            audioPlayer.stop();
            super.onButtonClick(globalHandler.appContext);
            globalHandler.sessionHandler.setMessageRecipients(selectedUsers);
            showInfo();
            if (globalHandler.sessionHandler.getMessagePhoto() == null)
                showCamera();
        }
    }


    @Override
    public void onPhoto() {
        super.onButtonClick(globalHandler.appContext);
        audioPlayer.stop();
        showCamera();
    }

    @Override
    public void onAudio() {
        super.onButtonClick(globalHandler.appContext);
        if (!audioRecorder.isRecording) {
            audioPlayer.stop();
            ((ImageView) findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.button_up_talkstop);
            audioRecorder.startRecording();
        } else {
            ((ImageView) findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.soundwave_final);
            audioRecorder.stopRecording();

            // playback the audio clip you recorded
            File audioFile = globalHandler.sessionHandler.getMessageAudio();
            if (audioFile != null) {
                Uri uri = Uri.parse(audioFile.getAbsolutePath());
                Log.i(Constants.LOG_TAG, uri.toString());
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                        ((ImageView) findViewById(R.id.f_session_info_send)).setImageResource(R.drawable.send_up);
                        audioPlayer.addAudio(R.raw.press_the_green_button);
                        audioPlayer.playAudio();
                    }
                });
                try {
                    mediaPlayer.setDataSource(this.getApplicationContext(), uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast toast = Toast.makeText(globalHandler.appContext, "Replaying audio recorded.", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (IOException e) {
                    Log.e(Constants.LOG_TAG, e.toString());
                    Log.e(Constants.LOG_TAG, audioFile.getAbsolutePath());
                }

            }
        }
    }

    @Override
    public void onSend() {
        super.onButtonClick(globalHandler.appContext);
        audioPlayer.stop();
        if (!audioRecorder.isRecording)
            globalHandler.sessionHandler.sendMessage(this);
    }


    public void success() {
        Uri uri = Uri.parse("android.resource://" + globalHandler.appContext.getPackageName() + "/" + R.raw.your_message_has_been_sent);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                Intent intent = new Intent(globalHandler.appContext, SelectionActivity.class);
                startActivity(intent);
            }
        });
        try {
            mediaPlayer.setDataSource(globalHandler.appContext, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        isSent = true;
    }


    public void fail() {
        isSent = false;
        Toast toast = Toast.makeText(this, "Your message failed to send.", Toast.LENGTH_LONG);
        toast.show();
    }
}
