package com.littlepay.processors;

import com.littlepay.PanTrips;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TripCsvRowProcessorTest {
    @Test
    public void testprocessrow_valid() {
        PanTrips panTrips = new PanTrips();
        TripCsvRowProcessor tripCsvRowProcessor = new TripCsvRowProcessor(panTrips);
        String[] input = new String[] {"1","22-01-2018 13:00:00","ON","Stop1","Company1","Bus37","5500005555555559"};

        tripCsvRowProcessor.processRow(input);

        Assert.assertEquals(panTrips.getAllTrips().keySet().size(), 1);
    }

    @Test
    public void testprocessrow_exception() {
        PanTrips panTrips = new PanTrips();
        TripCsvRowProcessor tripCsvRowProcessor = new TripCsvRowProcessor(panTrips);
        // wrong date should throw exception
        String[] input = new String[] {"1","22-012018 13:00:00","ON","Stop1","Company1","Bus37","5500005555555559"};

        tripCsvRowProcessor.processRow(input);

        Assert.assertEquals(panTrips.getAllTrips().keySet().size(), 0);
    }
}
