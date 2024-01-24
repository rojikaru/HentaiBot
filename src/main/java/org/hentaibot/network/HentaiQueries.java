package org.hentaibot.network;


import org.hentaibot.dtos.Rule34Dto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface HentaiQueries {
    @GET("/index.php?page=dapi&s=post&q=index&limit=1&json=1")
    Call<List<Rule34Dto>> getNsfw(
            @Query("tags") String tags,
            @Query("pid") int pageId
            );
}
