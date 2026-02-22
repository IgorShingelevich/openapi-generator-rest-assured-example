package com.viclovsky.example.oas.client.restassured.api;

import com.viclovsky.example.oas.client.restassured.model.Order;
import com.viclovsky.example.oas.client.restassured.model.Pet;
import com.viclovsky.example.oas.client.restassured.model.User;

import java.util.Collections;

/**
 * Static test data for pet_api, store_api and user_api tests.
 * Use these entities once in static context; tests only reference them and no longer build data inline.
 */
public final class TestDataProvider {

    private TestDataProvider() {
    }

    // --- Pet API ---

    public static Pet petForCreate() {
        return new Pet()
                .name("CRUD Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/photo.jpg"));
    }

    public static Pet petForRead() {
        return new Pet()
                .name("Read Test Pet")
                .status(Pet.StatusEnum.PENDING)
                .photoUrls(Collections.singletonList("http://example.com/read.jpg"));
    }

    public static Pet petForUpdate() {
        return new Pet()
                .name("Update Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/update.jpg"));
    }

    public static Pet petForDelete() {
        return new Pet()
                .name("Delete Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/delete.jpg"));
    }

    /** Pet body used in Store tests before placing an order. */
    public static Pet petForStoreOrder() {
        return new Pet()
                .name("Store Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/store.jpg"));
    }

    // --- Store API (Order bodies; petId set at call site) ---

    public static Order orderPlaced(long petId) {
        return new Order()
                .petId(petId)
                .quantity(2)
                .status(Order.StatusEnum.PLACED);
    }

    public static Order orderApproved(long petId) {
        return new Order()
                .petId(petId)
                .quantity(1)
                .status(Order.StatusEnum.APPROVED);
    }

    public static Order orderForDelete(long petId) {
        return new Order()
                .petId(petId)
                .quantity(1)
                .status(Order.StatusEnum.PLACED);
    }

    // --- User API ---

    public static User userForCreate() {
        return new User()
                .username("cruduser1")
                .firstName("Crud")
                .lastName("User")
                .email("crud1@example.com")
                .password("pass123")
                .userStatus(0);
    }

    public static User userForRead() {
        return new User()
                .username("readuser1")
                .firstName("Read")
                .lastName("User")
                .email("read1@example.com")
                .password("pass")
                .userStatus(0);
    }

    public static User userForUpdate() {
        return new User()
                .username("updateuser1")
                .firstName("Update")
                .lastName("User")
                .email("update1@example.com")
                .password("pass")
                .userStatus(0);
    }

    public static User userForUpdateModified() {
        return new User()
                .username("updateuser1")
                .firstName("UpdatedFirst")
                .lastName("UpdatedLast")
                .email("updated@example.com")
                .password("newpass")
                .userStatus(1);
    }

    public static User userForDelete() {
        return new User()
                .username("deleteuser1")
                .firstName("Delete")
                .lastName("User")
                .email("delete1@example.com")
                .password("pass")
                .userStatus(0);
    }
}
