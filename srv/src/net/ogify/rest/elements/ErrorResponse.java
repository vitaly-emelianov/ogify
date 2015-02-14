package net.ogify.rest.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by melges on 17.12.14.
 */
@XmlRootElement
public class ErrorResponse {
    @XmlElement
    public String error;

    @XmlElement
    public String errorDescription;

    public ErrorResponse() {
        error = "None";
        errorDescription = "Not provided";
    }

    public ErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }
}
