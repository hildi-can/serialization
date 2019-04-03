package io.github.hildi.can.asserts;

import io.github.hildi.can.model.User;

import java.io.File;

import static io.github.hildi.can.asserts.AssertFile.assertFileNotNull;

/**
 * Created by Serhii Hildi on 04.04.19.
 */
public class AssertUser {
    public static void assertParamNotNull(User user, File file) {
        if (user == null) {
            throw new IllegalArgumentException("Failed to serialize, reason: User is null.");
        }
        assertFileNotNull(file);
    }
}
