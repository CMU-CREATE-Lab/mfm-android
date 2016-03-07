package org.cmucreatelab.mfm_android.helpers.static_classes;

/**
 * Created by mike on 2/2/16.
 */
public class Constants {

    public static final String LOG_TAG = "MfmAndroid";

    public static final String APP_PACKAGE_NAME = "org.cmucreatelab.mfm_android";

    public static final String APP_VERSION = "1.0";

    public static final String MFM_API_URL = "http://dev.messagefromme.org";

    public final class AlertDialogFragment {
        public static final String title = "Oops! sorry!";
        public static final String message = "There was an error please try again";
        public static final String positiveButton = "OK";
    }

    // TODO these are placeholder constants; mostly just for testing stuff
    public static final String kioskID = "f6321b67d68cd8092806094f1d1f16c5";
    public static final String studentsURL = "http://dev.messagefromme.org/api/v2/students?kiosk_uid=" + kioskID;
    public static final String groupURL = "http://dev.messagefromme.org/api/v2/groups?kiosk_uid=" + kioskID;

}
