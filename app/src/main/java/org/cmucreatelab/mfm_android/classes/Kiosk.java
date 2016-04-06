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
    public static String appVersion = Constants.APP_VERSION;
    public static String ioSVersion = "android_";
    private School school;
    private String kioskUid;
    private String username;
    private String password;

    // getters/setters
    public School getSchool() { return school; }
    public String getKioskUid() { return kioskUid; }
    public String getAppVersion() { return appVersion; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setSchool(School school) { this.school = school; }
    public void setKioskUid(String id) { this.kioskUid = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }


    public Kiosk(School school, String kioskUid) {
        this.school = school;
        this.kioskUid = kioskUid;
    }

}
