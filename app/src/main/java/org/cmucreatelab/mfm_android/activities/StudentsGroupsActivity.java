package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

public class StudentsGroupsActivity extends AppCompatActivity {


    private GlobalHandler globalHandler;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_groups);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            if (globalHandler.mfmLoginHandler.getSchool().getStudents().size() == 0) {
                globalHandler.mfmLoginHandler.login(globalHandler.mfmLoginHandler.getSchool(), globalHandler.mfmLoginHandler.getKioskUid());
            }
            // TODO - display the list of students and groups
            Log.d(Constants.LOG_TAG, "display list of students and groups");
        } else {
            Intent intent = new Intent(this, NewLoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            GlobalHandler.getInstance(this.getApplicationContext()).mfmRequestHandler.logout(this);
            return true;
        } else if (id == R.id.orderByGroup) {
            // TODO - handle ordering by group
            return true;
        } else if (id == R.id.showAll) {
            // TODO - handle showing everything
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void logoutSuccess() {
        Intent intent = new Intent(this, NewLoginActivity.class);
        startActivity(intent);
        finish();
    }

}
