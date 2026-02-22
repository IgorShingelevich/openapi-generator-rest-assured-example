package com.viclovsky;

import io.restassured.filter.Filter;

import java.util.Collections;
import java.util.List;

/**
 * Factory for response-related filters (e.g. response time, custom validation).
 * RestAssured uses a single Filter for the whole request-response cycle;
 * Allure and logging are already in ReqFilterFactory. Add here any extra response-phase filters.
 */
public final class ResFilterFactory {

    private ResFilterFactory() {
    }

    /**
     * Returns additional filters applied to the request-response chain.
     * Empty by default; add e.g. ResponseTimeFilter or custom validation filters as needed.
     */
    public static List<Filter> getFilters() {
        return Collections.emptyList();
    }
}
