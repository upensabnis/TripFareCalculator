package com.littlepay;

import com.littlepay.exceptions.RuntimeParsingException;
import com.littlepay.model.trips.OutputStatus;
import com.littlepay.model.trips.OutputTrips;
import com.littlepay.model.trips.TapType;
import com.littlepay.model.trips.Trip;
import com.littlepay.processors.FaresCsvRowProcessor;
import com.littlepay.processors.TripCsvRowProcessor;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;

public class SimpleCSVParser {

    Logger log = Logger.getLogger(SimpleCSVParser.class.getName());

    public static final String OUTPUT_FILE = "trips.csv";

    private TripCsvRowProcessor tripCSVRowProcessor;
    private FaresCsvRowProcessor faresCsvRowProcessor;
    private PanTrips panTrips;
    private Fares fares;
    private String faresFilePath;

    // can be used to run application from command line
    public SimpleCSVParser(String faresFilePath) {
        this.panTrips = new PanTrips();
        this.tripCSVRowProcessor = new TripCsvRowProcessor(panTrips);
        this.fares = new Fares();
        this.faresFilePath = faresFilePath;
        this.faresCsvRowProcessor = new FaresCsvRowProcessor(fares);
        InputStream is = getFileFromResourceAsStream(faresFilePath);
        parseFaresCSV(is);
    }

