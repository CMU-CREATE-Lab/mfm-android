package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;

import java.io.IOException;

/**
 * Created by Steve on 6/3/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected AudioPlayer audioPlayer;


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
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        audioPlayer = AudioPlayer.getInstance(this.getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioPlayer.stop();
        audioPlayer.release();
    }

}
