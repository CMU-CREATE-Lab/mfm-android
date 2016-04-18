package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;

public class ViewGroupsActivity extends ListActivity{

    private ArrayList<Group> mGroups = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        if (globalHandler.mfmLoginHandler.kioskIsLoggedIn) {
            mGroups = globalHandler.mfmLoginHandler.getSchool().getGroups();
        }
        GroupAdapter adapter = new GroupAdapter(this, mGroups);
        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Group group = mGroups.get(position);
        globalHandler.sessionHandler.startSession(group);

        // Making sure everything is populated
        Log.i(Constants.LOG_TAG, group.getStudentIds().toString());
        Log.i(Constants.LOG_TAG, group.getStudents().toString());
        Log.i(Constants.LOG_TAG, group.getName());
        Log.i(Constants.LOG_TAG, group.getUpdatedAt());
        Log.i(Constants.LOG_TAG, group.getPhotoUrl());
        Log.i(Constants.LOG_TAG, group.getSenderType().toString());
        Log.i(Constants.LOG_TAG, String.format("%d", group.getId()));
        Log.i(Constants.LOG_TAG, String.format("%d", group.getDatabaseId()));
    }

}
