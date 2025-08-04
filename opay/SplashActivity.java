package com.pay.opay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Replace with your XML layout name

        final RelativeLayout splashLayout = findViewById(R.id.splashScreen);

        // Handler to change background after 3 seconds (3000ms)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change background to normal green (replace with your color)
                splashLayout.setBackgroundColor(Color.parseColor("#00B876")); // Example green

                // Handler to launch MainActivity after another 3 seconds (total 6s)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close SplashActivity to prevent going back
                    }
                }, 3000); // Second delay: 3000ms = 3s
            }
        }, 3000); // First delay: 3000ms = 3s
    }
}
