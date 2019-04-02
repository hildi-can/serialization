package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.exceptions.SerializationException;
import io.github.hildi.can.model.FullName;
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

    private void setParametersForNewUser(long userId, String userNickName, String userEmail, Collection<String> userPermissions,
                                         Map<String, String> userAttributes, Date createdAt) {

        setIdAndNickNameParameters(userId, userNickName);
        //print it
//        System.out.println();
//        System.out.println("userId = " + user.getId());
//        System.out.println("userNickName = " + user.getNickName());
        // end print it
        setFullName(firstName, lastName);
        //print it
//        System.out.println("fullName = " + fullName);
//        System.out.println("userName = " + fullName.getFirstName());
//        System.out.println("userLastName = " + fullName.getLastName());
        // end print it
        setUserEmail(userEmail);
        setUserPermissions(userPermissions);
        setUserAttributes(userAttributes);
        setUserCreatedData(createdAt);
    }

    private void setFullName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            fullName = new FullName(firstName, lastName);
        }
    }

    private void setIdAndNickNameParameters(long userId, String userNickName) {
        if (userId != 0 && userNickName != null) {
            user = new User(userId, userNickName);
        }
    }

    private void setUserEmail(String userEmail) {
        user.setEmail(userEmail);
        // TODO
//        System.out.println("userEmail = " + userEmail);
    }

    private void setUserPermissions(Collection<String> userPermissions) {
        user.setPermissions(userPermissions);
        // TODO
//        System.out.println("userPermissions = " + userPermissions);
    }

    private void setUserAttributes(Map<String, String> userAttributes) {
        user.setAttributes(userAttributes);
        // TODO
//        System.out.println("userAttributes = " + userAttributes);
    }

    private void setUserCreatedData(Date createdAt) {
        user.setCreatedAt(createdAt);
        // TODO
//        System.out.println("createdAt = " + createdAt);
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
            // TODO
//            System.out.println("id = " + userId);
        }
    }

    private void getUserNickName(String param, String[] line) {
        if (param.equals("nickName".toLowerCase().trim())) {
            userNickName = line[1];
            // TODO
//            System.out.println("nickname = " + userNickName);
        }
    }

    private void getUserFirstName(String param, String[] line) {
        if (param.equals("firstName".toLowerCase().trim())) {
            firstName = line[1].trim();
            // TODO
//            System.out.println("firstName = " + firstName);
        }
    }

    private void getUserLastName(String param, String[] line) {
        if (param.equals("lastName".toLowerCase().trim())) {
            lastName = line[1].trim();
            // TODO
//            System.out.println("lastName = " + lastName);
        }
    }

    private void getUserEmail(String param, String[] line) {
        if (param.equals("email".toLowerCase().trim())) {
            userEmail = line[1].toLowerCase().trim();
            // TODO
//            System.out.println("email = " + userEmail);
        }
    }

    private void getUserPermissions(String param, String[] line) {
        if (param.equals("permissions".toLowerCase().trim())) {
            String trimLine = line[1].trim();
            String[] permissionsSplit = trimLine.split(" , ");
            userPermissions = new ArrayList<>(Arrays.asList(permissionsSplit));
            // TODO
//            System.out.println("permissions = " + userPermissions);
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
            // TODO
//            System.out.println("attributes = " + userAttributes);
//            System.out.println("attributes = " + userAttributes.get("country"));
//            System.out.println("attributes = " + userAttributes.get("city"));
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

            // TODO
//            System.out.println("date = " + createdAt);
        }
    }

    private static void assertFileExtensionIsCorrect(File file) {
        String name = file.getName();
        if (!name.endsWith(".properties")){
            throw new InvalidFileException("Failed to serialize data, reason: invalid file extension " + file.getName());
        }
    }

    public static void main(String[] args) {
        File file = new File("User.properties");
        new JavaSerializationService().deserialize(file);
    }

}
