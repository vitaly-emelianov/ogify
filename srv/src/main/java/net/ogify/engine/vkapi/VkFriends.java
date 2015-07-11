package net.ogify.engine.vkapi;

import net.ogify.engine.vkapi.elements.VkFriendsGetResponse;
import net.ogify.engine.vkapi.exceptions.VkSideError;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by melges on 24.02.2015.
 */
public class VkFriends {
    /**
     * Method returns set of users friends ids.
     * @param vkUserId user id.
     * @return set of friends ids.
     */
    public static Set<Long> getFriends(Long vkUserId, String accessToken) throws VkSideError {
        final String methodName = "friends.get";
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", vkUserId);
        parameters.put("access_token", accessToken);

        return VkClient.call(VkClient.VK_API_URI + methodName, parameters, VkFriendsGetResponse.class).getItems();
    }
}
