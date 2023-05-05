package com.tanilsoo.citiesassignment.exception;

import org.springframework.http.HttpStatus;

public class InvalidValueException extends CustomException {

    public InvalidValueException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}