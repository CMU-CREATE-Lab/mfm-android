package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.MessageAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

public class RecordMessageActivity extends AppCompatActivity {

    Student mStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_message);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Intent intent = getIntent();
        mStudent = globalHandler.getIndividualStudentData();
        MessageAdapter adapter = new MessageAdapter(this, mStudent);

    }

}
