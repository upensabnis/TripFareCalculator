package com.littlepay;

import java.io.InputStream;
import java.util.logging.Logger;

public class Main {

    private static final String FARES_FILE_PATH = "fares.csv";
    private static final String TRIPS_INPUT_FILE_PATH = "input.csv";

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public static void main(String[] args) {
        Logger log = Logger.getLogger(Main.class.getName());
        Main main = new Main();
        try {
            SimpleCSVParser simpleCSVParser = new SimpleCSVParser(FARES_FILE_PATH);
            InputStream inputStream = main.getFileFromResourceAsStream(TRIPS_INPUT_FILE_PATH);
            simpleCSVParser.parseTripsCSV(inputStream);
        } catch(Exception ex) {
            log.warning("Exception in main: " + ex.getMessage());
        }
    }
}
