package com.example.SpringAppGB.Exceptions;

import java.util.Date;

public class AppErrors {
    private int errorCode;
    private String errorMessage;
    private Date date;

    public AppErrors(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.date = new Date();
    }
}
