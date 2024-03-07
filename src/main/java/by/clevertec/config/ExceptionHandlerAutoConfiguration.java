package by.clevertec.config;

import by.clevertec.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для обработчика исключений.
 */
@Configuration
@ConditionalOnProperty(prefix = "exception.handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExceptionHandlerAutoConfiguration {

    /**
     * Создает экземпляр глобального обработчика исключений.
     *
     * @return экземпляр GlobalExceptionHandler
     */
    @Bean
    public GlobalExceptionHandler controllerAdvice() {
        return new GlobalExceptionHandler();
    }

}
