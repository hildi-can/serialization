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

    private StandardJavaSerializationService service = new StandardJavaSerializationService();
    private User user = new User(2L,"test");
    private long userId;
    private String userNickName;

    @Override
    public void serialize(User user, File file) {
        assertFileExtensionIsCorrect(file);
        service.serialize(user, file);
    }

    @Override
    public User deserialize(File file) {
        assertFileNotNull(file);
        assertFileReadable(file);
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()) {
                setUserProperties(scanner);
            }
            setUserIdAndNickName();
        } catch (FileNotFoundException e) {
            throw new SerializationException("Failed to deserialize data, reason: file " + file.getName() + " is not found. ", e);
        }
        return null;
    }

    private void setUserProperties(Scanner scanner) {
        String[] line = scanner.nextLine().split(" = ");
        String param = line[0].toLowerCase().trim();

        setUserId(param, line);
        setUserNickName(param, line);
        setUserEmail(param, line);
        setUserPermissions(param, line);
        setUserAttributes(param, line);
        setUserCreatedData(param, line);

//        if (param.equals("firstName".toLowerCase().trim())) {
        // TODO
        // user.setName(splitLine[1].toLowerCase().trim());
//        }
//        if (param.equals("lastName".toLowerCase().trim())) {
        // TODO
        // user.setName(splitLine[1].toLowerCase().trim());
//        }
    }

    private void setUserIdAndNickName() {
        if (userId != 0 && userNickName != null){
            user = new User(userId, userNickName);
            System.out.println(user.getId()+" "+ user.getNickName());
        }
    }

    private void setUserCreatedData(String param, String[] line) {
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
            user.setCreatedAt(date);
            System.out.println("date");
        }
    }

    private void setUserAttributes(String param, String[] line) {
        if (param.equals("attributes".toLowerCase().trim())) {
            Map<String, String> attributes = new HashMap<>();
            String[] attributesSplit = line[1].trim().split(";");
            String[] countrySplit = attributesSplit[0].trim().split(":");
            String[] citySplit = attributesSplit[1].trim().split(":");
            attributes.put(countrySplit[0], countrySplit[1]);
            attributes.put(citySplit[0], citySplit[1]);
            user.setAttributes(attributes);
            System.out.println("attributes");
        }
    }

    private void setUserPermissions(String param, String[] line) {
        if (param.equals("permissions".toLowerCase().trim())) {
            String[] permissionsSplit = line[1].split(",");
            ArrayList<String> permissions = new ArrayList<>(Arrays.asList(permissionsSplit));
            user.setPermissions(permissions);
            System.out.println("permissions");
        }
    }

    private void setUserEmail(String param, String[] line) {
        if (param.equals("email".toLowerCase().trim())) {
            user.setEmail(line[1].toLowerCase().trim());
            System.out.println("email");
        }
    }

    private void setUserNickName(String param, String[] line) {
        if (param.equals("nickName".toLowerCase().trim())) {
            userNickName = line[1];
            System.out.println("nickname");
        }
    }

    private void setUserId(String param, String[] line) {
        if (param.equals("id".toLowerCase().trim())) {
            userId = (long) Integer.parseInt(line[1]);
            System.out.println("id");
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
