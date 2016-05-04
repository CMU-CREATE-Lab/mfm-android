package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment  {

    private static final String SERIALIZABLE_KEY = "student_key";
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<Student> mStudents;


    public StudentFragment() {

    }


    public static final Fragment newInstance(ArrayList<Student> students) {
        StudentFragment studentFragment = new StudentFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(SERIALIZABLE_KEY, students);
        studentFragment.setArguments(bdl);
        return studentFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_student, container, false);
        final GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mStudents = (ArrayList<Student>) this.getArguments().getSerializable(SERIALIZABLE_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewStudent);
        this.gridView.setAdapter(new StudentAdapter(rootView.getContext(), this.mStudents));

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                globalHandler.sessionHandler.startSession(mStudents.get(position));
                Intent intent = new Intent(rootView.getContext(), SessionActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
