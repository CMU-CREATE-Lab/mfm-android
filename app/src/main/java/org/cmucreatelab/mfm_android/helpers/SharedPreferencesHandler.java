package org.cmucreatelab.mfm_android.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by mike on 2/8/16.
 *
 * SharedPreferencesHandler
 *
 * Stores persistent key-value pairs for the application.
 *
 */
public class SharedPreferencesHandler {

    private GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;
    private boolean kioskIsLoggedIn;
    private String kioskSchoolName;
    private int kioskUid;
    // TODO should handle: Kiosk.isLoggedIn, Kiosk.schoolName, Kiosk.kioskUid


    // should this be a singleton similar to SettingsHandler in Air-Quality-Tracker?
    public SharedPreferencesHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.v(Constants.LOG_TAG, "SHAREDPREFERENCES: "  + sharedPreferences.getAll().toString());
    }

    public SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    // Is there more to do with these methods?
    public void setIsLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.PreferencesKeys.kioskIsLoggedIn, isLoggedIn);
        editor.apply();
        this.kioskIsLoggedIn = isLoggedIn;
    }

    public void setSchoolName(String schoolName) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.PreferencesKeys.kioskSchoolName, schoolName);
        editor.apply();
        this.kioskSchoolName = schoolName;
    }

    public void setKioskUId(int id) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(Constants.PreferencesKeys.kioskId, id);
        editor.apply();
        this.kioskUid = id;
    }

}
