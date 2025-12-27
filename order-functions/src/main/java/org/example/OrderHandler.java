package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class OrderHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        CustomLogger logger = new CustomLogger(context);
        long startTime = System.currentTimeMillis();

        try {
            logger.logStart(event);
            Map<String, String> pathParams = event.getPathParameters();
            if (pathParams == null || !pathParams.containsKey("id")) {
                logger.warn("Missing order ID in path parameters");
                return ResponseUtil.validationErrorResponse("Order ID is required");
            }

            String orderId = pathParams.get("id");

            logger.info("Processing request to retrieve order details");
            Map<String, Object> responseData = Map.of(
                    "id", orderId,
                    "orderNumber", "#111",
                    "item", "AWS Lambda",
                    "message", "Order details retrieved successfully"
            );

            logger.info("Order details retrieved successfully", responseData);
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
