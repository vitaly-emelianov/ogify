package net.ogify.engine.vkapi.elements.wall;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Class represents response on
 */
@XmlRootElement
public class WallGetResponse {
    public static class ResponseBody {
        @XmlElement(name = "count", required = true)
        int count;

        @XmlElement(name = "items", required = true)
        Set<WallPost> postSet;
    }

    @NotNull
    @XmlElement(name = "response", required = true)
    private ResponseBody response;

    public int getCount() {
        return response.count;
    }

    public Set<WallPost> getPostSet() {
        return response.postSet;
    }
}
