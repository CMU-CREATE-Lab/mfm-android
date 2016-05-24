package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Steve on 5/20/2016.
 *
 * Used to play audio.
 * Make sure to add the audio ids to the queue before calling playAudio().
 *
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener, Serializable {

    private static AudioPlayer classInstance;
    private Context appContext;
    private MediaPlayer mediaPlayer;
    private ConcurrentLinkedQueue<Integer> fileIds;


    private void playNext() throws IOException {
        Uri uri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + fileIds.poll());
        mediaPlayer.reset();
        mediaPlayer.setDataSource(appContext, uri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }


    private AudioPlayer(Context context) {
        fileIds = new ConcurrentLinkedQueue<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        appContext = context;
    }


    public static AudioPlayer newInstance(Context context) {
        if (classInstance == null) {
            return new AudioPlayer(context);
        } else {
            return classInstance;
        }
    }


    public synchronized void addAudio(Integer fileId) {
        fileIds.add(fileId);
    }


    public synchronized void playAudio() {
        if (!fileIds.isEmpty() && !mediaPlayer.isPlaying()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in playAudio - AudioPlayer.");
            }
        }
    }


    public synchronized void stop() {
        if (mediaPlayer != null) {
            fileIds.clear();
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }


    public synchronized void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public synchronized void onCompletion(MediaPlayer mediaPlayer) {
        if (!fileIds.isEmpty()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in onCompletion - AudioPlayer.");
            }
        } else {
            stop();
        }
    }
}
