package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.DeserializeException;
import io.github.hildi.can.exceptions.FileDoesNotExistsException;
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
    private File mock;

    @BeforeEach
    void setUp() throws IOException {
        service = new StandardJavaSerializationService();
        file = File.createTempFile("temp","txt");
        mock = mock(File.class);
        user = new User(1L, "Lord");
    }

    @Test
    void serializedThanDeserializeInstanceShouldBeEquals() {
        service.serialize(user, file);
        User deserialize = service.deserialize(file);

        User expected = user;

        assertEquals(expected, deserialize);
    }

    @Test
    void shouldThrowDeserializeExceptionWhenFileDoesNotExists() {
        assertThrows(DeserializeException.class, () -> {
            service.deserialize(file);
        });
    }

    @Test
    void shouldThrowFileIsNotExistsExceptionWhenFileDoesNotExists() {
        Mockito.when(mock.exists()).thenReturn(false);

        assertThrows(FileDoesNotExistsException.class, () -> {
            service.serialize(user, mock);
        });
    }

    @Test
    void shouldThrowNotWritableFileExceptionIfAppIsAllowedToWriteToTheFile() {
        Mockito.when(mock.exists()).thenReturn(true);
        Mockito.when(mock.canWrite()).thenReturn(false);

        assertThrows(NotWritableFileException.class, () -> {
            service.serialize(user, mock);
        });
    }
}
