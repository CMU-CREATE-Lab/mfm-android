package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

public class ViewStudentsActivity extends ListActivity {

    private ArrayList<Student> mStudents = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mStudents = globalHandler.mfmLoginHandler.getSchool().getStudents();
        }
        StudentAdapter adapter = new StudentAdapter(this, mStudents);
        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Student student = mStudents.get(position);
        globalHandler.sessionHandler.startSession(student);

        // Making sure everything is populated
        Log.i(Constants.LOG_TAG, student.getUsers().toString());
        Log.i(Constants.LOG_TAG, student.getFirstName());
        Log.i(Constants.LOG_TAG, student.getName());
        Log.i(Constants.LOG_TAG, student.getLastName());
        Log.i(Constants.LOG_TAG, student.getPhotoUrl());
        Log.i(Constants.LOG_TAG, student.getUpdatedAt());
        Log.i(Constants.LOG_TAG, String.format("%d", student.getId()));
        Log.i(Constants.LOG_TAG, String.format("%d", student.getDatabaseId()));
        Log.i(Constants.LOG_TAG, student.getSenderType().toString());

        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

}
