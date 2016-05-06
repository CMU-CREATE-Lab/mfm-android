package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {


    private static final String SERIALIZABLE_GROUP_KEY = "group_key";
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<Group> mGroups;


    public GroupFragment() {
        // Required empty public constructor
    }


    public static final GroupFragment newInstance(ArrayList<Group> groups) {
        GroupFragment studentFragment = new GroupFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(SERIALIZABLE_GROUP_KEY, groups);
        studentFragment.setArguments(bdl);
        return studentFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_group, container, false);
        final GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mGroups = globalHandler.mfmLoginHandler.getSchool().getGroups();
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewGroup);
        this.gridView.setAdapter(new GroupAdapter(rootView.getContext(), this.mGroups));

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                globalHandler.sessionHandler.startSession(mGroups.get(position));
                Intent intent = new Intent(rootView.getContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
