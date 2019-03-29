package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static io.github.hildi.can.service.StandardJavaSerializationService.assertFileNotNull;
import static io.github.hildi.can.service.StandardJavaSerializationService.assertFileReadable;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class JavaSerializationService implements SerializationService{

    private User user;

    private long userId;
    private String userNickName;
    private String userEmail;
    private Collection<String> userPermissions;
    private Map<String, String> userAttributes;
    private Date createdAt;

    @Override
    public void serialize(User user, File file) {
        // TODO
//      assertFileExtensionIsCorrect(file);
//      StandardJavaSerializationService service = new StandardJavaSerializationService();
//      service.serialize(user, file);
    }

    @Override
    public User deserialize(File file) {
        assertFileNotNull(file);
        assertFileReadable(file);
        assertFileExtensionIsCorrect(file);
        try (Scanner scanner = new Scanner(file)){
            getAndAssignParametersFromFile(scanner);
            setParametersForNewUser(userId, userNickName, userEmail, userPermissions, userAttributes, createdAt);
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: file " + file.getName() + " is not found. ", e);
        }
        return user;
    }

    private void getAndAssignParametersFromFile(Scanner scanner) {
        while (scanner.hasNextLine()) {
            getUserPropertiesFromFile(scanner);
        }
    }

    private void setIdAndNickNameParameters(long userId, String userNickName) {
        if (userId != 0 && userNickName != null) {
            user = new User(userId, userNickName);
        }
    }

    private void setParametersForNewUser(long userId, String userNickName, String userEmail, Collection<String> userPermissions,
                                         Map<String, String> userAttributes, Date createdAt) {

        setIdAndNickNameParameters(userId, userNickName);
        //print it
        System.out.println();
        System.out.println("userId = " + user.getId());
        System.out.println("userNickName = " + user.getNickName());
        // end print it
        setUserEmail(userEmail);
        setUserPermissions(userPermissions);
        setUserAttributes(userAttributes);
        setUserCreatedData(createdAt);
    }

    private void setUserCreatedData(Date createdAt) {
        user.setCreatedAt(createdAt);
        System.out.println("createdAt = " + createdAt);
    }

    private void setUserAttributes(Map<String, String> userAttributes) {
        user.setAttributes(userAttributes);
        System.out.println("userAttributes = " + userAttributes);
    }

    private void setUserPermissions(Collection<String> userPermissions) {
        user.setPermissions(userPermissions);
        System.out.println("userPermissions = " + userPermissions);
    }

    private void setUserEmail(String userEmail) {
        user.setEmail(userEmail);
        System.out.println("userEmail = " + userEmail);
    }

    private void getUserPropertiesFromFile(Scanner scanner) {
        String[] line = scanner.nextLine().split(" = ");
        String param = line[0].toLowerCase().trim();

        getUserId(param, line);
        getUserNickName(param, line);
        getUserEmail(param, line);
        getUserPermissions(param, line);
        getUserAttributes(param, line);
        getUserCreatedData(param, line);

//        if (param.equals("firstName".toLowerCase().trim())) {
        // TODO
        // user.setName(splitLine[1].toLowerCase().trim());
//        }
//        if (param.equals("lastName".toLowerCase().trim())) {
        // TODO
        // user.setName(splitLine[1].toLowerCase().trim());
//        }
    }

    private void getUserId(String param, String[] line) {
        if (param.equals("id".toLowerCase().trim())) {
            userId = (long) Integer.parseInt(line[1]);
            System.out.println("id = " + userId);
        }
    }

    private void getUserNickName(String param, String[] line) {
        if (param.equals("nickName".toLowerCase().trim())) {
            userNickName = line[1];
            System.out.println("nickname = " + userNickName);
        }
    }

    private void getUserEmail(String param, String[] line) {
        if (param.equals("email".toLowerCase().trim())) {
            userEmail = line[1].toLowerCase().trim();
            System.out.println("email = " + userEmail);
        }
    }

    private void getUserPermissions(String param, String[] line) {
        if (param.equals("permissions".toLowerCase().trim())) {
            String[] permissionsSplit = line[1].trim().split(",");
            userPermissions = new ArrayList<>(Arrays.asList(permissionsSplit));
            System.out.println("permissions = " + userPermissions);
        }
    }

    private void getUserAttributes(String param, String[] line) {
        if (param.equals("attributes".toLowerCase().trim())) {
            Map<String, String> attributes = new HashMap<>();
            String[] attributesSplit = line[1].trim().split(";");
            String[] countrySplit = attributesSplit[0].trim().split(":");
            String[] citySplit = attributesSplit[1].trim().split(":");
            attributes.put(countrySplit[0], countrySplit[1]);
            attributes.put(citySplit[0], citySplit[1]);
            userAttributes = attributes;
            System.out.println("attributes = " + userAttributes);
        }
    }

    private void getUserCreatedData(String param, String[] line) {
        if (param.equals("createdAt".toLowerCase().trim())) {
            Date date = new Date();
            String[] split = line[1].split("T");
            String[] splitDate = split[0].split("-");
            date.setYear(Integer.parseInt(splitDate[0]));
            date.setDate(Integer.parseInt(splitDate[1]));
            date.setMonth(Integer.parseInt(splitDate[2]));
            String[] splitTime = split[1].split(":");
            date.setHours((int) Long.parseLong(splitTime[0]));
            date.setMinutes((int) Long.parseLong(splitTime[1]));
            date.setSeconds((int) Long.parseLong(splitTime[2]));
            createdAt = date;
            System.out.println("date = " + createdAt);
        }
    }

    public static void main(String[] args) {
        File file = new File("User.properties");
        JavaSerializationService service1 = new JavaSerializationService();
        service1.deserialize(file);
    }

    private static void assertFileExtensionIsCorrect(File file) {
        String name = file.getName();
        if (!name.endsWith(".properties")){
            throw new InvalidFileException("Failed to serialize data, reason: invalid file extension " + file.getName());
        }
    }
}
