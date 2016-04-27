package org.cmucreatelab.mfm_android.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.fragments.GroupFragment;
import org.cmucreatelab.mfm_android.activities.fragments.StudentFragment;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

public class ViewStudentsAndGroupsActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_and_groups);

        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        Fragment students = StudentFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getStudents());
        Fragment groups = GroupFragment.newInstance(globalHandler.mfmLoginHandler.getSchool().getGroups());
        ft.add(R.id.studentFragmentContainer, students, "student fragment");
        ft.add(R.id.groupFragmentContainer, groups, "group fragment");
        ft.commit();
    }

}
