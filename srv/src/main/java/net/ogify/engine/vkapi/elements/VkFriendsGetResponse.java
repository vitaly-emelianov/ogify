package net.ogify.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

/**
 * Created by melges on 27.02.2015.
 */
@XmlRootElement
public class VkFriendsGetResponse {
    /**
     * Array of profiles
     */
    @XmlElement(name = "response", required = true, nillable = false)
    VkFriendsResponse response;

    public Integer getCount() {
        return response.getCount();
    }

    public Set<Long> getItems() {
        return response.getItems();
    }
}
