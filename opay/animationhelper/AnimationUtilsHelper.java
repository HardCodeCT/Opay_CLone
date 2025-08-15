package com.pay.opay.animationhelper;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.content.Context;

public class AnimationUtilsHelper {

    private static RotateAnimation rotateAnimation;

    public static void startRotating(Context context, View centerIcon) {
        rotateAnimation = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(context, android.R.interpolator.linear);
        centerIcon.startAnimation(rotateAnimation);
    }

    public static void stopRotating(View centerIcon) {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
            centerIcon.clearAnimation();
        }
    }
}

