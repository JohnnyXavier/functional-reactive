package com.baremetalcode.functionalreactive.services.common;


import com.baremetalcode.functionalreactive.configuration.AppConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PROTECTED;

/**
 * Base parent strategy from which all others derive.
 * <p>
 * Used on all strategies to self identify themselves on each context and adds the configuration ready to be used by all children strategies
 *
 * @author Johnny Xavier
 */
@Getter( PROTECTED )
public abstract class BaseStrategy
{
    @Autowired
    private AppConfig config;

    /**
     * Gets the strategy name.
     *
     * @return the string identifying a given strategy
     */
    public abstract String getStrategyId();
}
