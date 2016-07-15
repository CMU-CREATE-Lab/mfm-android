package org.cmucreatelab.mfm_android.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.cmucreatelab.mfm_android.activities.CameraActivity;


/**
 * Created by Steve on 7/15/2016.
 */
public class GestureDialogFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GestureDialog(getActivity());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        this.startActivity(intent);
    }
}
