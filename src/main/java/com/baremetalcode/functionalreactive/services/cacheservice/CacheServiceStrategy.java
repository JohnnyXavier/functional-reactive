package com.baremetalcode.functionalreactive.services.cacheservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.BaseStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Cache service top abstract strategy to be extended / implemented by inheritors.
 *
 * @author Johnny Xavier
 */
@Service
public abstract class CacheServiceStrategy extends BaseStrategy
{
    /**
     * Puts a key/value pair to the cache asynchronously.
     * <p>
     * we don't care to wait for the cache to be there or even succeed in real time.
     * <p>
     * if the operation fails (ie: the cache crashed), we will be able to investigate the logs but will not prevent the caller to get the requested data
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<PipelineMessage> put( PipelineMessage message );

    /**
     * Gets a record from the cache by "querying" it's key.
     * <p>
     * if the operation fails (ie: the cache crashed), we will be able to investigate the logs but will not prevent the caller to get the requested data and we will
     * move down the pipeline and try to find it if possible
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<PipelineMessage> get( PipelineMessage message );

    public abstract String getMapName();

    public abstract Long getTTL();
}
