package com.newsolution.almhrab.AppConstants;

import android.app.Activity;

public class ScreenUtils {
    public static void lockOrientation(Activity activity) {
        activity.setRequestedOrientation(14);
    }

    public static void unlockOrientation(Activity activity) {
        activity.setRequestedOrientation(2);
    }
}
