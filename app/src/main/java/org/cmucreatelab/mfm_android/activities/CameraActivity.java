package org.cmucreatelab.mfm_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
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

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity {


    public static int cameraId;

    private Activity parentActivity;
    private GlobalHandler globalHandler;
    private Camera mCamera;
    private CameraPreview mPreview;
    private byte[] possiblePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        this.globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
    }


    @Override
    protected void onResume(){
        super.onResume();
        this.mCamera = getCameraInstance();
        this.mCamera.setPreviewCallback(null);
        this.mPreview = new CameraPreview(getApplicationContext(), this.mCamera);

        // set the orientation and camera parameters
        int rotation  = this.getCameraOrientation();
        this.mCamera.setDisplayOrientation(rotation);
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotation);
        mCamera.setParameters(params);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(this.mPreview);
    }


    private static Camera getCameraInstance(){
        Camera c = null;

        try{
            c = Camera.open(Constants.DEFAULT_CAMERA_ID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return c;
    }


    private int getCameraOrientation(){
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Constants.DEFAULT_CAMERA_ID, info);

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

            }
        };

        this.mCamera.takePicture(null, null, jpegCallBack);
    }


    @OnClick(R.id.photo_no)
    public void retakePhoto() {
        finish();
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.photo_yes)
    public void acceptPhoto() {
        File picture = SaveFileHandler.getOutputMediaFile(globalHandler.appContext, SaveFileHandler.MEDIA_TYPE_IMAGE, globalHandler);

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
            Intent intent = new Intent(this, NewSessionActivity.class);
            startActivity(intent);
            finish();

        } catch (FileNotFoundException e) {
            Log.e(Constants.LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error gaining access to the file: " + e.getMessage());
        }
    }

}
