package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.graphics.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.MessageAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.io.File;

import butterknife.ButterKnife;

public class RecordMessageActivity extends AppCompatActivity {

    Student mStudent;
    private GlobalHandler globalHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_message);
        ButterKnife.bind(this);
        this.globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        Intent intent = getIntent();
        mStudent = globalHandler.getIndividualStudentData();
        MessageAdapter adapter = new MessageAdapter(this, mStudent);
    }

}
