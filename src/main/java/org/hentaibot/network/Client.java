package org.hentaibot.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit rule34Client = null;
    private static final String rule34Url = "https://api.rule34.xxx";

    public static HentaiQueries getNsfwClient() {
        if (rule34Client == null) {
            rule34Client = new Retrofit.Builder()
                    .baseUrl(rule34Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return rule34Client.create(HentaiQueries.class);
    }
}
