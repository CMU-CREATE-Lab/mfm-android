package org.cmucreatelab.mfm_android.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mike on 2/2/16.
 */
public class Message {

    // class attributes
    private Sender sender;
    private File audio;
    private File photo;
    private final ArrayList<User> recipients = new ArrayList<>(); // MessageType.Student only

    // getters/setters
    public ArrayList<User> getRecipients() { return recipients; }
    public Sender getSender() { return sender; }
    public File getAudio() { return audio; }
    public File getPhoto() { return photo; }
    public void setRecipients(Collection<User> recipients) { this.recipients.clear(); this.recipients.addAll(recipients); }
    public void setSender(Sender sender) { this.sender = sender; }
    public void setAudio(File audio) { this.audio = audio; }
    public void setPhoto(File photo) { this.photo = photo; }


    public Message(Sender sender) {
        this.sender = sender;
    }

}
