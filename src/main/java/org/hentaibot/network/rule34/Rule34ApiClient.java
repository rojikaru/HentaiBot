package org.hentaibot.network.rule34;

import org.hentaibot.configuration.Configuration;
import org.hentaibot.network.basic.ApiClient;

public class Rule34ApiClient {
    private static ApiClient NSFW_CLIENT = null;

    public static HentaiQueries getNsfwClient() {
        if (NSFW_CLIENT == null) {
            NSFW_CLIENT = new ApiClient(Configuration.getProperty("RULE34_API"));
        }
        return NSFW_CLIENT.getQueriesClient(HentaiQueries.class);
    }
}
