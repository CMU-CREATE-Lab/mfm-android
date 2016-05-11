package org.cmucreatelab.mfm_android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import butterknife.ButterKnife;
import butterknife.OnClick;


// TODO make the audio and camera activities fragments
// TODO display the picture after the picture was taken
public class SessionActivity extends FragmentActivity {

    private GlobalHandler globalHandler;
    private Student mStudent;
    private Group mGroup;

    // class methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        Sender sender = globalHandler.sessionHandler.getMessageSender();
        if (sender.getSenderType() == Sender.Type.Student) {
            mStudent = (Student)sender;
        } else {
            mGroup = (Group) sender;
        }
    }


    @OnClick(R.id.sendMessageButton)
    public void sendMessage() {
        globalHandler.sessionHandler.sendMessage();
    }

}
