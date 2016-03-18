package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.hardware.Camera;

import org.cmucreatelab.mfm_android.activities.CameraActivity;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.io.IOException;
import java.util.List;

/**
 * Created by Steve on 3/15/2016.
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

        /*Camera.Parameters params = this.mCamera.getParameters();
        String currentversion = android.os.Build.VERSION.SDK;
        int currentInt = android.os.Build.VERSION.SDK_INT;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (currentInt != 7) {
                this.mCamera.setDisplayOrientation(90);
            } else {
                Log.i(Constants.LOG_TAG, "Portrait " + currentInt);
                params.set("orientation", "portrait");
                params.setRotation(90);
                this.mCamera.setParameters(params);
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (currentInt != 7) {
                this.mCamera.setDisplayOrientation(0);
            } else {
                Log.i(Constants.LOG_TAG, "Landscape " + currentInt);
                params.set("orientation", "landscape");
                params.setRotation(90);
                this.mCamera.setParameters(params);
            }
        }*/

        try {
            this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
            this.mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (this.mSurfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(Constants.LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder.getSurface().release();
        System.out.println("Destroyed the surface");
    }

    public SurfaceHolder getmSurfaceHolder() {
        return mSurfaceHolder;
    }
}
