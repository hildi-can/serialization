package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.NotWritableFileException;
import io.github.hildi.can.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Serhii Hildi on 15.03.19.
 */
class StandardJavaSerializeServiceTest {

    private StandardJavaSerializationService service;
    private User user;
    private File file;

    @BeforeEach
    void setUp() {
        service = new StandardJavaSerializationService();
        file = mock(File.class);
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
    void shouldThrowIllegalArgumentExceptionWhenFileDoesNotExist() {
        when(file.exists()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            service.serialize(user, file);
        });
    }

    @Test
    void shouldThrowNotWritableFileExceptionIfAppIsAllowedToWriteToTheFile() {
        when(file.exists()).thenReturn(true);
        when(file.canWrite()).thenReturn(false);

        assertThrows(NotWritableFileException.class, () -> {
            service.serialize(user, file);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.serialize(null, file);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.serialize(user, null);
        });
    }
}
