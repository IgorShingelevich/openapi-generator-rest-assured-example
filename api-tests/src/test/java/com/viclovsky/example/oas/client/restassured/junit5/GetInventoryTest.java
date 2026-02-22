package com.viclovsky.example.oas.client.restassured.junit5;

import com.google.inject.Inject;
import com.viclovsky.example.oas.client.restassured.ApiClient;
import com.viclovsky.example.oas.client.restassured.module.ExampleApiModule;
import name.falgout.jeffrey.testing.junit5.GuiceExtension;
import name.falgout.jeffrey.testing.junit5.IncludeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static com.viclovsky.example.oas.client.restassured.ResponseSpecBuilders.shouldBeCode;
import static com.viclovsky.example.oas.client.restassured.ResponseSpecBuilders.validatedWith;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(GuiceExtension.class)
@IncludeModule(ExampleApiModule.class)
class GetInventoryTest extends BasePetstoreTest {

    @Inject
    private ApiClient api;

    @Test
    void shouldGetInventoryTest() {
        Map<String, Integer> inventory = api.store().getInventory().executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(inventory).isNotEmpty();
    }
}
