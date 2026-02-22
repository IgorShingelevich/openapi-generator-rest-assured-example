package com.viclovsky.example.oas.client.restassured.junit5;

import com.viclovsky.example.oas.client.PetstoreVerifier;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base for JUnit 5 API tests. Ensures Petstore is reachable and api.baseUri matches before any test runs.
 */
abstract class BasePetstoreTest {

    @BeforeAll
    static void verifyPetstoreUp() {
        PetstoreVerifier.verifyReachable();
    }
}
