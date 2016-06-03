package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.classes.BaseRefreshableActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.AppState;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

public class OrderByGroupActivity extends BaseRefreshableActivity {


    private BaseRefreshableActivity thisActivity;
    private GlobalHandler globalHandler;
    private ExtendedHeightGridView gridViewGroups;


    private final AdapterView.OnItemClickListener onGroupClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            thisActivity.onButtonClick(globalHandler.appContext);
            Intent intent = new Intent(thisActivity, OrderedActivity.class);
            intent.putExtra(Constants.GROUP_KEY, globalHandler.mfmLoginHandler.getSchool().getGroups().get(i));
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_group);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        setTitle(globalHandler.mfmLoginHandler.getSchool().getName() + " - " + getString(R.string.order_by_group));
        thisActivity = this;

        // display groups
        ArrayList<Group> groups = globalHandler.mfmLoginHandler.getSchool().getGroups();
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

        return super.onOptionsItemSelected(item);
    }

}
