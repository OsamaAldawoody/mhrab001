package com.newsolution.almhrab.Tempreture;

import java.text.DecimalFormat;

public class MeasuringDistance {
    public static double calculateAccuracy(int mPower, double rssi) {
        double distance;
        if (rssi == 0.0d) {
        }
        double ratio = (1.0d * rssi) / ((double) mPower);
        if (ratio < 1.0d) {
            try {
                distance = Math.pow(ratio, 10.0d);
            } catch (Exception e) {
                return -1.0d;
            }
        }
        distance = (0.89976d * Math.pow(ratio, 7.7095d)) + 0.111d;
        return Double.valueOf(new DecimalFormat("#.00").format(distance)).doubleValue();
    }
}
