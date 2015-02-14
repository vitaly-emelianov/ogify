package net.ogify.engine.vkapi.elements;

import net.ogify.database.entities.SocialNetwork;
import net.ogify.rest.elements.SNRequestUri;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object used for serialization of generated auth uri to vk servers.
 */
@XmlRootElement
public class VkAuthUri extends SNRequestUri{
    public VkAuthUri() {
        this.socialNetwork = SocialNetwork.Vk;
    }

    public VkAuthUri(String authUri) {
        this.requestUri = authUri;
        this.socialNetwork = SocialNetwork.Vk;
    }
}
