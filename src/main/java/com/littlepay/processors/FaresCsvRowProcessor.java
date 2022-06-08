package com.littlepay.processors;

import com.littlepay.Fares;
import com.opencsv.processor.RowProcessor;

import java.util.logging.Logger;

public class FaresCsvRowProcessor implements RowProcessor {
    Logger log = Logger.getLogger(TripCsvRowProcessor.class.getName());

    private Fares fares;

    public FaresCsvRowProcessor(Fares fares) {
        this.fares = fares;
    }

    @Override
    public String processColumnItem(String s) {
        return null;
    }

    @Override
    public void processRow(String[] strings) {
        if (strings.length == 3) {
            try {
                String source = strings[0].trim();
                String destination = strings[1].trim();
                double fare = Double.valueOf(strings[2].trim());

                fares.addRoute(source, destination, fare);
            } catch (Exception ex) {
                log.warning("Problem reading row from fares file, continuing with next row");
            }
        }
    }
}
