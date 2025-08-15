package com.pay.opay.date;

public class DateTimeHolder {
    private static DateTimeHolder instance;

    private String formattedDateTime;
    private String shortFormattedDateTime;

    private DateTimeHolder() {
        // private constructor
    }

    public static synchronized DateTimeHolder getInstance() {
        if (instance == null) {
            instance = new DateTimeHolder();
        }
        return instance;
    }

    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public void setShortFormattedDateTime(String shortFormattedDateTime) {
        this.shortFormattedDateTime = shortFormattedDateTime;
    }

    public String getShortFormattedDateTime() {
        return shortFormattedDateTime;
    }
}
