package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private static final String SERIALIZABLE_KEY = "user_key";
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<User> users;
    private Collection<User> selectedUsers;


    public UserFragment() {
        // Required empty public constructor
    }


    public static final Fragment newInstance(ArrayList<User> users) {
        UserFragment userFragment = new UserFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(SERIALIZABLE_KEY, users);
        userFragment.setArguments(bdl);
        return userFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_user, container, false);
        final GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());
        this.selectedUsers = new ArrayList<>();

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            users = (ArrayList<User>) this.getArguments().getSerializable(SERIALIZABLE_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewUser);
        this.gridView.setAdapter(new UserAdapter(rootView.getContext(), this.users));
        this.gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        // TODO Should we add a button to confirm when we have selected the users we want?
        // For now I will just update the recipients each time a user is selected or deselcted
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (gridView.isItemChecked(position)) {
                    selectedUsers.add(users.get(position));
                    Log.i(Constants.LOG_TAG, "Selected " + users.get(position).toString() + " to be added to the recipients list.");
                } else {
                    selectedUsers.remove(users.get(position));
                    Log.i(Constants.LOG_TAG, "Removed " + users.get(position).toString() + " to be added to the recipients list.");
                }
                globalHandler.sessionHandler.setMessageRecipients(selectedUsers);
            }
        });
        return rootView;
    }

}
