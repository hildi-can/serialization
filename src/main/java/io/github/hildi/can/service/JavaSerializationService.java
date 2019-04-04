package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.model.FullName;
import io.github.hildi.can.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.github.hildi.can.asserts.AssertFile.*;
import static io.github.hildi.can.asserts.AssertUser.assertParamNotNull;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class JavaSerializationService implements SerializationService{

    long userId;
    String userNickName;
    String userFirstName;
    String userLastName;
    String userEmail;
    Collection<String> userPermissions;
    Map<String, String> userAttributes;
    Date createdAt;

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

    @Override
    public User deserialize(File file) {
        assertFileNotNull(file);
        assertFileReadable(file);
        assertFileExtensionIsCorrect(file);

        User user = null;
        try (Scanner scanner = new Scanner(file)){
            do {
                String line = scanner.nextLine();
                getAndAssignParametersFromFile(line);
            } while (scanner.hasNextLine());

            if (userId != 0 && userNickName != null) {
                user = new User(userId, userNickName);
                setUserParameters(user, userEmail, userPermissions, userAttributes, createdAt);
            }
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: file " + file.getName() +
                " is not found. ", e);
        }
        assertParamNotNull(user, file);
        return user;
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

    private void setUserParameters(User user, String userEmail,
                                   Collection<String> userPermissions,
                                   Map<String, String> userAttributes, Date createdAt) {
        setFullName(user, userFirstName, userLastName);
        setUserEmail(user, userEmail);
        setUserPermissions(user, userPermissions);
        setUserAttributes(user, userAttributes);
        setUserCreatedData(user, createdAt);
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

    private void getAndAssignParametersFromFile(String line) {
        String[] partsOfLine = line.split(" = ");
        String param = partsOfLine[0].toLowerCase().trim();

        getUserId(param, partsOfLine);
        getUserNickName(param, partsOfLine);
        getUserFirstName(param, partsOfLine);
        getUserLastName(param, partsOfLine);
        getUserEmail(param, partsOfLine);
        getUserPermissions(param, partsOfLine);
        getUserAttributes(param, partsOfLine);
        getUserCreatedData(param, partsOfLine);
    }

    void getUserId(String param, String[] line) {
        if (param.equals("id".toLowerCase().trim())) {
            userId = (long) Integer.parseInt(line[1]);
        }
    }

    void getUserNickName(String param, String[] line) {
        if (param.equals("nickName".toLowerCase().trim())) {
            userNickName = line[1];
        }
    }

    void getUserFirstName(String param, String[] line) {
        if (param.equals("userFirstName".toLowerCase().trim())) {
            userFirstName = line[1].trim();
        }
    }

    void getUserLastName(String param, String[] line) {
        if (param.equals("userLastName".toLowerCase().trim())) {
            userLastName = line[1].trim();
        }
    }

    void getUserEmail(String param, String[] line) {
        if (param.equals("email".toLowerCase().trim())) {
            userEmail = line[1].toLowerCase().trim();
        }
    }

    void getUserPermissions(String param, String[] line) {
        if (param.equals("permissions".toLowerCase().trim())) {
            String[] trimLine = line[1].trim().split(", ");
            userPermissions = new ArrayList<>(Arrays.asList(trimLine));
        }
    }

    void getUserAttributes(String param, String[] line) {
        if (param.equals("attributes".toLowerCase().trim())) {
            Map<String, String> attributes = new LinkedHashMap<>();
            // country: ukraine; city: kharkiv
            String[] attributesSplit = line[1].trim().split("; ");
            // country: ukraine
            String[] countrySplit = attributesSplit[0].trim().split(": ");
            // city: kharkiv
            String[] citySplit = attributesSplit[1].trim().split(": ");
            // country
            // ukraine
            attributes.put(countrySplit[0], countrySplit[1]);
            // city
            // kharkiv
            attributes.put(citySplit[0], citySplit[1]);
            userAttributes = attributes;
        }
    }

    void getUserCreatedData(String param, String[] line) {
        if (param.equals("createdAt".toLowerCase().trim())) {
            String[] split = line[1].split("T");
            String[] splitDate = split[0].split("-");
            String[] splitTime = split[1].split(":");

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(splitDate[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(splitDate[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[2]));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
            calendar.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
            createdAt = calendar.getTime();

            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String text = date.format(formatter);
            LocalDate parsedDate = LocalDate.parse(text, formatter);
            createdAt = Date.from(Instant.from(parsedDate));
        }
    }
}
