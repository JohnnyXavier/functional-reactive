package com.baremetalcode.functionalreactive.services.dataprocessingservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.TargetStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * Data Processing service
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class DataProcessingService extends TargetStrategy<DataProcessingServiceStrategy>
{
    /**
     * In charge of calling the appropriate strategy to process data
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public Mono<PipelineMessage> processData( final PipelineMessage message )
    {
        final String strategyId = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );
        final BiFunction<DataProcessingServiceStrategy, PipelineMessage, Mono<PipelineMessage>> action = DataProcessingServiceStrategy::processData;

        return applyStrategy( message, message, strategyId, action, Mono::just );
    }
}
