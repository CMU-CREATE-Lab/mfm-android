package org.cmucreatelab.mfm_android.classes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.NewLoginActivity;
import org.cmucreatelab.mfm_android.activities.StudentsGroupsActivity;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

/**
 * Created by Steve on 6/3/2016.
 */
public abstract class BaseRefreshableActivity extends AppCompatActivity {

    public boolean isStudentsDone;
    public boolean isGroupsDone;


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


    public void onButtonClick(Context context) {
        AudioPlayer audio = AudioPlayer.newInstance(context);
        audio.addAudio(R.raw.button_click);
        audio.playAudio();
    }


    public void loginFailure() {
        // TODO - handle failures
    }


    public void logoutSuccess() {
        Intent intent = new Intent(this, NewLoginActivity.class);
        startActivity(intent);
        finish();
    }

}
