package com.newsolution.almhrab.Tempreture;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeripheryBluetoothService {
    public List<BLEGattService> BLEGattServiceList;
    public BluetoothDevice Periphery;
    protected BluetoothAdapter _BluetoothAdapter;
    protected BluetoothGatt _BluetoothGatt;
    int _ConnectIndex;
    Date _ConnectStartTime;
    protected ConnectionStatus _ConnectionStatus;
    protected Context _Context;
    private final BluetoothGattCallback _GattCallback;
    private String _MacAddress;
    protected IPeripheryBluetoothCallBack _PeripheryBluetoothServiceCallBack;
    private long _Timeout;

    class C01311 implements Runnable {
        C01311() {
        }

        public void run() {
            int i = 0;
            while (PeripheryBluetoothService.this._ConnectionStatus == ConnectionStatus.Connecting) {
                try {
                    int maxD = 15;
                    if (PeripheryBluetoothService.this._Timeout > 50) {
                        maxD = 40;
                    }
                    if (i > maxD) {
                        PeripheryBluetoothService.this.Close();
                        PeripheryBluetoothService.this._ConnectionStatus = ConnectionStatus.DisConnected;
                        Thread.sleep(3000);
                        if ((PeripheryBluetoothService.this._Timeout - new Date().getTime()) - PeripheryBluetoothService.this._ConnectStartTime.getTime() > 0) {
                            Log.i("Connect", "超时,尝试重新连接...");
                            PeripheryBluetoothService.this.Connect();
                            return;
                        }
                        return;
                    }
                    i++;
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    Log.e("Connect", ex.toString());
                    return;
                }
            }
        }
    }

    class C01322 extends BluetoothGattCallback {
        C01322() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onConnectionStateChange(BluetoothGatt r5, int r6, int r7) {
            /*
            r4 = this;
            r1 = "onConnectionStateChange";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "连接状态发生改变 status:";
            r2 = r2.append(r3);
            r2 = r2.append(r6);
            r3 = " newState:";
            r2 = r2.append(r3);
            r2 = r2.append(r7);
            r3 = "";
            r2 = r2.append(r3);
            r2 = r2.toString();
            android.util.Log.i(r1, r2);
            if (r6 != 0) goto L_0x00ba;
        L_0x002a:
            switch(r7) {
                case 0: goto L_0x0092;
                case 1: goto L_0x002d;
                case 2: goto L_0x0049;
                default: goto L_0x002d;
            };
        L_0x002d:
            r1 = "onConnectionStateChange";
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0087 }
            r2.<init>();	 Catch:{ Exception -> 0x0087 }
            r3 = "newState:";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0087 }
            r2 = r2.append(r7);	 Catch:{ Exception -> 0x0087 }
            r2 = r2.toString();	 Catch:{ Exception -> 0x0087 }
            android.util.Log.e(r1, r2);	 Catch:{ Exception -> 0x0087 }
        L_0x0045:
            super.onConnectionStateChange(r5, r6, r7);
            return;
        L_0x0049:
            r1 = "onConnectionStateChange";
            r2 = "已经连接上设备";
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._ConnectionStatus;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.Connecting;	 Catch:{ Exception -> 0x0087 }
            if (r1 != r2) goto L_0x0045;
        L_0x0058:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.Connected;	 Catch:{ Exception -> 0x0087 }
            r1._ConnectionStatus = r2;	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0119 }
            r1 = r1._PeripheryBluetoothServiceCallBack;	 Catch:{ Exception -> 0x0119 }
            if (r1 == 0) goto L_0x006b;
        L_0x0064:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0119 }
            r1 = r1._PeripheryBluetoothServiceCallBack;	 Catch:{ Exception -> 0x0119 }
            r1.OnConnected();	 Catch:{ Exception -> 0x0119 }
        L_0x006b:
            r1 = r5.getDevice();	 Catch:{ Exception -> 0x0119 }
            r1 = r1.getBondState();	 Catch:{ Exception -> 0x0119 }
            r2 = 12;
            if (r1 != r2) goto L_0x0083;
        L_0x0077:
            r1 = "onConnectionStateChange";
            r2 = "Waiting 1600 ms for a possible Service Changed indication...";
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x0119 }
            r2 = 1600; // 0x640 float:2.242E-42 double:7.905E-321;
            java.lang.Thread.sleep(r2);	 Catch:{ Exception -> 0x0119 }
        L_0x0083:
            r5.discoverServices();	 Catch:{ Exception -> 0x0087 }
            goto L_0x0045;
        L_0x0087:
            r0 = move-exception;
            r1 = "onConnectionStateChange";
            r2 = r0.toString();
            android.util.Log.e(r1, r2);
            goto L_0x0045;
        L_0x0092:
            r1 = "onConnectionStateChange";
            r2 = "连接已被断开";
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._ConnectionStatus;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.Connected;	 Catch:{ Exception -> 0x0087 }
            if (r1 != r2) goto L_0x00ae;
        L_0x00a1:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._PeripheryBluetoothServiceCallBack;	 Catch:{ Exception -> 0x0087 }
            if (r1 == 0) goto L_0x00ae;
        L_0x00a7:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._PeripheryBluetoothServiceCallBack;	 Catch:{ Exception -> 0x0087 }
            r1.OnDisConnected();	 Catch:{ Exception -> 0x0087 }
        L_0x00ae:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1.Close();	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.DisConnected;	 Catch:{ Exception -> 0x0087 }
            r1._ConnectionStatus = r2;	 Catch:{ Exception -> 0x0087 }
            goto L_0x0045;
        L_0x00ba:
            r1 = "onConnectionStateChange";
            r2 = "GATT 错误";
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1.Close();	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._ConnectionStatus;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.Connecting;	 Catch:{ Exception -> 0x0087 }
            if (r1 != r2) goto L_0x0104;
        L_0x00ce:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._ConnectIndex;	 Catch:{ Exception -> 0x0087 }
            r2 = 3;
            if (r1 > r2) goto L_0x0104;
        L_0x00d5:
            r1 = "Connect";
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0087 }
            r2.<init>();	 Catch:{ Exception -> 0x0087 }
            r3 = "Gatt错误!尝试重新连接(";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0087 }
            r3 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r3 = r3._ConnectIndex;	 Catch:{ Exception -> 0x0087 }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0087 }
            r3 = ")...";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x0087 }
            r2 = r2.toString();	 Catch:{ Exception -> 0x0087 }
            android.util.Log.i(r1, r2);	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.DisConnected;	 Catch:{ Exception -> 0x0087 }
            r1._ConnectionStatus = r2;	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1.Connect();	 Catch:{ Exception -> 0x0087 }
            goto L_0x0045;
        L_0x0104:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r2 = com.TZONE.Bluetooth.ConnectionStatus.DisConnected;	 Catch:{ Exception -> 0x0087 }
            r1._ConnectionStatus = r2;	 Catch:{ Exception -> 0x0087 }
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._PeripheryBluetoothServiceCallBack;	 Catch:{ Exception -> 0x0087 }
            if (r1 == 0) goto L_0x0045;
        L_0x0110:
            r1 = com.TZONE.Bluetooth.PeripheryBluetoothService.this;	 Catch:{ Exception -> 0x0087 }
            r1 = r1._PeripheryBluetoothServiceCallBack;	 Catch:{ Exception -> 0x0087 }
            r1.OnDisConnected();	 Catch:{ Exception -> 0x0087 }
            goto L_0x0045;
        L_0x0119:
            r1 = move-exception;
            goto L_0x0083;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.TZONE.Bluetooth.PeripheryBluetoothService.2.onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int):void");
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i("onServicesDiscovered", "正在获取设备Service信息......");
            if (status == 0) {
                try {
                    String messages = "-------------- （" + PeripheryBluetoothService.this.Periphery.getAddress() + "）服务Services信息： --------------------\n";
                    List<BluetoothGattService> serviceList = gatt.getServices();
                    messages = messages + "总共：" + serviceList.size() + "\n";
                    for (int i = 0; i < serviceList.size(); i++) {
                        BluetoothGattService theService = (BluetoothGattService) serviceList.get(i);
                        PeripheryBluetoothService.this.BLEGattServiceList.add(new BLEGattService(theService));
                        String msg = "---- 服务Service UUID：" + theService.getUuid().toString() + " -----\n";
                        List<BluetoothGattCharacteristic> characterList = theService.getCharacteristics();
                        msg = msg + "特征Characteristic UUID List：\n";
                        for (int j = 0; j < characterList.size(); j++) {
                            msg = msg + "(" + (j + 1) + ")：" + ((BluetoothGattCharacteristic) characterList.get(j)).getUuid() + "\n";
                        }
                        messages = messages + (msg + "\n\n");
                    }
                    Log.i("onServicesDiscovered", messages + "-------------- （" + PeripheryBluetoothService.this.Periphery.getAddress() + "）服务Services信息End --------------------\n");
                } catch (Exception ex) {
                    Log.e("onServicesDiscovered", "异常" + ex.toString());
                }
            }
            if (PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack != null) {
                PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack.OnServicesed(PeripheryBluetoothService.this.BLEGattServiceList);
            }
            super.onServicesDiscovered(gatt, status);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", "读取响应。uuid:" + characteristic.getUuid().toString() + " value:" + StringConvertUtil.bytesToHexString(characteristic.getValue()));
            if (status == 0) {
                try {
                    String uuid = characteristic.getUuid().toString();
                    byte[] val = characteristic.getValue();
                    for (int i = 0; i < PeripheryBluetoothService.this.BLEGattServiceList.size(); i++) {
                        BLEGattService theService = (BLEGattService) PeripheryBluetoothService.this.BLEGattServiceList.get(i);
                        for (int j = 0; j < theService.CharacterList.size(); j++) {
                            BLEGattService.BLEGattCharacteristic theCharacteristic = (BLEGattService.BLEGattCharacteristic) theService.CharacterList.get(j);
                            if (uuid.toLowerCase().equals(theCharacteristic.GattCharacteristic.getUuid().toString().toLowerCase())) {
                                theCharacteristic.val = val;
                                break;
                            }
                        }
                    }
                    if (PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack != null) {
                        PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack.OnReadCallBack(characteristic.getUuid(), val);
                    }
                } catch (Exception ex) {
                    Log.e("onCharacteristicRead", ex.toString());
                }
            }
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicWrite", "写入响应。uuid:" + characteristic.getUuid().toString());
            try {
                if (PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack != null) {
                    if (status == 0) {
                        PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack.OnWriteCallBack(characteristic.getUuid(), true);
                    } else {
                        PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack.OnWriteCallBack(characteristic.getUuid(), false);
                    }
                }
            } catch (Exception ex) {
                Log.e("onCharacteristicWrite", ex.toString());
            }
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] bytes = characteristic.getValue();
            Log.i("onCharacteristicChanged", "接收回传数据。uuid:" + characteristic.getUuid().toString() + " bytes:" + StringConvertUtil.bytesToHexString(bytes));
            if (PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack != null) {
                PeripheryBluetoothService.this._PeripheryBluetoothServiceCallBack.OnReceiveCallBack(characteristic.getUuid(), bytes);
            }
            super.onCharacteristicChanged(gatt, characteristic);
        }
    }

    public PeripheryBluetoothService(BluetoothAdapter bluetoothAdapter, Context context, String macAddress, IPeripheryBluetoothCallBack peripheryBluetoothServiceCallBack) throws Exception {
        this(bluetoothAdapter, context, macAddress, AppConfig.ConnectTimeout, peripheryBluetoothServiceCallBack);
    }

    public PeripheryBluetoothService(BluetoothAdapter bluetoothAdapter, Context context, String macAddress, long timeout, IPeripheryBluetoothCallBack peripheryBluetoothServiceCallBack) throws Exception {
        this._BluetoothAdapter = null;
        this._Timeout = 30000;
        this.Periphery = null;
        this.BLEGattServiceList = new ArrayList();
        this._ConnectionStatus = ConnectionStatus.NoConnect;
        this._ConnectStartTime = new Date();
        this._ConnectIndex = 0;
        this._GattCallback = new C01322();
        this._BluetoothAdapter = bluetoothAdapter;
        this._Context = context;
        this._MacAddress = macAddress;
        this._Timeout = timeout;
        this._ConnectionStatus = ConnectionStatus.NoConnect;
        this._PeripheryBluetoothServiceCallBack = peripheryBluetoothServiceCallBack;
        if (this._BluetoothAdapter == null || !this._BluetoothAdapter.isEnabled()) {
            throw new Exception("bluetoothAdapter对象不能为空,或蓝牙关机");
        } else if (this._BluetoothAdapter.getRemoteDevice(macAddress) == null) {
            throw new Exception("无法找到" + this._MacAddress + "的设备");
        }
    }

    public boolean Connect() {
        try {
            if (this._ConnectionStatus == ConnectionStatus.Connected || this._ConnectionStatus == ConnectionStatus.Connecting) {
                Log.i("Connect", "已经存在连接,无需重复连接...");
                return true;
            } else if (this._BluetoothAdapter == null) {
                Log.e("Connect", "BluetoothAdapter 为空.");
                return false;
            } else if (this._MacAddress == null || this._MacAddress.equals("")) {
                Log.e("Connect", "MacAddress 为空");
                return false;
            } else if (this.Periphery == null || !this.Periphery.getAddress().equals(this._MacAddress) || this._BluetoothGatt == null) {
                BluetoothDevice device = this._BluetoothAdapter.getRemoteDevice(this._MacAddress);
                if (device == null) {
                    Log.e("Connect", "无法找到" + this._MacAddress + "的设备");
                    return false;
                }
                this.Periphery = device;
                Log.i("Connect", "正在连接...");
                this._ConnectionStatus = ConnectionStatus.Connecting;
                if (this._ConnectIndex == 0) {
                    this._ConnectStartTime = new Date();
                }
                this._ConnectIndex++;
                this._BluetoothGatt = device.connectGatt(this._Context, false, this._GattCallback);
                if (VERSION.SDK_INT >= 21) {
                    this._BluetoothGatt.requestConnectionPriority(1);
                }
                new Thread(new C01311()).start();
                return true;
            } else {
                Log.d("Connect", "正尝试重新去连接...");
                this._ConnectionStatus = ConnectionStatus.Connecting;
                this._ConnectIndex++;
                if (this._BluetoothGatt.connect()) {
                    return true;
                }
                this._ConnectionStatus = ConnectionStatus.DisConnected;
                return false;
            }
        } catch (Exception ex) {
            Log.e("Connect", ex.toString());
            return false;
        }
    }

    public void Close() {
        try {
            if (this._BluetoothGatt != null) {
                Log.i("Close", "正在断开连接...");
                if (this._ConnectionStatus == ConnectionStatus.Connected) {
                    this._ConnectionStatus = ConnectionStatus.DisConnecting;
                }
                this.Periphery = null;
                this._BluetoothGatt.disconnect();
                if (this._BluetoothGatt.getDevice().getBondState() == 10) {
                    try {
                        Method refresh = this._BluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                        if (refresh != null) {
                            Log.i("Close", "gatt.refresh:" + ((Boolean) refresh.invoke(this._BluetoothGatt, new Object[0])).booleanValue());
                        }
                    } catch (Exception ex) {
                        Log.e("Close", "gatt.refresh:" + ex.toString());
                    }
                }
                this._BluetoothGatt.close();
                this._BluetoothGatt = null;
            }
        } catch (Exception ex2) {
            Log.e("Close", ex2.toString());
        }
    }

    public BluetoothGattCharacteristic GetCharacteristic(String uuid) {
        try {
            if (this._ConnectionStatus == ConnectionStatus.Connected) {
                BluetoothGattCharacteristic characteristic = null;
                for (int i = 0; i < this.BLEGattServiceList.size(); i++) {
                    BLEGattService theService = (BLEGattService) this.BLEGattServiceList.get(i);
                    for (int j = 0; j < theService.CharacterList.size(); j++) {
                        BLEGattService.BLEGattCharacteristic theCharacteristic = (BLEGattService.BLEGattCharacteristic) theService.CharacterList.get(j);
                        if (uuid.toLowerCase().equals(theCharacteristic.GattCharacteristic.getUuid().toString().toLowerCase())) {
                            characteristic = theCharacteristic.GattCharacteristic;
                            break;
                        }
                    }
                }
                if (characteristic != null) {
                    return characteristic;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public boolean IsExistCharacteristic(String uuid) {
        boolean isExist = false;
        if (GetCharacteristic(uuid) != null) {
            isExist = true;
        }
        Log.i("Write", "检查是否存在特征UUID：" + uuid + " -> " + isExist);
        return isExist;
    }

    public boolean WriteCharacteristic(String uuid, byte[] bytes) {
        try {
            BluetoothGattCharacteristic characteristic = GetCharacteristic(uuid);
            if (characteristic == null || bytes == null || characteristic == null) {
                return false;
            }
            characteristic.setValue(bytes);
            this._BluetoothGatt.writeCharacteristic(characteristic);
            Log.i("Write", "写入特征：" + uuid + " --> " + StringConvertUtil.bytesToHexString(bytes));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean ReadCharacteristic(String uuid) {
        try {
            BluetoothGattCharacteristic characteristic = GetCharacteristic(uuid);
            if (characteristic == null) {
                return false;
            }
            this._BluetoothGatt.readCharacteristic(characteristic);
            Log.i("Write", "读取特征：" + uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean EnableNotification(String uuid, boolean enabled) {
        boolean success = false;
        BluetoothGattCharacteristic characteristic = GetCharacteristic(uuid);
        if (characteristic != null) {
            success = this._BluetoothGatt.setCharacteristicNotification(characteristic, true);
            if (success) {
                for (BluetoothGattDescriptor dp : characteristic.getDescriptors()) {
                    if (dp != null) {
                        if ((characteristic.getProperties() & 16) != 0) {
                            if (enabled) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            } else {
                                dp.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                            }
                        } else if ((characteristic.getProperties() & 32) != 0) {
                            dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        }
                        this._BluetoothGatt.writeDescriptor(dp);
                    }
                }
                Log.i("Notification", "设置通知：" + uuid + " --> " + enabled);
            }
        }
        return success;
    }

    public ConnectionStatus GetConnectStatus() {
        return this._ConnectionStatus;
    }
}
