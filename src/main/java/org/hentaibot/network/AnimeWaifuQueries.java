package org.hentaibot.network;

import org.hentaibot.dtos.WaifuDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Random;

public interface AnimeWaifuQueries {

    Random random = new Random();
    //String[] categories = new String[]{"waifu", "neko", "shinobu", "megumin", "bully", "cuddle", "cry", "hug", "awoo", "kiss", "lick", "pat", "smug", "bonk", "yeet", "blush", "smile", "wave", "highfive", "handhold", "nom", "bite", "glomp", "slap", "kill", "kick", "happy", "wink", "poke", "dance", "cringe"};
    static String urlCategory = "waifu";
    static String urlForm = "/sfw/"; //+ urlCategory;
    @GET("/sfw/{category}")
    Call<WaifuDto> getSFW(

            @Path("category") String category

    );
}
