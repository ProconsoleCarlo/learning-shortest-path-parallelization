package it.proconsole.learning.shortestpath.parallelization.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per validare il setup del modulo common.
 */
@DisplayName("CommonUtils Test")
class CommonUtilsTest {

    @Test
    @DisplayName("Should return welcome message")
    void testGetWelcomeMessage() {
        String message = CommonUtils.getWelcomeMessage();
        assertNotNull(message, "Message should not be null");
        assertTrue(message.contains("Welcome"), "Message should contain 'Welcome'");
        assertTrue(message.contains("Common Module"), "Message should contain 'Common Module'");
    }

    @Test
    @DisplayName("Should validate setup")
    void testIsSetupValid() {
        assertTrue(CommonUtils.isSetupValid(), "Setup should be valid");
    }
}

