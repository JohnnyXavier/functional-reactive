package com.baremetalcode.functionalreactive.services.cachekeyservice.strategies;

import com.baremetalcode.functionalreactive.services.cachekeyservice.strategies.base.DefaultCacheKeyServiceStrategy;
import org.springframework.stereotype.Service;

/**
 * Cache key strategy to build the all posts cache key
 *
 * @author Johnny Xavier
 */
@Service
public class PostAllCacheKeyStrategy extends DefaultCacheKeyServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostsAll();
    }


    @Override
    public String getIdHeader()
    {
        return getConfig().getStrategies().getPostsAll();
    }
}
