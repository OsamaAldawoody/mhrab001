package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsolution.almhrab.Adapters.NewsAdapter;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.DateTimePicker.CustomDateTimePicker;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsActivity extends Activity {
    Activity activity;
    private RecyclerView rv_ads;
    private ImageView iv_addAds;
    NewsAdapter newsAdapter;
    ArrayList<News> adsArrayList;
    private DBOperations DBO;
    private ImageView iv_back;
    private int id;
    private LinearLayout parentPanel;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private Button btn_add, btn_cancel;
    private EditText ed_end, ed_start, ed_sort, ed_newsText;
    private TextView tv_tittle;
    private Dialog dialog;
    private ProgressDialog pd;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")//battar  droidkufi_regular droid_sans_arabic neosansarabic //mcs_shafa_normal
                .setFontAttrId(R.attr.fontPath)
                .build());
        activity = this;
        setColor();
        setContentView(R.layout.activity_news);
        DBO = new DBOperations(this);
        DBO.createDatabase();
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        tv_tittle.setText(getString(R.string.adv));
        rv_ads = (RecyclerView) findViewById(R.id.rv_ads);
        iv_addAds = (ImageView) findViewById(R.id.iv_addAds);
//        iv_addAds.setVisibility(View.GONE);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_addAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  addAdv();
                addNewsDialog(null);
            }
        });
        rv_ads.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DBO.open();
        adsArrayList = DBO.getNews();
        Log.i("+++ads", adsArrayList.size() + "  ");
        DBO.close();
        rv_ads.setLayoutManager(llm);
        setAdapter(adsArrayList);

    }

    private void setAdapter(final ArrayList<News> list) {
        newsAdapter = new NewsAdapter(this, list, new NewsAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                deleteAds(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                addNewsDialog(list.get(position));
//                editAdvDialog(position);
            }
        });
        rv_ads.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

    private void setColor() {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.back_text));
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
        }
    }

    private void addUpdateNews(final int action, final News object) {
        if (Utils.isOnline(activity)) {
            pd = new ProgressDialog(activity);
            pd.setMessage(getString(R.string.wait));
            pd.show();
            pd.setCanceledOnTouchOutside(false);
            WS.addUpdateNews(activity, object, new OnLoadedFinished() {
                @Override
                public void onSuccess(String response) {
//                    Utils.showCustomToast(activity, activity.getString(R.string.success_add));
//                    addToLocalDB(object);
//                    if (dialog.isShowing()) dialog.dismiss();
                    getNews(action);
                }

                @Override
                public void onFail(String error) {
                    if (pd.isShowing()) pd.dismiss();
                    Utils.showCustomToast(activity, error);
                }
            });
        } else {
            Utils.showCustomToast(activity, getString(R.string.no_internet));

        }

    }

    private void getNews(final int action) {
        WS.getAllNews(activity, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                if (action == 0)
                    Utils.showCustomToast(activity, getString(R.string.success_add));
                else Utils.showCustomToast(activity, getString(R.string.success_edit));
                updateAdapter();
                if (dialog.isShowing()) dialog.dismiss();
                if (pd.isShowing()) pd.dismiss();
            }

            @Override
            public void onFail(String error) {
                if (pd.isShowing()) pd.dismiss();
                Utils.showCustomToast(activity, error);
            }
        });
    }

    private void updateAdapter() {
        DBO.open();
        adsArrayList = DBO.getNews();
        DBO.close();
        setAdapter(adsArrayList);
    }

    private void deleteAds(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(getString(R.string.tv_delTitle)).
                setMessage(getString(R.string.tv_delAttention))
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        delete(position);

                    }
                })
                .setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void delete(final int pos) {
        if (sp.getInt("priority", 0) == 1) {
            if (Utils.isOnline(activity)) {
                final ProgressDialog pd = new ProgressDialog(activity);
                pd.setMessage(getString(R.string.deleting));
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                WS.NewsDelete(activity, adsArrayList.get(pos).getId(), new OnLoadedFinished() {
                    @Override
                    public void onSuccess(String response) {
                        pd.dismiss();
                        DBO.open();
                        DBO.delAds(adsArrayList.get(pos).getId());
                        adsArrayList.remove(pos);
                        newsAdapter.notifyDataSetChanged();
                        DBO.close();
                    }

                    @Override
                    public void onFail(String error) {
                        pd.dismiss();
                        Utils.showCustomToast(activity, error);
                    }
                });
            } else {
                Utils.showCustomToast(activity, getString(R.string.no_internet));
            }
        }else {
            DBO.open();
            DBO.delAds(adsArrayList.get(pos).getId());
            adsArrayList.remove(pos);
            newsAdapter.notifyDataSetChanged();
            DBO.close();
        }
    }

    private void addNewsDialog(final News objectNews) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US);
        final SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.US);

        View view = getLayoutInflater().inflate(R.layout.add_edit_ads, null);
        parentPanel = (LinearLayout) view.findViewById(R.id.mainLayout);
        ed_newsText = (EditText) view.findViewById(R.id.ed_newsText);
        ed_end = (EditText) view.findViewById(R.id.ed_end);
        ed_start = (EditText) view.findViewById(R.id.ed_start);
        ed_sort = (EditText) view.findViewById(R.id.ed_sort);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        // tv_tittle=(TextView)view.findViewById(R.id.tv_tittle);
        dialog = new Dialog(this);

        dialog.setTitle(getString(R.string.add_adv));
        if (objectNews == null) {
            btn_add.setText(getString(R.string.add));
        } else {
            btn_add.setText(getString(R.string.edit_adv));
            ed_sort.setText(objectNews.getSort() + "");
            String endDate = objectNews.getToDate();
            String startDate = objectNews.getFromDate();
            String start = startDate;
            String end = endDate;

            try {
                Date endD = sdf.parse(endDate);
                Date startD = sdf.parse(startDate);
                end = spf.format(endD.getTime());
                start = spf.format(startD.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ed_end.setText(end);
            ed_start.setText(start);
            ed_newsText.setText(objectNews.getTextAds());
        }

        ed_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker(ed_start);
//                showDatePicker(ed_start);
            }
        });
        ed_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker(ed_end);
