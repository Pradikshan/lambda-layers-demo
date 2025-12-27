package org.example;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static APIGatewayProxyResponseEvent successResponse(Object data, int statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withBody(convertToJson(data))
                .withStatusCode(statusCode);
    }

    public static APIGatewayProxyResponseEvent errorResponse(String message, int statusCode) {
        Map<String, Object> errorBody = Map.of(
                "error", message,
                "statusCode", statusCode
        );

        return new APIGatewayProxyResponseEvent()
                .withBody(convertToJson(errorBody))
                .withStatusCode(statusCode);
    }

    public static APIGatewayProxyResponseEvent validationErrorResponse(String message) {
        return errorResponse("Bad request: " + message, 400);
    }

    public static APIGatewayProxyResponseEvent serverErrorResponse(String message) {
        return errorResponse("Internal server error: " + message, 500);
    }

    private static String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Failed to serialize response\"}";
        }
    }
}
