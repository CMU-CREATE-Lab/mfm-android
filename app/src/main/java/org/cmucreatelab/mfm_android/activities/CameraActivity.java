package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Surface;
import android.widget.FrameLayout;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.CameraPreview;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_ID = 0;
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.mCamera = getCameraInstance();
        this.mCamera.setPreviewCallback(null);
        this.mPreview = new CameraPreview(getApplicationContext(), this.mCamera);

        this.mCamera.setDisplayOrientation(this.getCameraOrientation());

        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(this.mPreview);
    }

    private int getCameraOrientation(){
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(this.CAMERA_ID, info);

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

    private static Camera getCameraInstance(){
        Camera c = null;

        try{
            c = Camera.open(CAMERA_ID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return c;
    }

    @Override
    public void onPause(){
        super.onPause();

        if(this.mCamera != null) {
            this.mCamera.setPreviewCallback(null);
            this.mPreview.getHolder().removeCallback(this.mPreview);
            this.mCamera.release();
        }
    }

}
