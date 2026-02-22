package com.viclovsky.example.oas.client.restassured.provider;

import com.google.inject.Provider;
import com.viclovsky.ReqSpecFactory;
import com.viclovsky.example.oas.client.restassured.ApiClient;

public class ExampleApiProvider implements Provider<ApiClient> {

    @Override
    public ApiClient get() {
        return ApiClient.api(ApiClient.Config.apiConfig()
                .reqSpecSupplier(ReqSpecFactory::create));
    }
}
