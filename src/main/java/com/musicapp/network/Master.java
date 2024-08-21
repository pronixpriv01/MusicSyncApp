package com.musicapp.network;

import com.musicapp.util.AppConfig;
import java.util.logging.Logger;

public class Master {

    private static final Logger logger = Logger.getLogger(Master.class.getName());
    private AppConfig config;

    public Master(AppConfig config) {
        this.config = config;
    }

    public void start() {
        logger.info("Master wird gestartet");
        // Initialisiere Netwerkverbindung und starte die Steuerungslogik
        // Sende Steuerbefehle an Clients
    }

    public void stop() {
        logger.info("Maser wird gestoppt");
        // Stoppe die Steuerungslogik und schließe die Netzwerkanbindung
    }
}

