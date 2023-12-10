package com.chikilev.planesale.Exception;

import java.io.Serializable;

public class AircraftAlreadyExists extends Exception implements Serializable {
    public AircraftAlreadyExists(String message) {
        super(message);
    }
}
