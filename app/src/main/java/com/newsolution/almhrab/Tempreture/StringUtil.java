package com.newsolution.almhrab.Tempreture;

//import com.lowagie.text.pdf.Barcode128;

public class StringUtil {
    public static String PadLeft(String res, int size) {
        String temp = res;
        int len = res.length();
        if (len < size) {
            for (int i = 0; i < size - len; i++) {
                temp = "0" + temp;
            }
        }
        return temp;
    }

//    public static boolean IsHexString(String res) {
//        int i = 0;
//        while (i < res.length()) {
//            try {
//                char c = res.charAt(i);
//                if ((c < 'a' || c > Barcode128.FNC1_INDEX) && ((c < 'A' || c > 'F') && (c < '0' || c > '9'))) {
//                    return false;
//                }
//                i++;
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        return true;
//    }

    public static String ToString(Object res) {
        try {
            return res.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String ToString(double res, int number) {
        try {
            String temp = String.valueOf(res);
            String[] arr = temp.split("\\.");
            if (arr.length <= 1) {
                return temp;
            }
            arr[1] = TrimEnd(arr[1], '0');
            if (IsNullOrEmpty(arr[1])) {
                return arr[0];
            }
            if (arr[1].length() > number) {
                return arr[0] + "." + arr[1].substring(0, number);
            }
            return arr[0] + "." + arr[1];
        } catch (Exception e) {
            return "0";
        }
    }

    public static int GetInt(String res) {
        try {
            return Integer.parseInt(res);
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean IsNullOrEmpty(String res) {
        if (res == null || res.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static String TrimEnd(String res, char character) {
        try {
            for (int resLen = res.length(); resLen > 0; resLen--) {
                if (res.charAt(resLen - 1) != character) {
                    return res.substring(0, resLen);
                }
            }
            return "";
        } catch (Exception e) {
            return res;
        }
    }
}
