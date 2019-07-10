package com.newsolution.almhrab.Activity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    private BroadcastService _BroadcastService;
    boolean _IsClearing = false;
    private boolean _IsInit = false;
    private ListView_ScanDeviceListAdapter _ListView_deviceAdapter;
    public ILocalBluetoothCallBack _LocalBluetoothCallBack = new ILocalBluetoothCallBack() {
        public void OnEntered(BLE ble) {
            try {
                AddOrUpdate(ble);
            } catch (Exception ignored) {
            }
        }

        public void OnUpdate(BLE ble) {
            try {
                AddOrUpdate(ble);
            } catch (Exception ignored) {
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
    private TextWatcher textWatcher = new C03735();
    public TextView txtActivityTitle;
    public EditText txtSearch;

    class C03691 implements OnClickListener {
        C03691() {
        }

        public void onClick(View v) {
            finish();
        }
    }


    class C03713 implements OnClickListener {
        C03713() {
        }

        public void onClick(View v) {
            Clear();
        }
    }

    class C03724 extends TimerTask {
        C03724() {
        }

        public void run() {
            try {
                synchronized (this) {
                    if (_IsInit) {
                        _BroadcastService.StartScan();
                        if (!IsRefreshing) {
                            RefreshUI();
                        }
                    }
                }
            } catch (Exception ignored) {
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
                    _ListView_deviceAdapter.SearchKey = txtSearch.getText().toString();
                    Clear();
                } catch (Exception ignored) {
                }
            }
        }
    }

    class C03768 implements Runnable {
        C03768() {
        }

        public void run() {
            try {
                _ListView_deviceAdapter.Clear();
                _ListView_deviceAdapter.notifyDataSetChanged();
                txtActivityTitle.setText(getString(R.string.lan_150));
                _IsClearing = false;
            } catch (Exception ignored) {
            }
        }
    }

    class C03779 implements Runnable {
        C03779() {
        }

        @SuppressLint("SetTextI18n")
        public void run() {
            synchronized (this) {
                try {
                    _ListView_deviceAdapter.Sort();
                    _ListView_deviceAdapter.notifyDataSetChanged();
                    txtActivityTitle.setText(getString(R.string.lan_150) + "[" + _ListView_deviceAdapter.getCount() + "]");
                } catch (Exception ignored) {
                }
                IsRefreshing = false;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_scan_device);
        getWindow().setSoftInputMode(2);
        btnOptions = (ImageView) findViewById(R.id.btnOptions);
        txtActivityTitle = (TextView) findViewById(R.id.txtActivityTitle);
        btnSort = (ImageView) findViewById(R.id.btnSort);
        btnSort.setVisibility(View.GONE);
        btnClear = (ImageView) findViewById(R.id.btnClear);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        try {
            OpenIntentType = Integer.parseInt(getIntent().getExtras().getString("Type"));
        } catch (Exception ignored) {
        }
        btnOptions.setOnClickListener(new C03691());
        btnClear.setOnClickListener(new C03713());
        txtSearch.addTextChangedListener(this.textWatcher);
        InitScan();
    }

    public void InitScan() {
        try {
            if (_BroadcastService == null) {
                _BroadcastService = new BroadcastService();
            }
            BluetoothAdapter _BluetoothAdapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
            if (_BroadcastService.Init(_BluetoothAdapter, _LocalBluetoothCallBack)) {
                _IsInit = true;
                if (_ListView_deviceAdapter == null) {
                    _ListView_deviceAdapter = new ListView_ScanDeviceListAdapter(this, OpenIntentType);
                    setListAdapter(_ListView_deviceAdapter);
                    return;
                }
                return;
            }
            _IsInit = false;
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
            if (_Timer != null) {
                _Timer.cancel();
            }
            _Timer = new Timer();
            _Timer.schedule(new C03724(), 1000, 2000);
        } catch (Exception ignored) {
        }
    }

    protected void onDestroy() {
        try {
            _Timer.cancel();
            if (_BroadcastService != null) {
                _BroadcastService.StopScan();
            }
        } catch (Exception ignored) {
        }
        super.onDestroy();
    }

    private void Clear() {
        try {
            _IsClearing = true;
            if (_ListView_deviceAdapter != null) {
                runOnUiThread(new C03768());
            }
            if (_BroadcastService != null) {
                _BroadcastService.StopScan();
                _BroadcastService = null;
            }
            _IsInit = false;
            InitScan();
        } catch (Exception ignored) {
        }
    }

    private void AddOrUpdate(BLE ble) {
        try {
            if (_ListView_deviceAdapter != null) {
                boolean flag = false;
                for (int i = 0; i < _ListView_deviceAdapter.items.size(); i++) {
                    if (((BLE) _ListView_deviceAdapter.items.get(i)).MacAddress.equals(ble.MacAddress)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    _ListView_deviceAdapter.Update(ble);
                } else {
                    _ListView_deviceAdapter.Add(ble);
                }
            }
        } catch (Exception ex) {
            Log.e("ScanDevice", "AddOrUpdate:" + ex.toString());
        }
    }

    private void RefreshUI() {
        try {
            if (_ListView_deviceAdapter != null && _ListView_deviceAdapter.getCount() > 0) {
                int refreshInterval;
                Date now = new Date();
                long totaltime = now.getTime() - LastUpdateTime.getTime();
                IsRefreshing = true;
                int count = _ListView_deviceAdapter.getCount();
                if (count > 100) {
                    refreshInterval = (count / 100) * 4000;
                } else if (count > 60) {
                    refreshInterval = 3000;
                } else if (count > 30) {
                    refreshInterval = 2000;
                } else {
                    refreshInterval = CS_sRGB;
                }
                if (_ListView_deviceAdapter.SortType != 0) {
                    refreshInterval *= 2;
                }
                if (totaltime > ((long) refreshInterval)) {
                    runOnUiThread(new C03779());
                    LastUpdateTime = now;
                    return;
                }
                IsRefreshing = false;
            }
        } catch (Exception ex) {
            Log.e("ScanDevice", "RefreshUI:" + ex.toString());
            IsRefreshing = false;
        }
    }
}
