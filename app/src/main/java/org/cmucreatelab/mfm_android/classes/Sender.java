package org.cmucreatelab.mfm_android.classes;

/**
 * Created by mike on 2/2/16.
 *
 * Sender
 *
 * Interface for those that are able to send messages. In MFM, messages can come from Students or Groups.
 *
 */
public interface Sender {

    public enum Type {
        Student,
        Group
    }

    public Type getSenderType();

    public int getId();

    public String getName();

}
