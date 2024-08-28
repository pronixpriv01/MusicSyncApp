package com.musicapp.network;

import com.musicapp.network.Client;
import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * Die NetworkFactory-Klasse bietet Methoden zum Erstellen von Master- und Client-Instanzen.
 */
public class NetworkFactory {

    private static final Logger logger = LoggerFactory.getLogger(NetworkFactory.class);

    /**
     * Erstellt eine Master-Instanz basierend auf der AppConfig.
     *
     * @param config Die AppConfig-Instanz mit den Konfigurationseinstellungen.
     * @return Eine neue Master-Instanz.
     */
    public static Master createMaster(AppConfig config) {
        return new Master(config, config.getPort());
    }

    /**
     * Erstellt eine Client-Instanz basierend auf der AppConfig.
     *
     * @param config Die AppConfig-Instanz mit den Konfigurationseinstellungen.
     * @return Eine neue Client-Instanz.
     */
    public static Client createClient(AppConfig config) {
        try {
            return new Client(config, config.getServerUri());
        } catch (URISyntaxException e) {
            logger.error("Ungültige Server-URI", e);
            throw new RuntimeException("Fehler beim Erstellen des Clients aufgrund einer ungültigen URI", e);
        }
    }
}
