package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by Steve on 5/24/2016.
 *
 * // TODO - clean up this nonsense
 */
public interface Refreshable {
    public void populatedGroupsAndStudentsList();
    public void requestListSchoolsSuccess(ArrayList<School> schools);
    public void loginSuccess();
    public void loginFailure();
}
