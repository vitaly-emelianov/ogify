package net.ogify.engine.vkapi.elements;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Created by melges on 25.02.2015.
 */
@XmlRootElement
public class VkFriendsResponse {
    private Integer count;

    private Set<Long> items;

    public VkFriendsResponse() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<Long> getItems() {
        return items;
    }

    public void setItems(Set<Long> items) {
        this.items = items;
    }
}
