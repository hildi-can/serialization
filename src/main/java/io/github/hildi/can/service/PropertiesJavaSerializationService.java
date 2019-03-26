package io.github.hildi.can.service;

import io.github.hildi.can.exceptions.InvalidFileException;
import io.github.hildi.can.model.User;

import java.io.File;

/**
 * Created by Serhii Hildi on 26.03.19.
 */
public class PropertiesJavaSerializationService implements SerializationService{

    private StandardJavaSerializationService service = new StandardJavaSerializationService();;

    @Override
    public void serialize(User user, File file) {
        assertFileExtensionIsCorrect(file);
        service.serialize(user, file);
    }

    @Override
    public User deserialize(File file) {

        /*
         * TODO
         * 1. Метод который перебирает все строки в документе по сплиту.
         *      1.1. Проверяет первый параметр в строке на совпадение с переменными.
         *           Если есть переменные из списка совпадающие с нужными - записать их значение после разделителя (" = ") в переменную с соответсвующим названием.
         *              Если нет - идти к следующей переменной
         */

        return service.deserialize(file);
    }

    public static void main(String[] args) {
        File file = new File("/Users/macbook/Desktop/dev/hildi/serialization/user.properties");
        User valera = new User(1L, "Valera");
        PropertiesJavaSerializationService service = new PropertiesJavaSerializationService();

        service.serialize(valera, file);
        System.out.println(service.deserialize(file));
    }

    private static void assertFileExtensionIsCorrect(File file) {
        String name = file.getName();
        if (!name.endsWith(".properties")){
            throw new InvalidFileException("Failed to serialize data, reason: invalid file extension " + file.getName());
        }
    }
}
