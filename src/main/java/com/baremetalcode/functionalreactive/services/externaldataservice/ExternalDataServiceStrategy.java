package com.baremetalcode.functionalreactive.services.externaldataservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.BaseStrategy;
import com.baremetalcode.functionalreactive.webclient.VendorWebclient;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * External Data service top abstract strategy to be extended / implemented by inheritors.
 *
 * @author Johnny Xavier
 */
@Getter( AccessLevel.PROTECTED )
public abstract class ExternalDataServiceStrategy extends BaseStrategy
{
    @Autowired
    private VendorWebclient webclient;

    /**
     * Gets data from an external source and update the message with the proper payload and status
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public abstract Mono<PipelineMessage> get( PipelineMessage message );
}
