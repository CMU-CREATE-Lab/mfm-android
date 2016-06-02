package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionInfoFragment extends Fragment {

    private GlobalHandler globalHandler;
    private Activity parentActivity;
    private View rootView;
    private ExtendedHeightGridView recipientsView;
    private ExtendedHeightGridView fromView;
    private AudioPlayer audioPlayer;


    private int getScreenOrientation() {
        int rotation = parentActivity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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


    public static Fragment newInstance() {
        SessionInfoFragment fragment = new SessionInfoFragment();
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
        globalHandler = GlobalHandler.getInstance(parentActivity.getApplicationContext());
    }


    public SessionInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_session_info, container, false);
        this.audioPlayer = AudioPlayer.newInstance(globalHandler.appContext);
        ButterKnife.bind(this, rootView);

        // quick hack
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
        }

        if (globalHandler.sessionHandler.getMessageSender().getSenderType() == Sender.Type.student) {
            // From content
            ArrayList<Student> studentList = new ArrayList<>();
            studentList.add((Student) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_from);
            fromView.setAdapter(new StudentAdapter(parentActivity.getApplicationContext(), studentList));

            // To content
            recipientsView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_recipients);
            recipientsView.setAdapter(new UserAdapter(parentActivity.getApplicationContext(), globalHandler.sessionHandler.getRecipients()));
        } else {
            // From content
            ArrayList<Group> groupList = new ArrayList<>();
            groupList.add((Group) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_from);
            fromView.setAdapter(new GroupAdapter(parentActivity.getApplicationContext(), groupList));

            // To content
            recipientsView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_recipients);
            recipientsView.setAdapter(new GroupAdapter(parentActivity.getApplicationContext(), groupList));
        }

        // Media content and send
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            int orientation = 0;
            int rotation = 0;
            try {
                ExifInterface exif = new ExifInterface(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    rotation = 270;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    rotation = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    rotation = 180;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_photo)).setImageBitmap(rotated);
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.button_up_talk);
        }
        if (globalHandler.sessionHandler.getMessageAudio() != null) {
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.soundwave_final);
            ((ImageView) rootView.findViewById(R.id.f_session_info_send)).setImageResource(R.drawable.send_up);
        }

        fromView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout fromLL = (LinearLayout) rootView.findViewById(R.id.f_session_info_from_container);
                int height = fromLL.getHeight();
                int orientation = getScreenOrientation();

                //  TODO - more orientations
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    // 1 is 100%
                    // .15 is 15%
                    // 15% of the Linear Layout height is taken up by the toLL
                    fromView.setColumnWidth((int) ((height * (1-.15))/(1+.15)));
                } else if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                    TextView fromText = (TextView) rootView.findViewById(R.id.fromText);
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
                LinearLayout toLL = (LinearLayout) rootView.findViewById(R.id.f_session_info_to_container);
                int height = toLL.getHeight();
                int orientation = getScreenOrientation();

                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    // 1 is 100%
                    // .2 is 20%
                    // 20% of the Linear Layout height is taken up by the toLL
                    recipientsView.setColumnWidth((int) ((height * (1-.2))/(1+.2)));
                } else {
                    // 1 is 100%
                    // .7 is 70%
                    // 70% of the Linear Layout height is taken up by the toLL
                    recipientsView.setColumnWidth((int) ((height * (1-.7))/(1+.7)));
                }

            }
        });

        return rootView;
    }


    @OnClick(R.id.f_session_info_media_photo)
    public void onClickPhoto() {
        ((ImageView) rootView.findViewById(R.id.f_session_info_media_photo)).setImageResource(R.drawable.button_down_photo);
        ((SessionInfoListener) parentActivity).onPhoto();
    }


    @OnClick(R.id.f_session_info_media_audio)
    public void onClickAudio() {
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.button_down_talk);
            ((SessionInfoListener) parentActivity).onAudio();
        }
    }


    @OnClick(R.id.f_session_info_send)
    public void onClickSend() {
        if (globalHandler.sessionHandler.getMessageAudio() != null && !((SessionActivity) parentActivity).isSent) {
            ((ImageView) rootView.findViewById(R.id.f_session_info_send)).setImageResource(R.drawable.send_down);
            ((SessionInfoListener) parentActivity).onSend();
        }
    }

    public interface SessionInfoListener {
        public void onPhoto();
        public void onAudio();
        public void onSend();
    }

}
