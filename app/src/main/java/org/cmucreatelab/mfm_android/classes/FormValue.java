package org.cmucreatelab.mfm_android.classes;


/**
 * Created by mike on 5/7/16.
 *
 * Represents an element in a FormRequest.
 *
 */
public class FormValue {

    public enum FormType {
        VALUE, FILE
    }
    private final FormValue.FormType formType = FormType.VALUE;

    // class attributes
    private byte[] body;
    // getters/setters
    public byte[] getBody() { return body; }
    public void setBody(byte[] body) { this.body = body; }
    public FormType getFormType() { return formType; }


    public FormValue(byte[] body) {
        this.body = body;
    }


    public FormValue(String body) {
        this.body = body.getBytes();
    }


    // Default constructor required for FormFile
    public FormValue() {}

}