//                showDatePicker(ed_end);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);
                ed_newsText.setError(null);
                ed_sort.setError(null);
                ed_start.setError(null);
                ed_end.setError(null);
                if (TextUtils.isEmpty(ed_newsText.getText().toString())) {
                    ed_newsText.setError(getString(R.string.newstext));
                } else if (TextUtils.isEmpty(ed_start.getText().toString())) {
                    ed_start.setError(getString(R.string.start));
                } else if (TextUtils.isEmpty(ed_end.getText().toString())) {
                    ed_end.setError(getString(R.string.end));
                } else if (!Utils.compareDates(ed_start.getText().toString(), ed_end.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_date));
                    ed_end.setError(getString(R.string.error_date));
                } else if (TextUtils.isEmpty(ed_sort.getText().toString())) {
                    ed_sort.setError(getString(R.string.sort));
                } else {
                    Utils.hideSoftKeyboard(activity);
                    News object = new News();
                    int id = Utils.random9();
                    DBO.open();
                    if (sp.getInt("priority", 0) == 1) {
                        id =0;
                    }else {   if (DBO.getNewsById(id)) {
                            id = Utils.random9();
                        }
                    }
                    DBO.close();
                    object.setId((objectNews == null) ? id : objectNews.getId());
                    object.setTextAds(ed_newsText.getText().toString().trim());
                    object.setFromDate(ed_start.getText().toString().trim());
                    object.setToDate(ed_end.getText().toString().trim());
                    object.setSort(Integer.parseInt(ed_sort.getText().toString()));
                    object.setDeleted(false);
                    object.setUpdatedAt(Utils.getFormattedCurrentDate());
                    if (sp.getInt("priority", 0) == 1) {
                        addUpdateNews((objectNews == null) ? 0 : 1, object);
                    } else {
                        try {
                            Date endD = spf.parse(ed_end.getText().toString().trim());
                            Date startD = spf.parse(ed_start.getText().toString().trim());
                            object.setFromDate(sdf.format(startD.getTime()));
                            object.setToDate(sdf.format(endD.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        pd = new ProgressDialog(activity);
                        pd.setMessage(getString(R.string.wait));
                        pd.show();
                        pd.setCanceledOnTouchOutside(false);
                        DBOperations db = new DBOperations(activity);
                        ArrayList<News> newsList = new ArrayList<>();
                        newsList.add(object);
                        db.insertNews(newsList);
                        if ((objectNews == null))
                            Utils.showCustomToast(activity, getString(R.string.success_add));
                        else Utils.showCustomToast(activity, getString(R.string.success_edit));

                        updateAdapter();
                        if (dialog.isShowing()) dialog.dismiss();
                        if (pd.isShowing()) pd.dismiss();

                    }
//                    dialog.dismiss();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void showDateTimePicker(final EditText editText) {
        CustomDateTimePicker custom = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected,
                              Date dateSelected, int year, String monthFullName,
                              String monthShortName, int monthNumber, int date,
                              String weekDayFullName, String weekDayShortName,
                              int hour24, int hour12, int min, int sec,
                              String AM_PM) {
                editText.setText(year + "/" + (monthNumber + 1) + "/" + calendarSelected
                        .get(Calendar.DAY_OF_MONTH) + " " + hour24 + ":" + min);
            }

            @Override
            public void onCancel() {

            }
        });

        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());
        custom.showDialog();
    }

    public void showDatePicker(final EditText v) {
        final Calendar c = Calendar.getInstance(new Locale("en"));
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String Date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//                        String Date = getMonthForInt((monthOfYear )) + "-" + String.valueOf(year)+ "-" +dayOfMonth ;//31-DEC-15
                        v.setText(Date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
