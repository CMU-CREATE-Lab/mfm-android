package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.AppState;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

/**
 * Created by Steve on 6/3/2016.
 */
public abstract class BaseSelectionActivity extends BaseActivity {


    private BaseSelectionActivity mActivity;
    private GlobalHandler globalHandler;
    protected ArrayList<Student> mStudents;
    protected ArrayList<Group> mGroups;
    protected ArrayList<User> mUsers;
    protected ArrayList<User> mSelectedUsers;
    protected ExtendedHeightGridView gridViewUsers;


    protected AdapterView.OnItemClickListener onStudentClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mActivity.onButtonClick(globalHandler.appContext);
            globalHandler.sessionHandler.startSession(mStudents.get(i));
            Intent intent = new Intent(mActivity, UserSelectionActivity.class);
            intent.putExtra(Constants.STUDENT_KEY, mStudents.get(i));
            startActivity(intent);
        }
    };


    protected AdapterView.OnItemClickListener onGroupClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (globalHandler.appState == AppState.SELECTION_ORDER_BY_GROUP) {
                mActivity.onButtonClick(globalHandler.appContext);
                Intent intent = new Intent(mActivity, OrderedActivity.class);
                intent.putExtra(Constants.GROUP_KEY, mGroups.get(i));
                startActivity(intent);
            } else {
                globalHandler.sessionHandler.startSession(mGroups.get(i));
                Intent intent = new Intent(mActivity, CameraActivity.class);
                startActivity(intent);
            }
        }
    };


    protected final AdapterView.OnItemClickListener onUserClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mActivity.onButtonClick(globalHandler.appContext);
            User user = mUsers.get(i);
            // TODO - refactor audio

            ImageView chooseButton = (ImageView) findViewById(R.id.choose_button);
            Log.d(Constants.LOG_TAG, String.format("%b", gridViewUsers.isItemChecked(i)));
            if (gridViewUsers.isItemChecked(i)) {
                mSelectedUsers.add(user);
                Log.i(Constants.LOG_TAG, "Selected " + user.getId() + " to be added to the recipients list.");
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(50, Color.GREEN);
                Log.i(Constants.LOG_TAG, ((ViewGroup) view).getChildAt(0).toString());
                ((ViewGroup) view).getChildAt(0).setBackgroundDrawable(drawable);
            } else {
                mSelectedUsers.remove(user);
                Log.i(Constants.LOG_TAG, "Deselected " + user.getId() + " to be added to the recipients list.");
                ((ViewGroup) view).getChildAt(0).setBackgroundColor(Color.alpha(0));
            }

            if (!mSelectedUsers.isEmpty()) {
                chooseButton.setImageResource(R.drawable.choose_up);
            } else {
                chooseButton.setImageResource(R.drawable.choose_disabled);
            }
        }
    };


    public void initStudents(ArrayList<Student> students) {
        mStudents = students;
    }


    public void initGroups(ArrayList<Group> groups) {
        mGroups = groups;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.LOG_TAG, "initializing global handler in Base Activity");
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        mActivity = this;
    }

}
