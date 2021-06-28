package com.baremetalcode.functionalreactive.domain.engine;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;


/**
 * Main engine message.
 * <p>
 * Thou technically not a message in the immutable sense, it is the record that will navigate the pipelines and carry the different transformations, payloads, statuses and
 * errors within itself.
 */
@Data
@Accessors( chain = true )
public class PipelineMessage
{
    private HttpHeaders headers;
    private HttpStatus httpStatus;
    private ServerHttpResponse serverHttpResponse;
    private HttpMethod apiCallMethod;
    private String apiCallPath;
    private String apiCallBody;
    private String dataPayload;
}
