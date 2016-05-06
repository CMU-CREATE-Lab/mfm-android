package org.cmucreatelab.mfm_android.helpers;


import android.util.Log;
import org.cmucreatelab.mfm_android.classes.Message;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.io.File;
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


    private void sendStudentMessage() {
        // TODO student message actions
        globalHandler.mfmRequestHandler.sendMessageStudent(message);
    }


    private void sendGroupMessage() {
        // TODO group message actions
        globalHandler.mfmRequestHandler.sendMessageGroup(message);
    }


    public void startSession(Sender sender) {
        this.message = new Message(sender);
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
        switch (message.getSender().getSenderType()) {
            case Student:
                sendStudentMessage();
                break;
            case Group:
                sendGroupMessage();
                break;
            default:
                Log.e(Constants.LOG_TAG, "could not match SenderType in SessionHandler");
        }
    }

}
