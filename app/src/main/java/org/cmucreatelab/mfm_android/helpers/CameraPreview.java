package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.hardware.Camera;

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
        try{
            this.mCamera.setPreviewDisplay(surfaceHolder);
            this.mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

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
