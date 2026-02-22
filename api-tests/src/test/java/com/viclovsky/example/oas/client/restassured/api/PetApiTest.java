package com.viclovsky.example.oas.client.restassured.api;

import com.google.inject.Inject;
import com.viclovsky.example.oas.client.restassured.ApiClient;
import com.viclovsky.example.oas.client.restassured.model.Pet;
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
 * CRUD tests for Pet API. One explicit test per CRUD operation.
 */
@ExtendWith(GuiceExtension.class)
@IncludeModule(ExampleApiModule.class)
class PetApiTest extends BasePetstoreTest {

    @Inject
    private ApiClient api;

    @Test
    void shouldCreatePet() {
        Pet created = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.petForCreate())
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .as(Pet.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(TestDataProvider.petForCreate().getName());
    }

    @Test
    void shouldGetPetById() {
        long id = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.petForRead())
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        Pet read = api.pet().getPetById().petIdPath(id)
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getId()).isEqualTo(id);
        assertThat(read.getName()).isEqualTo(TestDataProvider.petForRead().getName());
    }

    @Test
    void shouldUpdatePet() {
        Pet created = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.petForUpdate())
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .as(Pet.class);
        created.name("Updated Name").status(Pet.StatusEnum.SOLD);

        api.pet().updatePet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(created)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        Pet updated = api.pet().getPetById().petIdPath(created.getId())
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getStatus()).isEqualTo(Pet.StatusEnum.SOLD);
    }

    @Test
    void shouldDeletePet() {
        long id = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(TestDataProvider.petForDelete())
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        api.pet().deletePet().petIdPath(id)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        api.pet().getPetById().petIdPath(id)
                .execute(validatedWith(shouldBeCode(SC_NOT_FOUND)));
    }
}
