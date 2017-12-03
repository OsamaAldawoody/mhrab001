package com.newsolution.almhrab.Tempreture;

public class BinaryUtil {
    public static byte[] CloneRange(byte[] bytes, int start, int len) {
        if (bytes.length <= start) {
            return null;
        }
        if (bytes.length - start < len) {
            len = bytes.length - start;
        }
        byte[] bArr = new byte[len];
        for (int i = 0; i < len; i++) {
            bArr[i] = bytes[start + i];
        }
        return bArr;
    }

    public static byte[] Merge(byte[] bytes1, byte[] bytes2) {
        byte[] bytes = new byte[(bytes1.length + bytes2.length)];
        System.arraycopy(bytes1, 0, bytes, 0, bytes1.length);
        System.arraycopy(bytes2, 0, bytes, bytes1.length, bytes2.length);
        return bytes;
    }

    public static byte[] MultipleMerge(byte[]... params) {
        byte[] targets = null;
        for (byte[] item : params) {
            if (targets == null) {
                targets = item;
            } else {
                targets = Merge(targets, item);
            }
        }
        return targets;
    }

    public static byte[] PadRight(byte[] bytes, int len) {
        byte[] target = new byte[len];
        int l = bytes.length;
        if (len < bytes.length) {
            l = len;
        }
        for (int i = 0; i < l; i++) {
            target[i] = bytes[i];
        }
        return target;
    }

    public static byte[] TrimEnd(byte[] bytes) {
        for (int targetLen = bytes.length; targetLen > 0; targetLen--) {
            if (bytes[targetLen - 1] != (byte) 0) {
                return CloneRange(bytes, 0, targetLen);
            }
        }
        return null;
    }
}
