package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.classes.Refreshable;
import org.cmucreatelab.mfm_android.helpers.CameraPreview;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.static_classes.SaveFileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    public static int cameraId;

    private Activity parentActivity;
    private GlobalHandler globalHandler;
    private View rootView;
    private Camera mCamera;
    private CameraPreview mPreview;
    private byte[] possiblePhoto;
    private AudioPlayer audioPlayer;


    private static int getCameraOrientation(Activity activity) {
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Constants.FRONT_FACING_CAMERA_ID, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    public static Fragment newInstance(int id) {
        cameraId = id;
        return new CameraFragment();
    }


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
        globalHandler = GlobalHandler.getInstance(parentActivity.getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);
        this.audioPlayer = AudioPlayer.newInstance(globalHandler.appContext);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.camera_swipe_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        if (cameraId == Constants.DEFAULT_CAMERA_ID){
                            ((SessionActivity) parentActivity).onPhoto(Constants.FRONT_FACING_CAMERA_ID);
                        } else {
                            ((SessionActivity) parentActivity).onPhoto(Constants.DEFAULT_CAMERA_ID);
                        }
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        return rootView;
    }


    @OnClick(R.id.takePicture)
    public void onTakePicture() {
        final GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());
        AudioPlayer click = AudioPlayer.newInstance(globalHandler.appContext);
        click.addAudio(R.raw.button_click);
        click.playAudio();

        // call back for creating the picture
        Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                ImageView photoNo = (ImageView) rootView.findViewById(R.id.photoNo);
                ImageView photoYes = (ImageView) rootView.findViewById(R.id.photoYes);
                photoNo.setImageResource(R.drawable.photo_no);
                photoYes.setImageResource(R.drawable.photo_yes);
                ((ImageView) rootView.findViewById(R.id.takePicture)).setVisibility(View.INVISIBLE);
                possiblePhoto = bytes;
                audioPlayer.addAudio(R.raw.picture_to_share);
                audioPlayer.playAudio();

            }
        };

        this.mCamera.takePicture(null, null, jpegCallBack);
    }


    @OnClick(R.id.photoNo)
    public void retakePhoto() {
        audioPlayer.stop();
        ((SessionInfoFragment.SessionInfoListener) this.getActivity()).onPhoto(cameraId);
    }


    @OnClick(R.id.photoYes)
    public void acceptPhoto() {
        audioPlayer.stop();
        File picture = SaveFileHandler.getOutputMediaFile(rootView.getContext(), SaveFileHandler.MEDIA_TYPE_IMAGE, globalHandler);

        if (picture == null) {
            Log.e(Constants.LOG_TAG, "Could not create the media file");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(picture);
            fos.write(possiblePhoto);
            fos.close();

            // We may not need to save the image to any directory if we do this.
            globalHandler.sessionHandler.setMessagePhoto(picture);
            ((SessionActivity) this.getActivity()).pictureTaken();

        } catch (FileNotFoundException e) {
            Log.e(Constants.LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error gaining access to the file: " + e.getMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        this.mCamera = Camera.open(cameraId);
        this.mCamera.setPreviewCallback(null);
        this.mPreview = new CameraPreview(globalHandler.appContext, this.mCamera);

        // set the orientation and camera parameters
        int rotation  = getCameraOrientation(this.getActivity());
        this.mCamera.setDisplayOrientation(rotation);
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotation);
        mCamera.setParameters(params);

        FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
        preview.addView(this.mPreview);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (this.mCamera != null && this.mPreview != null) {
            this.mCamera.setPreviewCallback(null);
            this.mPreview.getHolder().removeCallback(this.mPreview);
            this.mCamera.release();
            this.mCamera = null;
            this.mPreview = null;
        }
    }

}