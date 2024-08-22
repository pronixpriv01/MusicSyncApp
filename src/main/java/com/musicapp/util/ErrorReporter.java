package com.musicapp.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Sammelt Fehlerberichte und speichert sie lokal.
 */
public class ErrorReporter {

    /**
     * Speichert einen Fehlerbericht lokal.
     *
     * @param e Die Ausnahme, die berichtet werden soll.
     */
    public void report(Exception e) {
        String report = generateReport(e);
        try (FileWriter writer = new FileWriter("error_report_" + LocalDateTime.now() + ".log")) {
            writer.write(report);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Generiert einen detallierten Fehlerbericht.
     *
     * @param e Die Ausnahme, für die der Bericht erstellt wird.
     * @return Der generierte Fehlerbericht.
     */
    public String generateReport(Exception e) {
        StringBuilder report = new StringBuilder();
        report.append("Fehlerbericht - ").append(LocalDateTime.now()).append("\n");
        report.append("Fehlertyp: ").append(e.getClass().getName()).append("\n");
        report.append("Fehlermeldung: ").append(e.getMessage()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            report.append("\tat ").append(element.toString()).append("\n");
        }
        return report.toString();
    }
}
