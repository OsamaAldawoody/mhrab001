package com.newsolution.almhrab.DateTimePicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import com.newsolution.almhrab.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CustomDateTimePicker implements View.OnClickListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private ViewSwitcher viewSwitcher;

    private final int SET_DATE = 100, SET_TIME = 101, SET = 102, CANCEL = 103;

    private Button btn_setDate;
    private Button btn_setTime;

    private Calendar calendar_date = null;

    private Activity activity;

    private ICustomDateTimeListener iCustomDateTimeListener = null;

    private Dialog dialog;

    private boolean is24HourView = true;

    private int selectedHour, selectedMinute=0;

    public CustomDateTimePicker(Activity a,
                                ICustomDateTimeListener customDateTimeListener) {
        activity = a;
        iCustomDateTimeListener = customDateTimeListener;

        dialog = new Dialog(activity);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetData();
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = getDateTimePickerLayout();
        dialog.setContentView(dialogView);
    }

    private View getDateTimePickerLayout() {
        LinearLayout.LayoutParams linear_match_wrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams linear_wrap_wrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams frame_match_wrap = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

        LinearLayout linear_main = new LinearLayout(activity);
        linear_main.setLayoutParams(linear_match_wrap);
        linear_main.setOrientation(LinearLayout.VERTICAL);
        linear_main.setGravity(Gravity.CENTER);

        LinearLayout linear_child = new LinearLayout(activity);
        linear_child.setLayoutParams(linear_wrap_wrap);
        linear_child.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linear_top = new LinearLayout(activity);
        linear_top.setLayoutParams(linear_match_wrap);
        btn_setDate = new Button(activity);
        btn_setDate.setLayoutParams(button_params);
        btn_setDate.setText(R.string.date);
        btn_setDate.setId(SET_DATE);
        btn_setDate.setOnClickListener(this);

        btn_setTime = new Button(activity);
        btn_setTime.setLayoutParams(button_params);
        btn_setTime.setText(R.string.time);
        btn_setTime.setId(SET_TIME);
        btn_setTime.setOnClickListener(this);

        linear_top.addView(btn_setDate);
        linear_top.addView(btn_setTime);

        viewSwitcher = new ViewSwitcher(activity);
        viewSwitcher.setLayoutParams(frame_match_wrap);

        datePicker = new DatePicker(activity);
        timePicker = new TimePicker(activity);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                selectedHour = hourOfDay;
                selectedMinute = minute;
            }
        });

        viewSwitcher.addView(timePicker);
        viewSwitcher.addView(datePicker);

        LinearLayout linear_bottom = new LinearLayout(activity);
        linear_match_wrap.topMargin = 8;
        linear_bottom.setLayoutParams(linear_match_wrap);

        Button btn_set = new Button(activity);
        btn_set.setLayoutParams(button_params);
        btn_set.setText(R.string.set);
        btn_set.setId(SET);
        btn_set.setOnClickListener(this);
        Button btn_cancel = new Button(activity);
        btn_cancel.setLayoutParams(button_params);
        btn_cancel.setText(R.string.cancel);
        btn_cancel.setId(CANCEL);
        btn_cancel.setOnClickListener(this);

        linear_bottom.addView(btn_set);
        linear_bottom.addView(btn_cancel);

        linear_child.addView(linear_top);
        linear_child.addView(viewSwitcher);
        linear_child.addView(linear_bottom);

        linear_main.addView(linear_child);

        return linear_main;
    }

    public void showDialog() {
        if (!dialog.isShowing()) {
            if (calendar_date == null)
                calendar_date = Calendar.getInstance();
            selectedHour =0;
            selectedMinute =00;
            timePicker.setIs24HourView(is24HourView);
            timePicker.setCurrentHour(selectedHour);
            timePicker.setCurrentMinute(0);

            datePicker.updateDate(calendar_date.get(Calendar.YEAR),
                    calendar_date.get(Calendar.MONTH),
                    calendar_date.get(Calendar.DATE));

            dialog.show();

            btn_setDate.performClick();
        }
    }


    public void setDate(Calendar calendar) {
        if (calendar != null)
            calendar_date = calendar;
    }

    public void setDate(Date date) {
        if (date != null) {
            calendar_date = Calendar.getInstance();
            calendar_date.setTime(date);
        }
    }

    public void set24HourFormat(boolean is24HourFormat) {
        is24HourView = is24HourFormat;
    }

    public interface ICustomDateTimeListener {
        public void onSet(Dialog dialog, Calendar calendarSelected,
                          Date dateSelected, int year, String monthFullName,
                          String monthShortName, int monthNumber, int date,
                          String weekDayFullName, String weekDayShortName, int hour24,
                          int hour12, int min, int sec, String AM_PM);

        public void onCancel();
    }

    @Override
    public void onClick(View v) {
        boolean isAutoDismiss = true;
        switch (v.getId()) {
            case SET_DATE:
                btn_setTime.setEnabled(true);
                btn_setDate.setEnabled(false);
                viewSwitcher.showNext();
                break;

            case SET_TIME:
                btn_setTime.setEnabled(false);
                btn_setDate.setEnabled(true);
                viewSwitcher.showPrevious();
                break;

            case SET:
                if (iCustomDateTimeListener != null) {
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    int day = datePicker.getDayOfMonth();
                    calendar_date.set(year, month, day, selectedHour,
                            selectedMinute);

                    iCustomDateTimeListener.onSet(dialog, calendar_date,
                            calendar_date.getTime(), calendar_date
                                    .get(Calendar.YEAR),
                            getMonthFullName(calendar_date.get(Calendar.MONTH)),
                            getMonthShortName(calendar_date.get(Calendar.MONTH)),
                            calendar_date.get(Calendar.MONTH), calendar_date
                                    .get(Calendar.DAY_OF_MONTH),
                            getWeekDayFullName(calendar_date
                                    .get(Calendar.DAY_OF_WEEK)),
                            getWeekDayShortName(calendar_date
                                    .get(Calendar.DAY_OF_WEEK)), calendar_date
                                    .get(Calendar.HOUR_OF_DAY),
                            getHourIn12Format(calendar_date
                                    .get(Calendar.HOUR_OF_DAY)), calendar_date
                                    .get(Calendar.MINUTE), calendar_date
                                    .get(Calendar.SECOND), getAMPM(calendar_date));
                }
                if (dialog.isShowing() && isAutoDismiss)
                    dialog.dismiss();
                break;

            case CANCEL:
                if (iCustomDateTimeListener != null)
                    iCustomDateTimeListener.onCancel();
                if (dialog.isShowing())
                    dialog.dismiss();
                break;
        }
    }

    private String getMonthFullName(int monthNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthNumber);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        simpleDateFormat.setCalendar(calendar);
        String monthName = simpleDateFormat.format(calendar.getTime());

        return monthName;
    }

    private String getMonthShortName(int monthNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthNumber);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        simpleDateFormat.setCalendar(calendar);
        String monthName = simpleDateFormat.format(calendar.getTime());

        return monthName;
    }

    private String getWeekDayFullName(int weekDayNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        simpleDateFormat.setCalendar(calendar);
        String weekName = simpleDateFormat.format(calendar.getTime());

        return weekName;
    }

    private String getWeekDayShortName(int weekDayNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE");
        simpleDateFormat.setCalendar(calendar);
        String weekName = simpleDateFormat.format(calendar.getTime());

        return weekName;
    }

    private int getHourIn12Format(int hour24) {
        int hourIn12Format = 0;

        if (hour24 == 0)
            hourIn12Format = 12;
        else if (hour24 <= 12)
            hourIn12Format = hour24;
        else
            hourIn12Format = hour24 - 12;

        return hourIn12Format;
    }

    private String getAMPM(Calendar calendar) {
        String ampm = (calendar.get(Calendar.AM_PM) == (Calendar.AM)) ? "ص"
                : "م";
        return ampm;
    }

    private void resetData() {
        calendar_date = null;
        is24HourView = true;
    }

}