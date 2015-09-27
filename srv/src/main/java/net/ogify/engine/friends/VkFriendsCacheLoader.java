package net.ogify.engine.friends;

import com.google.common.cache.CacheLoader;
import net.ogify.database.entities.User;
import net.ogify.engine.vkapi.VkFriends;
import net.ogify.engine.vkapi.exceptions.VkSideError;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by melge on 27.09.2015.
 */
public class VkFriendsCacheLoader extends CacheLoader<User, Set<Long>> {
    @Override
    public Set<Long> load(User userId) throws VkSideError, ExecutionException {
        return VkFriends.getFriends(userId.getVkId(), userId.getVkToken().getToken());
    }
}