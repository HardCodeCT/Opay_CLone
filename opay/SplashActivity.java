package com.pay.opay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLayout = findViewById(R.id.splashScreen);

        clearAllAppDatabases();
        startSplashSequence();
    }

    private void clearAllAppDatabases() {
        deleteDatabase("amount_database.db");
        deleteDatabase("bank_name_database.db");
        deleteDatabase("bank_transfer_database.db");
        deleteDatabase("contact_database.db");
    }

    private void startSplashSequence() {
        delayThenChangeBackground();
    }

    private void delayThenChangeBackground() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            splashLayout.setBackgroundColor(Color.parseColor("#00B876"));
            delayThenLaunchMainActivity();
        }, 3000);
    }

    private void delayThenLaunchMainActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(this::launchMainActivity, 3000);
    }

    private void launchMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}

