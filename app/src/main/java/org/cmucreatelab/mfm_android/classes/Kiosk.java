package org.cmucreatelab.mfm_android.classes;

import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by mike on 2/2/16.
 *
 * Kiosk
 *
 * Object that represents the android app from within the MFM server. A Kiosk uses its uid to access data from its school.
 *
 */
public class Kiosk {

    private int schoolId;
    // TODO for iosVersion, prepend version number with "android_" to distinguish in database
    private String schoolName,kioskUid,iosVersion;
    final private String appVersion = Constants.APP_VERSION;
    private boolean isLoggedIn = false;

    public int getSchoolId() {
        return schoolId;
    }
    public String getSchoolName() {
        return schoolName;
    }
    public String getKioskUid() {
        return kioskUid;
    }
    public String getIosVersion() {
        return iosVersion;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public String getAppVersion() {
        return appVersion;
    }

    public void login(int schoolId, String schoolName, String kioskUid) {
        this.isLoggedIn = true;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.kioskUid = kioskUid;
    }

    public void logout() {
        this.isLoggedIn = false;
        this.schoolId = 0;
        this.schoolName = "";
        this.kioskUid = "";
    }

}
