package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
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
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

public class ViewStudentsAndGroupsActivity extends AppCompatActivity implements GroupFragment.GroupListener {

    private boolean isOrderByGroup;
    private Fragment students;
    private Fragment groups;
    private GlobalHandler globalHandler;


    // Shows the students based off of a selected group
    private void orderByGroup() {
        isOrderByGroup = true;
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (students != null) {
            ft.remove(students);
            students = null;
        }
        if (groups == null) {
            groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
            ft.add(R.id.studentsAndGroupsScrollable, groups, "group_fragment");
        }
        ft.commit();
    }


    // Shows all the students and groups in a school
    private void showAll() {
        isOrderByGroup = false;
        FragmentManager fm= this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (groups != null) {
            ft.remove(groups);
            groups = null;
        }
        if (students != null) {
            ft.remove(students);
            students = null;
        }

        students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
        groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
        ft.add(R.id.studentsAndGroupsScrollable, students, "student_fragment");
        ft.add(R.id.studentsAndGroupsScrollable, groups, "group_fragment");
        ft.commit();
    }


    public void logoutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_and_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        showAll();
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
    public void onGroupSelected(int position) {
        Group group = globalHandler.mfmLoginHandler.getSchool().getGroups().get(position);

        if (!isOrderByGroup) {
            globalHandler.sessionHandler.startSession(group);
            Intent intent = new Intent(this, SessionActivity.class);
            startActivity(intent);
        } else {
            FragmentManager fm= this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            students = StudentFragment.newInstance(group.getStudents());
            ft.add(R.id.studentsAndGroupsScrollable, students, "student_fragment");
            ft.remove(groups);
            groups = null;
            ft.commit();
        }
    }
}