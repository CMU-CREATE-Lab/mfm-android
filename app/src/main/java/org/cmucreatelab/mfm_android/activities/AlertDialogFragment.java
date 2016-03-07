package org.cmucreatelab.mfm_android.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by mohaknahta on 1/30/16.
 */
public class AlertDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(Constants.AlertDialogFragment.title)
                .setMessage(Constants.AlertDialogFragment.message)
                .setPositiveButton(Constants.AlertDialogFragment.positiveButton, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

}
