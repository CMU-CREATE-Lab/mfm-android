package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.os.Bundle;

import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

/**
 * Created by Steve on 6/3/2016.
 */
public abstract class BaseRefreshableActivity extends BaseSelectionActivity {

    public boolean isStudentsDone;
    public boolean isGroupsDone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void refreshStudentsAndGroups() {
        GlobalHandler.getInstance(getApplicationContext()).refreshStudentsAndGroups(this);
    }


    public void populatedGroupsAndStudentsList() {
        if (isStudentsDone && isGroupsDone) {
            Intent intent = new Intent(this, StudentsGroupsActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void loginFailure() {
        // TODO - handle failures
    }


    public void logoutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
