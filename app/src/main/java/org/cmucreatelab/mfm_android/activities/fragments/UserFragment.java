package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.views.ExtendedHeightGridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private static final String SERIALIZABLE_KEY = "user_key";
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<User> users;


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
        GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            users = (ArrayList<User>) this.getArguments().getSerializable(SERIALIZABLE_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewUser);
        this.gridView.setAdapter(new UserAdapter(rootView.getContext(), this.users));

        // TODO
        /*this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(rootView.getContext(), SessionActivity.class);
                startActivity(intent);
            }
        });*/
        return rootView;
    }

}
