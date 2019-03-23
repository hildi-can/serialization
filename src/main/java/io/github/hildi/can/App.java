package io.github.hildi.can;


import io.github.hildi.can.model.User;
import io.github.hildi.can.service.StandardJavaSerializationService;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        File file = new File("TestFile.txt");
        User user = new User(11L, "Piska");
        StandardJavaSerializationService sjss = new StandardJavaSerializationService();

            sjss.serialize(user, file);
            System.out.println(sjss.deserialize(file));
    }
}
