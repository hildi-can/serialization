package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.exceptions.NotReadableFileException;
import io.github.hildi.can.exceptions.NotWritableFileException;
import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.model.FullName;
import io.github.hildi.can.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class JavaSerializationService implements SerializationService{

    private User user;
    private FullName fullName;
    private long userId;
    private String userNickName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Collection<String> userPermissions;
    private Map<String, String> userAttributes;
    private Date createdAt;

    @Override
    public void serialize(User user, File file) {
        assertParamNotNull(user, file);
        assertFileExist(file);
        assertFileWritable(file);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(setUserParametersToFile(user));
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize data. " + file.getName(), e);
        }
    }

    @Override
    public User deserialize(File file) {
        assertFileNotNull(file);
        assertFileReadable(file);
        assertFileExtensionIsCorrect(file);
        try (Scanner scanner = new Scanner(file)){
            getAndAssignParametersFromFile(scanner);
            setUserParameters(userId, userNickName, userEmail, userPermissions, userAttributes, createdAt);
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: file " + file.getName() +
                " is not found. ", e);
        }
        return user;
    }

    private String setUserParametersToFile(User user) {
        return "id = " + user.getId() + "\n" +
            "nickName = " + user.getNickName() + "\n" +
//            "firstName = " + fullName.getFirstName() + "\n" +
//            "lastName = " + fullName.getLastName() + "\n" +
            "email = " + user.getEmail() + "\n" +
            "permissions = " + user.getPermissions() + "\n" +
            "attributes = " + user.getAttributes() + "\n" +
            "createdAt = " + user.getCreatedAt() + "\n";
    }

    private void getAndAssignParametersFromFile(Scanner scanner) {
        while (scanner.hasNextLine()) {
            getUserPropertiesFromFile(scanner);
        }
    }

    private void setUserParameters(long userId, String userNickName, String userEmail,
                                   Collection<String> userPermissions,
                                   Map<String, String> userAttributes, Date createdAt) {
        setIdAndNickNameParameters(userId, userNickName);
        setFullName(firstName, lastName);
        setUserEmail(userEmail);
        setUserPermissions(userPermissions);
        setUserAttributes(userAttributes);
        setUserCreatedData(createdAt);
    }

    private void setIdAndNickNameParameters(long userId, String userNickName) {
        if (userId != 0 && userNickName != null) {
            user = new User(userId, userNickName);
        }
    }

    private void setFullName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            fullName = new FullName(firstName, lastName);
        }
    }

    private void setUserEmail(String userEmail) {
        user.setEmail(userEmail);
    }

    private void setUserPermissions(Collection<String> userPermissions) {
        user.setPermissions(userPermissions);
    }

    private void setUserAttributes(Map<String, String> userAttributes) {
        user.setAttributes(userAttributes);
    }

    private void setUserCreatedData(Date createdAt) {
        user.setCreatedAt(createdAt);
    }

    private void getUserPropertiesFromFile(Scanner scanner) {
        String[] line = scanner.nextLine().split(" = ");
        String param = line[0].toLowerCase().trim();

        getUserId(param, line);
        getUserNickName(param, line);
        getUserFirstName(param, line);
        getUserLastName(param, line);
        getUserEmail(param, line);
        getUserPermissions(param, line);
        getUserAttributes(param, line);
        getUserCreatedData(param, line);
    }

    private void getUserId(String param, String[] line) {
        if (param.equals("id".toLowerCase().trim())) {
            userId = (long) Integer.parseInt(line[1]);
        }
    }

    private void getUserNickName(String param, String[] line) {
        if (param.equals("nickName".toLowerCase().trim())) {
            userNickName = line[1];
        }
    }

    private void getUserFirstName(String param, String[] line) {
        if (param.equals("firstName".toLowerCase().trim())) {
            firstName = line[1].trim();
        }
    }

    private void getUserLastName(String param, String[] line) {
        if (param.equals("lastName".toLowerCase().trim())) {
            lastName = line[1].trim();
        }
    }

    private void getUserEmail(String param, String[] line) {
        if (param.equals("email".toLowerCase().trim())) {
            userEmail = line[1].toLowerCase().trim();
        }
    }

    private void getUserPermissions(String param, String[] line) {
        if (param.equals("permissions".toLowerCase().trim())) {
            String trimLine = line[1].trim();
            String[] permissionsSplit = trimLine.split(" , ");
            userPermissions = new ArrayList<>(Arrays.asList(permissionsSplit));
        }
    }

    private void getUserAttributes(String param, String[] line) {
        if (param.equals("attributes".toLowerCase().trim())) {
            userAttributes = new LinkedHashMap<>();
            // country: ukraine; city: kharkiv
            String[] attributesSplit = line[1].trim().split("; ");
            // country: ukraine
            String[] countrySplit = attributesSplit[0].trim().split(": ");
            // city: kharkiv
            String[] citySplit = attributesSplit[1].trim().split(": ");
            // country
            // ukraine
            userAttributes.put(countrySplit[0], countrySplit[1]);
            // city
            // kharkiv
            userAttributes.put(citySplit[0], citySplit[1]);
        }
    }

    private void getUserCreatedData(String param, String[] line) {
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
        }
    }

    private static void assertFileExtensionIsCorrect(File file) {
        String name = file.getName();
        if (!name.endsWith(".properties")){
            throw new InvalidFileException("Failed to serialize data, reason: invalid file extension " + file.getName());
        }
    }

    private static void assertFileWritable(File file) {
        if (!file.canWrite()) {
            throw new NotWritableFileException("Failed to serialize, reason: " + file.getName() + " is not writable.");
        }
    }

    private static void assertFileReadable(File file) {
        if (!file.canRead()) {
            throw new NotReadableFileException("Failed to deserialize, reason: " + file.getName() + " is not readable or doesn't exist.");
        }
    }

    private static void assertParamNotNull(User user, File file) {
        if (user == null) {
            throw new IllegalArgumentException("Failed to serialize, reason: User is null.");
        }
        assertFileNotNull(file);
    }

    private static void assertFileNotNull(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Failed to serialize, reason: File is null.");
        }
    }

    private static void assertFileExist(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Failed to serialize, reason: file " + file.getName() + " doesn't exist.");
        }
    }
}
