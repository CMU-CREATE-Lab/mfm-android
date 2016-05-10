package org.cmucreatelab.mfm_android.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.cmucreatelab.mfm_android.classes.FormFile;
import org.cmucreatelab.mfm_android.classes.FormValue;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 5/7/16.
 *
 * Extends from the Volley library. Handles form data for multipart (MIME-like) requests.
 *
 */
public class FormRequestHandler extends JsonObjectRequest {

    /** A string that bounds each element in the form. */
    // TODO this can probably be generated in a better way to avoid collisions
    private static final String BOUNDARY = "95edcdfc1c216595775c8556bf7cf3ed";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("multipart/form-data; boundary=%s", BOUNDARY);

    /** This gets placed before/after the boundary. */
    private static final String BOUNDARY_PREFIX_SUFFIX = "--";

    /** This structure holds all of our form elements.
     * Every form value is associated with a key (i.e. "curl -F key=value"). */
    private final HashMap<String,FormValue> formElements = new HashMap<>();


    // helper method for concatenating byte arrays
    private static byte[] concatBytes(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    // class constructors


    public FormRequestHandler(int method, String url, HashMap<String,FormValue> formElements, Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        this(method,url,formElements,null,listener,errorListener);
    }


    public FormRequestHandler(int method, String url, HashMap<String,FormValue> formElements, JSONObject requestParams, Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        super(method,url,requestParams,listener,errorListener);
        this.formElements.putAll(formElements);
    }


    // override methods


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        // Volley appears to use "Accept-Encoding: gzip" somewhere, but including this will override that preference.
        headers.put("Accept", "*/*");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }


    /**
     * Responsible for constructing our form body. Each form element (both files and simple key-value pairs) is defined in here.
     * @return an array of bytes representing the body of the form to be requested
     */
    @Override
    public byte[] getBody() {
        byte[] result = (BOUNDARY_PREFIX_SUFFIX + BOUNDARY).getBytes();

        for (String key: formElements.keySet()) {
            result = concatBytes(result, ("\r\nContent-Disposition: form-data; name=\""+key+"\"").getBytes());

            FormValue element = formElements.get(key);
            if (element.getFormType() == FormValue.FormType.FILE) {
                FormFile file = (FormFile)element;
                result = concatBytes( result, ("; filename=\""+file.getFilename()+"\"\r\nContent-Type: "+file.getContentType()+"\r\n\r\n").getBytes() );
                result = concatBytes( result, file.getBody() );
            } else {
                result = concatBytes( result, "\r\n\r\n".getBytes() );
                result = concatBytes( result, element.getBody() );
            }
            result = concatBytes( result, ("\r\n"+BOUNDARY_PREFIX_SUFFIX+BOUNDARY).getBytes() );
        }
        result = concatBytes( result, BOUNDARY_PREFIX_SUFFIX.getBytes() );

        return result;
    }
}
