package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.helpers.CameraPreview;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
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

    private View rootView;
    private Camera mCamera;
    private CameraPreview mPreview;
    private GlobalHandler globalHandler;
    private byte[] possiblePhoto;


    private int getCameraOrientation() {
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Constants.CAMERA_ID, info);

        int rotation = this.getActivity().getWindowManager().getDefaultDisplay().getRotation();
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


    public static Fragment newInstance() {
        return new CameraFragment();
    }


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);
        globalHandler = GlobalHandler.getInstance(rootView.getContext());

        /*this.mCamera = Camera.open(Constants.CAMERA_ID);
        this.mCamera.setPreviewCallback(null);
        this.mPreview = new CameraPreview(this.getActivity().getApplicationContext(), this.mCamera);

        // Set the orientation and camera parameters.
        // Makes sure the image seen and the picture taken is oriented correctly
        int rotation  = this.getCameraOrientation();
        this.mCamera.setDisplayOrientation(rotation);
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotation);
        mCamera.setParameters(params);

        FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
        preview.addView(this.mPreview);*/


        return rootView;
    }


    @OnClick(R.id.takePicture)
    public void onTakePicture() {
        final GlobalHandler globalHandler = GlobalHandler.getInstance(rootView.getContext());

        // call back for creating the picture
        Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                ImageView photoNo = (ImageView) rootView.findViewById(R.id.photoNo);
                ImageView photoYes = (ImageView) rootView.findViewById(R.id.photoYes);
                photoNo.setImageResource(R.drawable.photo_no);
                photoYes.setImageResource(R.drawable.photo_yes);
                possiblePhoto = bytes;
            }
        };

        this.mCamera.takePicture(null, null, jpegCallBack);
    }


    @OnClick(R.id.cancelCamera)
    public void cancelCamera() {
        ((SessionActivity) this.getActivity()).pictureTakenOrCancelled();
    }


    @OnClick(R.id.photoNo)
    public void retakePhoto() {
        ((SessionActivity) this.getActivity()).startCamera();
    }


    @OnClick(R.id.photoYes)
    public void acceptPhoto() {
        File picture = SaveFileHandler.getOutputMediaFile(rootView.getContext(), SaveFileHandler.MEDIA_TYPE_IMAGE, globalHandler);

        if (picture == null) {
            Log.e(Constants.LOG_TAG, "Could not create the media file");
            return;
        }

        try {
            Log.i(Constants.LOG_TAG, picture.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(picture);
            fos.write(possiblePhoto);
            fos.close();

            String message = "Picture has been taken and saved.";
            Toast toast = Toast.makeText(globalHandler.appContext, message, Toast.LENGTH_SHORT);
            toast.show();

            // We may not need to save the image to any directory if we do this.
            globalHandler.sessionHandler.setMessagePhoto(picture);
            ((SessionActivity) this.getActivity()).pictureTakenOrCancelled();

        } catch (FileNotFoundException e) {
            Log.e(Constants.LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error gaining access to the file: " + e.getMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        this.mCamera = Camera.open(Constants.CAMERA_ID);
        this.mCamera.setPreviewCallback(null);
        this.mPreview = new CameraPreview(globalHandler.appContext, this.mCamera);

        // set the orientation and camera parameters
        int rotation  = this.getCameraOrientation();
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