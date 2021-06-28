package com.baremetalcode.functionalreactive.services.cachekeyservice.strategies;

import com.baremetalcode.functionalreactive.services.cachekeyservice.strategies.base.DefaultCacheKeyServiceStrategy;
import org.springframework.stereotype.Service;

/**
 * Cache key strategy to build the post by id cache key
 *
 * @author Johnny Xavier
 */
@Service
public class PostByIdCacheKeyStrategy extends DefaultCacheKeyServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostById();
    }


    @Override
    public String getIdHeader()
    {
        return getConfig().getHeaders().getPostIdHeader();
    }
}
