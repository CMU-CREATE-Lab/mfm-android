package org.cmucreatelab.mfm_android.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.SettingsActivity;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.AppState;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentsGroupsActivity extends BaseRefreshableActivity {


    private BaseRefreshableActivity thisActivity;
    private GlobalHandler globalHandler;
    private ExtendedHeightGridView gridViewStudents;
    private ExtendedHeightGridView gridViewGroups;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_groups);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        thisActivity = this;
        ButterKnife.bind(this);

        if (globalHandler.isNetworkConnected(this)) {
            if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
                globalHandler.mfmLoginHandler.login(globalHandler.mfmLoginHandler.getSchool(), globalHandler.mfmLoginHandler.getKioskUid());
                setTitle(globalHandler.mfmLoginHandler.getSchool().getName() + " - " +  getString(R.string.all_students_and_groups));

                // display students
                ArrayList<Student> students = globalHandler.mfmLoginHandler.getSchool().getStudents();
                super.initStudents(students);
                this.gridViewStudents = (ExtendedHeightGridView) findViewById(R.id.gridViewStudent);
                this.gridViewStudents.setAdapter(new StudentAdapter(getApplicationContext(), students));
                this.gridViewStudents.setOnItemClickListener(onStudentClick);

                // display groups
                ArrayList<Group> groups = globalHandler.mfmLoginHandler.getSchool().getGroups();
                super.initGroups(groups);
                this.gridViewGroups = (ExtendedHeightGridView) findViewById(R.id.gridViewGroup);
                this.gridViewGroups.setAdapter(new GroupAdapter(getApplicationContext(), groups));
                this.gridViewGroups.setOnItemClickListener(onGroupClick);
            } else {
                globalHandler.appState = AppState.LOGIN;
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        } else {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Connect to the internet and re-open application.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            globalHandler.appState = AppState.LOGIN;
            globalHandler.mfmRequestHandler.logout(this);
            return true;
        } else if (id == R.id.orderByGroup) {
            globalHandler.appState = AppState.SELECTION_ORDER_BY_GROUP;
            Intent intent = new Intent(this, OrderByGroupActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.showAll) {
            globalHandler.appState = AppState.SELECTION_SHOW_ALL;
            return true;
        }
        else if (id == R.id.settings)
        {
            globalHandler.appState = AppState.SETTINGS;
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @OnClick(R.id.image_refresh)
    public void onClickRefresh() {
        Log.d(Constants.LOG_TAG, "onClickRefresh");
        onButtonClick(this);
        refreshStudentsAndGroups();
        finish();
        startActivity(getIntent());
    }

}
