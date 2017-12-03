package com.newsolution.almhrab.Tempreture;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ConfigService extends ConfigServiceBase {
    private CharacteristicHandle _CharacteristicHandle;
    private String _Token;
    private byte[] temp;

    public ConfigService(BluetoothAdapter bluetoothAdapter, Context context, String macAddress, long timeout, IConfigCallBack configCallBack) throws Exception {
        super(bluetoothAdapter, context, macAddress, timeout, configCallBack);
        this._CharacteristicHandle = null;
        this._Token = "000000";
        this._CharacteristicHandle = new CharacteristicHandle();
    }

    public void CheckToken(String token) {
        this._Token = token;
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        Device device = new Device();
        device.Token = token;
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Token).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Token));
        super.Write(uuids);
    }

    public void ReadConfig_BT04(String version) {
        int v = Integer.parseInt(version);
        List<String> uuid = new ArrayList();
        if (v <= 5) {
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString());
        } else {
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SaveInterval).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.IsSaveOverwrite).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SavaCount).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Alarm).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Trip).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.ModelVersion).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOVoltage).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOTemp).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes1).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes2).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Name).toString());
            uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString());
        }
        super.Read(uuid);
    }

    public void ReadConfig_BT04B(String version) {
        List<String> uuid = new ArrayList();
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SaveInterval).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.IsSaveOverwrite).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SavaCount).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Alarm).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Trip).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.ModelVersion).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOVoltage).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOTemp).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes1).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes2).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Name).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString());
        super.Read(uuid);
    }

    public void ReadConfig_BT05(String version) {
        List<String> uuid = new ArrayList();
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SaveInterval).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.IsSaveOverwrite).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SavaCount).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Alarm).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Trip).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.ModelVersion).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOVoltage).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOTemp).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes1).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes2).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Name).toString());
        uuid.add(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString());
        super.Read(uuid);
    }

    public void WriteConfig_BT04(Device newConfig) {
        Device device = newConfig;
        int v = Integer.parseInt(device.Firmware);
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        if (v <= 5) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SN));
            if (device.TransmitPower != -1000) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.TransmitPower));
            }
            if (device.Interval > -1) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Interval));
            }
            if (device.SamplingInterval > -1) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SamplingInterval));
            }
            if (!device.Token.equals(this._Token)) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Token).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Token));
            }
        } else {
            device.OtherBytes.set(0, new byte[20]);
            device.OtherBytes.set(1, new byte[20]);
            device.OtherBytes.set(2, new byte[20]);
            device.OtherBytes.set(3, new byte[20]);
            device.OtherBytes.set(4, new byte[16]);
            byte[] notes = new byte[40];
            try {
                notes = device.Notes.getBytes("UTF-8");
            } catch (Exception e) {
            }
            if (notes.length > 0) {
                device.OtherBytes.set(0, BinaryUtil.PadRight(BinaryUtil.CloneRange(notes, 0, 20), 20));
            }
            byte[] description = new byte[56];
            try {
                description = device.Description.getBytes("UTF-8");
            } catch (Exception e2) {
            }
            if (description.length > 0) {
                device.OtherBytes.set(1, BinaryUtil.PadRight(BinaryUtil.CloneRange(description, 0, 20), 20));
            }
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.UTC));
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SN));
            if (device.TransmitPower != -1000) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.TransmitPower));
            }
            if (device.Interval > -1) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Interval));
            }
            if (device.SamplingInterval >= 5) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SamplingInterval));
            }
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SaveInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SaveInterval));
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Alarm).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Alarm));
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Trip).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Trip));
            if (device.LDOPower == 1) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOVoltage).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.LDOVoltage));
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.LDOPower));
            }
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes1).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.OtherBytes1));
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes2).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.OtherBytes2));
            if (device.Name != null && device.Name.length() > 0) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Name).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Name));
            }
            if (!device.Token.equals(this._Token)) {
                uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Token).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Token));
            }
        }
        super.Write(uuids);
    }

    public void WriteConfig_BT04B(Device newConfig) {
        Device device = newConfig;
        int v = Integer.parseInt(device.Firmware);
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        device.OtherBytes.set(0, new byte[20]);
        device.OtherBytes.set(1, new byte[20]);
        device.OtherBytes.set(2, new byte[20]);
        device.OtherBytes.set(3, new byte[20]);
        device.OtherBytes.set(4, new byte[16]);
        byte[] notes = new byte[40];
        try {
            notes = device.Notes.getBytes("UTF-8");
        } catch (Exception e) {
        }
        if (notes.length > 0) {
            device.OtherBytes.set(0, BinaryUtil.PadRight(BinaryUtil.CloneRange(notes, 0, 20), 20));
        }
        byte[] description = new byte[56];
        try {
            description = device.Description.getBytes("UTF-8");
        } catch (Exception e2) {
        }
        if (description.length > 0) {
            device.OtherBytes.set(1, BinaryUtil.PadRight(BinaryUtil.CloneRange(description, 0, 20), 20));
        }
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.UTC));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SN));
        if (device.TransmitPower != -1000) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.TransmitPower));
        }
        if (device.Interval > -1) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Interval));
        }
        if (device.SamplingInterval >= 5) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SamplingInterval));
        }
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SaveInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SaveInterval));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Alarm).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Alarm));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Trip).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Trip));
        if (device.LDOPower == 1) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOVoltage).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.LDOVoltage));
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.LDOPower));
        }
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes1).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.OtherBytes1));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes2).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.OtherBytes2));
        if (device.Name != null && device.Name.length() > 0) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Name).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Name));
        }
        if (!device.Token.equals(this._Token)) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Token).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Token));
        }
        super.Write(uuids);
    }

    public void WriteConfig_BT05(Device newConfig) {
        Device device = newConfig;
        device.OtherBytes.set(0, new byte[20]);
        device.OtherBytes.set(1, new byte[20]);
        device.OtherBytes.set(2, new byte[20]);
        device.OtherBytes.set(3, new byte[20]);
        device.OtherBytes.set(4, new byte[16]);
        byte[] notes = new byte[40];
        try {
            notes = device.Notes.getBytes("UTF-8");
        } catch (Exception e) {
        }
        if (notes.length > 0) {
            device.OtherBytes.set(0, BinaryUtil.PadRight(BinaryUtil.CloneRange(notes, 0, 20), 20));
        }
        byte[] description = new byte[56];
        try {
            description = device.Description.getBytes("UTF-8");
        } catch (Exception e2) {
        }
        if (description.length > 0) {
            device.OtherBytes.set(1, BinaryUtil.PadRight(BinaryUtil.CloneRange(description, 0, 20), 20));
        }
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.UTC));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SN).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SN));
        if (device.TransmitPower != -1000) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.TransmitPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.TransmitPower));
        }
        if (device.Interval > -1) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Interval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Interval));
        }
        if (device.SamplingInterval >= 5) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SamplingInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SamplingInterval));
        }
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SaveInterval).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.SaveInterval));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Alarm).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Alarm));
        if (device.TripStatus > 0) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Trip).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Trip));
        }
        if (device.LDOPower == 1) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOVoltage).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.LDOVoltage));
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.LDOPower).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.LDOPower));
        }
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes1).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.OtherBytes1));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.OtherBytes2).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.OtherBytes2));
        if (device.Name != null && device.Name.length() > 0) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Name).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Name));
        }
        if (!device.Token.equals(this._Token)) {
            uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Token).toString(), this._CharacteristicHandle.GetItemValue(device, CharacteristicType.Token));
        }
        super.Write(uuids);
    }

    public void SetDateTime() {
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.UTC).toString(), this._CharacteristicHandle.GetItemValue(new Device(), CharacteristicType.UTC));
        super.Write(uuids);
    }

    public void SetSyncDateTimeMode(int mode, int datetimeType) {
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        byte[] bytes = new byte[9];
        bytes[0] = (byte) 0;
        bytes[1] = (byte) 0;
        bytes[2] = (byte) 0;
        bytes[3] = (byte) 0;
        long timestamp = 0;
        if (datetimeType == 1) {
            timestamp = (new Date().getTime() / 1000) - 86400;
        } else if (datetimeType == 2) {
            timestamp = (new Date().getTime() / 1000) - 259200;
        } else if (datetimeType == 3) {
            timestamp = (new Date().getTime() / 1000) - 604800;
        } else if (datetimeType == 4) {
            timestamp = (new Date().getTime() / 1000) - 2592000;
        }
        if (timestamp > 0) {
            byte[] temp = StringConvertUtil.hexStringToBytes(StringUtil.PadLeft(Long.toHexString(timestamp), 8));
            bytes[0] = temp[0];
            bytes[1] = temp[1];
            bytes[2] = temp[2];
            bytes[3] = temp[3];
        }
        bytes[4] = (byte) 0;
        bytes[5] = (byte) 0;
        bytes[6] = (byte) 0;
        bytes[7] = (byte) 0;
        bytes[8] = (byte) (mode == 1 ? 1 : 0);
        Log.i("ConfigService", "SetSyncDateTimeMode:" + StringConvertUtil.bytesToHexString(bytes));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SyncMode).toString(), bytes);
        super.Write(uuids);
    }

    public void SetSyncDateTime(int mode, Date beginTime, Date endTime) {
        LinkedHashMap<String, byte[]> uuids = new LinkedHashMap();
        byte[] bytes = new byte[9];
        bytes[0] = (byte) 0;
        bytes[1] = (byte) 0;
        bytes[2] = (byte) 0;
        bytes[3] = (byte) 0;
        long timestamp = beginTime.getTime() / 1000;
        if (timestamp > 0) {
            byte[] temp = StringConvertUtil.hexStringToBytes(StringUtil.PadLeft(Long.toHexString(timestamp), 8));
            bytes[0] = temp[0];
            bytes[1] = temp[1];
            bytes[2] = temp[2];
            bytes[3] = temp[3];
        }
        bytes[4] = (byte) 0;
        bytes[5] = (byte) 0;
        bytes[6] = (byte) 0;
        bytes[7] = (byte) 0;
        timestamp = endTime.getTime() / 1000;
        if (timestamp > 0) {
            temp = StringConvertUtil.hexStringToBytes(StringUtil.PadLeft(Long.toHexString(timestamp), 8));
            bytes[4] = temp[0];
            bytes[5] = temp[1];
            bytes[6] = temp[2];
            bytes[7] = temp[3];
        }
        bytes[8] = (byte) (mode == 1 ? 1 : 0);
        Log.i("ConfigService", "SetSyncDateTime:" + StringConvertUtil.bytesToHexString(bytes));
        uuids.put(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.SyncMode).toString(), bytes);
        super.Write(uuids);
    }

    public void Sync(boolean enable) {
        super.EnableNotification(this._CharacteristicHandle.GetCharacteristicUUID(CharacteristicType.Sysn).toString(), enable);
    }

    public void Dispose() {
        super.Dispose();
    }

    public Device GetCofing(HashMap<String, byte[]> uuids) {
        try {
            Device device = this._CharacteristicHandle.Set(uuids);
            byte[] notes = BinaryUtil.TrimEnd((byte[]) device.OtherBytes.get(0));
            if (notes != null) {
                device.Notes = new String(notes, "UTF-8").trim();
            }
            byte[] description = BinaryUtil.TrimEnd((byte[]) device.OtherBytes.get(1));
            if (description == null) {
                return device;
            }
            device.Description = new String(description, "UTF-8").trim();
            return device;
        } catch (Exception ex) {
            Log.e("ConfigService", ex.toString());
            return null;
        }
    }

    public List<byte[]> GetDataBytes(byte[] data, String hardwareModel, String firmware, int syncMode) {
        try {
            List<byte[]> ls = new ArrayList();
            if (syncMode == 1) {
                int dataType = Integer.parseInt(StringUtil.PadLeft(Integer.toBinaryString(data[0]), 8).substring(0, 3), 2);
                if (dataType == 2 || dataType == 3) {
                    return null;
                }
                if (dataType == 1) {
                    if (data.length >= 13) {
                        ls.add(BinaryUtil.CloneRange(data, 10, 3));
                    }
                    if (data.length >= 16) {
                        ls.add(BinaryUtil.CloneRange(data, 13, 3));
                    }
                    if (data.length < 19) {
                        return ls;
                    }
                    ls.add(BinaryUtil.CloneRange(data, 16, 3));
                    return ls;
                }
                if (data.length >= 5) {
                    ls.add(BinaryUtil.CloneRange(data, 2, 3));
                }
                if (data.length >= 8) {
                    ls.add(BinaryUtil.CloneRange(data, 5, 3));
                }
                if (data.length >= 11) {
                    ls.add(BinaryUtil.CloneRange(data, 8, 3));
                }
                if (data.length >= 14) {
                    ls.add(BinaryUtil.CloneRange(data, 11, 3));
                }
                if (data.length >= 17) {
                    ls.add(BinaryUtil.CloneRange(data, 14, 3));
                }
                if (data.length < 20) {
                    return ls;
                }
                ls.add(BinaryUtil.CloneRange(data, 17, 3));
                return ls;
            } else if (((hardwareModel.equals("3A01") && Integer.parseInt(firmware) >= 12) || ((hardwareModel.equals("3901") && Integer.parseInt(firmware) >= 25) || (hardwareModel.equals("3C01") && Integer.parseInt(firmware) >= 26))) && data.length == 4 && ((data[0] == (byte) 42 || data[0] == (byte) 36) && data[3] == (byte) 35)) {
                return null;
            } else {
                if (!CRC(BinaryUtil.CloneRange(data, 0, data.length - 1)).equals(StringConvertUtil.bytesToHexString(BinaryUtil.CloneRange(data, data.length - 1, 1)))) {
                    return null;
                }
                int len;
                if ((!hardwareModel.equals("3901") || Integer.parseInt(firmware) >= 20) && (!hardwareModel.equals("3A01") || Integer.parseInt(firmware) >= 7)) {
                    len = 7;
                } else {
                    len = 6;
                }
                if (data.length >= len + 3) {
                    ls.add(BinaryUtil.CloneRange(data, 0, len));
                }
                if (data.length < (len * 2) + 3) {
                    return ls;
                }
                ls.add(BinaryUtil.CloneRange(data, len, len));
                return ls;
            }
        } catch (Exception ex) {
            Log.e("GetData", ex.toString());
            return null;
        }
    }

    public String CRC(byte[] data) {
        int num = 0;
        for (byte b : data) {
            num = (b + num) % 65535;
        }
        String hex = Integer.toHexString(num);
        if (hex.length() % 2 > 0) {
            hex = "0" + hex;
        }
        return hex.substring(hex.length() - 2);
    }
}
