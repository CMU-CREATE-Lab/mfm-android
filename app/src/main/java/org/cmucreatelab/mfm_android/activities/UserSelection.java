package org.cmucreatelab.mfm_android.activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

public class UserSelection extends BaseSelectionActivity {


    private BaseSelectionActivity thisActivity;
    private GlobalHandler globalHandler;


    private void onChoose() {
        if (!mSelectedUsers.isEmpty()) {
            // TODO - start next activity
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        thisActivity = this;
        mSelectedUsers = new ArrayList<>();

        // display users
        Student student = (Student) getIntent().getExtras().getSerializable(Constants.STUDENT_KEY);
        mUsers = student.getUsers();
        this.gridViewUsers = (ExtendedHeightGridView) findViewById(R.id.gridViewUser);
        this.gridViewUsers.setAdapter(new UserAdapter(getApplicationContext(), mUsers));
        this.gridViewUsers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        this.gridViewUsers.setOnItemClickListener(onUserClick);
    }

}
