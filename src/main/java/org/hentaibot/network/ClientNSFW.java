package org.hentaibot.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientNSFW {

    private static Retrofit clientNWaifu = null;

    private  static final String UrlNclient = "https://api.waifu.pics";

    public HentaiWaifuQueries getNSFWClient()
    {
        if(clientNWaifu == null)
        {
            clientNWaifu = new Retrofit.Builder().baseUrl(UrlNclient).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return clientNWaifu.create(HentaiWaifuQueries.class);
    }
}
