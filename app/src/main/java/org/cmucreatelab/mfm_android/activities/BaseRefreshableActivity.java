package org.cmucreatelab.mfm_android.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

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
        Log.d(Constants.LOG_TAG, "Failed to login");
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed to login");
        builder.setMessage("Check internet connection and retry.");
        final Activity activity = this;
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    public void logoutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
