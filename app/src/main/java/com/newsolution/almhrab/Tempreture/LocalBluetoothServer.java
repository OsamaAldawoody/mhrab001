package com.newsolution.almhrab.Tempreture;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings.Builder;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalBluetoothServer {
    public List<BLE> Devices = new ArrayList();
    private boolean IsScanning = false;
    public Date LastActiveTime = new Date();
    public Date StartTime = new Date();
    private BluetoothAdapter _BluetoothAdapter = null;
    private Handler _BluetoothHandler = null;
    private ILocalBluetoothCallBack _LocalBluetoothCallBack = null;
    private ScanCallbackEx _ScanCallbackEx = null;
    BluetoothLeScanner _Scanner = null;

    class C01271 implements Runnable {
        C01271() {
        }

        public void run() {
            LocalBluetoothServer.this.IsScanning = false;
            LocalBluetoothServer.this._BluetoothAdapter.stopLeScan(LocalBluetoothServer.this._ScanCallbackEx.mLeScanCallback);
            LocalBluetoothServer.this.IsBleExited();
            if (LocalBluetoothServer.this._LocalBluetoothCallBack != null) {
                LocalBluetoothServer.this._LocalBluetoothCallBack.OnScanComplete();
            }
        }
    }

    class C01282 implements Runnable {
        C01282() {
        }

        public void run() {
            LocalBluetoothServer.this.IsScanning = false;
            LocalBluetoothServer.this._Scanner.stopScan(LocalBluetoothServer.this._ScanCallbackEx.mLeScanCallback_LOLLIPOP);
            LocalBluetoothServer.this.IsBleExited();
            if (LocalBluetoothServer.this._LocalBluetoothCallBack != null) {
                LocalBluetoothServer.this._LocalBluetoothCallBack.OnScanComplete();
            }
        }
    }

    public class ScanCallbackEx {
        public LeScanCallback mLeScanCallback = null;
        public ScanCallback mLeScanCallback_LOLLIPOP = null;

        public ScanCallbackEx() {
            this.mLeScanCallback = new LeScanCallback() {
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    try {
                        BLE ble = new BLE();
                        ble.Name = device.getName();
                        ble.RSSI = rssi;
                        ble.ScanData = scanRecord;
                        ble.MacAddress = device.getAddress().toUpperCase();
                        ble.LastScanTime = new Date();
                        LocalBluetoothServer.this.IsBleNewEnter(ble);
                    } catch (Exception e) {

                   e.printStackTrace();
                    }
                }
            };
            if (VERSION.SDK_INT >= 21) {
                this.mLeScanCallback_LOLLIPOP = new ScanCallback() {
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        try {
                            if (VERSION.SDK_INT >= 21) {
                                ScanRecord scanRecord = result.getScanRecord();
                                BluetoothDevice device = result.getDevice();
                                BLE ble = new BLE();
                                ble.Name = device.getName();
                                ble.RSSI = result.getRssi();
                                ble.ScanData = scanRecord.getBytes();
                                ble.MacAddress = device.getAddress().toUpperCase();
                                ble.LastScanTime = new Date();
                                LocalBluetoothServer.this.IsBleNewEnter(ble);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onScanFailed(int errorCode) {
                        super.onScanFailed(errorCode);
                        if (LocalBluetoothServer.this._LocalBluetoothCallBack != null) {
                            LocalBluetoothServer.this._LocalBluetoothCallBack.OnScanComplete();
                        }
                    }
                };
            }
        }
    }

    public boolean Init(BluetoothAdapter bluetoothAdapter, ILocalBluetoothCallBack localBluetoothCallBack) {
        try {
            this._BluetoothAdapter = bluetoothAdapter;
            this._LocalBluetoothCallBack = localBluetoothCallBack;
            this._ScanCallbackEx = new ScanCallbackEx();
            this._BluetoothHandler = new Handler();
            this.StartTime = new Date();
            this.LastActiveTime = new Date();
            if (this._BluetoothAdapter != null && this._BluetoothAdapter.isEnabled()) {
                return true;
            }
        } catch (Exception ex) {
            Log.e("BluetoothServer", "Init:" + ex.toString());
        }
        return false;
    }

    private void ScanLeDevice(boolean enable) {
        if (VERSION.SDK_INT >= 21) {
            ScanLeDevice_LOLLIPOP(enable);
        } else {
            ScanLeDevice_JELLY_BEAN_MR2(enable);
        }
    }

    private void ScanLeDevice_JELLY_BEAN_MR2(boolean enable) {
        if (!enable) {
            this.IsScanning = false;
            this._BluetoothAdapter.stopLeScan(this._ScanCallbackEx.mLeScanCallback);
        } else if (!this.IsScanning) {
            this._BluetoothHandler.postDelayed(new C01271(), AppConfig.ScanRunTime);
            this.IsScanning = true;
            this.LastActiveTime = new Date();
            this._BluetoothAdapter.startLeScan(this._ScanCallbackEx.mLeScanCallback);
        }
    }

    @TargetApi(21)
    private void ScanLeDevice_LOLLIPOP(boolean enable) {
        if (VERSION.SDK_INT >= 21) {
            if (this._Scanner == null) {
                this._Scanner = this._BluetoothAdapter.getBluetoothLeScanner();
            }
            if (!enable) {
                this.IsScanning = false;
                this._Scanner.stopScan(this._ScanCallbackEx.mLeScanCallback_LOLLIPOP);
            } else if (!this.IsScanning) {
                this._BluetoothHandler.postDelayed(new C01282(), AppConfig.ScanRunTime);
                this.IsScanning = true;
                this.LastActiveTime = new Date();
                this._Scanner.startScan(null, new Builder().setScanMode(2).setReportDelay(0).build(),
                        this._ScanCallbackEx.mLeScanCallback_LOLLIPOP);
//                Log.i("////////8: ","start");
            }
        }
    }

    private void IsBleNewEnter(BLE ble) {
        if (ble != null) {
            boolean isExist = false;
            int i = 0;
            while (i < this.Devices.size()) {
                try {
                    BLE item = (BLE) this.Devices.get(i);
                    if (item.MacAddress.equals(ble.MacAddress)) {
                        item.Name = ble.Name;
                        item.RSSI = ble.RSSI;
                        item.ScanData = ble.ScanData;
                        item.MacAddress = ble.MacAddress;
                        item.LastScanTime = new Date();
                        isExist = true;
                    }
                    i++;
                } catch (Exception ex) {
                    Log.e("BluetoothServer", "IsNewEnter：" + ex.toString());
                    return;
                }
            }
            if (!isExist) {
                this.Devices.add(ble);
                if (this._LocalBluetoothCallBack != null) {
                    this._LocalBluetoothCallBack.OnEntered(ble);
                }
            } else if (this._LocalBluetoothCallBack != null) {
                this._LocalBluetoothCallBack.OnUpdate(ble);
            }
        }
    }

    private void IsBleExited() {
        try {
            long exitedTime = AppConfig.ExitedTime;
            Date now = new Date();
            for (int i = 0; i < this.Devices.size(); i++) {
                BLE item = (BLE) this.Devices.get(i);
                if ((now.getTime() - item.LastScanTime.getTime()) / 60000 > exitedTime) {
                    if (this._LocalBluetoothCallBack != null) {
                        this._LocalBluetoothCallBack.OnExited(item);
                    }
                    this.Devices.remove(i);
                }
            }
        } catch (Exception ex) {
            Log.e("BluetoothServer", "IsBleExited：" + ex.toString());
        }
    }

    public void StartScan() {
        ScanLeDevice(true);
    }

    public void StopScan() {
        ScanLeDevice(false);
    }
}
