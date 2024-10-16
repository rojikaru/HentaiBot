package org.hentaibot.network.waifu;

import org.hentaibot.dtos.WaifuDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AnimeQueries {
    @GET("/sfw/{category}")
    Call<WaifuDto> getSFW(
            @Path("category") String category
    );
}
