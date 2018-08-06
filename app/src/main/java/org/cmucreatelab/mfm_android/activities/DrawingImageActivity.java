package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.color_picker.dialog.ColorPickerDialogFragment;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.SessionHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.SaveFileHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class DrawingImageActivity extends AppCompatActivity implements Serializable, ColorPickerDialogFragment.ColorPickerDialogListener {
    @BindView(R.id.image_result)
    ImageView imageResult;

    private Bitmap masterBitmap;
    private Canvas masterCanvas;
    private GlobalHandler globalHandler;
    private SessionHandler sessionHandler;

    private float ratioWidth;
    private float ratioHeight;

    private Paint paintDraw;

    private float mX, mY;
    private Path mPath;
    private static final float TOUCH_TOLERANCE = 4;


    public void onColorSelected(int dialogId, int color, int strokeWidth) {
        paintDraw.setColor(color);
        paintDraw.setStrokeWidth(strokeWidth);
    }


    @OnClick(R.id.button_done)
    public void onClickDone() {
        if (masterBitmap != null) {
            saveBitmap(masterBitmap);
        }
        sessionHandler.clearDrawingPaths();
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.button_undo)
    public void onClickUndo() {
        if (!sessionHandler.getDrawingPaths().isEmpty()) {
            updateCanvasAndImageFromUri();
            sessionHandler.removeDrawingPath(sessionHandler.getDrawingPaths().size() - 1);
            imageResult.invalidate();
            for (PaintPath paintPath : sessionHandler.getDrawingPaths()) {
                masterCanvas.drawPath(paintPath.path, paintPath.paint);
            }
        }
    }


    @OnClick(R.id.button_advanced_settings)
    public void onClickAdavancedSettings() {
        ColorPickerDialogFragment colorPickerDialogFragment = ColorPickerDialogFragment.newInstance(0, "Color Picker", null, paintDraw.getColor(), false, paintDraw.getStrokeWidth(), this);
        colorPickerDialogFragment.show(getFragmentManager(), "Color Picker");
    }


    @OnClick(R.id.button_clear)
    public void onClickClear() {
        if (masterBitmap != null) {
            updateCanvasAndImageFromUri();
            sessionHandler.clearDrawingPaths();
            imageResult.invalidate();
        }
    }


    @Override
    public void onBackPressed() {
        sessionHandler.clearDrawingPaths();
        super.onBackPressed();
    }


    @OnTouch(R.id.image_result)
    public boolean onTouchImageResult(View view, MotionEvent event) {
        int x = Math.round(event.getX() * ratioWidth);
        int y = Math.round(event.getY() * ratioHeight);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }

        for (PaintPath paintPath : sessionHandler.getDrawingPaths()) {
            masterCanvas.drawPath(paintPath.path, paintPath.paint);
        }

        imageResult.invalidate();


        /*
         * Return 'true' to indicate that the event have been consumed.
         * If auto-generated 'false', your code can detect ACTION_DOWN only,
         * cannot detect ACTION_MOVE and ACTION_UP.
         */
        return true;
    }


    private void touchStart(float x, float y) {
        mPath = new Path();
        sessionHandler.addDrawingPath(new PaintPath(new Paint(paintDraw), mPath));

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }


    private void touchUp() {
        mPath.lineTo(mX, mY);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("paintDrawColor", paintDraw.getColor());
        outState.putInt("paintDrawStrokeWidth", (int) paintDraw.getStrokeWidth());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_drawing_picture_portrait);
        } else {
            setContentView(R.layout.activity_drawing_picture_landscape);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        this.globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        this.sessionHandler = globalHandler.sessionHandler;

        paintDraw = new Paint();
        paintDraw.setStyle(Paint.Style.STROKE);

        if (savedInstanceState != null) {
            paintDraw.setColor(savedInstanceState.getInt("paintDrawColor"));
            paintDraw.setStrokeWidth(savedInstanceState.getInt("paintDrawStrokeWidth"));
        } else {
            paintDraw.setColor(Color.WHITE);
            paintDraw.setStrokeWidth(10);
        }

        ButterKnife.bind(this);

        updateCanvasAndImageFromUri();

        for (PaintPath paintPath : sessionHandler.getDrawingPaths()) {
            masterCanvas.drawPath(paintPath.path, paintPath.paint);
        }
    }


    private void updateCanvasAndImageFromUri() {
        Bitmap immutableBitmap;
        double scaleFactor;
        try {
            int orientation;
            Matrix matrix = new Matrix();
            try {
                ExifInterface exif = new ExifInterface(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

                if (CameraActivity.cameraId == Constants.DEFAULT_CAMERA_ID) {
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_NORMAL:
                            break;
                        case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                            matrix.setScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.setRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                            matrix.setRotate(180);
                            matrix.postScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_TRANSPOSE:
                            matrix.setRotate(90);
                            matrix.postScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.setRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_TRANSVERSE:
                            matrix.setRotate(-90);
                            matrix.postScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.setRotate(-90);
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_NORMAL:
                            break;
                        case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                            matrix.setScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.setRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                            matrix.setRotate(180);
                            matrix.postScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_TRANSPOSE:
                            matrix.setRotate(-90);
                            matrix.postScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.setRotate(-90);
                            break;
                        case ExifInterface.ORIENTATION_TRANSVERSE:
                            matrix.setRotate(90);
                            matrix.postScale(-1, 1);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.setRotate(90);
                            break;
                        default:
                            break;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            immutableBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                scaleFactor = (double) width / immutableBitmap.getWidth();
            }
            else
            {
                scaleFactor = (double) height / immutableBitmap.getHeight();
            }

            int canvasWidth = (int) Math.round(immutableBitmap.getWidth() * scaleFactor);
            int canvasHeight = (int) Math.round(immutableBitmap.getHeight() * scaleFactor);

            //masterBitmap is mutable
            masterBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);

            masterCanvas = new Canvas(masterBitmap);
            masterCanvas.drawBitmap(immutableBitmap, null, new RectF(0, 0, canvasWidth, canvasHeight), null);

            imageResult.setImageBitmap(masterBitmap);

            final ViewTreeObserver imageResultViewTreeObserver = imageResult.getViewTreeObserver();
            imageResultViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ratioWidth = (float) masterBitmap.getWidth() / (float) imageResult.getWidth();
                    ratioHeight = (float) masterBitmap.getHeight() / (float) imageResult.getHeight();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveBitmap(Bitmap bm) {
        File picture = SaveFileHandler.getOutputMediaFile(globalHandler.appContext, SaveFileHandler.MEDIA_TYPE_IMAGE, globalHandler);

        if (picture == null) {
            Log.e(Constants.LOG_TAG, "Could not create the media file");
            return;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(picture.getPath());
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            // We may not need to save the image to any directory if we do this.
            globalHandler.sessionHandler.setMessagePhoto(picture);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}