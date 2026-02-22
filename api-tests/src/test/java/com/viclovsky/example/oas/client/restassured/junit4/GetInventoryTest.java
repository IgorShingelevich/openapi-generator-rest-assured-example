package com.viclovsky.example.oas.client.restassured.junit4;

import com.viclovsky.ReqSpecFactory;
import com.viclovsky.example.oas.client.PetstoreVerifier;
import com.viclovsky.example.oas.client.restassured.ApiClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static com.viclovsky.example.oas.client.restassured.ResponseSpecBuilders.shouldBeCode;
import static com.viclovsky.example.oas.client.restassured.ResponseSpecBuilders.validatedWith;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class GetInventoryTest {

    private ApiClient api;

    @BeforeClass
    public static void verifyPetstoreUp() {
        PetstoreVerifier.verifyReachable();
    }

    @Before
    public void createApi() {
        api = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(ReqSpecFactory::create));
    }

    @Test
    public void shouldGetInventoryTest() {
        Map<String, Integer> inventory = api.store().getInventory().executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(inventory.keySet().size(), greaterThan(0));
    }

}
