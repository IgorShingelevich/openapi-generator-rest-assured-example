package com.viclovsky.example.oas.client.restassured.api;

import com.google.inject.Inject;
import com.viclovsky.example.oas.client.restassured.ApiClient;
import com.viclovsky.example.oas.client.restassured.model.Order;
import com.viclovsky.example.oas.client.restassured.module.ExampleApiModule;
import com.viclovsky.example.oas.client.restassured.junit5.BasePetstoreTest;
import name.falgout.jeffrey.testing.junit5.GuiceExtension;
import name.falgout.jeffrey.testing.junit5.IncludeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.http.ContentType;

import static com.viclovsky.example.oas.client.restassured.ResponseSpecBuilders.shouldBeCode;
import static com.viclovsky.example.oas.client.restassured.ResponseSpecBuilders.validatedWith;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * CRUD tests for Store API. One explicit test per CRUD operation (no Update in Store).
 */
@ExtendWith(GuiceExtension.class)
@IncludeModule(ExampleApiModule.class)
class StoreApiTest extends BasePetstoreTest {

    @Inject
    private ApiClient api;

    private long createPetForOrder() {
        return api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.petForStoreOrder())
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");
    }

    @Test
    void shouldPlaceOrder() {
        long petId = createPetForOrder();
        Order created = api.store().placeOrder().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.orderPlaced(petId))
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(created.getId()).isNotNull();
        assertThat(created.getPetId()).isEqualTo(petId);
        assertThat(created.getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldGetOrderById() {
        long petId = createPetForOrder();
        long orderId = api.store().placeOrder().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.orderApproved(petId))
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        Order read = api.store().getOrderById().orderIdPath(orderId)
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getId()).isEqualTo(orderId);
        assertThat(read.getQuantity()).isEqualTo(1);
    }

    @Test
    void shouldDeleteOrder() {
        long petId = createPetForOrder();
        long orderId = api.store().placeOrder().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.orderForDelete(petId))
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        api.store().deleteOrder().orderIdPath(orderId)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        api.store().getOrderById().orderIdPath(orderId)
                .execute(validatedWith(shouldBeCode(SC_NOT_FOUND)));
    }
}
