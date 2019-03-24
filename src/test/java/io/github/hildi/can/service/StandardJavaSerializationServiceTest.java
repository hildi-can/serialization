package io.github.hildi.can.service;

import io.github.hildi.can.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
class StandardJavaSerializationServiceTest {

    private File file;
    private StandardJavaSerializationService service;
    private User user;

    @BeforeEach
    void setUp() throws IOException {
        service = new StandardJavaSerializationService();
        file = File.createTempFile("temp","txt");
        user = new User(1L, "Lord");
    }

    @Test
    void shouldReturnTrueIfSerializeDataEqualsDeserializeData() {
        service.serialize(user, file);
        User deserialize = service.deserialize(file);

        User expected = user;

        assertEquals(expected, deserialize);
    }

//    @Test
//    void shouldThrowCannotReadFileException() {
//        assertThrows(DeserializeException.class, () -> {
//            service.deserialize(file);
//        });
//    }
}
