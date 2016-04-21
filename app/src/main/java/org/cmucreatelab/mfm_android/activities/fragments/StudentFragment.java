package org.cmucreatelab.mfm_android.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {


    private View rootView;
    private GridView gridView;
    private ArrayList<Student> mStudents;


    public StudentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_student, container, false);
        GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mStudents = globalHandler.mfmLoginHandler.getSchool().getStudents();
        }
        this.gridView = (GridView) rootView.findViewById(R.id.gridViewStudent);
        this.gridView.setAdapter(new StudentAdapter(rootView.getContext(), this.mStudents));

        return rootView;
    }

}
