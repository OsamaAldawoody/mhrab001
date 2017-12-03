package com.newsolution.almhrab.AppConstants;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hp on 7/20/2016.
 */
public class AppConst {
    public static String PREFS = "Mhrab";
    public static final String LASTUPDATE = "last_update";
    public static final String DeviceNo = "DeviceNo";
    public static final String ENGLISH = "English";
    public static final String ARABIC = "Arabic";
    public static final String SWEDISH = "Svenska";

    public static String FONT_NAME = "battar.ttf";
    static Typeface font;

    public static void applyFont(final Context context, final View v) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/" + FONT_NAME);
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFont(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(font);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(font);
            } else if (v instanceof RadioButton) {
                ((RadioButton) v).setTypeface(font);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
