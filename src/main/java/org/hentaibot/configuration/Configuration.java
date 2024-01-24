package org.hentaibot.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Configuration {
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    private static final Properties properties = new Properties();
    static {
        try {
        InputStream in = Configuration.class.getClassLoader()
                .getResourceAsStream("application.properties");
            properties.load(in);
        } catch (Exception e) {
            logger.error("Failed to load properties file");
        }
    }

    public static String getBotName() {
        String name = System.getenv("BOT_NAME");
        if (name != null) return name;

        name = properties.getProperty("BOT_NAME");
        return name;
    }

    public static String getBotToken() {
        String token = System.getenv("BOT_TOKEN");
        if (token != null) return token;

        token = properties.getProperty("BOT_TOKEN");
        return token;
    }
}
