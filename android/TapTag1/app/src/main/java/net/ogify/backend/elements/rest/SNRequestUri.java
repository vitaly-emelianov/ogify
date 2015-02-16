package net.ogify.backend.elements.rest;

import net.ogify.backend.entities.SocialNetwork;

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