    // used by unit tests
    public SimpleCSVParser(PanTrips panTrips, Fares fares, String faresFilePath,
                           TripCsvRowProcessor tripCsvRowProcessor, FaresCsvRowProcessor faresCsvRowProcessor) {
        this.panTrips = panTrips;
        this.tripCSVRowProcessor = tripCsvRowProcessor;
        this.fares = fares;
        this.faresFilePath = faresFilePath;
        this.faresCsvRowProcessor = faresCsvRowProcessor;
        InputStream is = getFileFromResourceAsStream(faresFilePath);
        parseFaresCSV(is);
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public void parseFaresCSV(InputStream inputStream) {
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVReader faresCsvReader = new CSVReaderBuilder(streamReader)
                     .withSkipLines(1)
                     .withRowProcessor(faresCsvRowProcessor)
                     .build()) {
            while ((faresCsvReader.readNext()) != null);
        } catch (IOException e) {
            throw new RuntimeParsingException(e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeParsingException(e.getMessage());
        }
    }

    public void parseTripsCSV(InputStream inputStream) {
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(streamReader)
                     .withSkipLines(1)
                     .withRowProcessor(tripCSVRowProcessor)
                     .build()) {
            while ((csvReader.readNext()) != null) ;
            buildTripsOutput(panTrips.getAllTrips());
        } catch (IOException e) {
            throw new RuntimeParsingException(e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeParsingException(e.getMessage());
        }
    }

    public void buildTripsOutput(HashMap<BigInteger, HashSet<Trip>> allTrips) throws IOException {

        FileWriter fileWriter = new FileWriter(OUTPUT_FILE);

        try (CSVWriter writer = new CSVWriter(fileWriter, ',',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

            writer.writeNext(getOutputFileHeader());

            for (Map.Entry mapElement : allTrips.entrySet()) {
                Set<Trip> trips = (Set<Trip>) mapElement.getValue();
                ArrayList<Trip> tripList = new ArrayList<>(trips);

                // sort all trips of pan by time
                Collections.sort(tripList, Comparator.comparing(Trip::getTripDateTime));
                ArrayList<Trip> tripListCopy = (ArrayList<Trip>) tripList.clone();

                for (int i = 0; i < tripList.size() - 1; i++) {
                    if (!tripListCopy.contains(tripList.get(i))) {
                        continue;
                    }
                    // check if consecutive tap ON's - if present means tap OFF for current trip is missing
                    if (TapType.ON.equals(tripList.get(i).getTapType()) && TapType.ON.equals(tripList.get(i + 1).getTapType())) {
                        writer.writeNext(getIncompleteOutputRow(tripList.get(i)));
                        tripListCopy.remove(tripList.get(i));
                        continue;
                    }
                    for (int j = i + 1; j < tripList.size(); j++) {
                        if (!tripListCopy.contains(tripList.get(j))) {
                            continue;
                        }
                        // check if trip completed
                        boolean ifTripCompleted = checkIfTripCompleted(tripList.get(i), tripList.get(j));
                        if (ifTripCompleted) {
                            writer.writeNext(getCompletedOutputRow(tripList.get(i), tripList.get(j)));
                            tripListCopy.remove(tripList.get(i));
                            tripListCopy.remove(tripList.get(j));

                        } else {
                            // check if trip cancelled
                            boolean ifTripCancelled = checkIfTripCancelled(tripList.get(i), tripList.get(j));
                            if (ifTripCancelled) {
                                writer.writeNext(getCancelledOutputRow(tripList.get(i), tripList.get(j)));
                                tripListCopy.remove(tripList.get(i));
                                tripListCopy.remove(tripList.get(j));
                            }
                        }
                    }
                }

                // remaining trips for which match was not found will be marked as INCOMPLETE
                tripListCopy.stream().forEach(trip -> {
                    writer.writeNext(getIncompleteOutputRow(trip));
                });
            }
        }
    }

    public boolean checkIfTripCompleted(Trip trip1, Trip trip2) {

        // if the trip happened on same day then check if tap off happened in next 12 hours
        if (areTripsWithin12Hours(trip1, trip2)) {
            if (doesTripsVaryByTapType(trip1, trip2)) {
                if (!trip1.getStopId().equals(trip2.getStopId())) {
                    if (trip1.getCompanyId().equals(trip2.getCompanyId())) {
                        if (trip1.getBusId().equals(trip2.getBusId())) {
                            if (trip1.getPan().equals(trip2.getPan())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if (areTripsBetween2And12HoursWithSameStop(trip1, trip2)) {
            if (doesTripsVaryByTapType(trip1, trip2)) {
                if (trip1.getCompanyId().equals(trip2.getCompanyId())) {
                    if (trip1.getBusId().equals(trip2.getBusId())) {
                        if (trip1.getPan().equals(trip2.getPan())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkIfTripCancelled(Trip trip1, Trip trip2) {
        // if the trip happened on same day then check if tap off done within 2 hours
        if (areTripsWithin2Hours(trip1, trip2)) {
            if (doesTripsVaryByTapType(trip1, trip2)) {
                if (trip1.getStopId().equals(trip2.getStopId())) {
                    if (trip1.getCompanyId().equals(trip2.getCompanyId())) {
                        if (trip1.getBusId().equals(trip2.getBusId())) {
                            if (trip1.getPan().equals(trip2.getPan())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean areTripsWithin12Hours(Trip trip1, Trip trip2) {
        LocalDateTime onTripDate = trip1.getTripDateTime();
        LocalDateTime offTripDate = trip2.getTripDateTime();
        long hours = ChronoUnit.HOURS.between(onTripDate, offTripDate);
        return hours <= 12 ? true : false;
    }

    public boolean areTripsBetween2And12HoursWithSameStop(Trip trip1, Trip trip2) {
        LocalDateTime onTripDate = trip1.getTripDateTime();
        LocalDateTime offTripDate = trip2.getTripDateTime();
        long hours = ChronoUnit.HOURS.between(onTripDate, offTripDate);

        // case when trip is not cancelled but tap ON and OFF happened at the same stop
        if((hours > 2 && hours < 12) && trip1.getStopId().equals(trip2.getStopId())) {
            return true;
        }
        return false;
    }

    public boolean areTripsWithin2Hours(Trip trip1, Trip trip2) {
        LocalDateTime onTripDate = trip1.getTripDateTime();
        LocalDateTime offTripDate = trip2.getTripDateTime();
        long hours = ChronoUnit.HOURS.between(onTripDate, offTripDate);
        return hours <= 2 ? true : false;
    }

    public boolean doesTripsVaryByTapType(Trip trip1, Trip trip2) {
        if ((trip1.getTapType().equals(TapType.ON) && trip2.getTapType().equals(TapType.OFF)) ||
                (trip1.getTapType().equals(TapType.OFF) && trip2.getTapType().equals(TapType.ON))) {
            return true;
        }
        return false;
    }

    public String[] getOutputFileHeader() {
        OutputTrips outputTrips = new OutputTrips.OutputTripsBuilder().build();
        return outputTrips.getOutputHeaderRow();
    }

    public String[] getIncompleteOutputRow(Trip trip) {
        OutputTrips.OutputTripsBuilder builder = new OutputTrips.OutputTripsBuilder()
                .started(trip.getTripDateTime())
                .fromStopId(trip.getStopId())
                .chargeAmount(fares.getMaximumFareFromStop(trip.getStopId()))
                .companyId(trip.getCompanyId().getId())
                .busId(trip.getBusId().getId())
                .pan(trip.getPan())
                .outputStatus(OutputStatus.INCOMPLETE);

        OutputTrips trips = builder.build();

        return trips.getStringArrayOfValues();
    }

    public String[] getCompletedOutputRow(Trip trip1, Trip trip2) {
        OutputTrips.OutputTripsBuilder builder = new OutputTrips.OutputTripsBuilder()
                .started(trip1.getTripDateTime())
                .finished(trip2.getTripDateTime())
                .durationSecs(ChronoUnit.SECONDS.between(trip1.getTripDateTime(), trip2.getTripDateTime()))
                .fromStopId(trip1.getStopId())
                .toStopId(trip2.getStopId())
                .companyId(trip1.getCompanyId().getId())
                .busId(trip1.getBusId().getId())
                .pan(trip1.getPan())
                .outputStatus(OutputStatus.COMPLETED);

        if (trip1.getStopId().equals(trip2.getStopId())) {
            builder.chargeAmount(fares.getMinimumFareFromStop(trip1.getStopId()));
        } else {
            builder.chargeAmount(fares.getPriceBetweenStops(trip1.getStopId(), trip2.getStopId()));
        }

        OutputTrips trips = builder.build();

        return trips.getStringArrayOfValues();
    }

    public String[] getCancelledOutputRow(Trip trip1, Trip trip2) {
        OutputTrips.OutputTripsBuilder builder = new OutputTrips.OutputTripsBuilder()
                .started(trip1.getTripDateTime())
                .finished(trip2.getTripDateTime())
                .durationSecs(ChronoUnit.SECONDS.between(trip1.getTripDateTime(), trip2.getTripDateTime()))
                .fromStopId(trip1.getStopId())
                .toStopId(trip2.getStopId())
                .chargeAmount(fares.getPriceBetweenStops(trip1.getStopId(), trip2.getStopId()))
                .companyId(trip1.getCompanyId().getId())
                .busId(trip1.getBusId().getId())
                .pan(trip1.getPan())
                .outputStatus(OutputStatus.CANCELLED);

        OutputTrips trips = builder.build();

        return trips.getStringArrayOfValues();
    }
}
