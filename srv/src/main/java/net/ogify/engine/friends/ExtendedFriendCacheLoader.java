package net.ogify.engine.friends;

import com.google.common.cache.CacheLoader;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by melges on 09.03.2015.
 */
public class ExtendedFriendCacheLoader extends CacheLoader<Long, Set<Long>> {
    private final static Logger logger = Logger.getLogger(ExtendedFriendCacheLoader.class);

    private FriendService friendService;

    public ExtendedFriendCacheLoader(FriendService friendService) {
        this.friendService = friendService;
    }

    @Override
    public Set<Long> load(Long userId) throws VkSideError, ExecutionException {
        logger.debug(String.format("Calculate friends of friends for user with id %d", userId));
        return friendService.loadExtendedFriendList(userId);
    }
}
