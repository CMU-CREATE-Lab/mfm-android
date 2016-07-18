package org.cmucreatelab.mfm_android.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import org.cmucreatelab.mfm_android.classes.Group;

import java.util.ArrayList;


/**
 * Created by Steve on 7/15/2016.
 */
public class GestureDialogFragment extends DialogFragment {

    private static final String INDEX_KEY = "index_key";
    private static final String GROUP_KEY = "group_key";


    public static GestureDialogFragment newInstance(ArrayList<Group> g, int i) {
        Bundle args = new Bundle();
        GestureDialogFragment fragment = new GestureDialogFragment();
        args.putInt(INDEX_KEY, i);
        args.putSerializable(GROUP_KEY, g);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GestureDialog(getActivity(), (ArrayList<Group>) getArguments().getSerializable(GROUP_KEY), getArguments().getInt(INDEX_KEY));
    }

}
