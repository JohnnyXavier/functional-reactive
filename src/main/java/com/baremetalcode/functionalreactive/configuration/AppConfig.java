package com.baremetalcode.functionalreactive.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * Global application configuration object
 *
 * @author Johnny Xavier
 */
@Configuration
@RequiredArgsConstructor
@Getter
public class AppConfig
{
    private final VendorConfig vendor;
    private final StrategiesConfig strategies;
    private final HeadersConfig headers;
    private final CacheConfig cache;
}
