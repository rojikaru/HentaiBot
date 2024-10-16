package org.hentaibot.configuration;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
    private static final Logger logger = Logger
            .getLogger(Configuration.class.getName());

    private static final Properties properties = new Properties();

    static {
        try {
            InputStream in = Configuration.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");
            properties.load(in);
        } catch (Exception e) {
            logger.error("Failed to load properties file");
        }
    }

    public static String getProperty(String key) {
        String prop = System.getenv(key);
        if (prop != null) return prop;
        return properties.getProperty(key);
    }
}
