package com.newsolution.almhrab.Tempreture;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import java.util.ArrayList;
import java.util.List;

public class BLEGattService {
    public List<BLEGattCharacteristic> CharacterList = new ArrayList();
    public BluetoothGattService GattService;

    public class BLEGattCharacteristic {
        public BluetoothGattCharacteristic GattCharacteristic;
        public byte[] val;

        public BLEGattCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            this.GattCharacteristic = bluetoothGattCharacteristic;
        }
    }

    public BLEGattService(BluetoothGattService bluetoothGattService) {
        this.GattService = bluetoothGattService;
        List<BluetoothGattCharacteristic> bluetoothGattCharacteristicList = bluetoothGattService.getCharacteristics();
        for (int i = 0; i < bluetoothGattCharacteristicList.size(); i++) {
            this.CharacterList.add(new BLEGattCharacteristic((BluetoothGattCharacteristic) bluetoothGattCharacteristicList.get(i)));
        }
    }
}
