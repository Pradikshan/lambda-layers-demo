package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.LogEntry;

import java.util.HashMap;
import java.util.Map;

public class CustomLogger {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String functionName;
    private final String requestId;
    private final LambdaLogger lambdaLogger;

    public CustomLogger(Context context) {
        this.functionName = context.getFunctionName();
        this.requestId = context.getAwsRequestId();
        this.lambdaLogger = context.getLogger();
    }

    public void info(String message) {
        log("INFO", message, new HashMap<>());
    }

    public void info(String message, Map<String, Object> metadata) {
        log("INFO", message, metadata);
    }

    public void error(String message, Map<String, Object> metadata) {
        log("ERROR", message, metadata);
    }

    public void warn(String message) {
        log("WARN", message, new HashMap<>());
    }

    public void warn(String message, Map<String, Object> metadata) {
        log("WARN", message, metadata);
    }

    public void error(String message, Throwable throwable) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("exception", throwable.getClass().getName());
        metadata.put("message", throwable.getMessage());
        error(message, metadata);
    }

    public void logStart(Object event) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("event", event);
        info("Lambda function execution started", metadata);
    }

    public void logEnd(long duration) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("duration", duration);
        info("Lambda function execution completed", metadata);
    }

    private void log(String level, String message, Map<String, Object> metadata) {
        LogEntry logEntry = LogEntry.builder()
                .functionName(functionName)
                .requestId(requestId)
                .level(level)
                .message(message)
                .metadata(metadata)
                .build();

        try {
            String jsonLog = objectMapper.writeValueAsString(logEntry);
            lambdaLogger.log(jsonLog);
        } catch (JsonProcessingException e) {
            lambdaLogger.log(String.format("[%s] %s: %s\n", level, functionName, message));
        }
    }
}
