package org.cmucreatelab.mfm_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.CameraPreview;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.SaveFileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity {


    public static int cameraId;

    private Activity mActivity;
    private GlobalHandler globalHandler;
    private Camera mCamera;
    private CameraPreview mPreview;
    private byte[] possiblePhoto;


    private static Camera getCameraInstance(){
        Camera c = null;

        try{
            c = Camera.open(cameraId);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return c;
    }


    private int getOrientation() {
        int result = 0;

        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 1; break;
            case Surface.ROTATION_180: degrees = 2; break;
            case Surface.ROTATION_270: degrees = 3; break;
        }

        result = degrees;

        return result;
    }


    private int getCameraRotation(){
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        this.globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        mActivity = this;
    }


    @Override
    protected void onResume(){
        super.onResume();
        this.mCamera = getCameraInstance();
        this.mCamera.setPreviewCallback(null);
        this.mPreview = new CameraPreview(getApplicationContext(), this.mCamera);

        // set the orientation and camera parameters
        int rotation = this.getCameraRotation();
        this.mCamera.setDisplayOrientation(rotation);
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotation);

        ArrayList<String> list = (ArrayList<String>) params.getSupportedFocusModes();
        for (String item : list) {
            if (item.equals("continuous-picture")) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        mCamera.setParameters(params);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(this.mPreview);
    }


    @Override
    public void onPause(){
        super.onPause();

        if(this.mCamera != null && this.mPreview != null) {
            this.mCamera.setPreviewCallback(null);
            this.mPreview.getHolder().removeCallback(this.mPreview);
            this.mCamera.release();
            this.mCamera = null;
            this.mPreview = null;
        }
    }


    @OnClick(R.id.take_picture)
    public void onTakePicture(){
        super.onButtonClick(globalHandler.appContext);
        // call back for creating the picture
        Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                ImageView photoNo = (ImageView) findViewById(R.id.photo_no);
                ImageView photoYes = (ImageView) findViewById(R.id.photo_yes);
                photoNo.setImageResource(R.drawable.photo_no);
                photoYes.setImageResource(R.drawable.photo_yes);
                findViewById(R.id.take_picture).setVisibility(View.INVISIBLE);
                possiblePhoto = bytes;
                audioPlayer.addAudio(R.raw.picture_to_share);
                audioPlayer.playAudio();
                mCamera.stopPreview();
            }
        };

        this.mCamera.takePicture(null, null, jpegCallBack);
    }


    @OnClick(R.id.photo_no)
    public void retakePhoto() {
        super.onButtonClick(globalHandler.appContext);
        finish();
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.photo_yes)
    public void acceptPhoto() {
        super.onButtonClick(globalHandler.appContext);
        File picture = SaveFileHandler.getOutputMediaFile(globalHandler.appContext, SaveFileHandler.MEDIA_TYPE_IMAGE, globalHandler);

        if (picture == null) {
            Log.e(Constants.LOG_TAG, "Could not create the media file");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(picture.getPath());
            fos.write(possiblePhoto);
            fos.close();

            // We may not need to save the image to any directory if we do this.
            globalHandler.sessionHandler.setMessagePhoto(picture);

            // I have to do this for amazon devices because the kindle does not save exif data on its own...
            // I also cannot do this for the other devices too because as you can see, the cases for ROTATION_90
            // and ROTATION_270 do not make sense. I think amazon made some sort of mistake with how they handle orientation.
            // ...or maybe I am doing this very wrong
            if (Build.MANUFACTURER.equals("Amazon")) {
                ExifInterface exifInterface = new ExifInterface(picture.getAbsolutePath());
                int orientation = getOrientation();
                switch (orientation) {
                    case Surface.ROTATION_0:
                        if (cameraId == Constants.DEFAULT_CAMERA_ID) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_VERTICAL));
                        }
                        break;
                    case Surface.ROTATION_90:
                        if (cameraId == Constants.DEFAULT_CAMERA_ID) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_HORIZONTAL));
                        }
                        break;
                    case Surface.ROTATION_180:
                        if (cameraId == Constants.DEFAULT_CAMERA_ID) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
                        }
                        break;
                    case Surface.ROTATION_270:
                        if (cameraId == Constants.DEFAULT_CAMERA_ID) {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        } else {
                            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_HORIZONTAL));
                        }
                        break;
                    default:
                        exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                        break;
                }
                exifInterface.saveAttributes();
            }

            Intent intent = new Intent(this, SessionActivity.class);
            startActivity(intent);
            finish();

        } catch (FileNotFoundException e) {
            Log.e(Constants.LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error gaining access to the file: " + e.getMessage());
        }
    }


    @OnClick(R.id.flip_camera)
    public void flipCamera() {
        super.onButtonClick(this);
        Intent intent = getIntent();
        if (cameraId == Constants.DEFAULT_CAMERA_ID){
            CameraActivity.cameraId = Constants.FRONT_FACING_CAMERA_ID;
            startActivity(intent);
            finish();
        } else {
            CameraActivity.cameraId = Constants.DEFAULT_CAMERA_ID;
            startActivity(intent);
            finish();
        }
    }

}
