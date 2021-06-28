package com.baremetalcode.functionalreactive.services.externaldataservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.TargetStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * External data service.
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ExternalDataService extends TargetStrategy<ExternalDataServiceStrategy>
{
    /**
     * In charge of calling the appropriate strategy to get data from external sources
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public Mono<PipelineMessage> get( final PipelineMessage message )
    {

        final String strategyId = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );
        final BiFunction<ExternalDataServiceStrategy, PipelineMessage, Mono<PipelineMessage>> action = ExternalDataServiceStrategy::get;

        return applyStrategy( message, message, strategyId, action, Mono::just );
    }
}
