package com.baremetalcode.functionalreactive.services.externaldataservice.strategies;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.externaldataservice.ExternalDataServiceStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpMethod.GET;

/**
 * External vendor request class for all posts
 *
 * @author Johnny Xavier
 */
@Service
@Log4j2
public class PostsAllExternalDataServiceStrategy extends ExternalDataServiceStrategy
{
    @Override
    public String getStrategyId()
    {
        return getConfig().getStrategies().getPostsAll();
    }

    @Override
    public Mono<PipelineMessage> get( final PipelineMessage message )
    {
        final HttpMethod apiCallMethod = GET;
        final String apiCallPath = getConfig().getVendor().getPathPostsAll();

        message.setApiCallMethod( apiCallMethod );
        message.setApiCallPath( apiCallPath );

        log.debug( "loading record with apiCallMethod: {}, and apiCallPath: {}", apiCallMethod, apiCallPath );

        return getWebclient().requestData( message );
    }
}
