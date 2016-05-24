package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.GroupFragment;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.OnButtonClickAudio;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;


public class SelectionActivity extends OnButtonClickAudio implements GroupFragment.GroupListener,
                                                                    StudentFragment.StudentListener{

    private boolean isOrderByGroup;
    private Fragment students;
    private Fragment groups;
    private GlobalHandler globalHandler;
    private String schoolName;


    private void addFragment(int id, Fragment fragment) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.add(id, fragment);
        ft.show(fragment);
        ft.commit();
    }


    private void replaceFragment(int id, Fragment fragment) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.show(fragment);
        ft.commit();
    }


    private void hideFragment(Fragment fragment) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }


    // Shows the students based off of a selected group
    private void orderByGroup() {
        this.setTitle(schoolName + " - Groups");
        isOrderByGroup = true;
        hideFragment(students);
    }


    // Shows all the students and groups in a school
    private void showAll() {
        this.setTitle(schoolName +  " - All Students & Groups");
        isOrderByGroup = false;
        students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
        groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
        replaceFragment(R.id.selection_students_scrollable_container, students);
        replaceFragment(R.id.selection_groups_scrollable_container, groups);
    }


    public void logoutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        schoolName = globalHandler.mfmLoginHandler.getSchool().getName();

        if (savedInstanceState == null) {
            showAll();
        }
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
        if (!isOrderByGroup) {
            globalHandler.sessionHandler.startSession(group);
            Intent intent = new Intent(this, SessionActivity.class);
            startActivity(intent);
        } else {
            this.isOrderByGroup = false;
            String groupName = group.getName();
            this.setTitle(schoolName + " - " + groupName);
            ArrayList<Group> newGroupsList = new ArrayList<>();
            newGroupsList.add(group);
            groups = GroupFragment.newInstance(newGroupsList);
            students = StudentFragment.newInstance(group.getStudents());
            replaceFragment(R.id.selection_students_scrollable_container, students);
            replaceFragment(R.id.selection_groups_scrollable_container, groups);
        }
    }


    @Override
    public void onStudentSelected(Student student) {
        super.onButtonClick(globalHandler.appContext);
        globalHandler.sessionHandler.startSession(student);
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

}