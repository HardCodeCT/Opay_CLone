package com.pay.opay;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BankResolver {

    private static final String API_KEY = "f2532188f1mshafd8a611ae0a1c3p100201jsn543d8aa5c026";
    private static final String API_HOST = "nigeria-bank-account-validation.p.rapidapi.com";

    private final Handler backgroundHandler;
    private final Handler mainHandler;
    public BankResolver() {
        HandlerThread thread = new HandlerThread("BankResolverThread");
        thread.start();
        backgroundHandler = new Handler(thread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void resolveAccountName(String accountNumber, String bankCode) {
        backgroundHandler.post(() -> {
            String result;
            int status;

            try {
                String urlStr = "https://" + API_HOST + "/?account_number=" + accountNumber + "&bank_code=" + bankCode;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("x-rapidapi-key", API_KEY);
                conn.setRequestProperty("x-rapidapi-host", API_HOST);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    JSONObject json = new JSONObject(response.toString());
                    result = json.optString("account_name", "Account name not found");
                    status = 1; // Success
                } else {
                    result = "Error: " + responseCode;
                    status = 2; // Error
                }

                conn.disconnect();
            } catch (Exception e) {
                result = "Exception: " + e.getMessage();
                status = 2; // Error
            }

            final String finalResult = result;
            final int finalStatus = status;

            mainHandler.post(() -> {
                AccountInfo info = AccountInfo.getInstance();
                info.setUserAccount(finalResult);
                info.setResponse(finalStatus);
            });
        });
    }
}
