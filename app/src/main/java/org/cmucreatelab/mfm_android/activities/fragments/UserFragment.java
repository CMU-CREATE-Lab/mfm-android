package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
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
    private Activity parentActivity;
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<User> mUsers;
    private AudioPlayer audioPlayer;


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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
        globalHandler = GlobalHandler.getInstance(parentActivity.getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_user, container, false);
        final ImageView chooseButton = (ImageView) rootView.findViewById(R.id.selection_done_selecting_users);
        this.audioPlayer = AudioPlayer.newInstance(globalHandler.appContext);
        ButterKnife.bind(this, rootView);

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mUsers = (ArrayList<User>) this.getArguments().getSerializable(USERS_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewUser);
        this.gridView.setAdapter(new UserAdapter(rootView.getContext(), mUsers));
        this.gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((UserListener) parentActivity).onUserSelected(mUsers.get(position), gridView.isItemChecked(position), v);
            }
        });
        chooseButton.setImageResource(R.drawable.choose_disabled);

        return rootView;
    }


    @OnClick(R.id.selection_done_selecting_users)
    public void doneSelectingUsers() {
        ((ImageView) rootView.findViewById(R.id.selection_done_selecting_users)).setImageResource(R.drawable.choose_down);
        ((UserListener) parentActivity).onDoneSelectingUsers();
    }


    // This is used by the parent activity to make a decision based off of the main_menu
    public interface UserListener {
        public void onUserSelected(User user, boolean isChecked, View v);
        public void onDoneSelectingUsers();
    }

}