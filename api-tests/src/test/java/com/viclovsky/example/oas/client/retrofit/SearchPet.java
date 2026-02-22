package com.viclovsky.example.oas.client.retrofit;

import com.viclovsky.example.oas.client.PetstoreVerifier;
import com.viclovsky.example.oas.client.retrofit2.ApiClient;
import com.viclovsky.example.oas.client.retrofit2.api.PetApi;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchPet {

    private ApiClient api;

    @BeforeClass
    public static void verifyPetstoreUp() {
        PetstoreVerifier.verifyReachable();
    }

    @Before
    public void createApi() {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        api = new ApiClient();
        String baseUri = System.getProperty("api.baseUri", "http://127.0.0.1:80/v2");
        if (!baseUri.endsWith("/")) baseUri += "/";
        Retrofit.Builder builder = new ApiClient().getAdapterBuilder()
                .baseUrl(baseUri);
        api.setAdapterBuilder(builder);
        api.configureFromOkclient(client);
    }


    @Test
    @Ignore("Only for demonstration")
    public void shouldSearchPet() throws IOException {
        Response res = api.createService(PetApi.class)
                .getPetSearch("ok", null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null, null, null,
                        null, null).execute();
        assertThat(res.code(), equalTo(SC_OK));
    }
}
