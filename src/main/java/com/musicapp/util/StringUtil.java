package com.musicapp.util;

import com.musicapp.core.MusicApp;

import java.util.StringJoiner;

/**
 * Utility-Klasse für String-bezogene Operationen.
 * Diese Klasse bietet Methoden, um eine textuelle Darstellung
 * von Objekten zu generieren, insbesondere für die MusicApp-Anwendung.
 */
public class StringUtil {

    /**
     * Erzeugt eine textuelle Darstellung eines Objekts, basierend auf dessen Eigenschaften.
     * Unterstützt die Klassen MusicApp und AppConfig.
     *
     * @param obj Das Objekt, das als String dargestellt werden soll.
     * @return Eine String-Darstellung des Objekts.
     */
    public static String toString(Object obj) {
        if (obj instanceof MusicApp) {
            MusicApp app = (MusicApp) obj;
            return new StringJoiner(", ", MusicApp.class.getSimpleName() + "[", "]")
                    .add("config=" + app.getConfig().toString())
                    .toString(); // Hier wird der StringJoiner in einen String umgewandelt
        } else if (obj instanceof AppConfig) {
            AppConfig config = (AppConfig) obj;
            return new StringJoiner(", ", AppConfig.class.getSimpleName() + "[", "]")
                    .add("startParameter='" + config.getStartParameter() + "'")
                    .add("maxConnections=" + config.getMaxConnections())
                    .add("enableLogging=" + config.isEnableLogging())
                    .toString(); // Hier wird der StringJoiner in einen String umgewandelt
        }
        return obj.toString();
    }
}
