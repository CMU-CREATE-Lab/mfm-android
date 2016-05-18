package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.GroupFragment;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.activities.fragments.UserFragment;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


// TODO make two containers for the group and student fragments
public class SelectionActivity extends AppCompatActivity implements GroupFragment.GroupListener,
                                                                    StudentFragment.StudentListener,
                                                                    UserFragment.UserListener{

    private boolean isOrderByGroup;
    private Fragment students;
    private Fragment groups;
    private Fragment users;
    private GlobalHandler globalHandler;
    private ArrayList<User> selectedUsers;


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
        Log.i(Constants.LOG_TAG, String.format("%b", students.isHidden()));
    }


    // Shows the students based off of a selected group
    private void orderByGroup() {
        isOrderByGroup = true;
        hideFragment(students);
    }


    // Shows all the students and groups in a school
    private void showAll() {
        isOrderByGroup = false;
        students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
        groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
        replaceFragment(R.id.selection_students_scrollable_container, students);
        replaceFragment(R.id.selection_groups_scrollable_container, groups);
        if (users != null) {
            hideFragment(users);
            findViewById(R.id.selection_done_selecting_users).setVisibility(View.INVISIBLE);
        }
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
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        if (savedInstanceState == null) {
            students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
            groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
            addFragment(R.id.selection_students_scrollable_container, students);
            addFragment(R.id.selection_groups_scrollable_container, groups);
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
        if (!isOrderByGroup) {
            globalHandler.sessionHandler.startSession(group);
            Intent intent = new Intent(this, SessionActivity.class);
            startActivity(intent);
        } else {
            hideFragment(groups);
            students = StudentFragment.newInstance(group.getStudents());
            addFragment(R.id.selection_students_scrollable_container, students);
        }
    }


    @Override
    public void onStudentSelected(Student student) {
        globalHandler.sessionHandler.startSession(student);
        selectedUsers = new ArrayList<>();
        users = UserFragment.newInstance(student.getUsers());
        hideFragment(students);
        hideFragment(groups);
        addFragment(R.id.selection_users_scrollable_container, users);
    }


    @Override
    public void onUserSelected(User user, boolean isChecked, View v) {
        ImageView chooseButton = (ImageView) findViewById(R.id.selection_done_selecting_users);
        if (isChecked) {
            selectedUsers.add(user);
            Log.i(Constants.LOG_TAG, "Selected " + user.getId() + " to be added to the recipients list.");
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.GREEN);
            v.setBackgroundDrawable(drawable);
        } else {
            selectedUsers.remove(user);
            Log.i(Constants.LOG_TAG, "Deselected " + user.getId() + " to be added to the recipients list.");
            v.setBackgroundColor(Color.alpha(0));
        }

        if (!selectedUsers.isEmpty()) {
            chooseButton.setImageResource(R.drawable.choose_up_160x181px);
        } else {
            chooseButton.setImageResource(R.drawable.choose_disabled_160x181px);
        }
    }


    @OnClick(R.id.selection_done_selecting_users)
    public void onDoneSelectingUsers() {
        if (!selectedUsers.isEmpty()) {
            globalHandler.sessionHandler.setMessageRecipients(selectedUsers);
            Intent intent = new Intent(this, SessionActivity.class);
            startActivity(intent);
        }
    }
}