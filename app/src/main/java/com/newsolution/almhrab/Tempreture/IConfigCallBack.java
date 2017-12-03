package com.newsolution.almhrab.Tempreture;

import java.util.HashMap;

public interface IConfigCallBack extends IPeripheryBluetoothCallBack {
    void OnReadConfigCallBack(boolean z, HashMap<String, byte[]> hashMap);

    void OnWriteConfigCallBack(boolean z);
}
