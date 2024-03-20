package com.example.realestatemanager;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsTest {
    /**
     * Tests the currency conversion functionality from Euro to Dollar.
     * This test checks if the conversion method correctly converts 100 Euros to its Dollar equivalent.
     * The expected result is hardcoded as 123, assuming the conversion rate is 1 Euro = 1.23 Dollars.
     */
    @Test
    public void convertEuroToDollar_isCorrect() {
        // Calls the method being tested with 100 Euros as input, and checks if it returns 123,
        // indicating the method works as expected for this hardcoded conversion rate.
        assertEquals(123, Utils.convertEuroToDollar(100));
    }

    /**
     * Tests the functionality to get today's date in a specific format.
     * This test checks if the method correctly returns the current date formatted as "dd/MM/yyyy".
     * The expected result is dynamically generated to match the current date, ensuring the test is valid anytime it's run.
     */
    @Test
    public void getTodayDate_isCorrect() {
        // Creates an expected date string in "dd/MM/yyyy" format based on the current date.
        String expectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // Calls the method being tested and checks if it returns a string matching the expected date,
        // indicating the method correctly formats today's date.
        assertEquals(expectedDate, Utils.getTodayDate());
    }
}
