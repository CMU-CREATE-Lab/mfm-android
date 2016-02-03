package org.cmucreatelab.mfm_android.classes;

import java.util.ArrayList;

/**
 * Created by mike on 2/2/16.
 */
public class Message {

    Sender sender;
    ArrayList<User> recipients; // MessageType.Student only
    Object audio; // TODO audio object
    Object photo; // TODO photo object

}
