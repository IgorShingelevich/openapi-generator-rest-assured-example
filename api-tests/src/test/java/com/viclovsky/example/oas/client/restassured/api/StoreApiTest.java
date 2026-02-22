package com.viclovsky.example.oas.client.restassured.api;

import com.google.inject.Inject;
import com.viclovsky.example.oas.client.restassured.ApiClient;
import com.viclovsky.example.oas.client.restassured.model.Order;
import com.viclovsky.example.oas.client.restassured.model.Pet;
import com.viclovsky.example.oas.client.restassured.module.ExampleApiModule;
import com.viclovsky.example.oas.client.restassured.junit5.BasePetstoreTest;
import name.falgout.jeffrey.testing.junit5.GuiceExtension;
import name.falgout.jeffrey.testing.junit5.IncludeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.http.ContentType;

import java.util.Collections;

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
        Pet pet = new Pet()
                .name("Store Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/store.jpg"));
        return api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(pet)
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");
    }

    @Test
    void shouldPlaceOrder() {
        long petId = createPetForOrder();
        Order body = new Order()
                .petId(petId)
                .quantity(2)
                .status(Order.StatusEnum.PLACED);
        Order created = api.store().placeOrder().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(created.getId()).isNotNull();
        assertThat(created.getPetId()).isEqualTo(petId);
        assertThat(created.getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldGetOrderById() {
        long petId = createPetForOrder();
        Order order = new Order()
                .petId(petId)
                .quantity(1)
                .status(Order.StatusEnum.APPROVED);
        long orderId = api.store().placeOrder().reqSpec(r -> r.setContentType(ContentType.JSON)).body(order)
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
        Order order = new Order()
                .petId(petId)
                .quantity(1)
                .status(Order.StatusEnum.PLACED);
        long orderId = api.store().placeOrder().reqSpec(r -> r.setContentType(ContentType.JSON)).body(order)
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        api.store().deleteOrder().orderIdPath(orderId)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        api.store().getOrderById().orderIdPath(orderId)
                .execute(validatedWith(shouldBeCode(SC_NOT_FOUND)));
    }
}
