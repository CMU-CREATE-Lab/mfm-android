package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.CameraFragment;
import org.cmucreatelab.mfm_android.activities.fragments.SessionInfoFragment;
import org.cmucreatelab.mfm_android.activities.fragments.UserFragment;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;


// TODO - may add interfaces to the fragments that use this activity....similar to SelectionActivity
public class SessionActivity extends FragmentActivity implements UserFragment.UserListener{

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


    public void startCamera() {
        camera = new CameraFragment().newInstance();
        if (sessionInfo != null)
            hideFragment(sessionInfo);
        replaceFragment(R.id.session_camera, camera);
    }



    public void pictureTakenOrCancelled() {
        hideFragment(camera);
        sessionInfo = SessionInfoFragment.newInstance();
        replaceFragment(R.id.session_info, sessionInfo);
    }


    public void chooseUsers() {
        if (mSender.getSenderType() == Sender.Type.Student) {
            findViewById(R.id.selection_done_selecting_users).setVisibility(View.VISIBLE);
            selectedUsers = new ArrayList<>();
            if (sessionInfo != null)
                hideFragment(sessionInfo);
            users = UserFragment.newInstance(mStudent.getUsers());
            replaceFragment(R.id.session_users, users);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            mSender = globalHandler.sessionHandler.getMessageSender();

            if (mSender.getSenderType() == Sender.Type.Student) {
                mStudent = (Student) mSender;
                chooseUsers();
            } else {
                mGroup = (Group) mSender;
                startTimer();
            }
        }
    }


    @Override
    public void onUserSelected(User user, boolean isChecked, View v) {
        ImageView chooseButton = (ImageView) findViewById(R.id.selection_done_selecting_users);
        if (isChecked) {
            selectedUsers.add(user);
            Log.i(Constants.LOG_TAG, "Selected " + user.getId() + " to be added to the recipients list.");
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.GREEN);
            v.setBackgroundDrawable(drawable);
        } else {
            selectedUsers.remove(user);
            Log.i(Constants.LOG_TAG, "Deselected " + user.getId() + " to be added to the recipients list.");
            v.setBackgroundColor(Color.alpha(0));
        }

        if (!selectedUsers.isEmpty()) {
            chooseButton.setImageResource(R.drawable.choose_up_160x181px);
        } else {
            chooseButton.setImageResource(R.drawable.choose_disabled_160x181px);
        }
    }

    @OnClick(R.id.selection_done_selecting_users)
    public void onDoneSelectingUsers() {
        if (!selectedUsers.isEmpty()) {
            globalHandler.sessionHandler.setMessageRecipients(selectedUsers);
            sessionInfo = SessionInfoFragment.newInstance();
            hideFragment(users);
            replaceFragment(R.id.session_info, sessionInfo);
            if (globalHandler.sessionHandler.getMessagePhoto() == null)
                startTimer();
        }
    }

}
