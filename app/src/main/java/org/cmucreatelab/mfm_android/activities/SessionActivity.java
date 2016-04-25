package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.MessageAdapter;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;

public class SessionActivity extends AppCompatActivity {

    Student mStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_message);
        ButterKnife.bind(this);
        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        Intent intent = getIntent();
        Sender sender = globalHandler.sessionHandler.getMessageSender();
        if (sender.getSenderType() == Sender.Type.Student) {
            mStudent = (Student)sender;
        } else {
            Log.e(Constants.LOG_TAG, "SessionActivity onCreate: Sender is not of Type Student.");
        }
        MessageAdapter adapter = new MessageAdapter(this, mStudent);
        Log.i(Constants.LOG_TAG, "Finished creation of Session");
    }

}
