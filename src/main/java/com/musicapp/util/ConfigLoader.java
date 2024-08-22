package com.musicapp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lädt Konfigurationsdaten aus einer .properties-Datei und initialisiert AppConfig.
 */
public class ConfigLoader {

    /**
     * Lädt die Konfiguration aus einer .properties-Datei.
     *
     * @param filePath Pfad zur .properties-Datei.
     * @return Die initialisierte AppConfig-Instanz.
     * @throws IOException Wenn die Datei nicht gelesen werden kann.
     */
    public static AppConfig loadFromProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        }

        AppConfig config = new AppConfig();
        config.setStartParameter(properties.getProperty("startParameter", "default"));
        config.setMaxConnections(Integer.parseInt(properties.getProperty("maxConnections", "10")));
        config.setEnableLogging(Boolean.parseBoolean(properties.getProperty("enableLogging", "true")));
        config.setMaster(Boolean.parseBoolean(properties.getProperty("isMaster", "false")));
        config.setServerUri(properties.getProperty("serverUri", "ws://localhost:8887"));
        config.setPort(Integer.parseInt(properties.getProperty("port", "8887")));

        return config;
    }
}
