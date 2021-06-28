package com.baremetalcode.functionalreactive.services.cachekeyservice.strategies;

import com.baremetalcode.functionalreactive.services.cachekeyservice.strategies.base.DefaultCacheKeyServiceStrategy;
import org.springframework.stereotype.Service;

/**
 * Cache key strategy to build the comments by post by id cache key
 *
 * @author Johnny Xavier
 */
@Service
public class CommentsByPostByIdCacheKeyStrategy extends DefaultCacheKeyServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getCommentsByPostId();
    }


    @Override
    public String getIdHeader()
    {
        return getConfig().getHeaders().getPostIdHeader();
    }
}
