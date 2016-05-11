package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment  {

    private static final String USERS_KEY = "users_key";
    private GlobalHandler globalHandler;
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<User> mUsers;
    private ArrayList<User> selected;


    public UserFragment() {

    }


    /**
     * Needs to be called before the fragment is displayed.
     * Is used to instantiate the list of students.
     */
    public static final Fragment newInstance(ArrayList<User> users) {
        UserFragment userFragment = new UserFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(USERS_KEY, users);
        userFragment.setArguments(bdl);
        return userFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_user, container, false);
        globalHandler = GlobalHandler.getInstance(rootView.getContext());
        selected = new ArrayList<>();
        ButterKnife.bind(this, rootView);

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mUsers = (ArrayList<User>) this.getArguments().getSerializable(USERS_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewUser);
        this.gridView.setAdapter(new UserAdapter(rootView.getContext(), mUsers));
        this.gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (gridView.isItemChecked(position)) {
                    selected.add(mUsers.get(position));
                    Log.i(Constants.LOG_TAG, "Selected " + mUsers.get(position).getId() + " to be added to the recipients list.");
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setStroke(5, Color.GREEN);
                    v.setBackgroundDrawable(drawable);
                } else {
                    selected.remove(mUsers.get(position));
                    Log.i(Constants.LOG_TAG, "Deselected " + mUsers.get(position).getId() + " to be added to the recipients list.");
                    v.setBackgroundColor(Color.alpha(0));
                }
            }
        });

        return rootView;
    }


    @OnClick(R.id.finishedSelectingUsers)
    public void finishedSelecting() {
        globalHandler.sessionHandler.setMessageRecipients(selected);
        Intent intent = new Intent(rootView.getContext(), CameraActivity.class);
        startActivity(intent);
    }

}