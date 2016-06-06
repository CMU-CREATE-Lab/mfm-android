package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSelectionActivity extends BaseSelectionActivity {


    private GlobalHandler globalHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        ButterKnife.bind(this);
        mSelectedUsers = new ArrayList<>();
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        // display users
        Student student = (Student) getIntent().getExtras().getSerializable(Constants.STUDENT_KEY);
        mUsers = student.getUsers();
        this.gridViewUsers = (ExtendedHeightGridView) findViewById(R.id.gridViewUser);
        this.gridViewUsers.setAdapter(new UserAdapter(getApplicationContext(), mUsers));
        this.gridViewUsers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        this.gridViewUsers.setOnItemClickListener(onUserClick);
    }


    @Override
    protected void onResume() {
        super.onResume();
        audioPlayer.addAudio(R.raw.send_your_message_to_short);
        audioPlayer.playAudio();
    }

    @OnClick(R.id.choose_button)
    public void onChoose() {
        super.onButtonClick(globalHandler.appContext);
        if (!mSelectedUsers.isEmpty()) {
            globalHandler.sessionHandler.setMessageRecipients(mSelectedUsers);
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
