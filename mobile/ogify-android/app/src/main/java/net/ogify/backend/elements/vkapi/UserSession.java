package net.ogify.backend.elements.vkapi;

import net.ogify.backend.entities.User;

import java.util.Date;

/**
 * Created by melges.morgen on 14.02.15.
 */

public class UserSession {
    Long id;

    private String sessionSecret;

    Date expireIn;

    User owner;

    public UserSession() {
    }

    public UserSession(String sessionSecret, Long expireIn, User owner) {
        this.sessionSecret = sessionSecret;
        this.setExpireIn(expireIn);
        this.owner = owner;
    }

    public void setExpireIn(Long expireIn) {
        if (expireIn <= 0)
            expireIn = 2629743L * 1000L;
        this.expireIn = new Date(System.currentTimeMillis() + expireIn);
    }

    public Long getId() {
        return id;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public Date getExpireIn() {
        return expireIn;
    }

    public User getOwner() {
        return owner;
    }
}
