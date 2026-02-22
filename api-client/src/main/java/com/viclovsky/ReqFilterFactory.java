package com.viclovsky;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.util.Arrays;
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
     * First: Allure step wrapper "Вызов {method} to {endpoint}" so each call and its attachments are in one step.
     * Then: Allure (request/response attachments), error logging, optional request/response logging.
     */
    public static List<Filter> getFilters() {
        return Arrays.asList(
                new AllureStepFilter(),
                new AllureRestAssured(),
                new ErrorLoggingFilter(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
