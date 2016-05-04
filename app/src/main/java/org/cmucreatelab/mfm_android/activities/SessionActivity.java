package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.GroupFragment;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.activities.fragments.UserFragment;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionActivity extends FragmentActivity {

    private Student mStudent;
    private Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        Sender sender = globalHandler.sessionHandler.getMessageSender();
        if (sender.getSenderType() == Sender.Type.Student) {
            FragmentManager fm= this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mStudent = (Student)sender;
            Fragment user = UserFragment.newInstance(mStudent.getUsers());
            ft.add(R.id.itemScrollable, user, "user fragment");
            ft.commit();
        } else {
            mGroup = (Group) sender;
        }


    }

    @OnClick(R.id.cameraButton)
    public void startCameraActivity(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.audioButton)
    public void startAudioActivity(View view) {
        Intent intent = new Intent(this, AudioActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sendMessageButton)
    public void sendMessage() {
        // TODO
    }

}
