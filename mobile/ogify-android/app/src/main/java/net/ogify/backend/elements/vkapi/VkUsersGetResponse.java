package net.ogify.backend.elements.vkapi;

import java.util.List;

/**
 * Class represent answer which contain array of users info objects (such is returned from users.get method),
 * @{link VkUserInfo}.
 *
 * May be serialized in JSON or XML.
 *
 * @author Morgen Matvey
 */
public class VkUsersGetResponse {
    /**
     * Array of profiles
     */
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
