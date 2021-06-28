package com.baremetalcode.functionalreactive.services.cacheservice.strategies;

import com.baremetalcode.functionalreactive.services.cacheservice.strategies.base.IMDGCacheServiceStrategy;
import org.springframework.stereotype.Service;

/**
 * External vendor request class for al posts
 *
 * @author Johnny Xavier
 */
@Service
public class PostByIdDataGridStrategy extends IMDGCacheServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostById();
    }

    @Override
    public String getMapName()
    {
        return getConfig().getCache().getPostsById();
    }

    @Override
    public Long getTTL()
    {
        return Long.valueOf( getConfig().getCache().getPostsByIdTtl() );
    }
}
