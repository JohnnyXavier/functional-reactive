package com.baremetalcode.functionalreactive.services.cachekeyservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.BaseStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Cache key service top abstract strategy to be extended / implemented by inheritors.
 *
 * @author Johnny Xavier
 */
@Service
public abstract class CacheKeyServiceStrategy extends BaseStrategy
{
    /**
     * Generates a key to be used when retrieving or putting data to the cache
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<PipelineMessage> generateKey( PipelineMessage message );

    /**
     * returns and id header to be used in the creation of the cache key
     *
     * @return the id header.
     */
    public abstract String getIdHeader();
}
