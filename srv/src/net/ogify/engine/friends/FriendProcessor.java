package net.ogify.engine.friends;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import net.ogify.database.UserController;
import net.ogify.database.entities.User;
import net.ogify.engine.exceptions.SocialNetworkTokenMissedException;
import net.ogify.engine.vkapi.VkFriends;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by melges on 24.02.2015.
 */
public class FriendProcessor {
    private final static Logger logger = Logger.getLogger(FriendProcessor.class);

    private static final long cacheSize = 10000;

    protected static LoadingCache<Long, Set<Long>> friendsCache;

    protected static LoadingCache<Long, Set<Long>> extendedFriendsCache;

    static {
        friendsCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build(new FriendCacheLoader());

        extendedFriendsCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build(new ExtendedFriendCacheLoader());
    }

    protected static Set<Long> loadFriendList(Long userId)
            throws VkSideError, IllegalArgumentException, SocialNetworkTokenMissedException {
        User user = UserController.getUserById(userId);
        if(user == null)
            throw new IllegalArgumentException(String.format("Invalid user with id: %d", userId));

        if(user.getVkToken() == null)
            throw new SocialNetworkTokenMissedException(userId);

        Set<Long> vkFriendsIds = VkFriends.getFriends(user.getVkId(), user.getVkToken().getToken());
        List<User> filteredFriends = UserController.getUserWithVkIds(vkFriendsIds);
        HashSet<Long> resultSet = new HashSet<>(filteredFriends.size());
        for(User filteredUser : filteredFriends)
            resultSet.add(filteredUser.getId());

        return resultSet;
    }

    protected static Set<Long> loadExtendedFriendList(Long userId) throws ExecutionException {
        Set<Long> friends = getUserFriendsIds(userId);

        Set<Long> extendedFriends = new HashSet<>();
        for(Long friendId : friends)
            try {
                extendedFriends.addAll(getUserFriendsIds(friendId));
            } catch (ExecutionException loadException) {
                logger.warn(String.format("Error on loading friends for user with id %d: no valid social tokens",
                        userId));
            }

        return extendedFriends;
    }

    public static Set<Long> getUserFriendsIds(Long userId) throws ExecutionException {
        return friendsCache.get(userId);
    }

    public static Set<Long> getUserExtendedFriendsIds(Long userId) throws ExecutionException {
        return extendedFriendsCache.get(userId);
    }


}
