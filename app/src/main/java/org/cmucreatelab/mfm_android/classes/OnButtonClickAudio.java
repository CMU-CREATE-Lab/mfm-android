package org.cmucreatelab.mfm_android.classes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 5/23/2016.
 */
public abstract class OnButtonClickAudio extends AppCompatActivity{

    public void onButtonClick(Context context) {
        AudioPlayer audio = AudioPlayer.newInstance(context);
        audio.addAudio(R.raw.button_click);
        try {
            audio.playAudio();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.toString());
        }
    }

}
