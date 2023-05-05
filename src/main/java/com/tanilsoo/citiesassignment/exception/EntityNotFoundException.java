package com.tanilsoo.citiesassignment.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
