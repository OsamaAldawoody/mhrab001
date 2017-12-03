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
    public static long ConnectTimeout = 30000;
    public static long ExitedTime = 10;
    public static long ScanRunTime = 60000;
    public static Printer DefaultPrinter = null;
    static final String FilePath = (Environment.getExternalStorageDirectory() + File.separator + "TZ_BTLogger" + File.separator);
    public static final boolean IsLocalEmailClient = true;
    public static boolean IsShowToken = true;
    public static String ReviceMail = "";
    public static String[] SERVER_OTA_VERSION = null;
    public static int TemperatureUnit = 0;
    public static Double Timezone = Double.valueOf(8.0d);

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

    public static List<String> ReadFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader bufferedReader = new BufferedReader(read);
                List<String> arrayList = new ArrayList();
                while (true) {
                    String lineTxt = bufferedReader.readLine();
                    if (lineTxt != null) {
                        arrayList.add(lineTxt);
                    } else {
                        read.close();
                        return arrayList;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean SaveFile(String filePath, String writeText) {
        try {
            FileWriter fwriter = new FileWriter(filePath);
            fwriter.write(writeText);
            fwriter.flush();
            fwriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void DeleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
    }
}
