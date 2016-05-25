package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import org.cmucreatelab.mfm_android.helpers.static_classes.SaveFileHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by Steve on 5/17/2016.
 */
public class AudioRecorder {

    private Context appContext;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    public boolean isRecording;


    // class methods


    public void startRecording() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(appContext);
        this.audioFile = SaveFileHandler.getOutputMediaFile(appContext, SaveFileHandler.MEDIA_TYPE_AUDIO, globalHandler);
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
        this.isRecording = true;

        String message = "Recording audio";
        Toast toast = Toast.makeText(appContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void stopRecording() {
        this.mediaRecorder.stop();
        this.mediaRecorder.release();
        this.mediaRecorder = null;
        this.isRecording = false;

        String message = "Audio file has been saved.";
        Toast toast = Toast.makeText(appContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    public AudioRecorder(Context appContext) {
        this.appContext = appContext;
        this.isRecording = false;
        this.mediaRecorder = null;
    }

}
