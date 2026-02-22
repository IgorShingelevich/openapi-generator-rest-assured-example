package com.viclovsky.example.oas.client.retrofit;

import com.viclovsky.example.oas.client.PetstoreVerifier;
import com.viclovsky.example.oas.client.retrofit2.ApiClient;
import com.viclovsky.example.oas.client.retrofit2.api.StoreApi;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class GetInventoryTest {

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
        api.setAdapterBuilder(builder).configureFromOkclient(client);
    }

    @Test
    public void shouldGetInventoryTest() throws IOException {
        Response<Map<String, Integer>> inventory =
                api.createService(StoreApi.class).getInventory().execute();
        assertThat(inventory.code(), equalTo(SC_OK));
        assertThat(inventory.body().keySet().size(), greaterThan(0));
    }
}
