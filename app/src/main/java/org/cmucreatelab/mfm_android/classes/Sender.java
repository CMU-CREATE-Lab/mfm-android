package org.cmucreatelab.mfm_android.classes;

import java.io.Serializable;

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
        student,
        group
    }

    public Type getSenderType();

    public int getId();

    public String getName();

}
