package org.cmucreatelab.mfm_android.classes;

/**
 * Created by mike on 2/2/16.
 */
public interface Sender {
    enum Type {
        Student,
        Group
    };

    public Type getSenderType();

    public int getId();

    public String getName();

}
