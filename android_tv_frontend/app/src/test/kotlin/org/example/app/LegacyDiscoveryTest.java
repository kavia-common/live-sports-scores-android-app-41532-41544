package org.example.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PUBLIC_INTERFACE
 * A minimal JUnit Jupiter test to ensure Gradle's JUnit Platform discovers tests under the app module.
 */
public class LegacyDiscoveryTest {

    // PUBLIC_INTERFACE
    @Test
    public void testAlwaysPasses() {
        /** Always passes to satisfy CI test discovery. */
        assertTrue(true);
    }
}
