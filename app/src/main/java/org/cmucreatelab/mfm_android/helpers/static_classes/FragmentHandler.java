package org.cmucreatelab.mfm_android.helpers.static_classes;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by Steve on 5/25/2016.
 */
public class FragmentHandler {


    // Only use this method when you are first adding a fragment
    // Otherwise, use FragmentHandler.replaceFragment
    // If you do not do this, you will have multiple fragments added to the same fragment container
    public static void addFragment(Activity activity, int id, Fragment fragment, String tag) {
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.add(id, fragment, tag);
        ft.show(fragment);
        ft.commit();
    }


    public static void replaceFragment(Activity activity, int id, Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            ft.replace(id, fragment, tag);
            ft.show(fragment);
            ft.commit();
        }
    }


    public static void hideFragment(Activity activity, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }
    }
}
