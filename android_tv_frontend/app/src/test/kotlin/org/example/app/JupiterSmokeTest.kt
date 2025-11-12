package org.example.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * PUBLIC_INTERFACE
 * JUnit Jupiter smoke test to verify JUnit Platform discovery works in the app module.
 */
class JupiterSmokeTest {
    // PUBLIC_INTERFACE
    @Test
    fun addsNumbers() {
        /** Simple sanity check to assert JUnit5 engine discovery. */
        assertEquals(4, 2 + 2)
    }
}
