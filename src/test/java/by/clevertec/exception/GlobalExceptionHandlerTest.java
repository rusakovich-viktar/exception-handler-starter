package by.clevertec.exception;

import static by.clevertec.util.Constant.CONNECTION_ERROR;
import static by.clevertec.util.Constant.ENTITY_NOT_FOUND;
import static by.clevertec.util.Constant.ILLEGAL_ARGUMENT;
import static by.clevertec.util.Constant.NOT_FOUND;
import static by.clevertec.util.Constant.NULL_POINTER;
import static by.clevertec.util.Constant.OBJECT;
import static by.clevertec.util.Constant.UNEXPECTED_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import feign.FeignException;
import java.rmi.ConnectException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    public static final String ERROR_IN_FIELD_FIELD_1_MESSAGE_1_ERROR_IN_FIELD_FIELD_2_MESSAGE_2 = "Error in field 'field1': message1, Error in field 'field2': message2";
    public static final String MESSAGE_1 = "message1";
    public static final String MESSAGE_2 = "message2";
    public static final String FIELD_1 = "field1";
    public static final String FIELD_2 = "field2";
    @Mock
    private Exception exception;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private FeignException feignException;
    @Mock
    private BindingResult bindingResult;

    @Mock
    private IllegalArgumentException illegalArgumentException;

    @Mock
    private EntityNotFoundExceptionCustom entityNotFoundExceptionCustom;

    @Mock
    private NullPointerException nullPointerException;

    @Mock
    private ConnectException connectException;

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    public void testHandleException_MethodArgumentNotValidException() {
        FieldError fieldError1 = new FieldError(OBJECT, FIELD_1, MESSAGE_1);
        FieldError fieldError2 = new FieldError(OBJECT, FIELD_2, MESSAGE_2);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        ResponseEntity<ErrorResponse> response = handler.handleException(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ERROR_IN_FIELD_FIELD_1_MESSAGE_1_ERROR_IN_FIELD_FIELD_2_MESSAGE_2, response.getBody().getMessage());
    }

    @Test
    public void testHandleException_IllegalArgumentException() {
        when(illegalArgumentException.getMessage()).thenReturn(ILLEGAL_ARGUMENT);

        ResponseEntity<ErrorResponse> response = handler.handleException(illegalArgumentException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ILLEGAL_ARGUMENT, response.getBody().getMessage());
    }

    @Test
    public void testHandleException_EntityNotFoundExceptionCustom() {
        when(entityNotFoundExceptionCustom.getMessage()).thenReturn(ENTITY_NOT_FOUND);

        ResponseEntity<ErrorResponse> response = handler.handleException(entityNotFoundExceptionCustom);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ENTITY_NOT_FOUND, response.getBody().getMessage());
    }

    @Test
    public void testHandleException_NullPointerException() {
        when(nullPointerException.getMessage()).thenReturn(NULL_POINTER);

        ResponseEntity<ErrorResponse> response = handler.handleException(nullPointerException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(NULL_POINTER, response.getBody().getMessage());
    }

    @Test
    public void testHandleException_ConnectException() {
        when(connectException.getMessage()).thenReturn(CONNECTION_ERROR);

        ResponseEntity<ErrorResponse> response = handler.connectException(connectException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(CONNECTION_ERROR, response.getBody().getMessage());
    }

    @Test
    public void testHandleFeignNotFoundException() {
        when(feignException.status()).thenReturn(404);
        when(feignException.contentUTF8()).thenReturn("{\"message\":\"Not Found\"}");

        ResponseEntity<ErrorResponse> response = handler.handleFeignException(feignException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(NOT_FOUND, response.getBody().getMessage());
    }

    @Test
    public void testHandleException_Exception() {
        when(exception.getMessage()).thenReturn(UNEXPECTED_ERROR);

        ResponseEntity<ErrorResponse> response = handler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(UNEXPECTED_ERROR, response.getBody().getMessage());
    }

}
