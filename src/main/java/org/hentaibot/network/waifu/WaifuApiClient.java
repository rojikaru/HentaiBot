package org.hentaibot.network.waifu;

import org.hentaibot.configuration.Configuration;
import org.hentaibot.network.basic.ApiClient;

public class WaifuApiClient {
    private static ApiClient CLIENT = null;

    private static <T> T getQueriesClient(Class<T> clazz) {
        if (CLIENT == null) {
            CLIENT = new ApiClient(Configuration.getProperty("WAIFU_API"));
        }
        return CLIENT.getQueriesClient(clazz);
    }

    public static HentaiQueries getNsfwClient() {
        return getQueriesClient(HentaiQueries.class);
    }

    public static AnimeQueries getSfwClient() {
        return getQueriesClient(AnimeQueries.class);
    }
}
