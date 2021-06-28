package com.baremetalcode.functionalreactive.services.cacheservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.TargetStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * Validation service
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CacheService extends TargetStrategy<CacheServiceStrategy>
{

    public Mono<PipelineMessage> put( final PipelineMessage message )
    {
        final String strategyId = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );
        final BiFunction<CacheServiceStrategy, PipelineMessage, Mono<PipelineMessage>> action = CacheServiceStrategy::put;

        return applyStrategy( message, message, strategyId, action, Mono::just );
    }


    public Mono<PipelineMessage> get( final PipelineMessage message )
    {
        final String strategyId = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );
        final BiFunction<CacheServiceStrategy, PipelineMessage, Mono<PipelineMessage>> action = CacheServiceStrategy::get;

        return applyStrategy( message, message, strategyId, action, Mono::just );
    }
}
