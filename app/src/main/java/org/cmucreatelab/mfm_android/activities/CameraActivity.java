package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;
import android.widget.Toast;

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

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Context context;
    private GlobalHandler globalHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        this.globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        this.context = this.getApplicationContext();
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

        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(this.mPreview);
    }

    private static Camera getCameraInstance(){
        Camera c = null;

        try{
            c = Camera.open(Constants.CAMERA_ID);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return c;
    }

    private int getCameraOrientation(){
        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Constants.CAMERA_ID, info);

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

    @OnClick(R.id.takePicture)
    public void onTakePicture(){

        // call back for creating the picture
        Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                File picture = SaveFileHandler.getOutputMediaFile(context, SaveFileHandler.MEDIA_TYPE_IMAGE, globalHandler);

                if(picture == null){
                    Log.e(Constants.LOG_TAG, "Could not create the media file");
                    return;
                }

                try{
                    Log.i(Constants.LOG_TAG, picture.getAbsolutePath());
                    FileOutputStream fos = new FileOutputStream(picture);
                    fos.write(bytes);
                    fos.close();

                    String message = "Picture has been taken and saved.";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();

                    // We may not need to save the image to any directory if we do this.
                    globalHandler.setCurrentImage(picture);
                } catch (FileNotFoundException e) {
                    Log.e(Constants.LOG_TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(Constants.LOG_TAG, "Error gaining access to the file: " + e.getMessage());
                }
            }
        };

        this.mCamera.takePicture(null, null, jpegCallBack);
    }

}
