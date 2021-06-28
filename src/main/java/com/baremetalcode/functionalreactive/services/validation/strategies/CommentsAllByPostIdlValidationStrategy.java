package com.baremetalcode.functionalreactive.services.validation.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.validation.ValidationServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Validates the comments by post id input message.
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class CommentsAllByPostIdlValidationStrategy extends ValidationServiceStrategy
{

    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getCommentsByPostId();
    }

    @Override
    public Mono<PipelineMessage> validate( final PipelineMessage message )
    {
        String postId = message.getHeaders().getFirst( getConfig().getHeaders().getPostIdHeader() );

        return Optional.ofNullable( postId )
                       .map( ignoreAndReturn ->
                       {
                           log.debug( "message contains postId: {}", postId );
                           return Mono.just( message );
                       } )
                       .orElseGet( () ->
                       {
                           log.error( "missing postId on message, marking message with BAD_REQUEST and exiting pipeline immediately" );
                           message.getServerHttpResponse().setStatusCode( HttpStatus.BAD_REQUEST );
                           return Mono.error( new RuntimeException( "Missing post id" ) );
                       } );
    }
}
