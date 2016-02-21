package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewStudentsActivity extends ListActivity {

    public static final String TAG = ViewStudentsActivity.class.getSimpleName();
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String updatedAt = mStudents[position].getUpdatedAt();

        //testing onListClick
        String message = String.format("Updated at %s", updatedAt);
        Toast.makeText(ViewStudentsActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
