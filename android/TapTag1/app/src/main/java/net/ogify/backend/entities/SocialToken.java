package net.ogify.backend.entities;

import java.util.Date;

public class SocialToken {
    Long tokenId;

    SocialNetwork tokensSocialNetwork;

    String token;

    Date expireIn;

    User owner;

    public SocialToken() {
    }

    public SocialToken(String token, Long expireIn, SocialNetwork socialNetwork, User owner) {
        this.token = token;
        this.tokensSocialNetwork = socialNetwork;
        this.setExpireIn(expireIn);
        this.owner = owner;
    }

    public void setExpireIn(Long expireIn) {
        if (expireIn <= 0)
            expireIn = 2629743L * 1000L;
        this.expireIn = new Date(System.currentTimeMillis() + expireIn);;
    }
}
