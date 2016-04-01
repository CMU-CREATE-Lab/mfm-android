package org.cmucreatelab.mfm_android.helpers.readings;

/**
 * Created by Steve on 4/1/2016.
 */
public interface Readable {

    public enum Type {
        STUDENT, GROUP, USER
    }


    // returns the Type that the Readable object is
    public Type getReadableType();

    // returns a human-readable name asssociated with the Reading
    public String getName();

    // return true if the Readable object has a value
    public boolean hasReadableValue();

    // return a value (should be 0.0 by default but verified with hasReadableValue)
    public double getReadableValue();

}
