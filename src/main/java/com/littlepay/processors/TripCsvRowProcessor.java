package com.littlepay.processors;

import com.littlepay.PanTrips;
import com.littlepay.exceptions.RuntimeParsingException;
import com.littlepay.model.trips.BusId;
import com.littlepay.model.trips.CompanyId;
import com.littlepay.model.trips.TapType;
import com.littlepay.model.trips.Trip;
import com.opencsv.processor.RowProcessor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class TripCsvRowProcessor implements RowProcessor {

    Logger log = Logger.getLogger(TripCsvRowProcessor.class.getName());

    private PanTrips panTrips;

    public TripCsvRowProcessor(PanTrips panTrips) {
        this.panTrips = panTrips;
    }

    @Override
    public String processColumnItem(String s) {
        return null;
    }

    @Override
    public void processRow(String[] strings) {
        if(strings.length == 7) {
            try {
                Trip trip = new Trip.TripBuilder()
                        .tripId(new BigInteger(strings[0].trim()))
                        .tripDate(parseDateTime(strings[1].trim()))
                        .tapType(TapType.getIfPresent(strings[2].trim()))
                        .stopId(strings[3].trim())
                        .companyId(new CompanyId(strings[4].trim()))
                        .busId(new BusId(strings[5].trim()))
                        .pan(new BigInteger(strings[6].trim()))
                        .build();

                if (trip.getTapType() != null) {
                    panTrips.addTrip(trip);
                }

            } catch (Exception ex) {
                log.warning("Problem reading row with trip id: " + strings[0] + ", skipping it..");
            }
        }
    }

    private LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new RuntimeParsingException(ex.getMessage());
        }
    }
}
