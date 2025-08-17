package com.pay.opay.ImageSwitcher;

import android.os.Handler;
import android.widget.ImageView;
import java.util.Random;

public class ImageSwitcherUtil {

    private final int[] images;
    private final ImageView imageView;
    private final Handler handler;
    private int currentIndex;
    private final int switchInterval; // in milliseconds

    public ImageSwitcherUtil(ImageView imageView, int switchInterval, int... imageResIds) {
        this.imageView = imageView;
        this.images = imageResIds;
        this.switchInterval = switchInterval;
        this.handler = new Handler();
        this.currentIndex = new Random().nextInt(images.length); // start at random image
        imageView.setImageResource(images[currentIndex]);
    }

    private final Runnable switchRunnable = new Runnable() {
        @Override
        public void run() {
            currentIndex = (currentIndex + 1) % images.length;
            imageView.setImageResource(images[currentIndex]);
            handler.postDelayed(this, switchInterval);
        }
    };

    public void startSwitching() {
        handler.postDelayed(switchRunnable, switchInterval);
    }

    public void stopSwitching() {
        handler.removeCallbacks(switchRunnable);
    }
}

