package com.littlepay;

import com.littlepay.exceptions.RuntimeParsingException;
import com.littlepay.model.trips.BusId;
import com.littlepay.model.trips.CompanyId;
import com.littlepay.model.trips.TapType;
import com.littlepay.model.trips.Trip;
import com.littlepay.processors.FaresCsvRowProcessor;
import com.littlepay.processors.TripCsvRowProcessor;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SimpleCSVParserTest {

    private final String tripsFilePath = "input.csv";
    private final String faresFilePath = "fares.csv";

    private SimpleCSVParser simpleCSVParser;
    private TripCsvRowProcessor tripCSVRowProcessor;
    private FaresCsvRowProcessor faresCsvRowProcessor;
    private PanTrips panTrips;
    private Fares fares;

    @BeforeTest
    public void init() {
        panTrips = new PanTrips();
        tripCSVRowProcessor = new TripCsvRowProcessor(panTrips);
        fares = new Fares();
        faresCsvRowProcessor = new FaresCsvRowProcessor(fares);

        simpleCSVParser = new SimpleCSVParser(panTrips, fares, faresFilePath, tripCSVRowProcessor, faresCsvRowProcessor);
    }

    private List<String[]> getAllLinesFromCSV(File file) {
        try (FileReader fileReader = new FileReader(file);
             CSVReader csvReader = new CSVReaderBuilder(fileReader)
                     .withSkipLines(1)
                     .build()) {
            return csvReader.readAll();
        } catch (IOException e) {
            throw new RuntimeParsingException(e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeParsingException(e.getMessage());
        } catch (CsvException e) {
            throw new RuntimeParsingException(e.getMessage());
        }
    }

    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    @Test
    public void test_parsetripsscsv() {
        //ClassLoader classLoader = SimpleCSVParserTest.class.getClassLoader();
        //File inputFile = new File(classLoader.getResource(tripsFilePath).getFile());
        InputStream inputStream = getFileFromResourceAsStream(tripsFilePath);
        simpleCSVParser.parseTripsCSV(inputStream);

        File file = new File(SimpleCSVParser.OUTPUT_FILE);

        Assert.assertTrue(file.exists());

        List<String[]> allLines = getAllLinesFromCSV(file);
        Assert.assertTrue(allLines.get(0)[9].trim().equals("COMPLETED"));
    }

    @Test
    public void test_parsefarescsv() {

        //ClassLoader classLoader = SimpleCSVParserTest.class.getClassLoader();
        //File inputFile = new File(classLoader.getResource(faresFilePath).getFile());
        InputStream inputStream = getFileFromResourceAsStream(faresFilePath);

        simpleCSVParser.parseFaresCSV(inputStream);

        Assert.assertEquals(fares.getRouteAdjacencyMap().keySet().size(), 5);
    }

    @Test
    public void test_checkiftripcompleted() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Trip trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop1")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        Trip trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(LocalDateTime.parse("22-01-2018 18:05:00", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop4")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        Trip trip3 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop3")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B50"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        Trip trip4 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(4))
                .tripDate(LocalDateTime.parse("22-01-2018 14:05:00", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop3")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B50"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        Trip trip5 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(5))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop4")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B50"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        Trip trip6 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(6))
                .tripDate(LocalDateTime.parse("22-01-2018 16:05:00", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop4")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B50"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        boolean tripCompleted = simpleCSVParser.checkIfTripCompleted(trip1, trip2);
        Assert.assertTrue(tripCompleted);

        tripCompleted = simpleCSVParser.checkIfTripCompleted(trip3, trip4);
        Assert.assertFalse(tripCompleted);

        // case where gap between tap on and off is more than 2 hours at the same stop
        tripCompleted = simpleCSVParser.checkIfTripCompleted(trip5, trip6);
        Assert.assertTrue(tripCompleted);
    }

    @Test
    public void test_checkiftripcancelled() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Trip trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop1")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        Trip trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(LocalDateTime.parse("22-01-2018 18:05:00", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop4")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        Trip trip3 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop3")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B50"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        Trip trip4 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(4))
                .tripDate(LocalDateTime.parse("22-01-2018 14:05:00", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop3")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B50"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        boolean tripCancelled = simpleCSVParser.checkIfTripCancelled(trip1, trip2);
        Assert.assertFalse(tripCancelled);

        tripCancelled = simpleCSVParser.checkIfTripCancelled(trip3, trip4);
        Assert.assertTrue(tripCancelled);
    }

    @Test
    public void test_aretripswithin12hours() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Trip trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .build();

        Trip trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(LocalDateTime.parse("23-01-2018 01:06:00", dateTimeFormatter))
                .build();

        Trip trip3 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .build();

        Trip trip4 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(4))
                .tripDate(LocalDateTime.parse("23-01-2018 14:05:00", dateTimeFormatter))
                .build();

        boolean tripCancelled = simpleCSVParser.areTripsWithin12Hours(trip1, trip2);
        Assert.assertTrue(tripCancelled);

        tripCancelled = simpleCSVParser.areTripsWithin12Hours(trip3, trip4);
        Assert.assertFalse(tripCancelled);
    }

    @Test
    public void test_aretripswithin2hours() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Trip trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .build();

        Trip trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(LocalDateTime.parse("22-01-2018 15:04:59", dateTimeFormatter))
                .build();

        Trip trip3 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .build();

        Trip trip4 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(4))
                .tripDate(LocalDateTime.parse("23-01-2018 14:05:00", dateTimeFormatter))
                .build();

        boolean tripCancelled = simpleCSVParser.areTripsWithin2Hours(trip1, trip2);
        Assert.assertTrue(tripCancelled);

        tripCancelled = simpleCSVParser.areTripsWithin2Hours(trip3, trip4);
        Assert.assertFalse(tripCancelled);
    }

    @Test
    public void test_doestripsvarybytaptype() {
        Trip trip1 = new Trip.TripBuilder()
                .tapType(TapType.ON)
                .build();

        Trip trip2 = new Trip.TripBuilder()
                .tapType(TapType.OFF)
                .build();

        Trip trip3 = new Trip.TripBuilder()
                .tapType(TapType.ON)
                .build();

        Trip trip4 = new Trip.TripBuilder()
                .tapType(TapType.ON)
                .build();

        boolean tripCancelled = simpleCSVParser.doesTripsVaryByTapType(trip1, trip2);
        Assert.assertTrue(tripCancelled);

        tripCancelled = simpleCSVParser.doesTripsVaryByTapType(trip3, trip4);
        Assert.assertFalse(tripCancelled);
    }

    @Test
    public void test_getcompletedoutputrow() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        Trip trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop1")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        Trip trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(LocalDateTime.parse("22-01-2018 18:05:00", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop4")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        String[] outputRow = simpleCSVParser.getCompletedOutputRow(trip1, trip2);
        Assert.assertEquals(outputRow[9].trim(), "COMPLETED");
    }
}
