package com.baremetalcode.functionalreactive.webclient;

import com.baremetalcode.functionalreactive.configuration.AppConfig;
import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Reactive webclient class targeted for a specific vendor
 * <p>
 * It is better to build more webclients tied to other vendors to maximize the individual configuration possibilities for each one
 */
@Service
@Log4j2
public class VendorWebclient
{
    private final WebClient webClient;

    /**
     * Constructor:
     * <p>
     * we build and lock the webclient to a specific vendor.
     *
     * @param appConfig global application configuration.
     */
    public VendorWebclient( AppConfig appConfig )
    {
        webClient = WebClient.builder()
                             .baseUrl( appConfig.getVendor().getBaseUrl() )
                             .build();
    }

    /**
     * Calls a vendor rest API to request the desired data.
     *
     * @param message input {@link PipelineMessage}
     *
     * @return processed message
     */
    public Mono<PipelineMessage> requestData( PipelineMessage message )
    {
        final HttpMethod apiCallMethod = message.getApiCallMethod();
        final String apiCallPath = message.getApiCallPath();
        final String apiCallBody = message.getApiCallBody();

        log.debug( "calling vendor api with httpMethod: {}, at path: {}, with body: {}", apiCallMethod, apiCallPath, apiCallBody );

        /*
         * reactive webclient pipeline:
         * ---
         * 1: we set the method to use on the call
         * 2: we set the path to use on the call
         * 3: we set headers to only accept json as response
         * 4: we set a body if there is one
         * 5: we call the retrieve method
         * 6: we convert the response into an entity of the String type
         * 7: we flatMap the response to extract the body and status into the pipeline message
         * 8: in case of error we set the proper error code
         *
         * result: we return the message with proper payload and httpStatus
         * */
        final WebClient.RequestBodySpec clientWithMethodAndPath = webClient
                .method( apiCallMethod )
                .uri( apiCallPath )
                .accept( APPLICATION_JSON );

        Optional.ofNullable( apiCallBody ).ifPresent( clientWithMethodAndPath::bodyValue );

        return clientWithMethodAndPath
                .retrieve()
                .toEntity( String.class )
                .flatMap( responseEntity ->
                {
                    final String body = Optional.ofNullable(responseEntity.getBody()).orElse("");
                    final HttpStatusCode httpStatusCode = responseEntity.getStatusCode();

                    log.debug( "received from vendor - status-code: {}", httpStatusCode.value() );
                    log.debug( "received from vendor - body: {}", body.replace( "\n", "" ) );

                    message.setHttpStatusCode( httpStatusCode );
                    message.setDataPayload( body );

                    return Mono.just( message );
                } )
                .onErrorResume( WebClientResponseException.class, responseException ->
                {
                    final String errorMessage = responseException.getMessage();
                    final HttpStatusCode httpStatusCode = responseException.getStatusCode();

                    log.error( "ERROR from vendor - error-code: {}", httpStatusCode.value() );
                    log.error( "ERROR from vendor - errorMessage: {}", errorMessage );

                    message.setHttpStatusCode( httpStatusCode );
                    message.setDataPayload( errorMessage );

                    return Mono.just( message );
                } );
    }
}
