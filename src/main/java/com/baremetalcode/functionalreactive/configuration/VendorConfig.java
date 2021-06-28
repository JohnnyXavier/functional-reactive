package com.baremetalcode.functionalreactive.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Vendor related configuration object
 *
 * @author Johnny Xavier
 */
@Configuration
@ConfigurationProperties( prefix = "app-config.external-vendor" )
@Getter
@Setter
public class VendorConfig
{
    private String baseUrl;
    private String pathPostsAll;
    private String pathPostById;
    private String pathCommentsByPostId;
}
