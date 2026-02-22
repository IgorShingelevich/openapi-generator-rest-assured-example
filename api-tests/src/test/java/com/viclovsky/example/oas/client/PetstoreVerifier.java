package com.viclovsky.example.oas.client;

import com.viclovsky.ReqSpecFactory;

/**
 * Verifies that Petstore API is reachable at the configured base URI before running API tests.
 * Use in @BeforeAll / @BeforeClass so the build fails fast with a clear message if Docker/Petstore is not up.
 */
public final class PetstoreVerifier {

    private static final String PROBE_PATH = "/store/inventory";

    private PetstoreVerifier() {
    }

    /**
     * Checks that the Petstore API at {@code api.baseUri} responds with 200 on GET /store/inventory.
     * Must match Docker run: SWAGGER_BASE_PATH=/v2, port 80 - so baseUri should be http://127.0.0.1:80/v2.
     *
     * @throws AssertionError if the API is not reachable or returns non-200
     */
    public static void verifyReachable() {
        String baseUri = System.getProperty(ReqSpecFactory.PROP_API_BASE_URI, "http://127.0.0.1:80/v2");
        String url = baseUri.endsWith("/") ? baseUri + "store/inventory" : baseUri + "/store/inventory";

        int code;
        try {
            code = io.restassured.RestAssured.given()
                    .relaxedHTTPSValidation()
                    .when()
                    .get(url)
                    .getStatusCode();
        } catch (Exception e) {
            throw new AssertionError(
                    "Petstore API is not reachable at " + baseUri + ". "
                            + "Start Docker and run: docker run -d -e SWAGGER_BASE_PATH=/v2 -p 80:8080 --name petstore swaggerapi/petstore "
                            + "or build with -P petstore-docker. Error: " + e.getMessage(), e);
        }

        if (code != 200) {
            throw new AssertionError(
                    "Petstore at " + baseUri + " returned " + code + " for GET " + PROBE_PATH + ". "
                            + "Check that container is running and api.baseUri matches Docker port (e.g. http://127.0.0.1:80/v2).");
        }
    }
}
