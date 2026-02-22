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
        User data = TestDataProvider.userForCreate();
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(data)
                .execute(validatedWith(shouldBeCode(SC_OK)));
        User read = api.user().getUserByName().usernamePath(data.getUsername())
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getUsername()).isEqualTo(data.getUsername());
        assertThat(read.getFirstName()).isEqualTo(data.getFirstName());
    }

    @Test
    void shouldGetUserByName() {
        User data = TestDataProvider.userForRead();
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(data)
                .execute(validatedWith(shouldBeCode(SC_OK)));
        User read = api.user().getUserByName().usernamePath(data.getUsername())
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getUsername()).isEqualTo(data.getUsername());
        assertThat(read.getFirstName()).isEqualTo(data.getFirstName());
    }

    @Test
    void shouldUpdateUser() {
        User data = TestDataProvider.userForUpdate();
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(data)
                .execute(validatedWith(shouldBeCode(SC_OK)));
        api.user().updateUser().usernamePath(data.getUsername()).reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.userForUpdateModified())
                .execute(validatedWith(shouldBeCode(SC_OK)));
        User read = api.user().getUserByName().usernamePath(data.getUsername())
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getUsername()).isEqualTo(data.getUsername());
        assertThat(read.getFirstName()).isNotNull();
    }

    @Test
    void shouldDeleteUser() {
        User data = TestDataProvider.userForDelete();
        api.user().createUser().reqSpec(r -> r.setContentType(ContentType.JSON)).body(data)
                .execute(validatedWith(shouldBeCode(SC_OK)));
        api.user().deleteUser().usernamePath(data.getUsername())
                .execute(validatedWith(shouldBeCode(SC_OK)));
        api.user().getUserByName().usernamePath(data.getUsername())
                .execute(validatedWith(shouldBeCode(SC_NOT_FOUND)));
    }
}
