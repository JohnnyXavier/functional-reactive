package com.baremetalcode.functionalreactive.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Cache related configuration object
 *
 * @author Johnny Xavier
 */
@Configuration
@ConfigurationProperties( prefix = "app-config.cache" )
@Getter
@Setter
public class CacheConfig
{
    private String postsAll;
    private String postsById;
    private String commentsByPostsById;
    private String postsAllTtl;
    private String postsByIdTtl;
    private String commentsByPostsByIdTtl;
}
