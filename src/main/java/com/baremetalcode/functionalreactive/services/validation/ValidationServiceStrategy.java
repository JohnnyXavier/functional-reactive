package com.baremetalcode.functionalreactive.services.validation;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.BaseStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import reactor.core.publisher.Mono;

/**
 * Validation Service top abstract strategy to be extended / implemented by inheritors.
 *
 * @author Johnny Xavier
 */
@Getter( AccessLevel.PROTECTED )
public abstract class ValidationServiceStrategy extends BaseStrategy
{
    /**
     * Validates the input message to fail fast if something is missing or incorrect.
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<PipelineMessage> validate( PipelineMessage message );
}
