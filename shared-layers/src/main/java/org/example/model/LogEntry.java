package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LogEntry {
    @JsonProperty("functionName")
    private String functionName;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("level")
    private String level;

    @JsonProperty("message")
    private String message;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;
}
