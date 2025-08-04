package com.pay.opay;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.adapter.BankAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class selectbank extends AppCompatActivity {
    private RecyclerView recyclerView;
    Handler handler = new Handler(Looper.getMainLooper());
    private Runnable bankUpdateRunnable;
    AccountInfo accountInfo = AccountInfo.getInstance();
    private BankAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectbank);

        accountInfo.setBankUpdate(0);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> {
            finish(); // This will close the current activity and return to the previous one
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BankItem> bankItems = loadBankItemsFromJson();

        adapter = new BankAdapter(bankItems);
        recyclerView.setAdapter(adapter);
        checkBankUpdate();
    }

    private List<BankItem> loadBankItemsFromJson() {
        List<BankItem> result = new ArrayList<>();

        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("nigbanks.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray array = new JSONArray(builder.toString());

            List<JSONObject> banks = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                banks.add(array.getJSONObject(i));
            }

            // Sort banks by name
            Collections.sort(banks, (o1, o2) -> {
                String name1 = o1.optString("name", "");
                String name2 = o2.optString("name", "");
                return name1.compareToIgnoreCase(name2);
            });

            char lastInitial = 0;
            for (JSONObject bank : banks) {
                String name = bank.optString("name", "");
                String code = bank.optString("code", "");
                String imageName = bank.optString("image", "house"); // default to "house" if not provided

                if (name.isEmpty()) continue;

                char currentInitial = Character.toUpperCase(name.charAt(0));
                if (currentInitial != lastInitial) {
                    result.add(new BankItem(String.valueOf(currentInitial))); // Header
                    lastInitial = currentInitial;
                }

                int imageResId = getResources().getIdentifier(imageName, "mipmap", getPackageName());
                if (imageResId == 0) {
                    imageResId = R.mipmap.house; // fallback in case image name is wrong
                }

                result.add(new BankItem(name, imageResId, code));
            }

        } catch (Exception e) {
            Log.e("BankLoadError", "Error reading banks", e);
        }

        return result;
    }

    private void checkBankUpdate() {
        handler = new Handler(Looper.getMainLooper());
        bankUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (accountInfo.getBankUpdate() == 4) {
                    stopBankUpdateCheck();
                    finish();
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(bankUpdateRunnable, 1000);
    }

    private void stopBankUpdateCheck() {
        if (handler != null && bankUpdateRunnable != null) {
            handler.removeCallbacks(bankUpdateRunnable);
        }
    }
}