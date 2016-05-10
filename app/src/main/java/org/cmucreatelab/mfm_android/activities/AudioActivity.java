package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.SaveFileHandler;
import java.io.File;
import java.io.IOException;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioActivity extends AppCompatActivity {

    private Button recorderButton;
    private boolean isRecording;
    private MediaRecorder mediaRecorder;
    private Context context;
    private File audioFile;


    // Class methods


    private void startRecording() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        this.audioFile = SaveFileHandler.getOutputMediaFile(context, SaveFileHandler.MEDIA_TYPE_AUDIO, globalHandler);
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        this.mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        globalHandler.sessionHandler.setMessageAudio(this.audioFile);

        try {
            this.mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Could not prepare Recorder.");
        }

        this.mediaRecorder.start();

        String message = "Recording audio";
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    private void stopRecording() {
        this.mediaRecorder.stop();
        this.mediaRecorder.release();
        this.mediaRecorder = null;

        String message = "Audio file has been saved.";
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }


    // Activity methods and listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        this.recorderButton = (Button) findViewById(R.id.recordAudio);
        this.isRecording = false;
        this.mediaRecorder = null;
        this.context = this.getApplicationContext();
    }


    @OnClick(R.id.recordAudio)
    public void onStartAndStop() {
        if (!this.isRecording) {
            this.isRecording = true;
            this.recorderButton.setText(R.string.button_audio_stop);
            startRecording();
        } else {
            this.isRecording = false;
            stopRecording();
        }
    }

}
