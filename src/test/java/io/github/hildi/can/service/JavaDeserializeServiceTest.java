package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.exceptions.NotReadableFileException;
import io.github.hildi.can.model.FullName;
import io.github.hildi.can.model.User;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
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
    private File file;
    private User user;

    @BeforeEach
    void setUp() {
        service = new JavaSerializationService();
        user = new User(1L, "Valera");
        file = mock(File.class);
    }

    @Test
    void ShouldReturnEqualsWhenParameterFullNameFromFileIsSuccessfullyAssigned() {
        String firstName = "Serhii";
        String lastName = "Solohub";
        service.setFullName(user, firstName, lastName);
        FullName result = user.getFullName();

        FullName expected = new FullName("Serhii", "Solohub");

        assertEquals(expected, result);
    }

    @Test
    void ShouldReturnEqualsWhenParameterEmailFileIsSuccessfullyAssigned() {
        service.setUserEmail(user,"sergei.sologub@gmail.com");
        String result = user.getEmail();
        String expected = "sergei.sologub@gmail.com";

        assertEquals(expected, result);
    }

    @Test
    void ShouldReturnEqualsWhenPermissionListFromFileIsSuccessfullyAssigned() {
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
    void ShouldReturnEqualsWhenAttributesFromFileIsSuccessfullyAssigned() {
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
        String lineFromFile = "id = 1";
        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        service.getUserId(param, partsOfLine);
        long result = service.userId;

        assertEquals(1, result);
    }

    @Test
    void shouldAssignCorrectNickNameParameterFromFile() {
        String lineFromFile = "nickName = solo.yang";
        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        service.getUserNickName(param, partsOfLine);
        String result = service.userNickName;

        assertEquals("solo.yang", result);
    }

    @Test
    void shouldAssignCorrectFirstNameParameterFromFile() {
        String lineFromFile = "userFirstName = Serhii";

        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        service.getUserFirstName(param, partsOfLine);
        String result = service.userFirstName;

        assertEquals("Serhii", result);
    }

    @Test
    void shouldAssignCorrectLastNameParameterFromFile() {
        String lineFromFile = "userLastName = Solohub";

        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        service.getUserLastName(param, partsOfLine);
        String result = service.userLastName;

        assertEquals("Solohub", result);
    }

    @Test
    void shouldAssignCorrectEmailParameterFromFile() {
        String lineFromFile = "email = sergei@gmail.com";

        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        service.getUserEmail(param, partsOfLine);
        String result = service.userEmail;

        assertEquals("sergei@gmail.com", result);
    }

    @Test
    void shouldAssignCorrectPermissionsParameterFromFile() {
        String lineFromFile = "permissions = admin, user";
        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        ArrayList<String> list = new ArrayList<>(2);
        list.add("admin");
        list.add("user");

        service.getUserPermissions(param, partsOfLine);
        Collection<String> result = service.userPermissions;

        assertEquals(list, result);
    }

    @Test
    void shouldAssignCorrectAttributesFromFile() {
        String lineFromFile = "attributes = country: ukraine; city: kharkiv";
        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("country", "ukraine");
        attributes.put("city", "kharkiv");

        service.getUserAttributes(param, partsOfLine);
        Map<String, String> result = service.userAttributes;

        assertEquals(attributes, result);
    }

    @Ignore
    void shouldAssignCorrectCreateDateFromFile() {
        String lineFromFile = "createdAt = 2011-12-03T10:15:30";
        String[] partsOfLine = lineFromFile.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        // TODO
        service.getUserCreatedData(param, partsOfLine);
//        assertEquals(new Date(), ...);
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenFileExtensionIsNotCorrect() {
        when(file.exists()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.getName()).thenReturn("user.property");

        assertThrows(InvalidFileException.class, () -> {
            service.deserialize(file);
        });
    }

    @Test
    void shouldThrowNotReadableFileExceptionWhenFileIsNotReadable() {
        when(file.canRead()).thenReturn(false);

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
