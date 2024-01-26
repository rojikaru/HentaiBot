package org.hentaibot.network;

import org.hentaibot.dtos.WaifuDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface HentaiWaifuQueries {

    @GET("/nsfw/")
    Call<List<WaifuDto>> getNSFW(
            @Query("category") String category
    );




}
