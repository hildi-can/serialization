package io.github.hildi.can.service;

import io.github.hildi.can.asserts.AssertUser;
import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.exceptions.NotReadableFileException;
import io.github.hildi.can.model.FullName;
import io.github.hildi.can.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Serhii Hildi on 04.04.19.
 */
class JavaDeserializeServiceTest {

    private JavaSerializationService service;
    private User user;
    private File file;
    private File mockFile;

    @BeforeEach
    void setUp() throws IOException {
        service = new JavaSerializationService();
        user = new User(1L, "Valera");
        mockFile = mock(File.class);

        List<String> lines = Collections.singletonList("id = 1\n" +
            "nickName = solo-yolo\n" +
            "email = sergei.sologub@gmail.com\n" +
            "firstName = Serhii\n" +
            "lastName = Solohub\n" +
            "permissions = admin, user\n" +
            "attributes = country: ukraine; city: kharkiv\n" +
            "createdAt = 2011-12-03T10:15:30");
        Path realFile = Paths.get("user.properties");
        Files.write(realFile, lines, Charset.forName("UTF-8"));
        file = new File(String.valueOf(realFile));
    }

    @Test
    void shouldReturnEqualsWhenFullNameParameterFromFileIsSuccessfullyAssigned() {
        String firstName = "Serhii";
        String lastName = "Solohub";
        service.setFullName(user, firstName, lastName);
        FullName result = user.getFullName();

        FullName expected = new FullName("Serhii", "Solohub");

        assertEquals(expected, result);
    }

    @Test
    void shouldReturnEqualsWhenEmailParameterFromFileIsSuccessfullyAssigned() {
        service.setUserEmail(user,"sergei.sologub@gmail.com");
        String result = user.getEmail();
        String expected = "sergei.sologub@gmail.com";

        assertEquals(expected, result);
    }


    @Test
    void shouldReturnEqualsWhenPermissionsFromFileIsSuccessfullyAssigned() {
        ArrayList<String> list = new ArrayList<>();
        list.add("admin");
        list.add("user");

        service.setUserPermissions(user, list);

        Collection<String> result = user.getPermissions();

        ArrayList<String> exp = new ArrayList<>();
        exp.add("admin");
        exp.add("user");

        assertEquals(exp, result);
    }

    @Test
    void shouldReturnEqualsWhenAttributesFromFileIsSuccessfullyAssigned() {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("country", "ukraine");
        attributes.put("city", "kharkiv");

        service.setUserAttributes(user, attributes);

        Map<String, String> res = user.getAttributes();

        Map<String, String> exp = new LinkedHashMap<>();
        exp.put("country", "ukraine");
        exp.put("city", "kharkiv");

        assertEquals(exp, res);
    }

    @Test
    void shouldAssignCorrectIdParameterFromFile() {
        Long res = service.getAndAssignParametersFromFile(file).getId();
        assertEquals(1, res);
    }

    @Test
    void shouldAssignCorrectCreatedDateParameterFromFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2011-12-03T10:15:30", formatter);

        Date exp = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Date res = service.getAndAssignParametersFromFile(file).getCreatedAt();

        assertEquals(exp, res);
    }

    @Test
    void shouldAssignCorrectNickNameParameterFromFile() {
        String result = service.getAndAssignParametersFromFile(file).getNickName();
        assertEquals("solo-yolo", result);
    }

    @Test
    void shouldAssignCorrectFullNameParameterFromFile() {
        FullName result = service.getAndAssignParametersFromFile(file).getFullName();
        FullName fullName = new FullName("Serhii", "Solohub");
        assertEquals(fullName, result);
    }

    @Test
    void shouldAssignCorrectEmailParameterFromFile() {
        String result = service.getAndAssignParametersFromFile(file).getEmail();
        assertEquals("sergei.sologub@gmail.com", result);
    }

    @Test
    void shouldAssignCorrectPermissionsParameterFromFile() {
        ArrayList<String> permissionsList = new ArrayList<>();
        permissionsList.add("admin");
        permissionsList.add("user");

        Collection<String> result = service.getAndAssignParametersFromFile(file).getPermissions();

        assertEquals(permissionsList, result);
    }

    @Test
    void shouldAssignCorrectAttributesFromFile() {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("country", "ukraine");
        attributes.put("city", "kharkiv");

        Map<String, String> result = service.getAndAssignParametersFromFile(file).getAttributes();

        assertEquals(attributes, result);
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenFileExtensionIsNotCorrect() {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.getName()).thenReturn("user.property");

        assertThrows(InvalidFileException.class, () -> {
            service.deserialize(mockFile);
        });
    }

    @Test
    void shouldThrowNotReadableFileExceptionWhenFileIsNotReadable() {
        when(mockFile.canRead()).thenReturn(false);

        assertThrows(NotReadableFileException.class, () -> {
            service.deserialize(mockFile);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdParameterNotFoundAtFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.assertMainParametersIsAvailable(mockFile, 0, "sdas");
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNickNameParameterNotFoundAtFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.assertMainParametersIsAvailable(mockFile, 1L, null);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdAndNickNameParameterNotFoundAtFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.assertMainParametersIsAvailable(mockFile, 0, null);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUser.assertParamNotNull(null, mockFile);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deserialize(null);
        });
    }

    @AfterEach
    void tearDown() {
        file.delete();
        file = null;
        service = null;
        user = null;
    }
}
