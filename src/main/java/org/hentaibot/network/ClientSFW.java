package org.hentaibot.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientSFW {

    private static Retrofit clientWaifu = null;

    private static final String UrlClient = "https://api.waifu.pics/";

    public static AnimeWaifuQueries getSfwClient()
    {
        if (clientWaifu == null)
        {
            clientWaifu = new Retrofit.Builder().baseUrl(UrlClient).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return clientWaifu.create(AnimeWaifuQueries.class);
    }
}
