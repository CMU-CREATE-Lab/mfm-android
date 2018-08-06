package org.cmucreatelab.mfm_android;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general_settings);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}