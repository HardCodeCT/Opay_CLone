package com.pay.opay.newupdateresolver;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("bank/resolve")
    Call<BankAccountResponse> verifyAccount(
            @Query("account_number") String accountNumber,
            @Query("bank_code") String bankCode
    );
}
