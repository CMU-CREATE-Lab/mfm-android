package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.ViewUsersActivity;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 * Used to display a list of students.
 */
public class StudentFragment extends Fragment  {

    public static final String USERS_KEY = "users_key";
    private static final String STUDENTDS_KEY = "student_key";
    private View rootView;
    private ExtendedHeightGridView gridView;
    private ArrayList<Student> mStudents;


    public StudentFragment() {
        // empty
    }


    /**
     * Needs to be called before the fragment is displayed.
     * Is used to instantiate the list of students.
     */
    public static final Fragment newInstance(ArrayList<Student> students) {
        StudentFragment studentFragment = new StudentFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(STUDENTDS_KEY, students);
        studentFragment.setArguments(bdl);
        return studentFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_student, container, false);
        final GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mStudents = (ArrayList<Student>) this.getArguments().getSerializable(STUDENTDS_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewStudent);
        this.gridView.setAdapter(new StudentAdapter(rootView.getContext(), this.mStudents));

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Starts the user fragments once a student is selected.
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                globalHandler.sessionHandler.startSession(mStudents.get(position));
                Intent intent = new Intent(rootView.getContext(), ViewUsersActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(USERS_KEY, mStudents.get(position).getUsers());
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        return rootView;
    }

}
