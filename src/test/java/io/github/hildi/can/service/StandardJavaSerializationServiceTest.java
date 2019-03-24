package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.DeserializeException;
import io.github.hildi.can.exceptions.FileIsNotExistsException;
import io.github.hildi.can.exceptions.NotWritableFileException;
import io.github.hildi.can.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

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

    @Test
    void shouldThrowDeserializeException() {
        assertThrows(DeserializeException.class, () -> {
            service.deserialize(file);
        });
    }

    @Test
    void shouldThrowFileIsNotExistsExceptionIfFileIsNotExists() {
        File mock = mock(File.class);
        Mockito.when(mock.exists()).thenReturn(false);

        assertThrows(FileIsNotExistsException.class, () -> {
            service.serialize(user, mock);
        });
    }

    @Test
    void shouldThrowNotWritableFileExceptionIfAppIsAllowedToWriteToTheFile() {
        File mock = mock(File.class);
        Mockito.when(mock.exists()).thenReturn(true);
        Mockito.when(mock.canWrite()).thenReturn(false);

        assertThrows(NotWritableFileException.class, () -> {
            service.serialize(user, mock);
        });
    }
}
