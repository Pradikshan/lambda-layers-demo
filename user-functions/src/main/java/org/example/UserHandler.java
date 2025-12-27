package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class UserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        CustomLogger logger = new CustomLogger(context);
        long startTime = System.currentTimeMillis();
        logger.logStart(event);

        try {

            Map<String, String> pathParams = event.getPathParameters();
            if (pathParams == null || !pathParams.containsKey("id")) {
                logger.warn("Missing user ID in path parameters");
                return ResponseUtil.validationErrorResponse("User ID is required");
            }

            String userId = pathParams.get("id");

            logger.info("Processing request to retrieve user details");
            Map<String, Object> responseData = Map.of(
                    "id", userId,
                    "name", "John Doe",
                    "message", "User details retrieved successfully"
            );

            logger.info("User details retrieved successfully", responseData);
            return ResponseUtil.successResponse(responseData, 200);
        } catch (Exception e) {
            logger.error("Error processing request: ", e);
            return ResponseUtil.serverErrorResponse(e.getMessage());

        } finally {
            long endTime = System.currentTimeMillis() - startTime;
            logger.logEnd(endTime);
        }

    }

}
