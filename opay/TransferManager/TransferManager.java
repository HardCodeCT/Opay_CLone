package com.pay.opay.TransferManager;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.database.BankTransfer;
import com.pay.opay.date.DateTimeHolder;
import com.pay.opay.viewmodel.BankTransferViewModel;

public class TransferManager {

    private final BankTransferViewModel bankTransferViewModel;
    private final AccountInfo accountInfo;

    public TransferManager(BankTransferViewModel bankTransferViewModel, AccountInfo accountInfo) {
        this.bankTransferViewModel = bankTransferViewModel;
        this.accountInfo = accountInfo;
    }

    public void insertTransfer() {
        DateTimeHolder dateTimeHolder = DateTimeHolder.getInstance();

        accountInfo.setLongDateTime(dateTimeHolder.getFormattedDateTime());
        accountInfo.setShortDateTime(dateTimeHolder.getShortFormattedDateTime());

        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.bankimage = accountInfo.getActivebank();
        bankTransfer.senderName = accountInfo.getUserAccount();
        bankTransfer.accountNumber = accountInfo.getUserNumber();
        bankTransfer.bankName = accountInfo.getUserBank();
        bankTransfer.amount = accountInfo.getAmount();
        bankTransfer.longdatetime = accountInfo.getLongDateTime();
        bankTransfer.shortdatetime = accountInfo.getShortDateTime();

        bankTransferViewModel.insert(bankTransfer);
    }
}