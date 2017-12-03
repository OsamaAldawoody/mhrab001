package com.newsolution.almhrab.Tempreture;

public class StringConvertUtil {
    public static String byteToAsciiString(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result = result + ((char) b);
        }
        return result;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte b : bytes) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] intToByte(int res) {
        byte[] targets = new byte[4];
        for (int i = 0; i < targets.length; i++) {
            targets[i] = (byte) ((res >> ((3 - i) * 8)) & 255);
        }
        return targets;
    }

    public static int byteToInt(byte[] res) {
        int targets = 0;
        for (int i = 0; i < res.length; i++) {
            targets += (res[i] & 255) << ((3 - i) * 8);
        }
        return targets;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[(byte_1.length + byte_2.length)];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] byteMergerMultiple(byte[]... params) {
        byte[] targets = null;
        for (byte[] item : params) {
            if (targets == null) {
                targets = item;
            } else {
                targets = byteMerger(targets, item);
            }
        }
        return targets;
    }

    public static byte[] uint16ToByte(int res) {
        try {
            byte[] temp = hexStringToBytes(StringUtil.PadLeft(Integer.toHexString(res), 4));
            return new byte[]{temp[1], temp[0]};
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] uint8ToByte(int res) {
        try {
            return hexStringToBytes(StringUtil.PadLeft(Integer.toHexString(res), 2));
        } catch (Exception e) {
            return null;
        }
    }

    public static int byteToUint16(byte[] res) {
        int i = 0;
        try {
            i = Integer.parseInt(bytesToHexString(new byte[]{res[1], res[0]}), 16);
        } catch (Exception e) {
        }
        return i;
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0) {
            return null;
        }
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < bString.length(); i += 4) {
            int iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, (i + j) + 1)) << ((4 - j) - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            return null;
        }
        String bString = "";
        for (int i = 0; i < hexString.length(); i++) {
            String tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString = bString + tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static String LittleEndian(String hexString) {
        if (hexString != null) {
            try {
                if (hexString.length() % 2 == 0) {
                    String output = "";
                    for (int i = 0; i < hexString.length() / 2; i++) {
                        output = hexString.substring(i * 2, (i + 1) * 2) + output;
                    }
                    return output;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
