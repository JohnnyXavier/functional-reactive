package com.baremetalcode.functionalreactive.services.cacheservice.strategies.base;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.cacheservice.CacheServiceStrategy;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * In Memory Data Grid using Hazelcast cache strategy to act as basis for others cache strategies to extend
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public abstract class IMDGCacheServiceStrategy extends CacheServiceStrategy
{
    @Qualifier( "hazelcastInstance" )
    @Autowired
    private HazelcastInstance cache;

    @Override
    public Mono<PipelineMessage> put( final PipelineMessage message )
    {
        String cacheKey = message.getHeaders().getFirst( getConfig().getHeaders().getCacheKeyHeader() );
        String cacheValue = message.getDataPayload();

        Optional.ofNullable( cacheKey )
                .ifPresentOrElse( key ->
                        {
                            try
                            {
                                cache.getMap( getMapName() ).putAsync( cacheKey, message.getDataPayload(), getTTL(), TimeUnit.SECONDS );
                                log.debug( "putting record to cache - key: {}, value: {}", key, cacheValue.replace( "\n", "" ) );
                            }
                            catch ( IllegalStateException | IllegalArgumentException exception )
                            {
                                log.error( "error putting record to cache - error: {}", exception.getMessage() );
                            }

                        },
                        () -> log.debug( "null cache key: nothing to put to cache" ) );


        return Mono.just( message );
    }


    @Override
    public Mono<PipelineMessage> get( final PipelineMessage message )
    {
        String cacheKey = message.getHeaders().getFirst( getConfig().getHeaders().getCacheKeyHeader() );

        Optional.ofNullable( cacheKey )
                .ifPresentOrElse( key ->
                        {
                            try
                            {
                                Optional.ofNullable( cache.getMap( getMapName() ).getEntryView( key ) )
                                        .ifPresentOrElse( entryView ->
                                                {
                                                    String recordValue = String.valueOf( entryView.getValue() );

                                                    message.setDataPayload( recordValue );
                                                    message.getHeaders().set( getConfig().getHeaders().getSkipAllHeader(), "true" );

                                                    LocalDateTime expirationTime = ofInstant( ofEpochMilli( entryView.getExpirationTime() ), ZoneId.of( "Z" ) );

                                                    log.debug( "cache hit for key: {}, expiration time: {}, value: {} ", key, expirationTime.format( ISO_LOCAL_DATE_TIME ),
                                                            recordValue.replace( "\n", "" ) );
                                                },
                                                () -> log.debug( "cache miss for key: {}", key ) );

                            }
                            catch ( IllegalStateException | IllegalArgumentException exception )
                            {
                                log.error( "error getting record from cache - error: {}", exception.getMessage() );
                            }
                        },
                        () -> log.debug( "null cache key: nothing to get from cache" ) );


        return Mono.just( message );
    }
}
