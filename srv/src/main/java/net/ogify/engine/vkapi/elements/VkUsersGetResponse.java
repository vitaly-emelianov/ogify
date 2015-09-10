package net.ogify.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Class represent answer which contain array of users info objects (such is returned from users.get method),
 * {@link VkUserInfo}
 *
 * May be serialized in JSON or XML.
 *
 * @author Morgen Matvey
 */
@XmlRootElement
public class VkUsersGetResponse {
    /**
     * Array of profiles
     */
    @XmlElement(name = "response")
    List<VkUserInfo> users;

    /**
     * Default constructor.
     */
    public VkUsersGetResponse() {
        users = null;
    }

    public List<VkUserInfo> getUsers() {
        return users;
    }
}
