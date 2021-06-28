package com.baremetalcode.functionalreactive.services.common;

import com.baremetalcode.functionalreactive.configuration.AppConfig;
import com.baremetalcode.functionalreactive.domain.engine.PipelineMessage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PROTECTED;

/**
 * Abstract strategy to process a {@link PipelineMessage} by calling the corresponding strategy
 *
 * @param <T> the strategy family a given service is meant to process. ie: ValidationStrategies, or ResponseStrategies
 *
 * @author Johnny Xavier
 */
@Log4j2
public abstract class TargetStrategy<T extends BaseStrategy>
{
    @Autowired
    private List<T> strategiesList;

    @Getter( PROTECTED )
    private Map<String, T> strategiesMap;

    @Autowired
    @Getter( PROTECTED )
    private AppConfig config;

    @PostConstruct
    private void populateStrategiesMap()
    {
        strategiesMap = strategiesList.stream()
                                      .collect( toMap( BaseStrategy::getStrategyId, Function.identity() ) );
    }

    /**
     * @param message        the input {@link PipelineMessage} message
     * @param inputToProcess the given payload to process, commonly a {@link PipelineMessage}, but could be anything
     * @param strategyId     the strategy name that identifies what we want to execute
     * @param action         the action we want to execute on the inputToProcess
     * @param fallbackAction the fallback action we want to execute on the inputToProcess in case something goes wrong
     * @param <U>            type of the input to process, should usually be a {@link PipelineMessage}, but could be any type
     * @param <R>            return type, should also usually be a {@link PipelineMessage}, but can be anything (ie: httpService returns a Mono<String>)
     *
     * @return the processed input
     */
    protected <U, R> Mono<R> applyStrategy( final PipelineMessage message, final U inputToProcess, final String strategyId, final BiFunction<T, U, Mono<R>> action,
                                            final Function<U, Mono<R>> fallbackAction )
    {
        return Mono.just( skipStep( message ) )
                   .filter( shouldWeSkipStep -> !shouldWeSkipStep )
                   .flatMap( ignoreBooleanIfWeDontSkipStep ->
                           Optional.ofNullable( strategiesMap.get( strategyId ) )
                                   .map( strategy ->
                                   {
                                       log.debug( "strategy executed: {}", strategy.getClass().getSimpleName() );
                                       return action.apply( strategy, inputToProcess );
                                   } )
                                   .orElseGet( () ->
                                   {
                                       log.error( "no {} strategy registered for strategyId: {}", this.getClass().getSimpleName(), strategyId );
                                       return fallbackAction.apply( inputToProcess );
                                   } ) )
                   .switchIfEmpty( fallbackAction.apply( inputToProcess ) );
    }

    /**
     * allows for strategies to override this method to mark the conditions in which they can ignored executing
     *
     * @param message the input {@link PipelineMessage}
     *
     * @return true or false if the strategy is to be ignored
     */
    protected Boolean skipStep( PipelineMessage message )
    {
        String skipAll = message.getHeaders().getFirst( getConfig().getHeaders().getSkipAllHeader() );

        return Boolean.parseBoolean( skipAll );
    }
}
