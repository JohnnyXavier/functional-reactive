package com.baremetalcode.functionalreactive.services.httpservice.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.httpservice.HttpServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Http strategy that returns a {@link String} to respond to an http caller (or any other that has an interest in receiving a {@link String})
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class DefaultHttpServiceStrategy extends HttpServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getDefaultStrategy();
    }

    @Override
    public Mono<String> buildResponse( final PipelineMessage message )
    {
        Optional.ofNullable( message.getServerHttpResponse() )
                .ifPresent( serverHttpResponse -> serverHttpResponse.setStatusCode( message.getHttpStatus() ) );

        return Optional.ofNullable( message.getDataPayload() )
                       .map( body ->
                       {
                           log.debug( "payload found on message, returning body: {}", body.replace( "\n", "" ) );
                           return Mono.just( body );
                       } )
                       .orElseGet( () ->
                       {
                           log.debug( "NULL payload found on message, returning empty body" );
                           return Mono.just( "" );
                       } );
    }
}
