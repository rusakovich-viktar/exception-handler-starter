package by.clevertec.exception;

import static by.clevertec.util.Constant.ID;
import static by.clevertec.util.Constant.TEST_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.clevertec.util.TestEntity;
import org.junit.jupiter.api.Test;

public class EntityNotFoundExceptionCustomTest {


    @Test
    public void testOf() {
        String id = ID;
        EntityNotFoundExceptionCustom exception = EntityNotFoundExceptionCustom.of(TestEntity.class, id);

        assertEquals(TEST_ENTITY + " with id " + id + " does not exist", exception.getMessage());
    }
}