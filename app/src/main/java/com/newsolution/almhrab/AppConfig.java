package com.newsolution.almhrab;

import android.os.Environment;
import android.util.Printer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppConfig  {
    static final String FilePath = (Environment.getExternalStorageDirectory() + File.separator + "TZ_BTLogger" + File.separator);
    public static int TemperatureUnit = 0;

    public static boolean IsExistSDCard() {
        try {
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return false;
            }
            File dic = new File(FilePath);
            if (!dic.exists()) {
                dic.mkdir();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean IsReadWritePermission() {
        try {
            File file = new File(FilePath + "Temp.dat");
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            new FileWriter(file).write("");
            new FileReader(file).read();
            file.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
