package org.cmucreatelab.mfm_android.helpers;


import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.cmucreatelab.mfm_android.classes.Message;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


    public void startSession(Sender sender) {
        this.message = new Message(sender);
    }


    // This is ugly but I need it to send the post message correctly.
    public int[] getRecipientsIds() {
        int size = message.getRecipients().size();
        int[] ids = new int[size];
        for (int i = 0; i < size; i++) {
            ids[i] = message.getRecipients().get(i).getId();
        }
        return ids;
    }

    public ArrayList<User> getRecipients() {
        return message.getRecipients();
    }


    public Sender getMessageSender() {
        return message.getSender();
    }


    public File getMessagePhoto() { return this.message.getPhoto(); }


    public File getMessageAudio() { return this.message.getAudio(); }


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
        byte[] binPhoto = new byte[0];
        byte[] binAudio = new byte[0];
        try {
            binPhoto = FileUtils.readFileToByteArray(message.getPhoto());
            binAudio = FileUtils.readFileToByteArray(message.getAudio());
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error in reading a file to a byte array in sendMessage.");
        }
        globalHandler.sendPost(binPhoto, binAudio);
    }

}
