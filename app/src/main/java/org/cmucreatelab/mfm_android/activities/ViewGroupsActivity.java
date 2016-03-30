package org.cmucreatelab.mfm_android.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import java.util.ArrayList;

public class ViewGroupsActivity extends ListActivity{

    private ArrayList<Group> mGroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        mGroups = globalHandler.getGroupData();
        GroupAdapter adapter = new GroupAdapter(this, mGroups);
        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Group group = mGroups.get(position);
        globalHandler.setIndividualGroup(group);
    }

}
