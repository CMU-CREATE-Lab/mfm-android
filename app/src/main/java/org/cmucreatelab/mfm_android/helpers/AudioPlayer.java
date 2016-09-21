package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Steve on 5/20/2016.
 *
 * AudioPlayer
 *
 * A helper class to simplify playing audio clips.
 *
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener, Parcelable {

    public boolean playedBlueButton;
    private static AudioPlayer classInstance;
    private Context appContext;
    public MediaPlayer mediaPlayer;
    private ConcurrentLinkedQueue<Integer> fileIds;


    protected AudioPlayer(Parcel in) {
        playedBlueButton = in.readByte() != 0;
    }

    public static final Creator<AudioPlayer> CREATOR = new Creator<AudioPlayer>() {
        @Override
        public AudioPlayer createFromParcel(Parcel in) {
            return new AudioPlayer(in);
        }

        @Override
        public AudioPlayer[] newArray(int size) {
            return new AudioPlayer[size];
        }
    };

    private void playNext() throws IOException {
        Uri uri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + fileIds.poll());
        mediaPlayer.reset();
        mediaPlayer.setDataSource(appContext, uri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }


    private AudioPlayer(Context context) {
        fileIds = new ConcurrentLinkedQueue<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        appContext = context;
        playedBlueButton = false;
    }


    public static AudioPlayer getInstance(Context context) {
        if (classInstance == null) {
            return new AudioPlayer(context);
        } else {
            return classInstance;
        }
    }


    public void addAudio(Integer fileId) {
        fileIds.add(fileId);
    }


    public void playAudio() {
        if (!fileIds.isEmpty() && !mediaPlayer.isPlaying()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in playAudio - AudioPlayer.");
            }
        }
    }


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            fileIds.clear();
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }


    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!fileIds.isEmpty()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in onCompletion - AudioPlayer.");
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(fileIds);
    }
}