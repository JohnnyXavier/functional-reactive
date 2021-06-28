package com.baremetalcode.functionalreactive.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Strategies related configuration object
 *
 * @author Johnny Xavier
 */
@Configuration
@ConfigurationProperties( prefix = "app-config.strategies" )
@Getter
@Setter
public class StrategiesConfig
{
    private String postsAll;
    private String postById;
    private String commentsByPostId;
    private String defaultStrategy;
}
