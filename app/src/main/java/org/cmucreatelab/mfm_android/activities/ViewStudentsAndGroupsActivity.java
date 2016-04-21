package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.cmucreatelab.mfm_android.R;

public class ViewStudentsAndGroupsActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private Fragment studentFragment;
    private Fragment groupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_and_groups);

        this.fragmentManager = this.getFragmentManager();
        this.studentFragment = this.fragmentManager.findFragmentById(R.id.studentFragment);
        this.groupFragment = this.fragmentManager.findFragmentById(R.id.groupFragment);
    }

}
