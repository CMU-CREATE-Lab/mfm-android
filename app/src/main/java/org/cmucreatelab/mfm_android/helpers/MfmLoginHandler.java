package org.cmucreatelab.mfm_android.helpers;

import org.cmucreatelab.mfm_android.classes.Kiosk;
import org.cmucreatelab.mfm_android.classes.School;

/**
 * Created by mike on 3/30/16.
 */
public class MfmLoginHandler {

    private GlobalHandler globalHandler;
    private Kiosk kiosk;
    private boolean kioskIsLoggedIn;


    public MfmLoginHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.kiosk = new Kiosk();
        // TODO check values in SharedPreferencesHandler
    }


    public String getKioskUid() {
        return this.kiosk.getKioskUid();
    }


    public School getSchool() {
        return this.kiosk.getSchool();
    }


    public void login(School school, String kioskUid) {
        this.kioskIsLoggedIn = true;
        this.kiosk.setSchool(school);
        this.kiosk.setKioskUid(kioskUid);
        // TODO set values in SharedPreferencesHandler
    }


    public void logout() {
        this.kioskIsLoggedIn = false;
        this.kiosk.setSchool(null);
        this.kiosk.setKioskUid("");
        // TODO set values in SharedPreferencesHandler
    }

}
