package net.ogify.engine.friends;

import com.google.common.cache.CacheLoader;
import net.ogify.engine.exceptions.SocialNetworkTokenMissedException;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by melges on 24.02.2015.
 */
public class FriendCacheLoader extends CacheLoader<Long, Set<Long>> {
    private final static Logger logger = Logger.getLogger(FriendCacheLoader.class);

    FriendService friendService;

    public FriendCacheLoader(FriendService friendService) {
        this.friendService = friendService;
    }

    @Override
    public Set<Long> load(Long userId) throws VkSideError, SocialNetworkTokenMissedException, ExecutionException {
        logger.debug(String.format("Query vk for friends of user with id %d", userId));
        return friendService.loadFriendList(userId);
    }
}
