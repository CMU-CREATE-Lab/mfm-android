package org.cmucreatelab.mfm_android.helpers.static_classes;

import java.util.HashMap;

/**
 * Created by mike on 2/2/16.
 */
public class Constants {

    public static final String LOG_TAG = "MfmAndroid";

    public static final String APP_PACKAGE_NAME = "org.cmucreatelab.mfm_android";

    public static final String APP_VERSION = "1.0";

    public static final String MFM_API_URL = "http://dev.messagefromme.org";

    public static final int CAMERA_ID = 0;

    public static final class PreferencesKeys {
        public static final String kioskIsLoggedIn = "kiosk_logged_in";
        public static final String kioskSchoolId = "kiosk_school_id";
        public static final String kioskSchoolName = "kiosk_school_name";
        public static final String kioskUid = "kiosk_uid";
    }

    public static final HashMap<String, Object> DEFAULT_SETTINGS = new HashMap(){{
        put(PreferencesKeys.kioskIsLoggedIn, false);
        put(PreferencesKeys.kioskSchoolId, 0);
        put(PreferencesKeys.kioskSchoolName, "");
        put(PreferencesKeys.kioskUid, "");
    }};

}
