package com.baremetalcode.functionalreactive.services.externaldataservice.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.externaldataservice.ExternalDataServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpMethod.GET;

/**
 * External vendor request class for al posts
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class PostByIdExternalDataServiceStrategy extends ExternalDataServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostById();
    }

    @Override
    public Mono<PipelineMessage> get( final PipelineMessage message )
    {
        final String postId = message.getHeaders().getFirst( getConfig().getHeaders().getPostIdHeader() );
        final HttpMethod apiCallMethod = GET;
        final String apiCallPath = String.format( getConfig().getVendor().getPathPostById(), postId );

        message.setApiCallMethod( apiCallMethod );
        message.setApiCallPath( apiCallPath );

        log.debug( "loading record with apiCallMethod: {}, and apiCallPath: {}", apiCallMethod, apiCallPath );

        return getWebclient().requestData( message );
    }
}
