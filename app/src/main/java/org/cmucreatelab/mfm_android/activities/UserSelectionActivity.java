package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSelectionActivity extends BaseSelectionActivity {

    private GlobalHandler globalHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        ButterKnife.bind(this);
        mSelectedUsers = new ArrayList<>();
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        // display users
        Student student = (Student) getIntent().getExtras().getSerializable(Constants.STUDENT_KEY);
        mUsers = student.getUsers();
        Collections.sort(mUsers, new Comparator<User>() {
            @Override
            public int compare(User user, User t1) {
                return user.getFirstName().compareToIgnoreCase(t1.getFirstName());
            }
        });

        this.gridViewUsers = (ExtendedHeightGridView) findViewById(R.id.gridViewUser);
        this.gridViewUsers.setAdapter(new UserAdapter(getApplicationContext(), mUsers));
        this.gridViewUsers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        this.gridViewUsers.setOnItemClickListener(onUserClick);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!audioPlayer.playedSendMessageTo) {
            audioPlayer.addAudio(R.raw.send_your_message_to_short);
            audioPlayer.playAudio();
            audioPlayer.playedSendMessageTo = true;
        }
    }

    @OnClick(R.id.choose_button)
    public void onChoose() {
        if (!mSelectedUsers.isEmpty()) {
            super.onButtonClick(globalHandler.appContext);
            audioPlayer.stop();
            globalHandler.sessionHandler.setMessageRecipients(mSelectedUsers);
            CameraActivity.cameraId = Constants.DEFAULT_CAMERA_ID;
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final ImageView chooseButton = (ImageView) findViewById(R.id.choose_button);
        final GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(50, Color.GREEN);
        gridViewUsers.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        for (int i = 0; i < gridViewUsers.getChildCount(); i++) {
                            if (gridViewUsers.isItemChecked(i)) {
                                ((ViewGroup) gridViewUsers.getChildAt(i)).getChildAt(0).setBackgroundDrawable(drawable);
                                if (!mSelectedUsers.contains(mUsers.get(i))) {
                                    mSelectedUsers.add(mUsers.get(i));
                                }
                            }
                        }
                        if (!mSelectedUsers.isEmpty()) {
                            chooseButton.setImageResource(R.drawable.choose_up);
                        } else {
                            chooseButton.setImageResource(R.drawable.choose_disabled);
                        }
                    }
                }
        );
    }

}
