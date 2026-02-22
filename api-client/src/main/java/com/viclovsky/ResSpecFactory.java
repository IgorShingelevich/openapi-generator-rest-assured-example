package com.viclovsky;

import io.restassured.builder.ResponseSpecBuilder;

import static org.hamcrest.Matchers.lessThan;

/**
 * Factory for common response validation specs: status codes, response time, etc.
 * Use in tests with validatedWith(ResSpecFactory.shouldBeSuccess()) or chain with other specs.
 */
public final class ResSpecFactory {

    private static final int SC_OK = 200;
    private static final int SC_NOT_FOUND = 404;

    private ResSpecFactory() {
    }

    /**
     * Expects the given HTTP status code.
     */
    public static ResponseSpecBuilder shouldBeCode(int code) {
        return new ResponseSpecBuilder().expectStatusCode(code);
    }

    /**
     * Expects 200 OK.
     */
    public static ResponseSpecBuilder shouldBeSuccess() {
        return shouldBeCode(SC_OK);
    }

    /**
     * Expects 404 Not Found.
     */
    public static ResponseSpecBuilder shouldBeNotFound() {
        return shouldBeCode(SC_NOT_FOUND);
    }

    /**
     * Expects response time to be less than the given milliseconds.
     */
    public static ResponseSpecBuilder withMaxResponseTime(long millis) {
        return new ResponseSpecBuilder().expectResponseTime(lessThan(millis));
    }
}
