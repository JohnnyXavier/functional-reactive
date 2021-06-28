package com.baremetalcode.functionalreactive.services.cachekeyservice.strategies.base;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.cachekeyservice.CacheKeyServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Default cache key strategy to act as basis for others cacheKey strategies to extend
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public abstract class DefaultCacheKeyServiceStrategy extends CacheKeyServiceStrategy
{
    @Override
    public Mono<PipelineMessage> generateKey( final PipelineMessage message )
    {
        String strategy = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );

        // if there is no id for a given header, which can happen for an all users, all posts, all something, we just use "default" as id for the key
        String id = Optional.ofNullable( message.getHeaders().getFirst( getIdHeader() ) )
                            .orElse( "default" );

        String cacheKey = strategy + "-" + id;
        message.getHeaders().set( getConfig().getHeaders().getCacheKeyHeader(), cacheKey );

        log.debug( "generated cache-key: {}", cacheKey );

        return Mono.just( message );
    }
}
