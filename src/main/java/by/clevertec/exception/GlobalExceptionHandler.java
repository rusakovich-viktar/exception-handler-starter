package by.clevertec.exception;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import feign.FeignException;
import java.rmi.ConnectException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения валидации аргументов метода.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception) {
        String exceptionMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> "Error in field '" + fieldError.getField() + "': " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error", exceptionMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    /**
     * Обрабатывает исключения, возникающие при работе с Feign клиентом.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException exception) {
        HttpStatus httpStatus = HttpStatus.valueOf(exception.status());

        String errorMessage = exception.contentUTF8();
        String message = getStringFromJson(errorMessage);

        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message), httpStatus);
    }

    /**
     * Обрабатывает исключения некорректного использования аргументов.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException exception) {
        return getErrorResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, возникающие при отсутствии сущности.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(EntityNotFoundExceptionCustom.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFoundExceptionCustom exception) {
        return getErrorResponseEntity(exception, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключения, возникающие при обращении к null объекту.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleException(NullPointerException exception) {
        return getErrorResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, возникающие при проблемах с подключением.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponse> connectException(ConnectException exception) {
        return getErrorResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Обрабатывает все остальные исключения.
     *
     * @param exception исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unexpected error occurred: ", exception);
        return getErrorResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Создает объект ответа с информацией об ошибке.
     *
     * @param exception  исключение
     * @param httpStatus HTTP статус ошибки
     * @return объект ответа с информацией об ошибке
     */
    private static ResponseEntity<ErrorResponse> getErrorResponseEntity(Exception exception, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), exception.getMessage()), httpStatus);
    }

    /**
     * Извлекает сообщение об ошибке из JSON строки.
     *
     * @param errorMessage строка с JSON
     * @return сообщение об ошибке
     */
    private static String getStringFromJson(String errorMessage) {
        JsonElement jsonElement = JsonParser.parseString(errorMessage);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.get("message").getAsString();
    }

}
