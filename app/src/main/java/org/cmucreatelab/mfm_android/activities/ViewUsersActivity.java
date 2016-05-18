package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.activities.fragments.UserFragment;
import org.cmucreatelab.mfm_android.classes.User;

import java.util.ArrayList;

// TODO - Decide what to do with this activity since nothing goes on in it...it's kind of useless
// I may just combine the user selection in the ViewStudentsAndGroups Activity and just remove fragments on the fly.
// I don't like all these activities when they are all part of the same process.
// In fact, I think I will make a Selection Activity (handles selecting items in lists) and then keep the LoginActivity and the Session Activity.
// LoginActivity will remain the same.
// SessionActivity will handle taking the picture, recording audio, and sending the message...since that what a session is.
public class ViewUsersActivity extends AppCompatActivity {

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        Bundle b = getIntent().getExtras();
        users = (ArrayList<User>) b.getSerializable(StudentFragment.USERS_KEY);

        if (savedInstanceState == null) {
            FragmentManager fm= this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment usersFragment = UserFragment.newInstance(users);
            ft.add(R.id.usersScrollable, usersFragment, "user_fragment");
            ft.commit();
        }
    }
}
