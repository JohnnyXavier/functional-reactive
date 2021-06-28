package com.baremetalcode.functionalreactive.services.dataprocessingservice.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.dataprocessingservice.DataProcessingServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Data Processing Strategy for comments by posts by id.
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class CommentsByPostByIdDataProcessingStrategy extends DataProcessingServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getCommentsByPostId();
    }

    @Override
    public Mono<PipelineMessage> processData( final PipelineMessage message )
    {
        final String target = "b";
        final String replacement = "-B-";
        log.debug( "processing message payload - compressing double white space and will replace every '{}' by \"{}\"", target, replacement );

        message.setDataPayload( message.getDataPayload().replace( "  ", "" ).replace( target, replacement ) );

        return Mono.just( message );
    }
}
