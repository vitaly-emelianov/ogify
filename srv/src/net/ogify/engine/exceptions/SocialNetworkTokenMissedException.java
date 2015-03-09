package net.ogify.engine.exceptions;

/**
 * Created by melges on 09.03.2015.
 */
public class SocialNetworkTokenMissedException extends Exception {
    public Long userId;

    public SocialNetworkTokenMissedException(String message) {
        super(message);
    }

    public SocialNetworkTokenMissedException(Long userId) {
        super(String.format("User with id %d haven't valid social tokens", userId));
        this.userId = userId;
    }
}
