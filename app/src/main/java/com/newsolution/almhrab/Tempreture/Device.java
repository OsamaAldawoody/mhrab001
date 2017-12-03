package com.newsolution.almhrab.Tempreture;

import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Device extends BLE {
    public static final int CS_sRGB = 1000;
    public String AlarmType;
    public int Battery;
    public String Description;
    public String Firmware;
    public double HT;
    public String HardwareModel;
    public double Humidity;
    public int Interval;
    public boolean IsSaveOverwrite;
    public int LDOPower;
    public double LDOTemp;
    public double LDOVoltage;
    public double LT;
    public int MeasuredPower;
    public String Notes;
    public List<byte[]> OtherBytes;
    public String SN;
    public int SamplingInterval;
    public int SavaCount;
    public int SaveInterval;
    public int SaveInterval2;
    public double Temperature;
    public String Token;
    public int TransmitPower;
    public int TripStatus;
    public Date UTCTime;

    public Device() {
        this.TransmitPower = -1000;
        this.Interval = -1000;
        this.SamplingInterval = -1000;
        this.Temperature = -1000.0d;
        this.Humidity = -1000.0d;
        this.LT = -1000.0d;
        this.HT = -1000.0d;
        this.AlarmType = "00";
        this.LDOPower = 0;
        this.LDOVoltage = -1000.0d;
        this.LDOTemp = -1000.0d;
        this.Name = "";
        this.Notes = "";
        this.Description = "";
        this.OtherBytes = new ArrayList();
        this.OtherBytes.add(new byte[20]);
        this.OtherBytes.add(new byte[20]);
        this.OtherBytes.add(new byte[20]);
        this.OtherBytes.add(new byte[20]);
        this.OtherBytes.add(new byte[16]);
    }

    public boolean fromScanData(BLE ble) {
        return fromScanData(ble.Name, ble.MacAddress, ble.RSSI, ble.ScanData);
    }

    public boolean fromScanData(String name, String macAddress, int rssi, byte[] scanData) {
        try {
            this.Name = name;
            this.MacAddress = macAddress;
            this.RSSI = rssi;
            this.ScanData = scanData;
            this.LastScanTime = new Date();
            String serviceData = BroadcastPacketsUtil.GetScanParam(StringConvertUtil.bytesToHexString(scanData), "16");
            if (Integer.parseInt(serviceData.substring(4, 6), 16) >= 11) {
                this.HardwareModel = serviceData.substring(6, 10).toUpperCase();
                if (!this.HardwareModel.equals("3901") && !this.HardwareModel.equals("3A01") && !this.HardwareModel.equals("3C01")) {
                    return false;
                }
                this.Firmware = serviceData.substring(10, 12);
                this.SN = serviceData.substring(12, 20);
                this.Battery = Integer.parseInt(serviceData.substring(20, 22), 16);
            }
            int l_Temperature = Integer.parseInt(serviceData.substring(22, 24), 16);
            this.Temperature = -1000.0d;
            this.Humidity = -1000.0d;
            if (l_Temperature == 4) {
                String s_Temperature = StringConvertUtil.hexString2binaryString(serviceData.substring(24, 26));
                if (s_Temperature.substring(0, 1).equals("0")) {
                    int symbol = 1;
                    if (s_Temperature.substring(1, 2).equals("1")) {
                        symbol = -1;
                    }
                    this.Temperature = ((double) Integer.parseInt(StringConvertUtil.binaryString2hexString("00" + s_Temperature.substring(2, 8)) + serviceData.substring(26, 28), 16)) / 100.0d;
                    this.Temperature *= (double) symbol;
                }
                if (this.HardwareModel.equals("3901") || this.HardwareModel.equals("3C01")) {
                    String s_Humidity = StringConvertUtil.hexString2binaryString(serviceData.substring(28, 30));
                    if (s_Temperature.substring(0, 1).equals("0")) {
                        this.Humidity = ((double) Integer.parseInt(StringConvertUtil.binaryString2hexString("00" + s_Humidity.substring(2, 8)) + serviceData.substring(30, 32), 16)) / 100.0d;
                    }
                }
            }
            int n_3 = ((l_Temperature * 2) + 24) + 4;
            this.AlarmType = "00";
            if (serviceData.length() >= 38) {
                this.AlarmType = serviceData.substring(n_3, n_3 + 2);
            }
            if (this.Temperature != -1000.0d) {
                this.Temperature = Double.parseDouble(StringUtil.ToString(this.Temperature, 1));
            }
            if (this.Humidity != -1000.0d) {
                this.Humidity = Double.parseDouble(StringUtil.ToString(this.Humidity, 1));
            }
            return true;
        } catch (Exception ex) {
            Log.e("DeviceBase", "fromScanData" + StringConvertUtil.bytesToHexString(scanData) + " ex:" + ex.toString());
            return false;
        }
    }

    public boolean fromNotificationData(byte[] data) {
        String strLog = StringConvertUtil.bytesToHexString(data);
        try {
            Log.d("fromNotificationData", " data:" + strLog);
            if ((data.length == 6 && !StringConvertUtil.bytesToHexString(data).equals("000000000000")) || (data.length == 7 && !StringConvertUtil.bytesToHexString(data).equals("00000000000000"))) {
                int firmware = 15;
                if (!this.Firmware.equals("")) {
                    firmware = Integer.parseInt(this.Firmware);
                }
                String d;
                int month;
                int day;
                int hour;
                int minute;
                int second;
                int battery;
                int humidity;
                double temperature;
                Date rtc;
                if (this.HardwareModel.equals("3901")) {
                    if (firmware != 15 && firmware < 18) {
                        d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                        month = Integer.parseInt(d.substring(0, 4), 2);
                        day = Integer.parseInt("000" + d.substring(4, 9), 2);
                        hour = Integer.parseInt("000" + d.substring(9, 14), 2);
                        minute = Integer.parseInt("00" + d.substring(14, 20), 2);
                        second = Integer.parseInt("00" + d.substring(20, 26), 2);
                        battery = Integer.parseInt("0" + d.substring(26, 33), 2);
                        humidity = Integer.parseInt("0" + d.substring(33, 40), 2);
                        int temperature2 = Integer.parseInt(d.substring(40, 48), 2);
                        this.UTCTime = new Date(DateUtil.GetUTCTime().getYear(), month - 1, day, hour, minute, second);
                        this.Battery = battery;
                        if (temperature2 > 128) {
                            this.Temperature = (double) (temperature2 + InputDeviceCompat.SOURCE_ANY);
                        } else {
                            this.Temperature = (double) temperature2;
                        }
                        this.Humidity = (double) humidity;
                        return true;
                    } else if (firmware < 20) {
                        d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                        month = Integer.parseInt(d.substring(0, 4), 2);
                        day = Integer.parseInt("000" + d.substring(4, 9), 2);
                        hour = Integer.parseInt("000" + d.substring(9, 14), 2);
                        minute = Integer.parseInt("00" + d.substring(14, 20), 2);
                        second = Integer.parseInt("00" + d.substring(20, 26), 2);
                        humidity = Integer.parseInt("0" + d.substring(26, 33), 2);
                        temperature = (double) Integer.parseInt(d.substring(33, 44), 2);
                        if (humidity < 0 || humidity > 100) {
                            humidity = -1000;
                        }
                        if (temperature < 1250.0d) {
                            temperature /= 10.0d;
                        } else {
                            temperature = (temperature - 2048.0d) / 10.0d;
                        }
                        this.UTCTime = new Date(DateUtil.GetUTCTime().getYear(), month - 1, day, hour, minute, second);
                        if (temperature < -200.0d || temperature > 600.0d) {
                            this.Temperature = -1000.0d;
                        } else {
                            this.Temperature = temperature;
                        }
                        this.Humidity = (double) humidity;
                        return true;
                    } else if (firmware < 22) {
                        d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                        rtc = new Date((Long.parseLong(d.substring(0, 32), 2) * 1000) - ((long) ((DateUtil.GetTimeZone() * 60)
                                * CS_sRGB)));
                        humidity = Integer.parseInt("0" + d.substring(32, 39), 2) * 2;
                        temperature = (double) Integer.parseInt(d.substring(39, 48) + d.substring(54, 56), 2);
                        if (humidity < 0 || humidity > 100) {
                            humidity = -1000;
                        }
                        if (temperature < 1250.0d) {
                            temperature /= 10.0d;
                        } else {
                            temperature = (temperature - 2048.0d) / 10.0d;
                        }
                        this.UTCTime = rtc;
                        if (temperature < -200.0d || temperature > 600.0d) {
                            this.Temperature = -1000.0d;
                        } else {
                            this.Temperature = temperature;
                        }
                        this.Humidity = (double) humidity;
                        return true;
                    } else {
                        d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                        rtc = new Date((Long.parseLong(d.substring(0, 32), 2) * 1000) - ((long) ((DateUtil.GetTimeZone() * 60)
                                * CS_sRGB)));
                        humidity = Integer.parseInt("0" + d.substring(32, 39), 2);
                        temperature = (double) Integer.parseInt(d.substring(39, 50), 2);
                        if (humidity < 0 || humidity > 100) {
                            humidity = -1000;
                        }
                        if (temperature < 1250.0d) {
                            temperature /= 10.0d;
                        } else {
                            temperature = (temperature - 2048.0d) / 10.0d;
                        }
                        this.UTCTime = rtc;
                        if (temperature < -200.0d || temperature > 600.0d) {
                            this.Temperature = -1000.0d;
                        } else {
                            this.Temperature = temperature;
                        }
                        this.Humidity = (double) humidity;
                        return true;
                    }
                } else if (this.HardwareModel.equals("3C01")) {
                    if (firmware >= 26) {
                        d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                        rtc = new Date((Long.parseLong(d.substring(0, 32), 2) * 1000) - ((long) ((DateUtil.GetTimeZone() * 60) * CS_sRGB)));
                        humidity = Integer.parseInt("0" + d.substring(32, 39), 2);
                        temperature = (double) Integer.parseInt(d.substring(39, 50), 2);
                        if (humidity < 0 || humidity > 100) {
                            humidity = -1000;
                        }
                        if (temperature < 1250.0d) {
                            temperature /= 10.0d;
                        } else {
                            temperature = (temperature - 2048.0d) / 10.0d;
                        }
                        this.UTCTime = rtc;
                        if (temperature < -200.0d || temperature > 600.0d) {
                            this.Temperature = -1000.0d;
                        } else {
                            this.Temperature = temperature;
                        }
                        this.Humidity = (double) humidity;
                        return true;
                    }
                } else if (firmware < 7) {
                    d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                    month = Integer.parseInt(d.substring(0, 4), 2);
                    day = Integer.parseInt("000" + d.substring(4, 9), 2);
                    hour = Integer.parseInt("000" + d.substring(9, 14), 2);
                    minute = Integer.parseInt("00" + d.substring(14, 20), 2);
                    second = Integer.parseInt("00" + d.substring(20, 26), 2);
                    battery = Integer.parseInt("0" + d.substring(26, 33), 2);
                    temperature = (double) Integer.parseInt(d.substring(33, 44), 2);
                    if (temperature < 1250.0d) {
                        temperature /= 10.0d;
                    } else {
                        temperature = (temperature - 2048.0d) / 10.0d;
                    }
                    this.UTCTime = new Date(DateUtil.GetUTCTime().getYear(), month - 1, day, hour, minute, second);
                    this.Battery = battery;
                    if (temperature < -200.0d || temperature > 600.0d) {
                        this.Temperature = -1000.0d;
                    } else {
                        this.Temperature = temperature;
                    }
                    this.Humidity = (double) -1000;
                    return true;
                } else if (firmware < 9) {
                    d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                    rtc = new Date((Long.parseLong(d.substring(0, 32), 2) * 1000) - ((long) ((DateUtil.GetTimeZone() * 60)
                            * CS_sRGB)));
                    battery = Integer.parseInt("0" + d.substring(32, 39), 2);
                    temperature = (double) Integer.parseInt(d.substring(39, 48) + d.substring(52, 54), 2);
                    if (temperature < 1250.0d) {
                        temperature /= 10.0d;
                    } else {
                        temperature = (temperature - 2048.0d) / 10.0d;
                    }
                    this.UTCTime = rtc;
                    this.Battery = battery;
                    if (temperature < -200.0d || temperature > 600.0d) {
                        this.Temperature = -1000.0d;
                    } else {
                        this.Temperature = temperature;
                    }
                    this.Humidity = -1000.0d;
                    return true;
                } else {
                    d = StringConvertUtil.hexString2binaryString(StringConvertUtil.bytesToHexString(data));
                    rtc = new Date((Long.parseLong(d.substring(0, 32), 2) * 1000) - ((long) ((DateUtil.GetTimeZone() * 60) * CS_sRGB)));
                    battery = Integer.parseInt("0" + d.substring(32, 39), 2);
                    temperature = (double) Integer.parseInt(d.substring(39, 50), 2);
                    if (temperature < 1250.0d) {
                        temperature /= 10.0d;
                    } else {
                        temperature = (temperature - 2048.0d) / 10.0d;
                    }
                    this.UTCTime = rtc;
                    this.Battery = battery;
                    if (temperature < -200.0d || temperature > 600.0d) {
                        this.Temperature = -1000.0d;
                    } else {
                        this.Temperature = temperature;
                    }
                    this.Humidity = -1000.0d;
                    return true;
                }
            }
        } catch (Exception ex) {
            Log.e("Temperature", "fromNotificationData" + ex.toString() + " data:" + strLog);
        }
        return false;
    }

    public int ToRssi(int res) {
        if (res > 128) {
            return res + InputDeviceCompat.SOURCE_ANY;
        }
        return res;
    }

    public int ConverRssi(int res) {
        if (res < -128 || res >= 0) {
            return res;
        }
        return res + 256;
    }
}
