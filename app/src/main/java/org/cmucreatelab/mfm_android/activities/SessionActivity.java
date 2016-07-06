package org.cmucreatelab.mfm_android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean isSending;
    private boolean isReplaying;


    private int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e(Constants.LOG_TAG, "Unknown screen orientation. Defaulting to " +
                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        audioRecorder = new AudioRecorder(globalHandler.appContext);
        isSending = false;
        isReplaying = false;

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
            try {
                ExifInterface exif = new ExifInterface(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                if (CameraActivity.cameraId == Constants.DEFAULT_CAMERA_ID) {
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        rotation = 270;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        rotation = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        rotation = 180;
                    }
                } else {
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        rotation = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        rotation = 270;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        rotation = 180;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
            ((ImageView) findViewById(R.id.media_photo)).setImageBitmap(rotated);
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.button_up_talk);
        }
        if (globalHandler.sessionHandler.getMessageAudio() != null && !audioRecorder.isRecording) {
            ((ImageView) findViewById(R.id.media_audio)).setImageResource(R.drawable.soundwave_final);
            ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_up);
        }

        fromView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout fromLL = (LinearLayout) findViewById(R.id.from_container);
                int height = fromLL.getHeight();
                int orientation = getScreenOrientation();

                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    // 1 is 100%
                    // .15 is 15%
                    // 15% of the Linear Layout height is taken up by the toLL
                    fromView.setColumnWidth((int) ((height * (1-.15))/(1+.15)));
                } else if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
                    TextView fromText = (TextView) findViewById(R.id.from_text);
                    int heightText = fromText.getHeight();
                    height -= heightText;
                    // 1 is 100%
                    // .3 is 30%
                    // 30% of the Linear Layout height is taken up by the toLL
                    fromView.setColumnWidth((int) ((height * (1-.3))/(1+.3)));
                }
            }
        });
        recipientsView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout toLL = (LinearLayout) findViewById(R.id.to_container);
                int height = toLL.getHeight();
                int orientation = getScreenOrientation();

                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    // 1 is 100%
                    // .2 is 20%
                    // 20% of the Linear Layout height is taken up by the toLL
                    recipientsView.setColumnWidth((int) ((height * (1-.2))/(1+.2)));
                } else if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
                    // 1 is 100%
                    // .7 is 70%
                    // 70% of the Linear Layout height is taken up by the toLL
                    recipientsView.setColumnWidth((int) ((height * (1-.7))/(1+.7)));
                }

            }
        });

        // bring the camera button to the foreground since there is not an option in xml....
        findViewById(R.id.camera_button).bringToFront();
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
        globalHandler.sessionHandler.setMessageAudio(null);
        CameraActivity.cameraId = Constants.DEFAULT_CAMERA_ID;
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
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
                audio.setImageResource(R.drawable.soundwave_final);
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
    }


    @OnClick(R.id.send_button)
    public void onClickSend() {
        if (globalHandler.sessionHandler.getMessageAudio() != null && !isSending && !isReplaying && !audioRecorder.isRecording) {
            super.onButtonClick(globalHandler.appContext);
            audioPlayer.stop();
            isSending = true;
            ((ImageView) findViewById(R.id.send_button)).setImageResource(R.drawable.send_down);
            globalHandler.sessionHandler.sendMessage(this);
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
