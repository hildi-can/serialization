package io.github.hildi.can.service;

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
class StandardJavaSerializeServiceTest {

    private StandardJavaSerializationService service;
    private User user;
    private File mock;

    @BeforeEach
    void setUp() {
        service = new StandardJavaSerializationService();
        mock = mock(File.class);
        user = new User(1L, "Lord");
    }

    @Test
    void serializedThanDeserializeInstanceShouldBeEquals() throws IOException {
        File tempFile = File.createTempFile("temp", "txt");
        User tempUser = new User(1L, "Lord");

        service.serialize(tempUser, tempFile);
        User deserialize = service.deserialize(tempFile);

        assertEquals(tempUser, deserialize);
    }

    @Test
    void shouldThrowFileIsNotExistsExceptionWhenFileDoesNotExist() {
        Mockito.when(mock.exists()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
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

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.serialize(null, mock);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.serialize(user, null);
        });
    }
}
