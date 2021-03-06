package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.SettingsActivity;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.AppState;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

public class OrderByGroupActivity extends BaseRefreshableActivity {


    private GlobalHandler globalHandler;
    private ExtendedHeightGridView gridViewGroups;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_group);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        setTitle(globalHandler.mfmLoginHandler.getSchool().getName() + " - " + getString(R.string.order_by_group));

        // display groups
        ArrayList<Group> groups = globalHandler.mfmLoginHandler.getSchool().getGroups();
        super.initGroups(groups);
        this.gridViewGroups = (ExtendedHeightGridView) findViewById(R.id.gridViewGroup);
        this.gridViewGroups.setAdapter(new GroupAdapter(getApplicationContext(), groups));
        this.gridViewGroups.setOnItemClickListener(onGroupClick);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if(globalHandler.appState == AppState.SELECTION_SHOW_ALL) {
            finish();
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
            globalHandler.appState = AppState.LOGIN;
            globalHandler.mfmRequestHandler.logout(this);
            return true;
        } else if (id == R.id.orderByGroup) {
            globalHandler.appState = AppState.SELECTION_ORDER_BY_GROUP;
            return true;
        } else if (id == R.id.showAll) {
            globalHandler.appState = AppState.SELECTION_SHOW_ALL;
            finish();
            return true;
        }
        else if (id == R.id.settings)
        {
            globalHandler.appState = AppState.SETTINGS;
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
