package org.cmucreatelab.mfm_android.helpers;

import android.content.Context;
import org.cmucreatelab.mfm_android.classes.Kiosk;

/**
 * Created by mike on 1/28/16.
 *
 * GlobalHandler
 *
 * Singleton object for storing application data structures.
 *
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    protected Context appContext;
    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    // the "kiosk" that this application represents
    final public Kiosk kiosk;


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.httpRequestHandler = new HttpRequestHandler(this);
        this.kiosk = new Kiosk();
    }

}
