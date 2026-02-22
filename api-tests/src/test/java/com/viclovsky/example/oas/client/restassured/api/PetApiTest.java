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

import java.util.Collections;

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
        Pet body = new Pet()
                .name("CRUD Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/photo.jpg"));
        Pet created = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .as(Pet.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("CRUD Test Pet");
    }

    @Test
    void shouldGetPetById() {
        Pet body = new Pet()
                .name("Read Test Pet")
                .status(Pet.StatusEnum.PENDING)
                .photoUrls(Collections.singletonList("http://example.com/read.jpg"));
        long id = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        Pet read = api.pet().getPetById().petIdPath(id)
                .executeAs(validatedWith(shouldBeCode(SC_OK)));
        assertThat(read.getId()).isEqualTo(id);
        assertThat(read.getName()).isEqualTo("Read Test Pet");
    }

    @Test
    void shouldUpdatePet() {
        Pet body = new Pet()
                .name("Update Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/update.jpg"));
        Pet created = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
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
        Pet body = new Pet()
                .name("Delete Test Pet")
                .status(Pet.StatusEnum.AVAILABLE)
                .photoUrls(Collections.singletonList("http://example.com/delete.jpg"));
        long id = api.pet().addPet().reqSpec(r -> r.setContentType(ContentType.JSON)).body(body)
                .execute(validatedWith(shouldBeCode(SC_OK)))
                .jsonPath().getLong("id");

        api.pet().deletePet().petIdPath(id)
                .execute(validatedWith(shouldBeCode(SC_OK)));

        api.pet().getPetById().petIdPath(id)
                .execute(validatedWith(shouldBeCode(SC_NOT_FOUND)));
    }
}
