package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import org.cmucreatelab.mfm_android.R;

public class ViewStudentsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);


        String[] tempStudentList = {"jack", "john", "neal", "peter"}; //change this to list of student names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                tempStudentList);
        setListAdapter(adapter);
    }

}
