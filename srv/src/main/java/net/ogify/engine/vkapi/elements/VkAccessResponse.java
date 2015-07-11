package net.ogify.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object represent format which vk use for response on access get method.
 *
 * See more in vk docs at http://vk.com/dev
 *
 * May be serialized in JSON or XML.
 *
 * @author Morgen Matvey
 */
@XmlRootElement
public class VkAccessResponse {
    /**
     * Returned by vk access token. Access token is used for work with vk api and should be added to every query.
     */
    @XmlElement(name = "access_token")
    private String accessToken;

    /**
     * User id (we will call id vkId)
     */
    @XmlElement(name = "user_id")
    private long userId;

    /**
     * Tokens time of live in seconds
     */
    @XmlElement(name = "expires_in")
    private long expiresIn;

    /**
     * Users email
     */
    @XmlElement(name = "email", required = false)
    private String email;

    /**
     * Default constructor, make empty object
     */
    public VkAccessResponse() {
    }

    /**
     *
     * @param accessToken vk access token
     * @param userId
     * @param expiresIn
     */
    public VkAccessResponse(String accessToken, long userId, long expiresIn) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getUserId() {
        return userId;
    }

    /**
     * Time that access token is valid in milliseconds
     * @return time in milliseconds
     */
    public long getExpiresIn() {
        return expiresIn * 1000;
    }

    public String getEmail() {
        return email;
    }
}
