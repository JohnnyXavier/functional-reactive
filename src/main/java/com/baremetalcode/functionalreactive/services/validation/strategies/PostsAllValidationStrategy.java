package com.baremetalcode.functionalreactive.services.validation.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.validation.ValidationServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Validates the post all input message.
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class PostsAllValidationStrategy extends ValidationServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostsAll();
    }

    @Override
    public Mono<PipelineMessage> validate( final PipelineMessage message )
    {
        log.debug( "no validation required for strategy: {}", getStrategyId() );

        return Mono.just( message );
    }
}
