package com.pay.opay.SplashScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.Parse;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.MainActivity;
import com.pay.opay.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private RelativeLayout splashLayout;
    private boolean parseInitialized = false;
    private boolean firebaseInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLayout = findViewById(R.id.splashScreen);

        // Initialize components in parallel
        initializeFirebase();
        initializeParse();
        AccountInfo.initialize(this);
        //clearAllAppDatabases();

        // Start checking if both services are initialized
        startInitializationCheck();
    }

    private void initializeFirebase() {
        new Thread(() -> {
            try {
                if (FirebaseApp.getApps(this).isEmpty()) {
                    FirebaseApp.initializeApp(this);
                }
                FirebaseAuth.getInstance();
                firebaseInitialized = true;
                Log.d(TAG, "Firebase initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Firebase initialization failed", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Firebase init failed", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void initializeParse() {
        new Thread(() -> {
            try {
                Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("gZDdAxygNAE4mvsfpGyhHUbP7Sp0MNQWHWb3ylcr")
                         .clientKey("sqYIwNoWxk0MGhpqx8HEoFbllOpzDJ5bOVY5Nb2q")
                        .server("https://parseapi.back4app.com/")
                        .build()
                );
                parseInitialized = true;
                Log.d(TAG, "Parse initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Parse initialization failed", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Parse init failed", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void clearAllAppDatabases() {
        deleteDatabase("amount_database.db");
        deleteDatabase("bank_name_database.db");
        deleteDatabase("bank_transfer_database.db");
        deleteDatabase("contact_database.db");
    }

    private void startInitializationCheck() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                if (firebaseInitialized && parseInitialized) {
                    // Both services initialized, proceed with splash sequence
                    startSplashSequence();
                } else {
                    // Check again after 100ms
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(checkRunnable);
    }

    private void startSplashSequence() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            splashLayout.setBackgroundColor(Color.parseColor("#00B876"));
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }, 3000);
        }, 3000);
    }
}