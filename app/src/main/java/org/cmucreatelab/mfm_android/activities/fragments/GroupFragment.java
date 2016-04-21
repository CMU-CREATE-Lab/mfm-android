package org.cmucreatelab.mfm_android.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {


    private View rootView;
    private GridView gridView;
    private ArrayList<Group> mGroups;


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_group, container, false);
        GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mGroups = globalHandler.mfmLoginHandler.getSchool().getGroups();
        }
        this.gridView = (GridView) rootView.findViewById(R.id.gridViewGroup);
        this.gridView.setAdapter(new GroupAdapter(rootView.getContext(), this.mGroups));

        return rootView;
    }

}
