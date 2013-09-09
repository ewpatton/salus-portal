package edu.rpi.tw.mobilehealth.model;

import java.io.Serializable;
import java.net.URI;

public class NamedThing implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4040740996177533795L;
    private URI uri;
    private String label;
    private String comment;

    public NamedThing() {
        setUri(null);
        setLabel(null);
        setComment(null);
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
