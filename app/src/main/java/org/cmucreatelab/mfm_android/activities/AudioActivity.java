package org.cmucreatelab.mfm_android.activities;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.SaveFileHandler;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioActivity extends AppCompatActivity {

    private GlobalHandler globalHandler;
    private Button recorderButton;
    private boolean isRecording;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        GlobalHandler gh = GlobalHandler.getInstance(this.getApplicationContext());
        this.globalHandler = gh;
        this.recorderButton = (Button) findViewById(R.id.recordAudio);
        this.isRecording = false;
        this.mediaRecorder = null;
    }

    private void startRecording(){
        File audioFile = SaveFileHandler.getOutputMediaFile(globalHandler.getAppContext(), SaveFileHandler.MEDIA_TYPE_AUDIO);
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        this.mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        this.globalHandler.setCurrentAudio(audioFile);

        try {
            this.mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Could not prepare Recorder.");
        }

        this.mediaRecorder.start();
    }

    private void stopRecording(){
        this.mediaRecorder.stop();
        this.mediaRecorder.release();
        this.mediaRecorder = null;
    }

    @OnClick(R.id.recordAudio)
    public void onStartAndStop(){
        if(!this.isRecording){
            this.isRecording = true;
            this.recorderButton.setText(R.string.button_audio_stop);
            startRecording();
        }
        else{
            this.isRecording = false;
            this.recorderButton.setText(R.string.button_audio_start);
            stopRecording();
        }
    }

}
