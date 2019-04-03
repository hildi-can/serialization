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
    private File file;

    @BeforeEach
    void setUp() {
        service = new StandardJavaSerializationService();
        file = mock(File.class);
    }

    @Test
    void shouldThrowNotReadableFileExceptionWhenFileIsNotReadable() {
        Mockito.when(file.canRead()).thenReturn(false);

        assertThrows(NotReadableFileException.class, () -> {
            service.deserialize(file);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deserialize(null);
        });
    }
}
