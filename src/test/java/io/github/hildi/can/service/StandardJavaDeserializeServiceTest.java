package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.NotReadableFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
class StandardJavaDeserializeServiceTest {

    private StandardJavaSerializationService service;
    private File mock;

    @BeforeEach
    void setUp() {
        service = new StandardJavaSerializationService();
        mock = mock(File.class);
    }

    @Test
    void shouldThrowNotReadableFileExceptionWhenFileIsNotReadable() {
        Mockito.when(mock.canRead()).thenReturn(false);

        assertThrows(NotReadableFileException.class, () -> {
            service.deserialize(mock);
        });
    }

    @Test
    void shouldThrowNotReadableFileExceptionWhenFileDoesNotExist() {
        Mockito.when(mock.exists()).thenReturn(false);

        assertThrows(NotReadableFileException.class, () -> {
            service.deserialize(mock);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deserialize(null);
        });
    }
}
