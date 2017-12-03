package com.newsolution.almhrab.Activity;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.newsolution.almhrab.Adapters.ListView_ScanDeviceListAdapter;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.Tempreture.BLE;
import com.newsolution.almhrab.Tempreture.BroadcastService;
import com.newsolution.almhrab.Tempreture.ILocalBluetoothCallBack;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScanDeviceActivity extends ListActivity {
    public static final int CS_sRGB = 1000;
    boolean IsRefreshing = false;
    Date LastUpdateTime = new Date();
    public int OpenIntentType = 0;
    private int SortType = 0;
    private BluetoothAdapter _BluetoothAdapter;
    private BroadcastService _BroadcastService;
    boolean _IsClearing = false;
    private boolean _IsInit = false;
    private ListView_ScanDeviceListAdapter _ListView_deviceAdapter;
    public ILocalBluetoothCallBack _LocalBluetoothCallBack = new ILocalBluetoothCallBack() {
        public void OnEntered(BLE ble) {
            try {
                ScanDeviceActivity.this.AddOrUpdate(ble);
            } catch (Exception e) {
            }
        }

        public void OnUpdate(BLE ble) {
            try {
                ScanDeviceActivity.this.AddOrUpdate(ble);
            } catch (Exception e) {
            }
        }

        public void OnExited(BLE ble) {
        }

        public void OnScanComplete() {
        }
    };
    private Timer _Timer;
    public ImageView btnClear;
    public ImageView btnOptions;
    public ImageView btnSort;
    private PopupWindow popupwindow;
    private TextWatcher textWatcher = new C03735();
    public TextView txtActivityTitle;
    public EditText txtSearch;

    class C03691 implements OnClickListener {
        C03691() {
        }

        public void onClick(View v) {
            ScanDeviceActivity.this.finish();
        }
    }

//    class C03702 implements OnClickListener {
//        C03702() {
//        }
//
//        public void onClick(View v) {
//            if (ScanDeviceActivity.this.popupwindow == null || !ScanDeviceActivity.this.popupwindow.isShowing()) {
//                ScanDeviceActivity.this.initPopupWindowView();
//                ScanDeviceActivity.this.popupwindow.showAsDropDown(v, 20, 5);
//                return;
//            }
//            ScanDeviceActivity.this.popupwindow.dismiss();
//        }
//    }

    class C03713 implements OnClickListener {
        C03713() {
        }

        public void onClick(View v) {
            ScanDeviceActivity.this.Clear();
        }
    }

    class C03724 extends TimerTask {
        C03724() {
        }

        public void run() {
            try {
                synchronized (this) {
                    if (ScanDeviceActivity.this._IsInit) {
                        ScanDeviceActivity.this._BroadcastService.StartScan();
                        if (!ScanDeviceActivity.this.IsRefreshing) {
                            ScanDeviceActivity.this.RefreshUI();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    class C03735 implements TextWatcher {
        C03735() {
        }

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            synchronized (this) {
                try {
                    ScanDeviceActivity.this._ListView_deviceAdapter.SearchKey = ScanDeviceActivity.this.txtSearch.getText().toString();
                    ScanDeviceActivity.this.Clear();
                } catch (Exception e) {
                }
            }
        }
    }

    class C03746 implements OnTouchListener {
        C03746() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            try {
                if (ScanDeviceActivity.this.popupwindow != null && ScanDeviceActivity.this.popupwindow.isShowing()) {
                    ScanDeviceActivity.this.popupwindow.dismiss();
                    ScanDeviceActivity.this.popupwindow = null;
                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }
    }

//    class C03757 implements OnCheckedChangeListener {
//        C03757() {
//        }
//
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            try {
//                switch (group.getCheckedRadioButtonId()) {
//                    case R.id.rb1:
//                        ScanDeviceActivity.this.SortType = 1;
//                        break;
//                    case R.id.rb2:
//                        ScanDeviceActivity.this.SortType = 2;
//                        break;
//                    case R.id.rb3:
//                        ScanDeviceActivity.this.SortType = 3;
//                        break;
//                    case R.id.rb4:
//                        ScanDeviceActivity.this.SortType = 4;
//                        break;
//                    default:
//                        ScanDeviceActivity.this.SortType = 0;
//                        break;
//                }
//                if (ScanDeviceActivity.this._ListView_deviceAdapter != null) {
//                    ScanDeviceActivity.this._ListView_deviceAdapter.SortType = ScanDeviceActivity.this.SortType;
//                    ScanDeviceActivity.this.Clear();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }

    class C03768 implements Runnable {
        C03768() {
        }

        public void run() {
            try {
                ScanDeviceActivity.this._ListView_deviceAdapter.Clear();
                ScanDeviceActivity.this._ListView_deviceAdapter.notifyDataSetChanged();
                ScanDeviceActivity.this.txtActivityTitle.setText(ScanDeviceActivity.this.getString(R.string.lan_150));
                ScanDeviceActivity.this._IsClearing = false;
            } catch (Exception e) {
            }
        }
    }

    class C03779 implements Runnable {
        C03779() {
        }

        public void run() {
            synchronized (this) {
                try {
                    ScanDeviceActivity.this._ListView_deviceAdapter.Sort();
                    ScanDeviceActivity.this._ListView_deviceAdapter.notifyDataSetChanged();
                    ScanDeviceActivity.this.txtActivityTitle.setText(ScanDeviceActivity.this.getString(R.string.lan_150) + "[" + ScanDeviceActivity.this._ListView_deviceAdapter.getCount() + "]");
                } catch (Exception e) {
                }
                ScanDeviceActivity.this.IsRefreshing = false;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_scan_device);
        getWindow().setSoftInputMode(2);
        this.btnOptions = (ImageView) findViewById(R.id.btnOptions);
        this.txtActivityTitle = (TextView) findViewById(R.id.txtActivityTitle);
        this.btnSort = (ImageView) findViewById(R.id.btnSort);
        this.btnSort.setVisibility(View.GONE);
        this.btnClear = (ImageView) findViewById(R.id.btnClear);
        this.txtSearch = (EditText) findViewById(R.id.txtSearch);
        try {
            this.OpenIntentType = Integer.parseInt(getIntent().getExtras().getString("Type"));
        } catch (Exception e) {
        }
        this.btnOptions.setOnClickListener(new C03691());
//        this.btnSort.setOnClickListener(new C03702());
        this.btnClear.setOnClickListener(new C03713());
        this.txtSearch.addTextChangedListener(this.textWatcher);
        InitScan();
    }

    public void InitScan() {
        try {
            if (this._BroadcastService == null) {
                this._BroadcastService = new BroadcastService();
            }
            this._BluetoothAdapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
            if (this._BroadcastService.Init(this._BluetoothAdapter, this._LocalBluetoothCallBack)) {
                this._IsInit = true;
                if (this._ListView_deviceAdapter == null) {
                    this._ListView_deviceAdapter = new ListView_ScanDeviceListAdapter(this, this.OpenIntentType);
                    setListAdapter(this._ListView_deviceAdapter);
                    return;
                }
                return;
            }
            this._IsInit = false;
            Toast.makeText(this, getString(R.string.lan_7), 0).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.lan_7), 0).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan_device, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        try {
            if (this._Timer != null) {
                this._Timer.cancel();
            }
            this._Timer = new Timer();
            this._Timer.schedule(new C03724(), 1000, 2000);
        } catch (Exception e) {
        }
    }

    protected void onDestroy() {
        try {
            this._Timer.cancel();
            if (this._BroadcastService != null) {
                this._BroadcastService.StopScan();
            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }

//    public void initPopupWindowView() {
//        try {
//            View customView = getLayoutInflater().inflate(R.layout.popupwindow_sort, null, false);
//            if (this.popupwindow == null) {
//                this.popupwindow = new PopupWindow(customView, -2, -2, true);
//            }
//            customView.setOnTouchListener(new C03746());
//            RadioGroup rgSort = (RadioGroup) customView.findViewById(R.id.rgSort);
//            rgSort.getChildAt(this.SortType).setSelected(true);
//            rgSort.setOnCheckedChangeListener(new C03757());
//        } catch (Exception ex) {
//            Log.e("Home", "initPopupWindowView:" + ex.toString());
//        }
//    }

    private void Clear() {
        try {
            this._IsClearing = true;
            if (this._ListView_deviceAdapter != null) {
                runOnUiThread(new C03768());
            }
            if (this._BroadcastService != null) {
                this._BroadcastService.StopScan();
                this._BroadcastService = null;
            }
            this._IsInit = false;
            InitScan();
        } catch (Exception e) {
        }
    }

    private void AddOrUpdate(BLE ble) {
        try {
            if (this._ListView_deviceAdapter != null) {
                boolean flag = false;
                for (int i = 0; i < this._ListView_deviceAdapter.items.size(); i++) {
                    if (((BLE) this._ListView_deviceAdapter.items.get(i)).MacAddress.equals(ble.MacAddress)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    this._ListView_deviceAdapter.Update(ble);
                } else {
                    this._ListView_deviceAdapter.Add(ble);
                }
            }
        } catch (Exception ex) {
            Log.e("ScanDevice", "AddOrUpdate:" + ex.toString());
        }
    }

    private void RefreshUI() {
        try {
            if (this._ListView_deviceAdapter != null && this._ListView_deviceAdapter.getCount() > 0) {
                int refreshInterval;
                Date now = new Date();
                long totaltime = now.getTime() - this.LastUpdateTime.getTime();
                this.IsRefreshing = true;
                int count = this._ListView_deviceAdapter.getCount();
                if (count > 100) {
                    refreshInterval = (count / 100) * 4000;
                } else if (count > 60) {
                    refreshInterval = 3000;
                } else if (count > 30) {
                    refreshInterval = 2000;
                } else {
                    refreshInterval = CS_sRGB;
                }
                if (this._ListView_deviceAdapter.SortType != 0) {
                    refreshInterval *= 2;
                }
                if (totaltime > ((long) refreshInterval)) {
                    runOnUiThread(new C03779());
                    this.LastUpdateTime = now;
                    return;
                }
                this.IsRefreshing = false;
            }
        } catch (Exception ex) {
            Log.e("ScanDevice", "RefreshUI:" + ex.toString());
            this.IsRefreshing = false;
        }
    }
}
