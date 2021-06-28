package com.baremetalcode.functionalreactive.webservice;

import com.baremetalcode.functionalreactive.configuration.AppConfig;
import com.baremetalcode.functionalreactive.engine.PipelineEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * This class represents the user facing webservice in charge of the -POSTS- requests.
 *
 * @author Johnny Xavier
 */
@RestController
@RequestMapping( produces = "application/json" )
@RequiredArgsConstructor
@Log4j2
public class PostsController
{
    private final PipelineEngine engine;
    private final AppConfig config;

    @GetMapping( path = "/posts" )
    public Mono<String> getAllPosts( final ServerHttpResponse serverHttpResponse )
    {
        log.debug( "received request for all posts" );

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set( config.getHeaders().getStrategyHeader(), config.getStrategies().getPostsAll() );

        return engine.processMessage( httpHeaders, serverHttpResponse );
    }

    @GetMapping( path = "/posts/{postId}" )
    public Mono<String> getPostById( final ServerHttpResponse serverHttpResponse, @PathVariable( "postId" ) final String postId )
    {
        log.debug( "received request for postId: {}", postId );

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set( config.getHeaders().getStrategyHeader(), config.getStrategies().getPostById() );
        httpHeaders.set( config.getHeaders().getPostIdHeader(), postId );

        return engine.processMessage( httpHeaders, serverHttpResponse );
    }
}
