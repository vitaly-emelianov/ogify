package net.ogify.engine.friends;

import com.google.common.cache.CacheLoader;
import net.ogify.engine.exceptions.SocialNetworkTokenMissedException;
import net.ogify.engine.vkapi.exceptions.VkSideError;

import java.util.Set;

/**
 * Created by melges on 24.02.2015.
 */
public class FriendCacheLoader extends CacheLoader<Long, Set<Long>> {
    @Override
    public Set<Long> load(Long userId) throws VkSideError, SocialNetworkTokenMissedException {
        return FriendProcessor.loadFriendList(userId);
    }
}
