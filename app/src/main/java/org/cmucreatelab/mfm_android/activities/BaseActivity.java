package org.cmucreatelab.mfm_android.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;

/**
 * Created by Steve on 6/3/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {


    public void onButtonClick(Context context) {
        AudioPlayer audio = AudioPlayer.newInstance(context);
        audio.addAudio(R.raw.button_click);
        audio.playAudio();
    }

}
