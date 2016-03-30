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

    // class attributes
    final private String appVersion = Constants.APP_VERSION;
    private int schoolId;
    private String schoolName;
    private String kioskUid;
    private String iosVersion; // TODO prepend version number with "android_" to distinguish in database
    private boolean isLoggedIn = false;

    // getters/setters
    public int getSchoolId() { return schoolId; }
    public String getSchoolName() { return schoolName; }
    public String getKioskUid() { return kioskUid; }
    public String getIosVersion() { return iosVersion; }
    public boolean isLoggedIn() { return isLoggedIn; }
    public String getAppVersion() { return appVersion; }
    public void setSchoolName(String name) { this.schoolName = name; }
    public void setKioskUid(String id) { this.kioskUid = id; }
    public void setIsLoggedIn(boolean loggedIn) { this.isLoggedIn = loggedIn; }

}
