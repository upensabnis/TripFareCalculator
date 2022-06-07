package com.littlepay;

import com.opencsv.validators.RowValidator;

public class TripCSVRowValidator implements RowValidator {

    @Override
    public boolean isValid(String[] strings) {
        return false;
    }

    @Override
    public void validate(String[] strings) {

    }
}
