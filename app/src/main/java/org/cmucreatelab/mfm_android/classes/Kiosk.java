package org.cmucreatelab.mfm_android.classes;

import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by mike on 2/2/16.
 */
public class Kiosk {
    int schoolId;
    String schoolName;
    String kioskUid;
    final String appVersion = Constants.APP_VERSION;
    String iosVersion; // TODO prepend version number with "android_" to distinguish in database
    boolean isLoggedIn;

}
