package com.pay.opay;


import com.pay.opay.database.BankTransfer;

public class LastTwoTransfersWrapper {
    public BankTransfer first;
    public BankTransfer second;

    public LastTwoTransfersWrapper(BankTransfer first, BankTransfer second) {
        this.first = first;
        this.second = second;
    }
}
