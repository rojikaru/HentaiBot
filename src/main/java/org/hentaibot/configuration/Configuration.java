package org.hentaibot.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("CallToPrintStackTrace")
public class Configuration {
    private static final Properties properties = new Properties();
    static {
        InputStream in = Configuration.class.getClassLoader()
                .getResourceAsStream("application.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            System.err.println("Failed to load properties file");
            e.printStackTrace();
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
