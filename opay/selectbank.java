package com.pay.opay;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.adapter.BankAdapter;
import com.pay.opay.adapter.BankSearchAdapter;
import com.pay.opay.BankItem;
import com.pay.opay.SearchBankItem;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class selectbank extends AppCompatActivity {
    private RecyclerView recyclerView, recyclerrView;
    EditText et_search_bank;
    private ImageView ivBack;
    private Handler handler;
    private Runnable bankUpdateRunnable;
    private BankAdapter adapter;
    private BankSearchAdapter searchAdapter;
    private final AccountInfo accountInfo = AccountInfo.getInstance();
    private List<BankItem> bankItems;
    LinearLayout fixedbanks;
    TextView fixedbankstext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectbank);
        accountInfo.setBankUpdate(0);
        handler = new Handler(Looper.getMainLooper());
        setupView();
        setupListeners();
        setupRecyclerView();
        checkBankUpdate();
    }

    private void setupView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerrView = findViewById(R.id.recyclerrView);
        ivBack = findViewById(R.id.iv_back);
        et_search_bank = findViewById(R.id.et_search_bank);
        fixedbanks = findViewById(R.id.fixedbanks);
        fixedbankstext = findViewById(R.id.fixedbankstext);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        et_search_bank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    searchAdapter.updateList(new ArrayList<>());
                    recyclerrView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    fixedbanks.setVisibility(View.VISIBLE);
                    fixedbankstext.setVisibility(View.VISIBLE);
                } else {
                    fixedbanks.setVisibility(View.GONE);
                    recyclerrView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    fixedbankstext.setVisibility(View.GONE);
                    filterBankItems(query);
                }
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerrView.setLayoutManager(new LinearLayoutManager(this));

        bankItems = loadBankItemsFromJson();
        adapter = new BankAdapter(bankItems);
        recyclerView.setAdapter(adapter);

        searchAdapter = new BankSearchAdapter(new ArrayList<>());
        recyclerrView.setAdapter(searchAdapter);
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
            Collections.sort(banks, (o1, o2) -> {
                String name1 = o1.optString("name", "");
                String name2 = o2.optString("name", "");
                return name1.compareToIgnoreCase(name2);
            });
            char lastInitial = 0;
            for (JSONObject bank : banks) {
                String name = bank.optString("name", "");
                String code = bank.optString("code", "");
                String imageName = bank.optString("image", "house");
                if (name.isEmpty()) continue;
                char currentInitial = Character.toUpperCase(name.charAt(0));
                if (currentInitial != lastInitial) {
                    result.add(new BankItem(String.valueOf(currentInitial)));
                    lastInitial = currentInitial;
                }
                int imageResId = getResources().getIdentifier(imageName, "mipmap", getPackageName());
                if (imageResId == 0) {
                    imageResId = R.mipmap.house;
                }
                result.add(new BankItem(name, imageResId, code));
            }
        } catch (Exception e) {
            // Handle error silently
        }
        return result;
    }

    private void filterBankItems(String query) {
        List<SearchBankItem> filteredList = new ArrayList<>();
        for (BankItem item : bankItems) {
            String name = item.getName() != null ? item.getName() : "";
            if (item.getType() == BankItem.TYPE_BANK
                    && name.trim().toLowerCase().contains(query.trim().toLowerCase())) {
                filteredList.add(new SearchBankItem(name, item.getLogoResId(), item.getBankCode()));
            }
        }
        searchAdapter.updateList(filteredList);
        searchAdapter.setQuery(query);
    }

    private void checkBankUpdate() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBankUpdateCheck();
    }
}