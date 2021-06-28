package com.baremetalcode.functionalreactive.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Headers related configuration object
 *
 * @author Johnny Xavier
 */
@Configuration
@ConfigurationProperties( prefix = "app-config.headers" )
@Getter
@Setter
public class HeadersConfig
{
    private String strategyHeader;
    private String postIdHeader;
    private String cacheKeyHeader;
    private String skipAllHeader;
}
