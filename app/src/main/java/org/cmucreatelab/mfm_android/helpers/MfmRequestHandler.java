package org.cmucreatelab.mfm_android.helpers;

/**
 * Created by mike on 2/3/16.
 */
public class MfmRequestHandler {

    private GlobalHandler globalHandler;


    public MfmRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void requestListStudents() {
        // TODO /api/v2/students?kiosk_uid=$KIOSK_UID
    }


    public void requestStudent() {
        // TODO /api/v2/students/$1?kiosk_uid=$KIOSK_UID
    }


    public void requestListGroups() {
        // TODO /api/v2/groups?kiosk_uid=$KIOSK_UID
    }


    public void ping() {
        // TODO /api/v2/ping?kiosk_uid=$KIOSK_UID&app_version=1.2.10&os_version=android_4.5
    }


    public void requestListSchools() {
        // TODO /api/v2/schools?username=$MFM_USERNAME&password=$MFM_PASSWORD
    }


    public void login() {
        // TODO /api/v2/login?username=$MFM_USERNAME&password=$MFM_PASSWORD&with_school=$MFM_SCHOOL_ID&app_version=1.2.10&os_version=android_4.5
    }


    public void logout() {
        //TODO /api/v2/logout?kiosk_uid=$KIOSK_UID
    }

}
