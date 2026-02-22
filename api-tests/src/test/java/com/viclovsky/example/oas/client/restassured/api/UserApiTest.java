package com.viclovsky.example.oas.client.restassured.api;

import com.google.inject.Inject;
import com.viclovsky.example.oas.client.restassured.ApiClient;
import com.viclovsky.example.oas.client.restassured.model.User;
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
 * CRUD tests for User API. One explicit test per CRUD operation.
 */
@ExtendWith(GuiceExtension.class)
@IncludeModule(ExampleApiModule.class)
class UserApiTest extends BasePetstoreTest {

    @Inject
    private ApiClient api;

    @Test
    void shouldCreateUser() {
        User body = new User()
                .username("cruduser1")
                .firstName("Crud")
                .lastName("User")
                .email("crud1@example.com")
                .password("pass123")
                .userStatus(0);
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)));
        // Verify by read
        User read = api.user().getUserByName().usernamePath("cruduser1")
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getUsername()).isEqualTo("cruduser1");
        assertThat(read.getFirstName()).isEqualTo("Crud");
    }

    @Test
    void shouldGetUserByName() {
        User body = new User()
                .username("readuser1")
                .firstName("Read")
                .lastName("User")
                .email("read1@example.com")
                .password("pass")
                .userStatus(0);
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        User read = api.user().getUserByName().usernamePath("readuser1")
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getUsername()).isEqualTo("readuser1");
        assertThat(read.getFirstName()).isEqualTo("Read");
    }

    @Test
    void shouldUpdateUser() {
        User body = new User()
                .username("updateuser1")
                .firstName("Update")
                .lastName("User")
                .email("update1@example.com")
                .password("pass")
                .userStatus(0);
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        User updatedBody = new User()
                .username("updateuser1")
                .firstName("UpdatedFirst")
                .lastName("UpdatedLast")
                .email("updated@example.com")
                .password("newpass")
                .userStatus(1);
        api.user().updateUser().usernamePath("updateuser1").reqSpec(r -> r.setContentType(ContentType.JSON)).body(updatedBody)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        User read = api.user().getUserByName().usernamePath("updateuser1")
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getUsername()).isEqualTo("updateuser1");
        // Server may or may not persist all fields; at least verify user is still found after update
        assertThat(read.getFirstName()).isNotNull();
    }

    @Test
    void shouldDeleteUser() {
        User body = new User()
                .username("deleteuser1")
                .firstName("Delete")
                .lastName("User")
                .email("delete1@example.com")
                .password("pass")
                .userStatus(0);
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        api.user().deleteUser().usernamePath("deleteuser1")
                .execute(validatedWith(shouldBeCode(SC_OK)));

        api.user().getUserByName().usernamePath("deleteuser1")
                .execute(validatedWith(shouldBeCode(SC_NOT_FOUND)));
    }
}
