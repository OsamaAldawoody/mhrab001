package com.newsolution.almhrab.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsolution.almhrab.AppConfig;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.Tempreture.BLE;
import com.newsolution.almhrab.Tempreture.Device;
import com.newsolution.almhrab.Tempreture.MeasuringDistance;
import com.newsolution.almhrab.Tempreture.StringUtil;
import com.newsolution.almhrab.Tempreture.TemperatureUnitUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListView_ScanDeviceListAdapter extends BaseAdapter {
    public int OpenIntentType = 0;
    public String SearchKey = "";
    public int SortType = 0;
    private Activity _Activity;
    private LayoutInflater _Inflater;
    public List<BLE> items;

    class C03002 implements Comparator<BLE> {
        C03002() {
        }

        public int compare(BLE lhs, BLE rhs) {
            return new Double(ListView_ScanDeviceListAdapter.this.GetDistance(lhs)).compareTo(new Double(ListView_ScanDeviceListAdapter.this.GetDistance(rhs)));
        }
    }

    class C03013 implements Comparator<BLE> {
        C03013() {
        }

        public int compare(BLE lhs, BLE rhs) {
            return new Double((double) rhs.RSSI).compareTo(new Double((double) lhs.RSSI));
        }
    }

    class C03024 implements Comparator<BLE> {
        C03024() {
        }

        public int compare(BLE lhs, BLE rhs) {
            return new Double((double) ListView_ScanDeviceListAdapter.this.GetBattery(lhs)).compareTo(new Double((double) ListView_ScanDeviceListAdapter.this.GetBattery(rhs)));
        }
    }

    class C03035 implements Comparator<BLE> {
        C03035() {
        }

        public int compare(BLE lhs, BLE rhs) {
            return new Double((double) ListView_ScanDeviceListAdapter.this.GetSN(lhs)).compareTo(new Double((double) ListView_ScanDeviceListAdapter.this.GetSN(rhs)));
        }
    }

    public final class ViewHolder {
        public ImageView btnDetail;
        public ImageView imgBattery;
        public ImageView imgRssi;
        public LinearLayout layoutEddystone;
        public LinearLayout layoutIbeacon;
        public LinearLayout layoutTemperature;
        public TextView txtBattery;
        public TextView txtDistance;
        public TextView txtHumidity;
        public TextView txtMacAddress;
        public TextView txtMajor;
        public TextView txtMinor;
        public TextView txtName;
        public TextView txtProtocol;
        public TextView txtRSSI;
        public TextView txtSN;
        public TextView txtTemperature;
        public TextView txtUUID;
        public TextView txtUrl;
    }

    public ListView_ScanDeviceListAdapter(Activity activity, int openIntentType) {
        this._Activity = activity;
        this.items = new ArrayList();
        this._Inflater = activity.getLayoutInflater();
        this.OpenIntentType = openIntentType;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int position) {
        return this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        try {
            ViewHolder holder;
            BLE ble = (BLE) this.items.get(position);
            if (view == null) {
                holder = new ViewHolder();
                view = this._Inflater.inflate(R.layout.control_scan_device_list, null);
                holder.txtRSSI = (TextView) view.findViewById(R.id.txtRSSI);
                holder.txtDistance = (TextView) view.findViewById(R.id.txtDistance);
                holder.txtName = (TextView) view.findViewById(R.id.txtName);
                holder.txtMajor = (TextView) view.findViewById(R.id.txtMajor);
                holder.txtMinor = (TextView) view.findViewById(R.id.txtMinor);
                holder.txtUrl = (TextView) view.findViewById(R.id.txtUrl);
                holder.txtMacAddress = (TextView) view.findViewById(R.id.txtMacAddress);
                holder.txtProtocol = (TextView) view.findViewById(R.id.txtProtocol);
                holder.txtSN = (TextView) view.findViewById(R.id.txtSN);
                holder.btnDetail = (ImageView) view.findViewById(R.id.btnDetail);
                holder.imgRssi = (ImageView) view.findViewById(R.id.imgRssi);
                holder.imgBattery = (ImageView) view.findViewById(R.id.imgBattery);
                holder.txtBattery = (TextView) view.findViewById(R.id.txtBattery);
                holder.layoutIbeacon = (LinearLayout) view.findViewById(R.id.layoutIbeacon);
                holder.layoutEddystone = (LinearLayout) view.findViewById(R.id.layoutEddystone);
                holder.layoutTemperature = (LinearLayout) view.findViewById(R.id.layoutTemperature);
                holder.txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);
                holder.txtHumidity = (TextView) view.findViewById(R.id.txtHumidity);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            String sn = "";
            final Device d = new Device();
            if (!d.fromScanData(ble)) {
                return view;
            }
            int battery = d.Battery;
            sn = d.SN;
            holder.layoutTemperature.setVisibility(0);
            holder.layoutIbeacon.setVisibility(8);
            holder.layoutEddystone.setVisibility(8);
            int rssi = ble.RSSI;
            holder.txtRSSI.setText("rssi:" + rssi + " dBm");
            holder.txtDistance.setText("" + MeasuringDistance.calculateAccuracy(-60, (double) rssi) + "m  " + -60 + "");
            String strName = "";
            if (ble.Name == null || ble.Name.equals("")) {
                strName = "--";
            } else {
                strName = ble.Name;
            }
            holder.txtName.setText(strName);
            holder.txtMacAddress.setText(ble.MacAddress);
            if (d.HardwareModel.equals("3901")) {
                holder.txtProtocol.setText("BT04 (v" + d.Firmware + ")");
            } else if (d.HardwareModel.equals("3C01")) {
                holder.txtProtocol.setText("BT04B (v" + d.Firmware + ")");
            } else if (d.HardwareModel.equals("3A01")) {
                holder.txtProtocol.setText("BT05 (v" + d.Firmware + ")");
            } else {
                holder.txtProtocol.setText(d.HardwareModel + " (v" + d.Firmware + ")");
            }
            holder.txtSN.setText("--");
            if (!(sn == null || sn.isEmpty())) {
                holder.txtSN.setText(sn);
            }
            if (rssi > -60) {
                holder.imgRssi.setImageResource(R.drawable.rssi_5);
            } else if (rssi > -70) {
                holder.imgRssi.setImageResource(R.drawable.rssi_4);
            } else if (rssi > -80) {
                holder.imgRssi.setImageResource(R.drawable.rssi_3);
            } else if (rssi > -90) {
                holder.imgRssi.setImageResource(R.drawable.rssi_2);
            } else {
                holder.imgRssi.setImageResource(R.drawable.rssi_1);
            }
            if ((new Date().getTime() - ble.LastScanTime.getTime()) / 1000 > 60) {
                view.setBackgroundColor(Color.parseColor("#AFCCCCCC"));
            } else {
                view.setBackgroundColor(0);
            }
            if (battery < 20) {
                holder.imgBattery.setImageResource(R.drawable.battery_00);
            } else if (battery < 40) {
                holder.imgBattery.setImageResource(R.drawable.battery_20);
            } else if (battery < 60) {
                holder.imgBattery.setImageResource(R.drawable.battery_40);
            } else if (battery < 80) {
                holder.imgBattery.setImageResource(R.drawable.battery_60);
            } else if (battery < 100) {
                holder.imgBattery.setImageResource(R.drawable.battery_80);
            } else {
                holder.imgBattery.setImageResource(R.drawable.battery_100);
            }
            if (battery >= 0) {
                holder.txtBattery.setText("" + battery + "%");
                holder.imgBattery.setVisibility(0);
                holder.txtBattery.setVisibility(0);
            } else {
                holder.txtBattery.setText("--");
                holder.imgBattery.setVisibility(8);
                holder.txtBattery.setVisibility(8);
            }
            holder.txtTemperature.setText("-- ");
            if (d.Temperature != -1000.0d) {
                holder.txtTemperature.setText(new TemperatureUnitUtil(d.Temperature).GetStringTemperature(AppConfig.TemperatureUnit));
            }
            holder.txtHumidity.setText("--");
            if (d.Humidity != -1000.0d) {
                holder.txtHumidity.setText(StringUtil.ToString(d.Humidity, 1) + " %");
            }
            if (d.AlarmType.equals("80") || d.AlarmType.equals("40") || d.AlarmType.equals("C0")) {
                if (d.AlarmType.equals("40") || d.AlarmType.equals("C0")) {
                    holder.txtTemperature.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                if (d.AlarmType.equals("80") || d.AlarmType.equals("C0")) {
                    holder.txtBattery.setTextColor(SupportMenu.CATEGORY_MASK);
                }
            } else {
                holder.txtTemperature.setTextColor(Color.parseColor("#808080"));
                holder.txtBattery.setTextColor(Color.parseColor("#808080"));
            }
//            view.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    ListView_ScanDeviceListAdapter.this._Activity.finish();
//                    try {
//                        Bundle bundle;
//                        if (ListView_ScanDeviceListAdapter.this.OpenIntentType == 1) {
//                            Intent openSyncIntent = new Intent(ListView_ScanDeviceListAdapter.this._Activity, SyncActivity.class);
//                            bundle = new Bundle();
//                            bundle.putString("SN", d.SN);
//                            openSyncIntent.putExtras(bundle);
//                            ListView_ScanDeviceListAdapter.this._Activity.startActivity(openSyncIntent);
//                        } else if (ListView_ScanDeviceListAdapter.this.OpenIntentType == 2) {
//                            Intent openConfigIntent = new Intent(ListView_ScanDeviceListAdapter.this._Activity, ConfigActivity.class);
//                            bundle = new Bundle();
//                            bundle.putString("SN", d.SN);
//                            openConfigIntent.putExtras(bundle);
//                            ListView_ScanDeviceListAdapter.this._Activity.startActivity(openConfigIntent);
//                        } else if (ListView_ScanDeviceListAdapter.this.OpenIntentType == 3) {
//                            Intent openDeivceIntent = new Intent(ListView_ScanDeviceListAdapter.this._Activity, DeviceNoConnectActivity.class);
//                            bundle = new Bundle();
//                            bundle.putString("SN", d.SN);
//                            openDeivceIntent.putExtras(bundle);
//                            ListView_ScanDeviceListAdapter.this._Activity.startActivity(openDeivceIntent);
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//            });
            return view;

        } catch (Exception ex) {
            Log.e("getView", "异常：" + ex.toString());
            return null;
        }

    }

    public void Add(BLE ble) {
        boolean flag = true;
        int i = 0;
        while (i < this.items.size()) {
            try {
                if (((BLE) this.items.get(i)).MacAddress.equals(ble.MacAddress)) {
                    flag = false;
                }
                i++;
            } catch (Exception e) {
                return;
            }
        }
        if (flag && IsFilter(ble)) {
            this.items.add(ble);
        }
    }

    private void Delete(int position) {
        try {
            this.items.remove(position);
        } catch (Exception e) {
        }
    }

    public void Delete(String macAddress) {
        int i = 0;
        while (i < this.items.size()) {
            try {
                if (((BLE) this.items.get(i)).MacAddress.equals(macAddress)) {
                    this.items.remove(i);
                }
                i++;
            } catch (Exception e) {
                return;
            }
        }
    }

    public void Update(BLE ble) {
        int i = 0;
        while (i < this.items.size()) {
            try {
                BLE item = (BLE) this.items.get(i);
                if (item.MacAddress.equals(ble.MacAddress)) {
                    item.Name = ble.Name;
                    item.RSSI = ble.RSSI;
                    item.ScanData = ble.ScanData;
                    item.LastScanTime = ble.LastScanTime;
                }
                i++;
            } catch (Exception e) {
                return;
            }
        }
    }

    public void Clear() {
        this.items.clear();
    }

    private boolean IsFilter(BLE device) {
        try {
            String key = this.SearchKey;
            Device d = new Device();
            d.fromScanData(device);
            if (d == null || d.Name == null || d.Name.equals("") || d.SN == null || d.SN.length() != 8) {
                return false;
            }
            if (!(key == null || key.isEmpty())) {
                if (key.substring(0, 1).equals("-") || key.substring(0, 1).equals("—")) {
                    int rssiRange = -200;
                    try {
                        if (key.length() > 1) {
                            rssiRange = Integer.parseInt(key.substring(1)) * -1;
                        }
                    } catch (Exception e) {
                    }
                    if (device.RSSI < rssiRange) {
                        Log.i("IsFilter", "接收范围 > " + rssiRange + "dBm");
                        return false;
                    }
                } else if (!(d.SN.contains(key) || String.valueOf(d.Name).contains(key))) {
                    Log.i("IsFilter", "过滤SN、Name中不包含" + key + "的设备。");
                    return false;
                }
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    public void Sort() {
        try {
            if (this.SortType == 1) {
                Collections.sort(this.items, new C03002());
            } else if (this.SortType == 2) {
                Collections.sort(this.items, new C03013());
            } else if (this.SortType == 3) {
                Collections.sort(this.items, new C03024());
            } else if (this.SortType == 4) {
                Collections.sort(this.items, new C03035());
            }
        } catch (Exception ex) {
            Log.e("ListView_DeviceAdapter", "Sort:" + ex.toString());
        }
    }

    public double GetDistance(BLE ble) {
        new Device().fromScanData(ble);
        return MeasuringDistance.calculateAccuracy(-60, (double) ble.RSSI);
    }

    public int GetBattery(BLE ble) {
        Device d = new Device();
        d.fromScanData(ble);
        return d.Battery;
    }

    public int GetSN(BLE ble) {
        Device d = new Device();
        d.fromScanData(ble);
        try {
            return Integer.parseInt(d.SN);
        } catch (Exception e) {
            return 0;
        }
    }
}
