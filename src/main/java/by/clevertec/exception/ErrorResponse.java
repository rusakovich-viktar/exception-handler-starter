package by.clevertec.exception;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Класс для представления информации об ошибке.
 */
@Getter
public class ErrorResponse {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;

    /**
     * Конструктор для создания объекта ErrorResponse.
     *
     * @param status  HTTP статус ошибки
     * @param error   описание ошибки
     * @param message сообщение об ошибке
     */
    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
    }

}
