package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.StudentList;

import java.util.Arrays;

public class ViewStudentsActivity extends ListActivity {

    private Student[] mStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.STUDENT_LIST);
        mStudents = Arrays.copyOf(parcelables, parcelables.length, Student[].class);
        StudentAdapter adapter = new StudentAdapter(this, mStudents);
        setListAdapter(adapter);
    }

}
