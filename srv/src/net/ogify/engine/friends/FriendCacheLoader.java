package net.ogify.engine.friends;

import com.google.common.cache.CacheLoader;
import net.ogify.engine.vkapi.exceptions.VkSideError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by melges on 24.02.2015.
 */
public class FriendCacheLoader extends CacheLoader<Long, Set<Long>> {
    @Override
    public Set<Long> load(Long aLong) throws VkSideError {
        return new HashSet<Long>();
    }
}
