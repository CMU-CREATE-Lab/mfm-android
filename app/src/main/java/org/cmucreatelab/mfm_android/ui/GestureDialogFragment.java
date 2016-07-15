package org.cmucreatelab.mfm_android.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.util.ArrayList;


/**
 * Created by Steve on 7/15/2016.
 */
public class GestureDialogFragment extends DialogFragment {

    private int index;
    private ArrayList<Group> groups;

    public GestureDialogFragment(int i, ArrayList<Group> g) {
        index = i;
        groups = g;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GestureDialog(getActivity());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        GlobalHandler.getInstance(getActivity()).sessionHandler.startSession(groups.get(index));
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        this.startActivity(intent);
    }
}
