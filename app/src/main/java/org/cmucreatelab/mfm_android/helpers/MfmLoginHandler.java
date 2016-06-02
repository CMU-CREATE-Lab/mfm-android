package org.cmucreatelab.mfm_android.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.database.DbHelper;

/**
 * Created by mike on 3/30/16.
 *
 * MfmLoginHandler
 *
 * Represents the entire Logging in process and Kiosk state
 *
 */
public class MfmLoginHandler {

    private GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;
    private Kiosk kiosk;
    public boolean kioskIsLoggedIn;


    public MfmLoginHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        this.kioskIsLoggedIn = sharedPreferences.getBoolean(Constants.PreferencesKeys.kioskIsLoggedIn, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskIsLoggedIn));
        // get shared preference values and initialize the school.
        if (this.kioskIsLoggedIn) {
            String kioskUid = sharedPreferences.getString(Constants.PreferencesKeys.kioskUid, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskUid));
            String schoolName = sharedPreferences.getString(Constants.PreferencesKeys.kioskSchoolName, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolName));
            int schoolId = sharedPreferences.getInt(Constants.PreferencesKeys.kioskSchoolId, (Integer) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolId));
            School school = new School(schoolId, schoolName);
            this.kiosk = new Kiosk(school, kioskUid);
        }
    }


    public String getKioskUid() {
        if (kioskIsLoggedIn) {
            return this.kiosk.getKioskUid();
        } else {
            Log.w(Constants.LOG_TAG, "Requested KioskUid when Kiosk is not logged in.");
            return "";
        }
    }


    public School getSchool() {
        if (kioskIsLoggedIn) {
            return this.kiosk.getSchool();
        } else {
            Log.w(Constants.LOG_TAG, "Requested School when Kiosk is not logged in.");
            return null;
        }
    }


    public void login(School school, String kioskUid) {
        if (this.kiosk.getSchool() == null) {
            this.kioskIsLoggedIn = true;
            this.kiosk = new Kiosk(school, kioskUid);
            this.kiosk.setSchool(school);
            this.kiosk.setKioskUid(kioskUid);

            // set SharedPreferences values
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putBoolean(Constants.PreferencesKeys.kioskIsLoggedIn, true);
            editor.putInt(Constants.PreferencesKeys.kioskSchoolId, school.getId());
            editor.putString(Constants.PreferencesKeys.kioskUid, kioskUid);
            editor.putString(Constants.PreferencesKeys.kioskSchoolName, school.getName());
            editor.apply();

            // load from database
            DbHelper.loadFromDb(globalHandler.appContext);
        }
    }


    public void logout() {
        DbHelper.clearAll(globalHandler);

        this.kioskIsLoggedIn = false;
        if (this.kiosk != null) {
            this.kiosk.setSchool(null);
            this.kiosk.setKioskUid("");
        }
        // set SharedPreferences values
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.PreferencesKeys.kioskIsLoggedIn, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskIsLoggedIn));
        editor.putInt(Constants.PreferencesKeys.kioskSchoolId, (Integer) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolId));
        editor.putString(Constants.PreferencesKeys.kioskUid, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskUid));
        editor.putString(Constants.PreferencesKeys.kioskSchoolName, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolName));
        editor.apply();
    }

}
