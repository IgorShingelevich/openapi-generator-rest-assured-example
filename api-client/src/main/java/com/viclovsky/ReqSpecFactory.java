package com.viclovsky;

import com.viclovsky.example.oas.client.restassured.GsonObjectMapper;
import io.restassured.builder.RequestSpecBuilder;

import java.util.stream.Stream;

import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;

/**
 * Single point for creating RequestSpecBuilder: base URI, RestAssured config, and all request/response filters.
 * Used by ExampleApiProvider to supply the API client with a consistent "black box" configuration.
 */
public final class ReqSpecFactory {

    private static final String DEFAULT_BASE_URI = "http://127.0.0.1:80/v2";

    private ReqSpecFactory() {
    }

    /**
     * Creates a RequestSpecBuilder with default base URI and all standard filters.
     */
    public static RequestSpecBuilder create() {
        return create(DEFAULT_BASE_URI);
    }

    /**
     * Creates a RequestSpecBuilder with the given base URI and all standard filters.
     * Use e.g. System.getProperty("api.baseUri", DEFAULT_BASE_URI) for config-driven base URI.
     */
    public static RequestSpecBuilder create(String baseUri) {
        RequestSpecBuilder spec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setConfig(config()
                        .objectMapperConfig(objectMapperConfig()
                                .defaultObjectMapper(GsonObjectMapper.gson())));

        Stream.concat(
                ReqFilterFactory.getFilters().stream(),
                ResFilterFactory.getFilters().stream()
        ).forEach(spec::addFilter);

        return spec;
    }
}
