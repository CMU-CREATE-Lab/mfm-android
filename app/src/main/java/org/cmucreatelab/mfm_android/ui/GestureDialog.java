package org.cmucreatelab.mfm_android.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.Random;

/**
 * Created by Steve on 7/15/2016.
 */
public class GestureDialog extends AlertDialog{

    private static String title = "Complete the gesture to continue.";
    private static String appendToMessage = " anywhere outside of this box.";
    private static String[] messages = {"Double tap",
            "Swipe quickly",
            "Long press"};

    private boolean isDoubleTap = false;
    private boolean isFling = false;
    private boolean isLongPress = false;

    private GestureDetector gestureDetector;
    private MyGestureDetector myGestureDetector;

    public GestureDialog(Context context) {
        super(context);
        myGestureDetector = new MyGestureDetector();
        gestureDetector = new GestureDetector(myGestureDetector);
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                isDoubleTap = true;
                break;
            case 1:
                isFling = true;
                break;
            case 2:
                isLongPress = true;
                break;
            default:
                isDoubleTap = true;
                break;
        }
        this.setTitle(title);
        this.setMessage(messages[rand] + appendToMessage);
        this.setCanceledOnTouchOutside(false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            Log.d(Constants.LOG_TAG, "motion event");
            return true;
        } else {
            return false;
        }
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isDoubleTap) {
                Log.d(Constants.LOG_TAG, "onDoubleTab");
                dismiss();
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (isFling) {
                Log.d(Constants.LOG_TAG, "onFling");
                dismiss();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isLongPress) {
                Log.d(Constants.LOG_TAG, "onLongPress");
                dismiss();
            }
            super.onLongPress(e);
        }

    }
}
