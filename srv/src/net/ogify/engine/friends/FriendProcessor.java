package net.ogify.engine.friends;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import net.ogify.database.UserController;
import net.ogify.database.entities.User;
import net.ogify.engine.vkapi.VkFriends;
import net.ogify.engine.vkapi.exceptions.VkSideError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by melges on 24.02.2015.
 */
public class FriendProcessor {
    private static final long cacheSize = 10000;

    protected static LoadingCache<Long, Set<Long>> friendsCache;

    static {
        friendsCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build(new FriendCacheLoader());
    }

    protected static Set<Long> loadFriendList(Long userId) throws VkSideError {
        User user = UserController.getUserById(userId);
        if(user == null)
            return null;

        Set<Long> vkFriendsIds = VkFriends.getFriends(user.getVkId());
        List<User> filteredFriends = UserController.getUserWithVkIds(vkFriendsIds);
        HashSet<Long> resultSet = new HashSet<>(filteredFriends.size());
        for(User filteredUser : filteredFriends)
            resultSet.add(filteredUser.getId());

        return resultSet;
    }

    public static Set<Long> getUserFriendsIds(Long userId) throws ExecutionException {
        return friendsCache.get(userId);
    }

    public static Set<Long> getUserExtendedFriendsIds(Long userId) throws VkSideError {
        return loadFriendList(userId);
    }


}
