
package com.pay.opay.utils;

import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class LoaderHelper {

    private static final long ROTATE_DURATION = 30000; // rotation time
    private static final long TIMEOUT_DURATION = 2000;  // delay before finishing

    public static void startLoaderRotation(View loader, View progressBar, Runnable onComplete) {
        if (loader != null) loader.setVisibility(View.VISIBLE);

        if (progressBar != null) {
            RotateAnimation rotate = new RotateAnimation(
                    0f, 360f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f
            );
            rotate.setDuration(ROTATE_DURATION);
            rotate.setInterpolator(new LinearInterpolator());
            progressBar.startAnimation(rotate);
        }

        new Handler().postDelayed(() -> {
            if (loader != null) loader.setVisibility(View.GONE);
            if (progressBar != null) progressBar.clearAnimation();
            if (onComplete != null) onComplete.run();
        }, TIMEOUT_DURATION);
    }
}
