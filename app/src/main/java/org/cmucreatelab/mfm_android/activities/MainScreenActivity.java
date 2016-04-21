package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreenActivity extends AppCompatActivity {


    public void logoutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void logoutFailure() {
        // TODO
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            GlobalHandler.getInstance(this.getApplicationContext()).mfmRequestHandler.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.studentListButton)
    public void startStudentActivity(View view) {
        // TODO temporarily changed the class to view students and groups
        Intent intent = new Intent(this, ViewStudentsAndGroupsActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.groupListButton)
    public void startGroupActivity(View view) {
        Intent intent = new Intent(this, ViewGroupsActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.cameraButton)
    public void startCameraActivity(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.audioButton)
    public void startAudioActivity(View view) {
        Intent intent = new Intent(this, AudioActivity.class);
        startActivity(intent);
    }

}