package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.activities.ViewStudentsAndGroupsActivity;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 * Used to display a list of groups.
 */
public class GroupFragment extends Fragment {


    private static final String SERIALIZABLE_GROUP_KEY = "group_key";
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<Group> mGroups;
    private Activity parentActivity;

    public GroupFragment() {
        // Required empty public constructor
    }


    /**
    * Needs to be called before the fragment is displayed.
    * Is used to instantiate the list of groups.
    *
    * We might not need to instantiate the groups because there may never be an
    * instance where a different list of groups is displayed. (it may always be the schools groups)
    * but I figured this is good practice just in case.
    */
    public static final GroupFragment newInstance(ArrayList<Group> groups) {
        GroupFragment studentFragment = new GroupFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(SERIALIZABLE_GROUP_KEY, groups);
        studentFragment.setArguments(bdl);
        return studentFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
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
                try {
                    ((GroupListener) parentActivity).onGroupSelected(position);
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, "Error in GroupFragment setOnItemClickListener: " + e.toString());
                }
            }
        });

        return rootView;
    }


    // This is used by the parent activity to make a decision based off of the main_menu
    public interface GroupListener {
        public void onGroupSelected(int position);
    }

}
