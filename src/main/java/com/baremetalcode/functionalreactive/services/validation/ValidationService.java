package com.baremetalcode.functionalreactive.services.validation;

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
public class ValidationService extends TargetStrategy<ValidationServiceStrategy>
{
    /**
     * In charge of calling the appropriate strategy to validate the input message
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public Mono<PipelineMessage> validate( final PipelineMessage message )
    {
        final String strategyId = message.getHeaders().getFirst( getConfig().getHeaders().getStrategyHeader() );
        final BiFunction<ValidationServiceStrategy, PipelineMessage, Mono<PipelineMessage>> action = ValidationServiceStrategy::validate;

        return applyStrategy( message, message, strategyId, action, Mono::just );
    }
}
