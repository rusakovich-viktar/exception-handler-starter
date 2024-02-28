package by.clevertec.exception;

import static by.clevertec.util.Constant.NOT_FOUND;
import static by.clevertec.util.Constant.RESOURCE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ErrorResponseTest {


    @Test
    public void testErrorResponse() {
        int status = 404;
        String error = NOT_FOUND;
        String message = RESOURCE_NOT_FOUND;

        ErrorResponse errorResponse = new ErrorResponse(status, error, message);

        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());

    }
}