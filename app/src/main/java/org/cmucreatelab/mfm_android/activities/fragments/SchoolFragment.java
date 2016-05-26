package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.SchoolAdapter;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolFragment extends Fragment {

    private static final String SCHOOL_KEY = "school";

    private Activity parentActivity;
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<School> mSchools;


    public SchoolFragment() {
        // Required empty public constructor
    }


    public static final SchoolFragment newInstance(ArrayList<School> schools) {
        SchoolFragment schoolFragment = new SchoolFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(SCHOOL_KEY, schools);
        schoolFragment.setArguments(bdl);
        return schoolFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_school, container, false);

        mSchools = (ArrayList<School>) this.getArguments().getSerializable(SCHOOL_KEY);

        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewSchool);
        this.gridView.setAdapter(new SchoolAdapter(rootView.getContext(), this.mSchools));
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    ((SchoolListener) parentActivity).onSchoolSelected(mSchools.get(position));
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG, "Error in SchoolFragment setOnItemClickListener: " + e.toString());
                }
            }
        });

        return rootView;
    }


    public interface SchoolListener {
        public void onSchoolSelected(School school);
    }

}
