package com.baremetalcode.functionalreactive.services.dataprocessingservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.BaseStrategy;
import reactor.core.publisher.Mono;

/**
 * Data Processing service top abstract strategy to be extended / implemented by inheritors.
 *
 * @author Johnny Xavier
 */
public abstract class DataProcessingServiceStrategy extends BaseStrategy
{
    /**
     * Processes the message in any way required.
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<PipelineMessage> processData( PipelineMessage message );
}
