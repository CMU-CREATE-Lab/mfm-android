package org.cmucreatelab.mfm_android.classes;


/**
 * Created by mike on 5/7/16.
 *
 * Represents a File (defined by Content-Type) in a FormRequest.
 *
 */
public final class FormFile extends FormValue {

    private final FormValue.FormType formType = FormType.FILE;

    // class attributes
    private String filename;
    private String contentType;
    // getters/setters
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public FormType getFormType() { return formType; }


    public FormFile(String filename, String contentType, byte[] body) {
        this.filename = filename;
        this.contentType = contentType;
        this.setBody(body);
    }

}
