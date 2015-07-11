package net.ogify.rest.elements;

import net.ogify.database.entities.SocialNetwork;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by melges.morgen on 15.02.15.
 */
@XmlRootElement
public class SNRequestUri {
    protected SocialNetwork socialNetwork;

    protected String requestUri;

    public SocialNetwork getSocialNetwork() {
        return socialNetwork;
    }

    public String getRequestUri() {
        return requestUri;
    }
}
