package com.pay.opay;

import android.app.Activity;

public class Terminator {
    public static void killApp(Activity activity) {
        if (activity != null) {
            activity.finishAffinity(); // Close all activities
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

