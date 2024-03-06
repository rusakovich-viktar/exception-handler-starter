package by.clevertec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается, когда сущность не найдена.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundExceptionCustom extends RuntimeException {

    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке
     */
    public EntityNotFoundExceptionCustom(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением о том, что сущность с указанным идентификатором не найдена.
     *
     * @param clazz класс сущности
     * @param field идентификатор сущности
     * @return исключение EntityNotFoundExceptionCustom
     */
    public static EntityNotFoundExceptionCustom of(Class<?> clazz, Object field) {
        return new EntityNotFoundExceptionCustom(String.format("%s with id %s does not exist", clazz.getSimpleName(), field));
    }
}
