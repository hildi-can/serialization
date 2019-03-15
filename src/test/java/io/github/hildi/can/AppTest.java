package io.github.hildi.can;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    @DisplayName("should run main without any exceptions")
    void shouldRun() {
        App.main(new String[]{});
    }
}
