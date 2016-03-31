package org.cmucreatelab.mfm_android.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by mike on 3/30/16.
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
        if (this.kioskIsLoggedIn) {
            String kioskUid = sharedPreferences.getString(Constants.PreferencesKeys.kioskUid, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskUid));
            String schoolName = sharedPreferences.getString(Constants.PreferencesKeys.kioskSchoolName, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolName));
            int schoolId = sharedPreferences.getInt(Constants.PreferencesKeys.kioskSchoolId, (Integer) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolId));
            School school = new School(schoolId, schoolName);
            this.kiosk = new Kiosk(school, kioskUid);
        }
    }


    public String getKioskUid() {
        return this.kiosk.getKioskUid();
    }


    public School getSchool() {
        return this.kiosk.getSchool();
    }


    public void login(School school, String kioskUid) {
        this.kioskIsLoggedIn = true;
        this.kiosk = new Kiosk(school, kioskUid);
        // set SharedPreferences values
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.PreferencesKeys.kioskIsLoggedIn, true);
        editor.putInt(Constants.PreferencesKeys.kioskSchoolId, school.getId());
        editor.putString(Constants.PreferencesKeys.kioskUid, kioskUid);
        editor.putString(Constants.PreferencesKeys.kioskSchoolName, school.getName());
        editor.apply();
    }


    public void logout() {
        this.kioskIsLoggedIn = false;
        this.kiosk.setSchool(null);
        this.kiosk.setKioskUid("");
        // set SharedPreferences values
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.PreferencesKeys.kioskIsLoggedIn, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskIsLoggedIn));
        editor.putInt(Constants.PreferencesKeys.kioskSchoolId, (Integer) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolId));
        editor.putString(Constants.PreferencesKeys.kioskUid, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskUid));
        editor.putString(Constants.PreferencesKeys.kioskSchoolName, (String) Constants.DEFAULT_SETTINGS.get(Constants.PreferencesKeys.kioskSchoolName));
        editor.apply();
    }

}
