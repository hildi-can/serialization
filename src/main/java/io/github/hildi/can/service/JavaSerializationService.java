package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.model.FullName;
import io.github.hildi.can.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.github.hildi.can.asserts.AssertFile.*;
import static io.github.hildi.can.asserts.AssertUser.assertParamNotNull;
import static io.github.hildi.can.asserts.AssertUser.assertUserNotNull;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class JavaSerializationService implements SerializationService{

    @Override
    public void serialize(User user, File file) {
        assertParamNotNull(user, file);
        assertFileExist(file);
        assertFileWritable(file);
        try (FileWriter writer = new FileWriter(file)) {
            String param = setUserParametersToFile(user);
            writer.write(param);
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize data. " + file.getName(), e);
        }
    }

    private String setUserParametersToFile(User user) {
        return "id = " + user.getId() + "\n" +
            "nickName = " + user.getNickName() + "\n" +
            "fullName = " + user.getFullName() + "\n" +
            "email = " + user.getEmail() + "\n" +
            "permissions = " + user.getPermissions() + "\n" +
            "attributes = " + user.getAttributes() + "\n" +
            "createdAt = " + user.getCreatedAt() + "\n";
    }

    @Override
    public User deserialize(File file) {
        assertFileNotNull(file);
        assertFileReadable(file);
        assertFileExtensionIsCorrect(file);
        return getAndAssignParametersFromFile(file);
    }

    User getAndAssignParametersFromFile(File file) {

        long userId = 0;
        String userNickName = null;
        String userFirstName = null;
        String userLastName = null;
        String userEmail = null;
        Collection<String> userPermissions = null;
        Map<String, String> userAttributes = null;
        Date userCreateDate = null;

        try (Scanner scanner = new Scanner(file)){
            do {
                String line = scanner.nextLine();
                String[] divideLine = dividedLineIntoTwoParts(line);
                String userParameter = getLeftSideOfLine(divideLine);

                userId = getUserId(userId, userParameter, divideLine);
                userNickName = getUserNickName(userNickName, userParameter, divideLine);
                userFirstName = getUserFirstName(userFirstName, userParameter, divideLine);
                userLastName = getUserLastName(userLastName, userParameter, divideLine);
                userEmail = getUserEmail(userEmail, userParameter, divideLine);
                userPermissions = getUserPermissions(userPermissions, userParameter, divideLine);
                userAttributes = getUserAttributes(userAttributes, userParameter, divideLine);
                userCreateDate = getUserCreatedData(userCreateDate, userParameter, divideLine);
            } while (scanner.hasNextLine());
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: file " + file.getName() +
                " is not found. ", e);
        }

        assertMainParametersIsAvailable(file, userId, userNickName);
        User user = new User(userId, userNickName);
        setAllParametersForNewUser(user, userFirstName, userLastName, userEmail,
            userPermissions, userAttributes, userCreateDate);
        assertUserNotNull(user);
        return user;
    }

    void assertMainParametersIsAvailable(File file, long userId, String userNickName) {
        if (userId == 0 || userNickName == null) {
            throw new IllegalArgumentException("Failed to deserialize data, reason: 'id' and 'nickName' " +
                "parameters don't found at file " + file.getName());
        }
    }

    private void setAllParametersForNewUser(User user, String userFirstName, String userLastName, String userEmail, Collection<String> userPermissions, Map<String, String> userAttributes, Date userCreateDate) {
        setFullName(user, userFirstName, userLastName);
        setUserEmail(user, userEmail);
        setUserPermissions(user, userPermissions);
        setUserAttributes(user, userAttributes);
        setUserCreatedData(user, userCreateDate);

    }

    private String[] dividedLineIntoTwoParts(String line) {
        return line.split(" = ");
    }

    private String getLeftSideOfLine(String[] lines) {
        return lines[0].toLowerCase().trim();
    }

    void setFullName(User user, String firstName, String lastName) {
        FullName fullName = new FullName(firstName, lastName);
        user.setFullName(fullName);
    }

    void setUserEmail(User user, String userEmail) {
        user.setEmail(userEmail);
    }

    void setUserPermissions(User user, Collection<String> userPermissions) {
        user.setPermissions(userPermissions);
    }

    void setUserAttributes(User user, Map<String, String> userAttributes) {
        user.setAttributes(userAttributes);
    }

    private void setUserCreatedData(User user, Date createdAt) {
        user.setCreatedAt(createdAt);
    }

    private long getUserId(long userId, String param, String[] line) {
        if (param.equals("id".toLowerCase().trim())) {
            userId = (long) Integer.parseInt(line[1]);
        }
        return userId;
    }

    private String getUserNickName(String userNickName, String param, String[] line) {
        if (param.equals("nickName".toLowerCase().trim())) {
            userNickName = line[1];
        }
        return userNickName;
    }

    private String getUserFirstName(String userFirstName, String param, String[] line) {
        if (param.equals("firstName".toLowerCase().trim())) {
            userFirstName = line[1].trim();
        }
        return userFirstName;
    }

    private String getUserLastName(String userLastName, String param, String[] line) {
        if (param.equals("lastName".toLowerCase().trim())) {
            userLastName = line[1].trim();
        }
        return userLastName;
    }

    private String getUserEmail(String userEmail, String param, String[] line) {
        if (param.equals("email".toLowerCase().trim())) {
            userEmail = line[1].toLowerCase().trim();
        }
        return userEmail;
    }

    private Collection<String> getUserPermissions(Collection<String> userPermissions, String param, String[] line) {
        if (param.equals("permissions".toLowerCase().trim())) {
            String[] permissions = line[1].trim().split(", ");
            userPermissions = new ArrayList<>(Arrays.asList(permissions));
        }
        return userPermissions;
    }

    private Map<String, String> getUserAttributes(Map<String, String> userAttributes, String param, String[] line) {
        if (param.equals("attributes".toLowerCase().trim())) {
            userAttributes = new LinkedHashMap<>();
            String[] attributes = line[1].trim().split("; ");
            String[] country = attributes[0].trim().split(": ");
            String[] city = attributes[1].trim().split(": ");
            userAttributes.put(country[0], country[1]);
            userAttributes.put(city[0], city[1]);
        }
        return userAttributes;
    }

    private Date getUserCreatedData(Date userCreateDate, String param, String[] line) {
        if (param.equals("createdAt".toLowerCase().trim())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(line[1], formatter);
            userCreateDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        return userCreateDate;
    }
}
