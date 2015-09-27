package net.ogify.engine.walls;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.ogify.database.UserController;
import net.ogify.database.entities.User;
import net.ogify.engine.exceptions.LoadWallPostsException;
import net.ogify.engine.friends.FriendService;
import net.ogify.engine.vkapi.VkWall;
import net.ogify.engine.vkapi.elements.wall.WallPost;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Service for retrieving wall posts from vk
 */
@Service
public class WallPostsService {
    private final static Logger logger = Logger.getLogger(WallPostsService.class);

    private final static long cacheSize = 10000;

    LoadingCache<Long, Set<WallPost>> wallPostCache;

    @Autowired
    UserController userController;

    @Autowired
    FriendService friendService;

    public WallPostsService() {
        wallPostCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Long, Set<WallPost>>() {
                    @Override
                    public Set<WallPost> load(Long userId) throws Exception {
                        return getWallPostsOfUser(userId);
                    }
                });
    }

    public Set<WallPost> getWallPostsOfUser(Long userId) throws VkSideError {
        User user = userController.getUserById(userId);
        return VkWall.getPosts(user.getVkId(), user.getVkToken().getToken());
    }

    public Set<WallPost> getWallPostsOfUser(User user) throws LoadWallPostsException {
        Exception lastException = null;
        for (int tries = 0; tries < 3; tries++) {
            try {
                return wallPostCache.get(user.getId());
            } catch (ExecutionException e) {
                logger.warn(String.format("Error while retrieving wall posts from vk: %s, attempt %d from 3",
                        e.getCause().getLocalizedMessage(), tries));
                lastException = e;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    throw new RuntimeException("Can't wait for next attempt while download posts from vk walls",
                            e1);
                }
            }
        }

        throw new LoadWallPostsException("Error loading posts in 3 attempts", lastException);
    }

    public Set<WallPost> getWallPostsOfUsers(Set<Long> userIds) throws VkSideError {
        List<User> usersList = userController.getUsersWithIds(userIds);

        Set<WallPost> resultSet = new HashSet<>();
        for(User user : usersList)
            resultSet.addAll(getWallPostsOfUser(user));

        return resultSet;
    }

    public List<WallPost> getAllPosts(int maxCount) throws VkSideError {
        List<WallPost> resultList = new ArrayList<>(maxCount);
        for(int usersPage = 0; resultList.size() < maxCount; usersPage++) {
            List <Long> usersList = userController.getAllUsersIds(200, usersPage);
            if(usersList.isEmpty())
                break;

            resultList.addAll(getWallPostsOfUsers(new HashSet<>(usersList)));
        }

        return resultList;
    }
}
