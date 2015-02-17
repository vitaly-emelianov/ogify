package net.ogify.backend.elements.vkapi;

import net.ogify.backend.entities.SocialNetwork;
import net.ogify.backend.elements.rest.SNRequestUri;


/**
 * Object used for serialization of generated auth uri to vk servers.
 */
public class VkAuthUri extends SNRequestUri {
    public VkAuthUri() {
        this.socialNetwork = SocialNetwork.Vk;
    }

    public VkAuthUri(String authUri) {
        this.requestUri = authUri;
        this.socialNetwork = SocialNetwork.Vk;
    }
}
