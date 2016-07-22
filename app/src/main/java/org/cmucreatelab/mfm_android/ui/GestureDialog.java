package org.cmucreatelab.mfm_android.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;

import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Steve on 7/15/2016.
 */

public class GestureDialog extends AlertDialog{

    private static final String UP = "up";
    private static final String RIGHT = "right";
    private static final String DOWN = "down";
    private static final String LEFT = "left";

    private String direction;
    private int numFingers;
    private boolean isFingerCount;

    private float downX;
    private float downY;

    private ArrayList<Group> groups;
    private int index;


    private void success() {
        GlobalHandler.getInstance(getContext()).sessionHandler.startSession(groups.get(index));
        Intent intent = new Intent(getContext(), CameraActivity.class);
        getContext().startActivity(intent);
        dismiss();
    }


    public GestureDialog(Context context, ArrayList<Group> g, int i) {
        super(context);
        Random random = new Random();
        int result = random.nextInt(2);
        switch (result) {
            case 0:
                numFingers = 2;
                break;
            case 1:
                numFingers = 3;
                break;
            default:
                numFingers = 2;
                break;
        }

        switch (numFingers) {
            case 2:
                this.setTitle("With two fingers:");
                break;
            case 3:
                this.setTitle("With three fingers:");
                break;
            default:
                this.setTitle("With two fingers:");
                break;
        }

        result = random.nextInt(4);
        switch (result) {
            case 0:
                direction = UP;
                break;
            case 1:
                direction = RIGHT;
                break;
            case 2:
                direction = DOWN;
                break;
            case 3:
                direction = LEFT;
                break;
            default:
                break;
        }
        this.setMessage("Swipe " + direction);
        this.setCanceledOnTouchOutside(false);
        downX = 0;
        downY = 0;
        isFingerCount = false;
        groups = g;
        index = i;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (event.getPointerCount() == numFingers || isFingerCount) {
            isFingerCount = true;
            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX(0);
                    downY = event.getY(0);
                    Log.d(Constants.LOG_TAG, "downX: " + downX + " downY: " + downY);
                    break;
                case MotionEvent.ACTION_UP:
                    float upX = event.getX(0);
                    float upY = event.getY(0);
                    Log.d(Constants.LOG_TAG, "upX: " + upX + " upY: " + upY);

                    if (Math.abs(downX - upX) > Math.abs(downY - upY)) {
                        Log.d(Constants.LOG_TAG, "x");
                        if (downX < upX) {
                            if (direction.equals(RIGHT)) {
                                success();
                            }
                            Log.d(Constants.LOG_TAG, "right");
                        }
                        if (downX > upX) {
                            if (direction.equals(LEFT)) {
                                success();
                            }
                            Log.d(Constants.LOG_TAG, "left");
                        }

                    } else {
                        Log.d(Constants.LOG_TAG, "y ");
                        if (downY < upY) {
                            if (direction.equals(DOWN)) {
                                success();
                            }
                            Log.d(Constants.LOG_TAG, "down");
                        }
                        if (downY > upY) {
                            if (direction.equals(UP)) {
                                success();
                            }
                            Log.d(Constants.LOG_TAG, "up");
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

}
