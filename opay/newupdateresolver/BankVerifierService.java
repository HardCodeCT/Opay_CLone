package com.pay.opay.newupdateresolver;

import android.content.Context;
import android.widget.Toast;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.resolver.BankResolver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BankVerifierService {
    private Context context;
    private BankResolver bankResolver = new BankResolver();

    public BankVerifierService(Context context) {
        this.context = context;
    }

    public void verifyBankAccount(String accountNumber, String bankCode) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<BankAccountResponse> call = apiService.verifyAccount(accountNumber, bankCode);

        call.enqueue(new Callback<BankAccountResponse>() {
            @Override
            public void onResponse(Call<BankAccountResponse> call, Response<BankAccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BankAccountResponse result = response.body();
                    if (result.isStatus() && result.getData() != null) {
                        String accountName = result.getData().getAccountName();

                        // Update AccountInfo singleton (like original BankResolver)
                        AccountInfo info = AccountInfo.getInstance();
                        info.setResponse(1); // Success status
                        info.setUserAccount(accountName);

                        // Show success toast
                        //Toast.makeText(context, "Account Name: " + accountName, Toast.LENGTH_LONG).show();
                    } else {

                        bankResolver.resolveAccountName(context, accountNumber, bankCode);
                    }
                } else {
                    // Update AccountInfo for HTTP error
                    bankResolver.resolveAccountName(context, accountNumber, bankCode);
                }
            }

            @Override
            public void onFailure(Call<BankAccountResponse> call, Throwable t) {
                bankResolver.resolveAccountName(context, accountNumber, bankCode);
            }
        });
    }
}
