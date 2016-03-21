package org.cmucreatelab.mfm_android.helpers.static_classes;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Steve on 3/18/2016.
 */
public class SaveFileHandler {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_AUDIO = 2;

    public static File getOutputMediaFile(Context context, int type){

        File mediaStorageDir = new File("","");

        if(type == MEDIA_TYPE_IMAGE){
            mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Message From Me");
        } else if(type == MEDIA_TYPE_AUDIO){
            mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Message From Me");
        }

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Message From Me", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_AUDIO){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "AUDIO_" + timeStamp + ".wav");
        } else{
            return null;
        }

        return mediaFile;
    }
}
