package com.newsolution.almhrab.Tempreture;

import java.util.Dictionary;
import java.util.Hashtable;

public class BroadcastPacketsUtil {
    public static Dictionary<String, String> GetScanDictionary(String hexScanData) {
        Dictionary<String, String> dic = new Hashtable();
        while (hexScanData.length() > 0) {
            int len = Integer.parseInt(hexScanData.substring(0, 2), 16);
            String type = "00";
            String value = "";
            if (len >= 1) {
                dic.put(hexScanData.substring(2, 4), hexScanData.substring(4, (len + 1) * 2));
            }
            hexScanData = hexScanData.substring((len + 1) * 2, hexScanData.length());
        }
        return dic;
    }

    public static String GetScanParam(String hexScanData, String hexType) {
        try {
            return ((String) GetScanDictionary(hexScanData).get(hexType)).toString();
        } catch (Exception e) {
            return "";
        }
    }
}
