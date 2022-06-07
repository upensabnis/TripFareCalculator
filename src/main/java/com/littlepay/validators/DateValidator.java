package com.littlepay.validators;

import com.littlepay.exceptions.RuntimeParsingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator implements ValidatorInterface {

    @Override
    public void validateInput(String date) throws RuntimeParsingException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new RuntimeParsingException(ex.getMessage());
        }
    }

}
