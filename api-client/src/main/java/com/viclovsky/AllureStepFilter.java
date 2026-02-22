package com.viclovsky;

import io.qameta.allure.Allure;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

/**
 * Wraps each API call in an Allure step "Вызов {method} to {endpoint}".
 * Request and response attachments (from AllureRestAssured) are added inside this step
 * because this filter runs first (order MIN), so the rest of the chain runs inside {@link io.qameta.allure.Allure#step}.
 */
public final class AllureStepFilter implements OrderedFilter {

    private static final String STEP_NAME_TEMPLATE = "Вызов %s to %s";

    @Override
    public Response filter(final FilterableRequestSpecification requestSpec,
                           final FilterableResponseSpecification responseSpec,
                           final FilterContext filterContext) {
        String method = String.valueOf(requestSpec.getMethod());
        String endpoint = requestSpec.getURI();
        String stepName = String.format(STEP_NAME_TEMPLATE, method, endpoint);
        return Allure.step(stepName, () -> filterContext.next(requestSpec, responseSpec));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
