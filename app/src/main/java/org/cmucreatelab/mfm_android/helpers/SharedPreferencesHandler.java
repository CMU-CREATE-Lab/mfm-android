package org.cmucreatelab.mfm_android.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    // TODO should handle: Kiosk.isLoggedIn, Kiosk.schoolName, Kiosk.kioskUid


    public SharedPreferencesHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
    }

}
