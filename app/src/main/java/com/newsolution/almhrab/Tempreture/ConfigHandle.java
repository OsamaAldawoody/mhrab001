package com.newsolution.almhrab.Tempreture;

import android.util.Log;
import java.util.HashMap;
import java.util.Map.Entry;

public class ConfigHandle {
    public HashMap<String, Boolean> Configs;
    private boolean isRequestComplete;

    public ConfigHandle() {
        if (this.Configs == null) {
            this.Configs = new HashMap();
        }
        this.isRequestComplete = false;
    }

    public void ConfigRequest(String uuid) {
        this.Configs.put(uuid.replace("-", ""), Boolean.valueOf(false));
    }

    public void ConfigRespond(String uuid) {
        uuid = uuid.replace("-", "");
        if (this.Configs.containsKey(uuid)) {
            this.Configs.put(uuid, Boolean.valueOf(true));
        }
    }

    public void ConfigRequestComplete() {
        this.isRequestComplete = true;
    }

    public boolean IsRespond(String uuid) {
        uuid = uuid.replace("-", "");
        for (Entry entry : this.Configs.entrySet()) {
            String key = (String) entry.getKey();
            boolean val = ((Boolean) entry.getValue()).booleanValue();
            if (key.equals(uuid) && val) {
                return true;
            }
        }
        return false;
    }

    public boolean IsComplete() {
        if (!this.isRequestComplete) {
            return false;
        }
        for (Entry entry : this.Configs.entrySet()) {
            if (!((Boolean) entry.getValue()).booleanValue()) {
                Log.i("IsComplete", ((String) entry.getKey()) + " ---> false");
                return false;
            }
        }
        return true;
    }
}
