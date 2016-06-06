package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSessionActivity extends BaseActivity {


    private GlobalHandler globalHandler;;
    private ExtendedHeightGridView recipientsView;
    private ExtendedHeightGridView fromView;


    private int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "Unknown screen orientation. Defaulting to " +
                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());

        if (globalHandler.sessionHandler.getMessageSender().getSenderType() == Sender.Type.student) {
            // From content
            ArrayList<Student> studentList = new ArrayList<>();
            studentList.add((Student) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) findViewById(R.id.from_content);
            fromView.setAdapter(new StudentAdapter(globalHandler.appContext, studentList));

            // To content
            recipientsView = (ExtendedHeightGridView) findViewById(R.id.recipients_content);
            recipientsView.setAdapter(new UserAdapter(globalHandler.appContext, globalHandler.sessionHandler.getRecipients()));
        } else {
            // From content
            ArrayList<Group> groupList = new ArrayList<>();
            groupList.add((Group) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) findViewById(R.id.from_content);
            fromView.setAdapter(new GroupAdapter(globalHandler.appContext, groupList));

            // To content
            recipientsView = (ExtendedHeightGridView) findViewById(R.id.recipients_content);
            recipientsView.setAdapter(new GroupAdapter(globalHandler.appContext, groupList));
        }

        // Media content and send
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            int orientation = 0;
            int rotation = 0;
            try {
                ExifInterface exif = new ExifInterface(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                if (CameraActivity.cameraId == Constants.DEFAULT_CAMERA_ID) {
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        rotation = 270;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        rotation = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        rotation = 180;
                    }
                } else {
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        rotation = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        rotation = 270;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        rotation = 180;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
            ((ImageView) findViewById(R.id.media_photo)).setImageBitmap(rotated);
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.button_up_talk);
        }
        if (globalHandler.sessionHandler.getMessageAudio() != null) {
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.soundwave_final);
            ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_up);
        }

        fromView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout fromLL = (LinearLayout) findViewById(R.id.from_container);
                int height = fromLL.getHeight();
                int orientation = getScreenOrientation();

                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    // 1 is 100%
                    // .15 is 15%
                    // 15% of the Linear Layout height is taken up by the toLL
                    fromView.setColumnWidth((int) ((height * (1-.15))/(1+.15)));
                } else if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
                    TextView fromText = (TextView) findViewById(R.id.from_text);
                    int heightText = fromText.getHeight();
                    height -= heightText;
                    // 1 is 100%
                    // .3 is 30%
                    // 30% of the Linear Layout height is taken up by the toLL
                    fromView.setColumnWidth((int) ((height * (1-.3))/(1+.3)));
                }
            }
        });
        recipientsView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout toLL = (LinearLayout) findViewById(R.id.to_container);
                int height = toLL.getHeight();
                int orientation = getScreenOrientation();

                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    // 1 is 100%
                    // .2 is 20%
                    // 20% of the Linear Layout height is taken up by the toLL
                    recipientsView.setColumnWidth((int) ((height * (1-.2))/(1+.2)));
                } else if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
                    // 1 is 100%
                    // .7 is 70%
                    // 70% of the Linear Layout height is taken up by the toLL
                    recipientsView.setColumnWidth((int) ((height * (1-.7))/(1+.7)));
                }

            }
        });
    }


    @OnClick(R.id.media_photo)
    public void onClickPhoto() {
        ((ImageView) findViewById(R.id.media_photo)).setImageResource(R.drawable.button_down_photo);
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.media_audio)
    public void onClickAudio() {
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.button_down_talk);
            ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_disabled);
            // TODO - record audio
        }
    }


    @OnClick(R.id.send_button)
    public void onClickSend() {
        if (globalHandler.sessionHandler.getMessageAudio() != null) {
            ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_down);
            // TODO - send message
        }
    }

}
