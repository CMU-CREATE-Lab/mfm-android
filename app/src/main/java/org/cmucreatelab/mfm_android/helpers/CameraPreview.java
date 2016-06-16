package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.io.IOException;

/**
 * Created by Steve on 3/15/2016.
 *
 * CameraPreview
 *
 * A helper class that displays the camera.
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
            this.mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (this.mSurfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(Constants.LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder.getSurface().release();
        Log.i(Constants.LOG_TAG, "Destroyed the camera surface");
    }

}
