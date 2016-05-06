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

public class ViewUsersActivity extends AppCompatActivity {

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        Bundle b = getIntent().getExtras();
        users = (ArrayList<User>) b.getSerializable(StudentFragment.USERS_KEY);

        FragmentManager fm= this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment usersFragment = UserFragment.newInstance(users);
        ft.add(R.id.usersScrollable, usersFragment, "user_fragment");
        ft.commit();
    }
}
