/*
package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.GroupFragment;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.ClickableActivity;
import org.cmucreatelab.mfm_android.classes.Refreshable;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.AppState;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.FragmentHandler;

import java.util.ArrayList;


public class SelectionActivity extends ClickableActivity implements Refreshable,
                                                                    GroupFragment.GroupListener,
                                                                    StudentFragment.StudentListener{

    private static final String STUDENT_TAG = "student";
    private static final String GROUP_TAG = "group";
    private Fragment students;
    private Fragment groups;
    private GlobalHandler globalHandler;
    private String schoolName;
    private SwipeRefreshLayout swipeLayout;
    private Group selectedGroup;


    // Shows all the students and groups in a school
    private void showAll() {
        this.setTitle(schoolName + " - All Students & Groups");
        globalHandler.appState = AppState.SELECTION_SHOW_ALL;

        students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
        groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
        FragmentHandler.replaceFragment(this, R.id.selection_students_scrollable_container, students, STUDENT_TAG);
        FragmentHandler.replaceFragment(this, R.id.selection_groups_scrollable_container, groups, GROUP_TAG);
    }


    // Shows the students based off of a selected group
    private void orderByGroup() {
        this.setTitle(schoolName + " - Order By Group");
        globalHandler.appState = AppState.SELECTION_ORDER_BY_GROUP;

        groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
        FragmentManager fm = this.getFragmentManager();
        FragmentHandler.hideFragment(this, fm.findFragmentByTag(STUDENT_TAG));
        FragmentHandler.replaceFragment(this, R.id.selection_groups_scrollable_container, groups, GROUP_TAG);
    }


    private void showGroup() {
        this.setTitle(schoolName + " - " + selectedGroup.getName());
        globalHandler.appState = AppState.SELECTION_GROUP;

        ArrayList<Group> newGroupsList = new ArrayList<>();
        newGroupsList.add(selectedGroup);
        groups = GroupFragment.newInstance(newGroupsList);
        students = StudentFragment.newInstance(selectedGroup.getStudents());
        FragmentHandler.replaceFragment(this, R.id.selection_students_scrollable_container, students, STUDENT_TAG);
        FragmentHandler.replaceFragment(this, R.id.selection_groups_scrollable_container, groups, GROUP_TAG);
    }


    public void logoutSuccess() {
        */
/*Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*//*

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        schoolName = globalHandler.mfmLoginHandler.getSchool().getName();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.selection_swipe_layout);
        final Refreshable thisActivity = this;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        globalHandler.refreshStudentsAndGroups(thisActivity);
                        showAll();
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        if (savedInstanceState == null) {
            this.setTitle(schoolName + " - All Students & Groups");
            students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
            groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
            FragmentHandler.addFragment(this, R.id.selection_students_scrollable_container, students, STUDENT_TAG);
            FragmentHandler.addFragment(this, R.id.selection_groups_scrollable_container, groups, GROUP_TAG);
        } else {
            selectedGroup = (Group) savedInstanceState.getSerializable(GROUP_TAG);
            switch (globalHandler.appState) {
                case SELECTION_SHOW_ALL:
                    showAll();
                    break;
                case SELECTION_ORDER_BY_GROUP:
                    orderByGroup();
                    break;
                case SELECTION_GROUP:
                    showGroup();
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        out.putSerializable(GROUP_TAG, selectedGroup);
        super.onSaveInstanceState(out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            GlobalHandler.getInstance(this.getApplicationContext()).mfmRequestHandler.logout(this);
            finish();
            return true;
        } else if (id == R.id.orderByGroup) {
            orderByGroup();
            return true;
        } else if (id == R.id.showAll) {
            showAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Displays the students in the selected group.
    @Override
    public void onGroupSelected(Group group) {
        super.onButtonClick(globalHandler.appContext);
        if (globalHandler.appState == AppState.SELECTION_ORDER_BY_GROUP) {
            globalHandler.sessionHandler.startSession(group);
            Intent intent = new Intent(this, SessionActivity.class);
            startActivity(intent);
        } else {
            selectedGroup = group;
            showGroup();
        }
    }


    @Override
    public void onStudentSelected(Student student) {
        super.onButtonClick(globalHandler.appContext);
        globalHandler.sessionHandler.startSession(student);
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    // TODO - clean this up so I do not have to have empty overrides...
    // this was just a quick hack to get the lists to refresh
    @Override
    public void populatedGroupsAndStudentsList() {
        // empty
    }

    @Override
    public void requestListSchoolsSuccess(ArrayList<School> schools) {
        // empty
    }

    @Override
    public void loginSuccess() {
        // empty
    }

    @Override
    public void loginFailure() {
        // empty
    }

}*/
