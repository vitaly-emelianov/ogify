package net.ogify.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object represent error response from vk.
 *
 * Contain error code and description.
 *
 * May be serialized to JSON or XML.
 *
 * @author Morgen Matvey
 */
@XmlRootElement
public class VkErrorResponse {
    /**
     * Error code.
     */
    @XmlElement(name = "error")
    private String error;

    /**
     * Error description.
     */
    @XmlElement(name = "error_description")
    private String errorDescription;

    /**
     * Construct empty object.
     */
    public VkErrorResponse() {
    }

    /**
     * Construct error with description.
     * @param error error code.
     * @param errorDescription error description.
     */
    public VkErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
