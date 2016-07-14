package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.AudioRecorder;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionActivity extends BaseActivity {


    private GlobalHandler globalHandler;
    private ExtendedHeightGridView recipientsView;
    private ExtendedHeightGridView fromView;
    private AudioRecorder audioRecorder;
    private ViewFlipper audioFlipper;
    private ViewFlipper sendFlipper;
    private boolean isSending;
    private boolean isReplaying;


    private static boolean isLandscape270() {
        return android.os.Build.MANUFACTURER.equals("Amazon")
                && !(android.os.Build.MODEL.equals("KFOT") || android.os.Build.MODEL.equals("Kindle Fire"));
    }


    private int getOrientation() {

        int port = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        int revP = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        int land = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        int revL = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        if (Build.VERSION.SDK_INT < 9) {
            revL = land;
            revP = port;
        } else if (isLandscape270()) {
            land = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            revL = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }

        Display display = this.getWindowManager().getDefaultDisplay();
        boolean wide = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return wide ? land : port;
            case Surface.ROTATION_90:
                return wide ? land : revP;
            case Surface.ROTATION_180:
                return wide ? revL : revP;
            case Surface.ROTATION_270:
                return wide ? revL : port;
            default:
                throw new AssertionError();
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        audioRecorder = new AudioRecorder(globalHandler.appContext);
        isSending = false;
        isReplaying = false;
        audioFlipper = (ViewFlipper) findViewById(R.id.audio_flipper);
        sendFlipper = (ViewFlipper) findViewById(R.id.send_flipper);

        if (globalHandler.sessionHandler.getMessageSender().getSenderType() == Sender.Type.student) {
            // From content
            ArrayList<Student> studentList = new ArrayList<>();
            studentList.add((Student) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) findViewById(R.id.from_content);
            fromView.setAdapter(new StudentAdapter(globalHandler.appContext, studentList));

            // To content
            recipientsView = (ExtendedHeightGridView) findViewById(R.id.recipients_content);
            recipientsView.setAdapter(new UserAdapter(globalHandler.appContext, globalHandler.sessionHandler.getRecipients()));
        } else {
            // From content
            ArrayList<Group> groupList = new ArrayList<>();
            groupList.add((Group) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) findViewById(R.id.from_content);
            fromView.setAdapter(new GroupAdapter(globalHandler.appContext, groupList));

            // To content
            recipientsView = (ExtendedHeightGridView) findViewById(R.id.recipients_content);
            recipientsView.setAdapter(new GroupAdapter(globalHandler.appContext, groupList));
        }

        // Media content and send
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            int orientation = 0;
            int rotation = 0;
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
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            ((ImageView) findViewById(R.id.media_photo)).setImageBitmap(rotated);
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.button_up_talk);
        }
        if (globalHandler.sessionHandler.getMessageAudio() != null && !audioRecorder.isRecording) {
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.soundwave_final);
            ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_up);
            findViewById(R.id.audio_button).bringToFront();
        }

        // bring the camera button to the foreground since there is not an option in xml....
        findViewById(R.id.camera_button).bringToFront();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (globalHandler.sessionHandler.getMessageAudio() == null && globalHandler.sessionHandler.getMessagePhoto() != null) {
            audioPlayer.stop();
            audioPlayer.addAudio(R.raw.what_did_take);
            audioPlayer.playAudio();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (audioRecorder.isRecording) {
            globalHandler.sessionHandler.setMessageAudio(null);
            audioRecorder.stopRecording();
        }
    }

    @OnClick(R.id.camera_button)
    public void onClickPhoto() {
        super.onButtonClick(globalHandler.appContext);
        if (audioRecorder.isRecording) {
            audioRecorder.stopRecording();
        }

        // reset audio clip
        audioPlayer.stop();
        globalHandler.sessionHandler.setMessageAudio(null);
        CameraActivity.cameraId = Constants.DEFAULT_CAMERA_ID;
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.media_audio)
    public void onClickAudio() {
        if (globalHandler.sessionHandler.getMessageAudio() == null || audioRecorder.isRecording) {
            super.onButtonClick(globalHandler.appContext);
            audioPlayer.stop();
            ImageView audio = (ImageView) findViewById(R.id.media_audio);
            ImageView send = (ImageView) findViewById(R.id.send_button);

            if (globalHandler.sessionHandler.getMessagePhoto() != null && !audioRecorder.isRecording) {
                send.setImageResource(R.drawable.send_disabled);
                audioRecorder.startRecording();
                audio.setImageResource(R.drawable.button_up_talkstop);
            } else if (audioRecorder.isRecording) {
                audioRecorder.stopRecording();
                audioFlipper.setInAnimation(this, R.anim.in_from_left);
                audioFlipper.showNext();
                findViewById(R.id.audio_button).bringToFront();

                // playback the audio clip you recorded
                File audioFile = globalHandler.sessionHandler.getMessageAudio();
                if (audioFile != null) {
                    Uri uri = Uri.parse(audioFile.getAbsolutePath());
                    Log.i(Constants.LOG_TAG, uri.toString());
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (!audioRecorder.isRecording) {
                                ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_up);
                                ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_up);
                                audioPlayer.addAudio(R.raw.press_the_green_button);
                                audioPlayer.playAudio();
                            }
                            mediaPlayer.release();
                            isReplaying = false;
                        }
                    });
                    try {
                        mediaPlayer.setDataSource(this.getApplicationContext(), uri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        isReplaying = true;
                        Toast toast = Toast.makeText(globalHandler.appContext, "Replaying audio recorded.", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        Log.e(Constants.LOG_TAG, e.toString());
                        Log.e(Constants.LOG_TAG, audioFile.getAbsolutePath());
                    }

                }
            }
        }
    }


    @OnClick(R.id.audio_button)
    public void onClickReRecord() {
        super.onButtonClick(this);
        ImageView audio = (ImageView) findViewById(R.id.media_audio);
        ImageView send = (ImageView) findViewById(R.id.send_button);
        audioRecorder.stopRecording();
        audioPlayer.stop();
        globalHandler.sessionHandler.setMessageAudio(null);
        audioRecorder.startRecording();
        audio.setImageResource(R.drawable.button_up_talkstop);
        send.setImageResource(R.drawable.send_disabled);
        audioFlipper.setInAnimation(null);
        audioFlipper.showPrevious();
    }


    @OnClick(R.id.send_button)
    public void onClickSend() {
        if (globalHandler.sessionHandler.getMessageAudio() != null && !isSending && !isReplaying && !audioRecorder.isRecording) {
            if (globalHandler.isNetworkConnected(this)) {
                super.onButtonClick(globalHandler.appContext);
                audioPlayer.stop();
                isSending = true;
                ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_down);
                globalHandler.sessionHandler.sendMessage(this);

                sendFlipper.setInAnimation(this, R.anim.in_from_left);
                sendFlipper.setOutAnimation(this, R.anim.out_to_right);
                sendFlipper.showNext();
            } else {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Internet Connection");
                builder.setMessage("Connect to the internet to send your message.");
                builder.setPositiveButton("Ok", null);
                dialog = builder.create();
                dialog.show();
            }
        }
    }


    public void success() {
        audioPlayer.addAudio(R.raw.your_message_has_been_sent);
        audioPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Intent intent = new Intent(globalHandler.appContext, StudentsGroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        audioPlayer.playAudio();
    }


    public void failure() {
        Toast toast = Toast.makeText(this, "Your message failed to send.", Toast.LENGTH_LONG);
        toast.show();
    }

}
