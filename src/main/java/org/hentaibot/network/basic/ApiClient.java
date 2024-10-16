package org.hentaibot.network.basic;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private final Retrofit retrofit;

    public ApiClient(String baseUrl) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T getQueriesClient(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
