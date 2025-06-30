package com.pay.opay;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class LoaderRotator {
    private final View rotatingFrame;
    private final View loader;
    private final View rootLayout;
    private static final long DURATION = 10000;
    private RotateAnimation rotate;
    public LoaderRotator(View rotatingFrame, View loader, View rootLayout) {
        this.rotatingFrame = rotatingFrame;
        this.loader = loader;
        this.rootLayout = rootLayout;
        setupAnimation();
    }

    private void setupAnimation() {
        rotate = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(DURATION);
        rotate.setInterpolator(new LinearInterpolator());
    }

    public void start() {
        rotatingFrame.startAnimation(rotate);
        loader.setVisibility(View.VISIBLE);
        rootLayout.setVisibility(View.GONE);

        rotatingFrame.postDelayed(this::stop, DURATION);
    }

    public void stop() {
        rotatingFrame.clearAnimation();
        loader.setVisibility(View.GONE);
        rootLayout.setVisibility(View.VISIBLE);
    }
}
