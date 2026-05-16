package kelompok11.turnbaserpg.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Pongo
 */
public class GameLogger {

    private static final DateTimeFormatter FORMAT
            = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static String getTime() {

        return LocalTime.now().format(FORMAT);
    }

    private static void writeLog(
            String level,
            String text
    ) {

        String log
                = "[" + level + "][" + getTime() + "] " + text;

//        System.out.println(log);

        try {

            FileWriter fw
                    = new FileWriter("logs/game.log", true);

            PrintWriter pw = new PrintWriter(fw);

            pw.println(log);

            pw.close();

        } catch (IOException e) {

            System.out.println("Gagal menulis log");
            System.out.println(e.getMessage());
        }
    }

    public static void info(String text) {

        writeLog("INFO", text);
    }

    public static void warning(String text) {

        writeLog("WARNING", text);
    }

    public static void error(String text) {

        writeLog("ERROR", text);
    }

    public static void debug(String text) {

        writeLog("DEBUG", text);
    }

}
