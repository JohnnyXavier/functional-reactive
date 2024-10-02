package com.baremetalcode.functionalreactive.engine;

import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import com.baremetalcode.functionalreactive.services.cachekeyservice.CacheKeyService;
import com.baremetalcode.functionalreactive.services.cacheservice.CacheService;
import com.baremetalcode.functionalreactive.services.dataprocessingservice.DataProcessingService;
import com.baremetalcode.functionalreactive.services.externaldataservice.ExternalDataService;
import com.baremetalcode.functionalreactive.services.httpservice.HttpService;
import com.baremetalcode.functionalreactive.services.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;


/**
 * This is the core class of the application.
 * <p>
 * It performs every step required to process a given message to it's final transformation
 *
 * @author Johnny Xavier
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class PipelineEngine
{
    private final ValidationService validationService;
    private final ExternalDataService externalDataService;
    private final DataProcessingService dataProcessingService;
    private final HttpService httpService;
    private final CacheService cacheService;
    private final CacheKeyService cacheKeyService;

    /**
     * Entry to the pipeline via a http request
     * <p>
     * It passed the message to the main pipeline and transforms it to the proper payload at the end.
     *
     * @param rwHttpHeaders      read/write headers from a webservice
     * @param serverHttpResponse response object from the originating caller
     *
     * @return the string to be returned to caller and the server response loaded with the proper fields (http-status)
     */
    public Mono<String> processMessage( HttpHeaders rwHttpHeaders, ServerHttpResponse serverHttpResponse )
    {
        log.debug( "message entering -http- processing pipeline" );

        final PipelineMessage message = new PipelineMessage().setHeaders( rwHttpHeaders );
        message.setServerHttpResponse( serverHttpResponse );

        return processMessage( message )
                .flatMap( httpService::buildResponse );
    }

    /**
     * Main pipeline of actions.
     * <p>
     * This is the core of the application.<br>
     * One after the other the strategies are executed in order further processing the input message as the progress.
     * <p>
     * strategies can be synchronous or asynchronous (fire and forget)
     * <p>
     * We can comment one step in the pipeline a the whole functionality is gone or we can also mark the message to skip the rest of the pipeline steps or a specific step
     *
     * @param message the {@link PipelineMessage} to process.
     *
     * @return the processed message to return to the caller of the main pipeline.
     */
    public Mono<PipelineMessage> processMessage( PipelineMessage message )
    {
        log.debug( "message entering -main- processing pipeline" );

        /*
         * pipeline steps:
         * ---
         * 1: we validate the input from the web service
         * 2: we generate a cache key for the given request
         * 3: we try to find the requested data internally in our cache and, if found, we mark the record to skip the rest of the pipeline
         * 4: we call the external data service if data was not found internally
         * 5: we process the external response as required, this could be json or xml transformations, trimmings, splittings, etc
         * 6: we put the processed data into the cache asynchronously (fire & forget)
         * 7: in case one of the steps throws an error, we gracefully handle it and move on, we do not crash the pipeline
         *
         * result: we return the message properly processed with payload and httpStatus, and in case the data was new or refreshed, we put it in the cache
         * */
        return Mono.just( message )
                   .filter( message1 -> true )
                   /*1*/.flatMap( validationService::validate )
                   /*2*/.flatMap( cacheKeyService::generateKey )
                   /*3*/.flatMap( cacheService::get )
                   /*4*/.flatMap( externalDataService::get )
                   /*5*/.flatMap( dataProcessingService::processData )
                   /*6*/.flatMap( cacheService::put )
                   /*7*/.onErrorResume( error ->
                {
                    log.error( "error on pipeline: {}", error.getMessage() );

                    Optional.ofNullable( message.getHttpStatusCode() )
                            .ifPresentOrElse( ignoreIfNotNull -> Function.identity(), () -> message.setHttpStatusCode( HttpStatus.INTERNAL_SERVER_ERROR ) );

                    return Mono.just( message );
                } );
    }
}
