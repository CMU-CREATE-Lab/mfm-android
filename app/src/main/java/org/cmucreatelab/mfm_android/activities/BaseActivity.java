package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;

import java.io.IOException;

/**
 * Created by Steve on 6/3/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String AUDIO_PLAYER_KEY = "audio_player";
    protected AudioPlayer audioPlayer;


    @Override
    public void onBackPressed() {
        audioPlayer.stop();
        super.onBackPressed();
    }


    public void onButtonClick(Context context) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });

        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.button_click);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (savedInstanceState != null) {
            audioPlayer = (AudioPlayer) savedInstanceState.getParcelable(AUDIO_PLAYER_KEY);
        } else {*/
        audioPlayer = AudioPlayer.getInstance(this.getApplicationContext());
        //}
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /* Causes error when exiting and opening app because some classes
        inside audio player are not parcelable */
        //outState.putParcelable(AUDIO_PLAYER_KEY, audioPlayer);
    }

}
