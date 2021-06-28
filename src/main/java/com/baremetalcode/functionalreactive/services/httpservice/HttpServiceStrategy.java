package com.baremetalcode.functionalreactive.services.httpservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.BaseStrategy;
import reactor.core.publisher.Mono;

/**
 * Http Service top abstract strategy to be extended / implemented by inheritors.
 *
 * @author Johnny Xavier
 */
public abstract class HttpServiceStrategy extends BaseStrategy
{
    /**
     * builds a a suitable http response by passing the body as string and filling in the status code of the server response appropriately
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<String> buildResponse( PipelineMessage message );
}
