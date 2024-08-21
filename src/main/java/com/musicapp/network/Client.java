package com.musicapp.network;

import com.musicapp.util.AppConfig;
import java.util.logging.Logger;

public class Client {

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private AppConfig config;

    public Client(AppConfig config) {
        this.config = config;
    }

    public void connectToMaster() {
        logger.info("Client verbindet sich mit dem Master.");
        // Verbindungsaufbau zum Master
    }

    public void start() {
        logger.info("Client startet die Wiedergabe.");
        // Empfange Steuerbefehle und synchronisiere die Musikwiedergabe
    }

    public void stop() {
        logger.info("Client wird gestoppt.");
        // Stoppe die Wiedergabe und trenne die Verbindung zum Master
    }

    public void disconnect() {
        logger.info("Client trennt die Verbindung zum Master.");
        // Trenne die Verbindung zum Master
    }
}
