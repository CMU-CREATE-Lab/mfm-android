package org.cmucreatelab.mfm_android.helpers;


import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.cmucreatelab.mfm_android.classes.Message;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by mike on 3/30/16.
 *
 * SessionHandler
 *
 * Represents the process of the app-user trying to send a message (selecting sender, recipients, photo/audio, and sending)
 *
 */
public class SessionHandler {

    private GlobalHandler globalHandler;
    private Message message;


    public SessionHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // TODO We probably don't need these seperate methods since the globalHandler.sendPost handles it
    private void sendStudentMessage() {
        // TODO student message actions
    }


    private void sendGroupMessage() {
        // TODO group message actions
    }


    public void startSession(Sender sender) {
        this.message = new Message(sender);
    }


    public int[] getRecipientsIds() {
        int size = message.getRecipients().size();
        int[] ids = new int[size];
        for (int i = 0; i < size; i++) {
            ids[i] = message.getRecipients().get(i).getId();
        }
        return ids;
    }


    public Sender getMessageSender() {
        return message.getSender();
    }


    public void setMessagePhoto(File photo) {
        this.message.setPhoto(photo);
    }


    public void setMessageAudio(File audio) {
        this.message.setAudio(audio);
    }


    public void setMessageRecipients(Collection<User> recipients) {
        this.message.setRecipients(recipients);
    }


    public void sendMessage() {
        /*switch (message.getSender().getSenderType()) {
            case Student:
                sendStudentMessage();
                break;
            case Group:
                sendGroupMessage();
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not match SenderType in SessionHandler");
        }*/

        byte[] photo = new byte[0];
        byte[] audio = new byte[0];
        try {
            photo = FileUtils.readFileToByteArray(message.getPhoto());
            audio = FileUtils.readFileToByteArray(message.getAudio());
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error in reading a file to a byte array in sendMessage.");
        }
        String encodedPhoto = Base64.encodeToString(photo, 0);
        String encodedAudio = Base64.encodeToString(audio, 0);

        byte[] binPhoto = Base64.decode(encodedPhoto, Base64.DEFAULT);
        byte[] binAudio = Base64.decode(encodedAudio, Base64.DEFAULT);
        globalHandler.sendPost(binPhoto, binAudio);
    }

}
