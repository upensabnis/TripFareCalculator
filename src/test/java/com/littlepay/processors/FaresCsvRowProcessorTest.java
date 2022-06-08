package com.littlepay.processors;

import com.littlepay.Fares;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FaresCsvRowProcessorTest {

    @Test
    public void testprocessrow_valid() {
        Fares fares = new Fares();
        FaresCsvRowProcessor faresCsvRowProcessor = new FaresCsvRowProcessor(fares);
        String[] input = new String[] {"stop1","stop2","4.0"};

        faresCsvRowProcessor.processRow(input);

        Assert.assertEquals(fares.getAllRoutesFromStop("stop1").size(), 1);
    }

    @Test
    public void testprocessrow_exception() {
        Fares fares = new Fares();
        FaresCsvRowProcessor faresCsvRowProcessor = new FaresCsvRowProcessor(fares);
        // third string is expected to be double so this should throw exception
        String[] input = new String[] {"stop1","stop2","abc"};

        faresCsvRowProcessor.processRow(input);

        Assert.assertEquals(fares.getAllRoutesFromStop("stop1").size(), 0);
    }
}
