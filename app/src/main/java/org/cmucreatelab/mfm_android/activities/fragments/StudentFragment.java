package org.cmucreatelab.mfm_android.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
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
    private Activity parentActivity;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_student, container, false);
        GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mStudents = (ArrayList<Student>) this.getArguments().getSerializable(STUDENTDS_KEY);
        }
        this.gridView = (ExtendedHeightGridView) rootView.findViewById(R.id.gridViewStudent);
        this.gridView.setAdapter(new StudentAdapter(rootView.getContext(), this.mStudents));

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Starts the user fragments once a student is selected.
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ((StudentListener) parentActivity).onStudentSelected(mStudents.get(position));
            }
        });

        return rootView;
    }


    // This is used by the parent activity to make a decision based off of the main_menu
    public interface StudentListener {
        public void onStudentSelected(Student student);
    }

}
