package com.baremetalcode.functionalreactive.services.dataprocessingservice.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.dataprocessingservice.DataProcessingServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Data Processing Strategy for posts by id.
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class PostByIdDataProcessingStrategy extends DataProcessingServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostById();
    }

    @Override
    public Mono<PipelineMessage> processData( final PipelineMessage message )
    {
        log.debug( "processing message payload - compressing double white space and will transform the string to lowercase" );

        message.setDataPayload( message.getDataPayload().replace( "  ", "" ).toLowerCase() );

        return Mono.just( message );
    }
}
