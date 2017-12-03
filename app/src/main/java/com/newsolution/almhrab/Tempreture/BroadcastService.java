package com.newsolution.almhrab.Tempreture;


public class BroadcastService extends LocalBluetoothServer {
    public Device ConvertDevice(BLE ble) {
        Device device = new Device();
        return device.fromScanData(ble) ? device : null;
    }
}
