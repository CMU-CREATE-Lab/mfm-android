package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.GroupFragment;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;

public class ViewStudentsInGroupActivity extends FragmentActivity {


    private static final String SERIALIZABLE_STUDENT_KEY = "students_group_key";


    // Call this before starting activity to initialize the list of students
    public static final Bundle setArguments(Group group) {
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(SERIALIZABLE_STUDENT_KEY, group);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_in_group);

        Group group = (Group) this.getIntent().getExtras().getSerializable(SERIALIZABLE_STUDENT_KEY);

        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        Fragment student = StudentFragment.newInstance(group.getStudents());
        ft.add(R.id.studentsScrollable, student, "students in group");
        ft.commit();
    }

}
