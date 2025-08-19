package com.pay.opay.SplashScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.MainActivity;
import com.pay.opay.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private RelativeLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLayout = findViewById(R.id.splashScreen);

        // Initialize Firebase first
        initializeFirebase();

        // Initialize other components
        AccountInfo.initialize(this);
        clearAllAppDatabases();

        startSplashSequence();
    }

    private void initializeFirebase() {
        try {
            // Check if already initialized
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
            }
            // Verify initialization by getting auth instance
            FirebaseAuth.getInstance();
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed", e);
            // Handle error or retry
        }
    }

    private void clearAllAppDatabases() {
        deleteDatabase("amount_database.db");
        deleteDatabase("bank_name_database.db");
        deleteDatabase("bank_transfer_database.db");
        deleteDatabase("contact_database.db");
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

