package org.cmucreatelab.mfm_android.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.SchoolAdapter;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

public class SchoolActivity extends AppCompatActivity {


    private Activity thisActivity;
    private ExtendedHeightGridView gridView;
    private ArrayList<School> mSchools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        thisActivity = this;

        Bundle bundle = this.getIntent().getExtras();
        mSchools = (ArrayList<School>) bundle.getSerializable(Constants.SCHOOL_KEY);

        this.gridView = (ExtendedHeightGridView) findViewById(R.id.gridViewSchool);
        this.gridView.setAdapter(new SchoolAdapter(getApplicationContext(), this.mSchools));
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO - figure out a cleaner way to pass the school to NewLoginActivity
                NewLoginActivity.currentSchool = mSchools.get(position);
                finish();
            }
        });
    }

}
