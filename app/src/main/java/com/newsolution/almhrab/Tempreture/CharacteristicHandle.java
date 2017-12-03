package com.newsolution.almhrab.Tempreture;

import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class CharacteristicHandle {
    public List<Characteristic> CharacteristicList;
    private byte[][] r14;

    public class Characteristic {
        public CharacteristicType Type;
        public UUID UUID;

        public Characteristic(String uuid, CharacteristicType type) {
            UUID uuid2 = this.UUID;
            this.UUID = UUID.fromString(uuid);
            this.Type = type;
        }
    }

    public CharacteristicHandle() {
        if (this.CharacteristicList == null) {
            this.CharacteristicList = new ArrayList();
            this.CharacteristicList.add(new Characteristic("27763B11-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.SN));
            this.CharacteristicList.add(new Characteristic("27763B12-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.Interval));
            this.CharacteristicList.add(new Characteristic("27763B14-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.TransmitPower));
            this.CharacteristicList.add(new Characteristic("27763B13-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.Token));
            this.CharacteristicList.add(new Characteristic("27763B15-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.SamplingInterval));
            this.CharacteristicList.add(new Characteristic("27763B16-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.SaveInterval));
            this.CharacteristicList.add(new Characteristic("27763B17-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.IsSaveOverwrite));
            this.CharacteristicList.add(new Characteristic("27763B18-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.SavaCount));
            this.CharacteristicList.add(new Characteristic("27763B19-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.Alarm));
            this.CharacteristicList.add(new Characteristic("27763B20-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.UTC));
            this.CharacteristicList.add(new Characteristic("27763B21-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.Sysn));
            this.CharacteristicList.add(new Characteristic("27763B22-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.Trip));
            this.CharacteristicList.add(new Characteristic("27763B23-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.ModelVersion));
            this.CharacteristicList.add(new Characteristic("27763B24-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.LDOVoltage));
            this.CharacteristicList.add(new Characteristic("27763B25-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.LDOTemp));
            this.CharacteristicList.add(new Characteristic("27763B26-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.LDOPower));
            this.CharacteristicList.add(new Characteristic("27763B27-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.OtherBytes1));
            this.CharacteristicList.add(new Characteristic("27763B28-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.OtherBytes2));
            this.CharacteristicList.add(new Characteristic("27763B29-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.OtherBytes3));
            this.CharacteristicList.add(new Characteristic("27763B2A-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.OtherBytes4));
            this.CharacteristicList.add(new Characteristic("27763B2B-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.OtherBytes5));
            this.CharacteristicList.add(new Characteristic("27763B40-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.Name));
            this.CharacteristicList.add(new Characteristic("27763B31-999C-4D6A-9FC4-C7272BE10900", CharacteristicType.SyncMode));
        }
    }

    public UUID GetCharacteristicUUID(CharacteristicType characteristicType) {
        for (Characteristic item : this.CharacteristicList) {
            if (characteristicType.equals(item.Type)) {
                return item.UUID;
            }
        }
        return null;
    }

    public CharacteristicType GetCharacteristicType(UUID uuid) {
        for (Characteristic item : this.CharacteristicList) {
            if (item.UUID.toString().equals(uuid.toString())) {
                return item.Type;
            }
        }
        return CharacteristicType.Unknown;
    }

    public byte[] GetItemValue(Device bt, CharacteristicType type) {
        try {
            if (type.equals(CharacteristicType.SN)) {
                Log.i("GetItemValue", "SN:" + bt.SN);
                return StringConvertUtil.hexStringToBytes(bt.SN);
            }
            byte[] b0;
            byte[] b1;
            byte[] b2;
            byte[] b3;
            byte[] b4;
            byte[] b5;
            if (!type.equals(CharacteristicType.Token) || bt.Token == null) {
                if (!type.equals(CharacteristicType.Interval) || bt.Interval == -1) {
                    if (!type.equals(CharacteristicType.TransmitPower) || bt.TransmitPower == -1) {
                        if (!type.equals(CharacteristicType.SamplingInterval) || bt.SamplingInterval == -1) {
                            byte[] bytes;
                            if (!type.equals(CharacteristicType.SaveInterval) || (bt.SaveInterval == -1 && bt.SaveInterval2 == -1)) {
                                if (type.equals(CharacteristicType.IsSaveOverwrite)) {
                                    Log.i("GetItemValue", "IsSaveOverwrite:" + bt.IsSaveOverwrite);
                                    if (bt.IsSaveOverwrite) {
                                        return StringConvertUtil.uint8ToByte(0);
                                    }
                                    return StringConvertUtil.uint8ToByte(1);
                                }
                                if (type.equals(CharacteristicType.SavaCount)) {
                                    Log.i("GetItemValue", "SavaCount:" + bt.SavaCount);
                                    return StringConvertUtil.uint16ToByte(bt.SavaCount);
                                }
                                if (type.equals(CharacteristicType.Alarm)) {
                                    Log.i("GetItemValue", "LT:" + bt.LT + " " + bt.HT);
                                    byte b02 = (byte) new Double(bt.LT).intValue();
                                    byte b12 = (byte) new Double(bt.HT).intValue();
                                    return new byte[]{b02, b12};
                                }
                                if (type.equals(CharacteristicType.UTC)) {
                                    Date utc = DateUtil.GetUTCTime();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(utc);
                                    b0 = StringConvertUtil.uint8ToByte(Integer.parseInt(String.valueOf(cal.get(1)).substring(1)));
                                    b1 = StringConvertUtil.uint8ToByte(cal.get(2) + 1);
                                    b2 = StringConvertUtil.uint8ToByte(cal.get(5));
                                    b3 = StringConvertUtil.uint8ToByte(cal.get(11));
                                    b4 = StringConvertUtil.uint8ToByte(cal.get(12));
                                    b5 = StringConvertUtil.uint8ToByte(cal.get(13));
                                    bytes = StringConvertUtil.byteMergerMultiple(b0, b1, b2, b3, b4, b5);
                                    Log.i("GetItemValue", "UTC:" + StringConvertUtil.bytesToHexString(bytes));
                                    return bytes;
                                }
                                if (type.equals(CharacteristicType.Sysn)) {
                                    return null;
                                }
                                if (type.equals(CharacteristicType.Trip)) {
                                    bytes = StringConvertUtil.uint8ToByte(bt.TripStatus);
                                    Log.i("GetItemValue", "TripStatus:" + StringConvertUtil.bytesToHexString(bytes));
                                    return bytes;
                                }
                                if (type.equals(CharacteristicType.LDOVoltage)) {
                                    bytes = StringConvertUtil.uint16ToByte(new Double(bt.LDOVoltage).intValue());
                                    Log.i("GetItemValue", "LDOVoltage:" + StringConvertUtil.bytesToHexString(bytes));
                                    return bytes;
                                }
                                if (type.equals(CharacteristicType.LDOTemp)) {
                                    return null;
                                }
                                if (type.equals(CharacteristicType.LDOPower)) {
                                    bytes = StringConvertUtil.uint8ToByte(bt.LDOPower);
                                    Log.i("GetItemValue", "LDOPower:" + StringConvertUtil.bytesToHexString(bytes));
                                    return bytes;
                                }
                                if (type.equals(CharacteristicType.OtherBytes1)) {
                                    return (byte[]) bt.OtherBytes.get(0);
                                }
                                if (type.equals(CharacteristicType.OtherBytes2)) {
                                    return (byte[]) bt.OtherBytes.get(1);
                                }
                                if (type.equals(CharacteristicType.OtherBytes3)) {
                                    return (byte[]) bt.OtherBytes.get(2);
                                }
                                if (type.equals(CharacteristicType.OtherBytes4)) {
                                    return (byte[]) bt.OtherBytes.get(3);
                                }
                                if (type.equals(CharacteristicType.OtherBytes5)) {
                                    return (byte[]) bt.OtherBytes.get(4);
                                }
                                if (!type.equals(CharacteristicType.Name)) {
                                    return null;
                                }
                                r14 = new byte[2][];
                                r14[0] = new byte[]{(byte) bt.Name.getBytes("UTF-8").length};
                                r14[1] = bt.Name.getBytes("UTF-8");
                                bytes = BinaryUtil.PadRight(BinaryUtil.MultipleMerge(r14), 8);
                                Log.i("GetItemValue", "Name:" + StringConvertUtil.bytesToHexString(bytes));
                                return bytes;
                            }
                            b0 = StringConvertUtil.hexStringToBytes(StringConvertUtil.LittleEndian(StringUtil.PadLeft(Integer.toHexString(bt.SaveInterval), 4)));
                            b1 = StringConvertUtil.hexStringToBytes(StringConvertUtil.LittleEndian(StringUtil.PadLeft(Integer.toHexString(bt.SaveInterval2), 4)));
                            bytes = StringConvertUtil.byteMergerMultiple(b0, b1);
                            Log.i("GetItemValue", "SaveInterval:" + StringConvertUtil.bytesToHexString(bytes));
                            return bytes;
                        }
                        Log.i("GetItemValue", "SamplingInterval:" + bt.SamplingInterval);
                        return StringConvertUtil.hexStringToBytes(StringConvertUtil.LittleEndian(StringUtil.PadLeft(Integer.toHexString(bt.SamplingInterval), 8)));
                    }
                    Log.i("GetItemValue", "TransmitPower:" + bt.TransmitPower);
                    return StringConvertUtil.hexStringToBytes(StringUtil.PadLeft(GetTransmitPowerIndex(bt.TransmitPower) + "", 2));
                }
                Log.i("GetItemValue", "Interval:" + bt.Interval);
                return StringConvertUtil.uint16ToByte(bt.Interval);
            }
            Log.i("GetItemValue", "Token:" + bt.Token);
            String strToken = StringUtil.PadLeft(bt.Token, 6);
            if (strToken.length() != 6) {
                return null;
            }
            b0 = StringConvertUtil.uint8ToByte(Integer.parseInt(strToken.substring(0, 1)));
            b1 = StringConvertUtil.uint8ToByte(Integer.parseInt(strToken.substring(1, 2)));
            b2 = StringConvertUtil.uint8ToByte(Integer.parseInt(strToken.substring(2, 3)));
            b3 = StringConvertUtil.uint8ToByte(Integer.parseInt(strToken.substring(3, 4)));
            b4 = StringConvertUtil.uint8ToByte(Integer.parseInt(strToken.substring(4, 5)));
            b5 = StringConvertUtil.uint8ToByte(Integer.parseInt(strToken.substring(5, 6)));
            return StringConvertUtil.byteMergerMultiple(b0, b1, b2, b3, b4, b5);
        } catch (Exception ex) {
            Log.e("GetItemValue", "异常：" + ex.toString());
            return null;
        }
    }

    public Device SetItemValue(Device bt, CharacteristicType type, byte[] res) {
        try {
            Log.i("SetItemValue", StringConvertUtil.bytesToHexString(res));
            if (type.equals(CharacteristicType.SN)) {
                bt.SN = StringConvertUtil.bytesToHexString(res);
            } else if (type.equals(CharacteristicType.Interval)) {
                bt.Interval = StringConvertUtil.byteToUint16(res);
            } else if (type.equals(CharacteristicType.TransmitPower)) {
                bt.TransmitPower = GetTransmitPower(Integer.parseInt(StringConvertUtil.bytesToHexString(res), 16));
            } else if (type.equals(CharacteristicType.SamplingInterval)) {
                bt.SamplingInterval = Integer.parseInt(StringConvertUtil.LittleEndian(StringConvertUtil.bytesToHexString(res)), 16);
            } else if (type.equals(CharacteristicType.SaveInterval)) {
                bt.SaveInterval = Integer.parseInt(StringConvertUtil.bytesToHexString(new byte[]{res[0], res[1]}), 16);
                bt.SaveInterval2 = Integer.parseInt(StringConvertUtil.bytesToHexString(new byte[]{res[2], res[3]}), 16);
            } else if (type.equals(CharacteristicType.IsSaveOverwrite)) {
                if (res[0] == (byte) 1) {
                    bt.IsSaveOverwrite = false;
                } else {
                    bt.IsSaveOverwrite = true;
                }
            } else if (type.equals(CharacteristicType.SavaCount)) {
                bt.SavaCount = StringConvertUtil.byteToUint16(res);
            } else if (type.equals(CharacteristicType.Alarm)) {
                bt.LT = (double) res[0];
                bt.HT = (double) res[1];
            } else if (type.equals(CharacteristicType.UTC)) {
                bt.UTCTime = DateUtil.ToData("20" + res[0] + "-" + res[1] + "-" + res[2] + " " + res[3] + ":" + res[4] + ":" + res[5], "yyyy-MM-dd HH:mm:ss");
            } else if (!type.equals(CharacteristicType.Sysn)) {
                if (type.equals(CharacteristicType.Trip)) {
                    bt.TripStatus = StringConvertUtil.byteToInt(new byte[]{res[0]});
                } else if (type.equals(CharacteristicType.ModelVersion)) {
                    bt.HardwareModel = StringConvertUtil.bytesToHexString(new byte[]{res[0], res[1]});
                    bt.Firmware = StringConvertUtil.bytesToHexString(new byte[]{res[2]});
                } else if (type.equals(CharacteristicType.LDOVoltage)) {
                    bt.LDOVoltage = (double) StringConvertUtil.byteToUint16(res);
                } else if (type.equals(CharacteristicType.LDOTemp)) {
                    bt.LDOTemp = (double) StringConvertUtil.byteToUint16(res);
                } else if (type.equals(CharacteristicType.LDOPower)) {
                    bt.LDOPower = StringConvertUtil.byteToInt(new byte[]{res[0]});
                } else if (type.equals(CharacteristicType.OtherBytes1)) {
                    bt.OtherBytes.set(0, BinaryUtil.CloneRange(res, 0, 20));
                } else if (type.equals(CharacteristicType.OtherBytes2)) {
                    bt.OtherBytes.set(1, BinaryUtil.CloneRange(res, 0, 20));
                } else if (type.equals(CharacteristicType.OtherBytes3)) {
                    bt.OtherBytes.set(2, BinaryUtil.CloneRange(res, 0, 20));
                } else if (type.equals(CharacteristicType.OtherBytes4)) {
                    bt.OtherBytes.set(3, BinaryUtil.CloneRange(res, 0, 20));
                } else if (type.equals(CharacteristicType.OtherBytes5)) {
                    bt.OtherBytes.set(4, BinaryUtil.CloneRange(res, 0, 16));
                } else if (type.equals(CharacteristicType.Name)) {
                    bt.Name = new String(BinaryUtil.CloneRange(res, 1, res[0]), "UTF-8").trim();
                }
            }
        } catch (Exception ex) {
            Log.e("SetItemValue", "异常：" + ex.toString());
        }
        return bt;
    }

    public Device Set(HashMap<String, byte[]> uuids) {
        Device device = new Device();
        try {
            for (Entry entry : uuids.entrySet()) {
                byte[] bytes = (byte[]) entry.getValue();
                CharacteristicType type = GetCharacteristicType(UUID.fromString((String) entry.getKey()));
                if (type != CharacteristicType.Unknown) {
                    SetItemValue(device, type, bytes);
                }
            }
        } catch (Exception ex) {
            Log.e("SetItemValue", "异常：" + ex.toString());
        }
        return device;
    }

    public static int GetTransmitPower(int index) {
        try {
            return Integer.parseInt(new String[]{"4", "0", "-4", "-8", "-12", "-16", "-20", "-30"}[index]);
        } catch (Exception ex) {
            Log.e("GetTransmitPower", ex.toString());
            return -20;
        }
    }

    public static int GetTransmitPowerIndex(int value) {
        try {
            String[] strings = new String[]{"4", "0", "-4", "-8", "-12", "-16", "-20", "-30"};
            for (int i = 0; i < strings.length; i++) {
                if (value >= Integer.parseInt(strings[i])) {
                    return i;
                }
            }
        } catch (Exception ex) {
            Log.e("GetTransmitPowerIndex", ex.toString());
        }
        return 0;
    }
}
