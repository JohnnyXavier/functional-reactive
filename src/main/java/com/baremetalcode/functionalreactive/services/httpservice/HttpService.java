package com.baremetalcode.functionalreactive.services.httpservice;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.common.TargetStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * http service
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class HttpService extends TargetStrategy<HttpServiceStrategy>
{
    /**
     * In charge of calling the appropriate strategy to transform the message into a suitable http response
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public Mono<String> buildResponse( final PipelineMessage message )
    {
        final String strategyId = getConfig().getStrategies().getDefaultStrategy();
        final BiFunction<HttpServiceStrategy, PipelineMessage, Mono<String>> action = HttpServiceStrategy::buildResponse;
        final Function<PipelineMessage, Mono<String>> fallbackAction = pipelineMessage ->
        {
            Optional.ofNullable( message.getServerHttpResponse() )
                    .ifPresent( serverHttpResponse -> serverHttpResponse.setStatusCode( INTERNAL_SERVER_ERROR ) );

            return Mono.just( "" );
        };

        return applyStrategy( message, message, strategyId, action, fallbackAction );
    }

    @Override
    protected Boolean skipStep( final PipelineMessage message )
    {
        // we don't want to ever skip this step as it is the one building the response to an http caller
        return false;
    }
}
