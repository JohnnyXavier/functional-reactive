package com.baremetalcode.functionalreactive.services.cachekeyservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.TargetStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * Cache key service.
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CacheKeyService extends TargetStrategy<CacheKeyServiceStrategy>
{
    /**
     * In charge of calling the appropriate strategy to generate a cache key
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public Mono<PipelineMessage> generateKey( final PipelineMessage message )
    {
        final String strategyId = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );
        final BiFunction<CacheKeyServiceStrategy, PipelineMessage, Mono<PipelineMessage>> action = CacheKeyServiceStrategy::generateKey;

        return applyStrategy( message, message, strategyId, action, Mono::just );
    }
}
