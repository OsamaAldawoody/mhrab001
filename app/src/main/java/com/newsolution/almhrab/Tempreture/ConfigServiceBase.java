package com.newsolution.almhrab.Tempreture;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class ConfigServiceBase {
    public boolean IsConnected;
    public boolean IsRunning;
    public HashMap<String, byte[]> ReadItems;
    private IConfigCallBack _ConfigCallBack;
    private PeripheryBluetoothService _PeripheryBluetoothService;
    private ConfigHandle _ReadConfigs;
    private ConfigHandle _WriteConfigs;
    public IPeripheryBluetoothCallBack peripheryBluetoothCallBack;

    class C05043 implements IPeripheryBluetoothCallBack {
        C05043() {
        }

        public void OnConnected() {
            ConfigServiceBase.this.IsConnected = true;
            if (ConfigServiceBase.this.IsRunning && ConfigServiceBase.this._ConfigCallBack != null) {
                ConfigServiceBase.this._ConfigCallBack.OnConnected();
            }
        }

        public void OnDisConnected() {
            ConfigServiceBase.this.IsConnected = false;
            if (ConfigServiceBase.this.IsRunning && ConfigServiceBase.this._ConfigCallBack != null) {
                ConfigServiceBase.this._ConfigCallBack.OnDisConnected();
            }
        }

        public void OnServicesed(List<BLEGattService> services) {
            if (ConfigServiceBase.this.IsRunning && ConfigServiceBase.this._ConfigCallBack != null) {
                ConfigServiceBase.this._ConfigCallBack.OnServicesed(services);
            }
        }

        public void OnReadCallBack(UUID uuid, byte[] data) {
            if (ConfigServiceBase.this.IsRunning && ConfigServiceBase.this._ReadConfigs != null) {
                ConfigServiceBase.this._ReadConfigs.ConfigRespond(uuid.toString());
                if (ConfigServiceBase.this.ReadItems.containsKey(uuid.toString())) {
                    ConfigServiceBase.this.ReadItems.put(uuid.toString(), data);
                }
                if (ConfigServiceBase.this._ConfigCallBack != null) {
                    ConfigServiceBase.this._ConfigCallBack.OnReadCallBack(uuid, data);
                }
            }
        }

        public void OnWriteCallBack(UUID uuid, boolean isSuccess) {
            if (ConfigServiceBase.this.IsRunning && ConfigServiceBase.this._WriteConfigs != null) {
                if (isSuccess) {
                    ConfigServiceBase.this._WriteConfigs.ConfigRespond(uuid.toString());
                }
                if (ConfigServiceBase.this._ConfigCallBack != null) {
                    ConfigServiceBase.this._ConfigCallBack.OnWriteCallBack(uuid, isSuccess);
                }
            }
        }

        public void OnReceiveCallBack(UUID uuid, byte[] data) {
            if (ConfigServiceBase.this.IsRunning && ConfigServiceBase.this._ConfigCallBack != null) {
                ConfigServiceBase.this._ConfigCallBack.OnReceiveCallBack(uuid, data);
            }
        }
    }

    public ConfigServiceBase(BluetoothAdapter bluetoothAdapter, Context context, String macAddress, long timeout, IConfigCallBack configCallBack) throws Exception {
        this.IsConnected = false;
        this.IsRunning = false;
        this.ReadItems = new HashMap();
        this.peripheryBluetoothCallBack = new C05043();
        this.IsRunning = true;
        this._ConfigCallBack = configCallBack;
        this._PeripheryBluetoothService = new PeripheryBluetoothService(bluetoothAdapter, context, macAddress, timeout, this.peripheryBluetoothCallBack);
        if (!this._PeripheryBluetoothService.Connect()) {
            throw new Exception("无法连接");
        }
    }

    public boolean Read(final List<String> uuids) {
        if (this.IsRunning) {
            try {
                if (this.IsConnected && this._PeripheryBluetoothService != null) {
                    this._ReadConfigs = new ConfigHandle();
                    this.ReadItems.clear();
                    new Thread(new Runnable() {
                        public void run() {
                            int i = 0;
                            try {
                                int j;
                                String uuid = "";
                                int c = 10;
                                while (i < uuids.size()) {
                                    if (i <= 0 || uuid.equals("") || ConfigServiceBase.this._ReadConfigs.IsRespond(uuid)) {
                                        uuid = (String) uuids.get(i);
                                        if (ConfigServiceBase.this._PeripheryBluetoothService.IsExistCharacteristic(uuid)) {
                                            Log.i("Read", "请求读取特征 uuid : " + uuid);
                                            ConfigServiceBase.this.ReadItems.put(uuid, null);
                                            ConfigServiceBase.this._ReadConfigs.ConfigRequest(uuid);
                                            ConfigServiceBase.this._PeripheryBluetoothService.ReadCharacteristic(uuid);
                                            c = 10;
                                        } else {
                                            uuid = "";
                                        }
                                        i++;
                                    } else if (c > 0) {
                                        Thread.sleep(100);
                                        c--;
                                    } else {
                                        if (ConfigServiceBase.this._ConfigCallBack != null) {
                                            ConfigServiceBase.this._ConfigCallBack.OnReadConfigCallBack(false, null);
                                        }
                                        Log.i("Read", "读取完毕");
                                        ConfigServiceBase.this._ReadConfigs.ConfigRequestComplete();
                                        j = 0;
                                        while (j < 3) {
                                            if (ConfigServiceBase.this._ReadConfigs.IsComplete()) {
                                                j++;
                                                Thread.sleep(1000);
                                            } else {
                                                if (ConfigServiceBase.this._ConfigCallBack != null) {
                                                    ConfigServiceBase.this._ConfigCallBack.OnReadConfigCallBack(true, ConfigServiceBase.this.ReadItems);
                                                }
                                                ConfigServiceBase.this._ReadConfigs = null;
                                            }
                                        }
                                        if (ConfigServiceBase.this._ConfigCallBack != null) {
                                            ConfigServiceBase.this._ConfigCallBack.OnReadConfigCallBack(false, null);
                                        }
                                        ConfigServiceBase.this._ReadConfigs = null;
                                    }
                                }
                                Log.i("Read", "读取完毕");
                                ConfigServiceBase.this._ReadConfigs.ConfigRequestComplete();
                                j = 0;
                                while (j < 3) {
                                    if (ConfigServiceBase.this._ReadConfigs.IsComplete()) {
                                        j++;
                                        Thread.sleep(1000);
                                    } else {
                                        if (ConfigServiceBase.this._ConfigCallBack != null) {
                                            ConfigServiceBase.this._ConfigCallBack.OnReadConfigCallBack(true, ConfigServiceBase.this.ReadItems);
                                        }
                                        ConfigServiceBase.this._ReadConfigs = null;
                                    }
                                }
                                if (ConfigServiceBase.this._ConfigCallBack != null) {
                                    ConfigServiceBase.this._ConfigCallBack.OnReadConfigCallBack(false, null);
                                }
                                ConfigServiceBase.this._ReadConfigs = null;
                            } catch (Exception ex) {
                                Log.e("Read", ex.toString());
                            }
                        }
                    }).start();
                }
            } catch (Exception ex) {
                Log.e("Read", ex.toString());
            }
        }
        return false;
    }

    public boolean Write(final LinkedHashMap<String, byte[]> uuids) {
        if (this.IsRunning) {
            try {
                if (this.IsConnected && this._PeripheryBluetoothService != null) {
                    this._WriteConfigs = new ConfigHandle();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                for (Entry entry : uuids.entrySet()) {
                                    String uuid = (String) entry.getKey();
                                    byte[] bytes = (byte[]) entry.getValue();
                                    if (ConfigServiceBase.this._PeripheryBluetoothService.IsExistCharacteristic(uuid) && bytes != null) {
                                        ConfigServiceBase.this._WriteConfigs.ConfigRequest(uuid);
                                        ConfigServiceBase.this._PeripheryBluetoothService.WriteCharacteristic(uuid, bytes);
                                        Log.d("Write", "uuid => " + uuid + " ok");
                                        int i = 0;
                                        while (!ConfigServiceBase.this._WriteConfigs.IsRespond(uuid)) {
                                            if (i <= 30) {
                                                i++;
                                                Thread.sleep(100);
                                            } else if (ConfigServiceBase.this._ConfigCallBack != null) {
                                                ConfigServiceBase.this._ConfigCallBack.OnWriteConfigCallBack(false);
                                            }
                                        }
                                        continue;
                                    }
                                }
                                Log.i("Write", "写入完毕");
                                ConfigServiceBase.this._WriteConfigs.ConfigRequestComplete();
                                int j = 0;
                                while (j < 3) {
                                    if (ConfigServiceBase.this._WriteConfigs.IsComplete()) {
                                        if (ConfigServiceBase.this._ConfigCallBack != null) {
                                            ConfigServiceBase.this._ConfigCallBack.OnWriteConfigCallBack(true);
                                        }
                                        ConfigServiceBase.this._WriteConfigs = null;
                                    }
                                    j++;
                                    Thread.sleep(1000);
                                }
                                if (ConfigServiceBase.this._ConfigCallBack != null) {
                                    ConfigServiceBase.this._ConfigCallBack.OnWriteConfigCallBack(false);
                                }
                                ConfigServiceBase.this._WriteConfigs = null;
                            } catch (Exception ex) {
                                Log.e("Write", ex.toString());
                            }
                        }
                    }).start();
                }
            } catch (Exception ex) {
                Log.e("Write", ex.toString());
            }
        }
        return false;
    }

    public boolean EnableNotification(String uuid, boolean enabled) {
        if (this.IsRunning) {
            return this._PeripheryBluetoothService.EnableNotification(uuid, enabled);
        }
        return false;
    }

    public void Dispose() {
        try {
            this.IsRunning = false;
            this._ReadConfigs = null;
            this._WriteConfigs = null;
            if (this._PeripheryBluetoothService != null) {
                this._PeripheryBluetoothService.Close();
            }
        } catch (Exception ex) {
            Log.e("Dispose", ex.toString());
        }
    }
}
