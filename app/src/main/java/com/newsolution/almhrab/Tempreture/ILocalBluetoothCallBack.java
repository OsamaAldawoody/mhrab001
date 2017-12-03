package com.newsolution.almhrab.Tempreture;

public interface ILocalBluetoothCallBack {
    void OnEntered(BLE ble);

    void OnExited(BLE ble);

    void OnScanComplete();

    void OnUpdate(BLE ble);
}
