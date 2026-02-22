package com.viclovsky;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Factory for request-side filters: Allure attachments, error and request/response logging.
 * Single point to configure filters applied to every API request.
 */
public final class ReqFilterFactory {

    private ReqFilterFactory() {
    }

    /**
     * Returns the list of filters to apply to requests (order matters).
     * Includes: Allure (request/response attachments), error logging, optional request/response logging.
     */
    public static List<Filter> getFilters() {
        return Arrays.asList(
                new AllureRestAssured(),
                new ErrorLoggingFilter(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
