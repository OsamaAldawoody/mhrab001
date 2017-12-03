package com.newsolution.almhrab.Tempreture;

import java.util.List;
import java.util.UUID;

public interface IPeripheryBluetoothCallBack {
    void OnConnected();

    void OnDisConnected();

    void OnReadCallBack(UUID uuid, byte[] bArr);

    void OnReceiveCallBack(UUID uuid, byte[] bArr);

    void OnServicesed(List<BLEGattService> list);

    void OnWriteCallBack(UUID uuid, boolean z);
}
