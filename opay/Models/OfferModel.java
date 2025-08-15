package com.pay.opay.Models;

public class OfferModel {
    public int imageRes;
    public String title, subtitle;

    public OfferModel(int imageRes, String title, String subtitle) {
        this.imageRes = imageRes;
        this.title = title;
        this.subtitle = subtitle;
    }
}
