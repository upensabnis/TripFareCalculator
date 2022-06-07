package com.littlepay.validators;

import com.littlepay.exceptions.RuntimeValidationException;

public interface ValidatorInterface {
    void validateInput(String input) throws RuntimeValidationException;
}
