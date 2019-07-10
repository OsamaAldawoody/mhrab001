package com.newsolution.almhrab.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsolution.almhrab.Adapters.AzkarAdapter;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.Azkar;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import java.util.ArrayList;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AzkarAct extends Activity {
    Activity activity;
    private RecyclerView rv_ads;
    AzkarAdapter adapter;
    ArrayList<Azkar> adsArrayList;
    private DBOperations DBO;
    private int id;
    private SharedPreferences sp;
    private EditText ed_count, ed_sort, ed_newsText;
    private Dialog dialog;
    private ProgressDialog pd;
    private CheckBox cb_isha, cb_magrib, cb_asr, cb_duhr, cb_fajer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        activity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setColor();
        }
        setContentView(R.layout.activity_news);
        DBO = new DBOperations(this);
        DBO.createDatabase();
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        SharedPreferences.Editor spedit = sp.edit();
        TextView tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        ImageView iv_addAds = (ImageView) findViewById(R.id.iv_addAds);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_tittle.setText(getString(R.string.azkar));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_addAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAzkarDialog(null);
            }
        });

        rv_ads = (RecyclerView) findViewById(R.id.rv_ads);
        rv_ads.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        DBO.open();
        adsArrayList = DBO.getAzkar();
        DBO.close();
        rv_ads.setLayoutManager(llm);
        setAdapter(adsArrayList);
    }

    private void setAdapter(final ArrayList<Azkar> list) {
        adapter = new AzkarAdapter(this, list, new AzkarAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                deleteAzkar(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                addAzkarDialog(list.get(position));
            }
        });
        rv_ads.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    private void addUpdateAzkar(final int action, final Azkar object) {
        if (Utils.isOnline(activity)) {
            pd = new ProgressDialog(activity);
            pd.setMessage(getString(R.string.wait));
            pd.show();
            pd.setCanceledOnTouchOutside(false);
            WS.addUpdateAzkar(activity, object, new OnLoadedFinished() {
                @Override
                public void onSuccess(String response) {
                    getAzkars(action);
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

    private void getAzkars(final int action) {
        WS.getAllAzkar(activity, new OnLoadedFinished() {
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
        adsArrayList = DBO.getAzkar();
        DBO.close();
        setAdapter(adsArrayList);
    }

    private void deleteAzkar(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(getString(R.string.confirm_delete)).
                setMessage(getString(R.string.tv_delAttention1))
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
                WS.AzkarDelete(activity, adsArrayList.get(pos).getId(), new OnLoadedFinished() {
                    @Override
                    public void onSuccess(String response) {
                        pd.dismiss();
                        DBO.open();
                        DBO.delAzkar(adsArrayList.get(pos).getId());
                        adsArrayList.remove(pos);
                        adapter.notifyDataSetChanged();
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
        } else {
            DBO.open();
            DBO.delAzkar(adsArrayList.get(pos).getId());
            adsArrayList.remove(pos);
            adapter.notifyDataSetChanged();
            DBO.close();
        }

    }

    private void addAzkarDialog(final Azkar objectAzkar) {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_edit_azkar, null);
        ed_newsText = (EditText) view.findViewById(R.id.ed_newsText);
        ed_count = (EditText) view.findViewById(R.id.ed_count);
        ed_sort = (EditText) view.findViewById(R.id.ed_sort);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);
        cb_fajer = (CheckBox) view.findViewById(R.id.cb_fajer);
        cb_duhr = (CheckBox) view.findViewById(R.id.cb_duhr);
        cb_asr = (CheckBox) view.findViewById(R.id.cb_asr);
        cb_magrib = (CheckBox) view.findViewById(R.id.cb_magrib);
        cb_isha = (CheckBox) view.findViewById(R.id.cb_isha);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        dialog = new Dialog(this);

        dialog.setTitle(getString(R.string.add_azkar));
        if (objectAzkar == null) {
            btn_add.setText(getString(R.string.add));
        } else {
            btn_add.setText(getString(R.string.edit_adv));
            ed_sort.setText(String.valueOf(objectAzkar.getSort()));
            ed_count.setText(String.valueOf(objectAzkar.getCount()));
            cb_fajer.setChecked(objectAzkar.isFajr());
            cb_duhr.setChecked(objectAzkar.isDhuhr());
            cb_asr.setChecked(objectAzkar.isAsr());
            cb_magrib.setChecked(objectAzkar.isMagrib());
            cb_isha.setChecked(objectAzkar.isha());
            ed_newsText.setText(objectAzkar.getTextAzakar());
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_newsText.setError(null);
                ed_sort.setError(null);
                ed_count.setError(null);
                if (TextUtils.isEmpty(ed_newsText.getText().toString())) {
                    ed_newsText.setError(getString(R.string.newstext));
                } else if (TextUtils.isEmpty(ed_sort.getText().toString())) {
                    ed_sort.setError(getString(R.string.sort));
                } else if (TextUtils.isEmpty(ed_count.getText().toString())) {
                    ed_count.setError(getString(R.string.count));
                } else {
                    Utils.hideSoftKeyboard(activity);
                    Azkar object = new Azkar();
                    int id = Utils.random9();
                    DBO.open();
                    if (sp.getInt("priority", 0) == 1) {
                        id = 0;
                    } else {
                        if (DBO.getAzkarById(id)) {
                            id = Utils.random9();
                        }
                    }
                    DBO.close();
                    object.setId((objectAzkar == null) ? id : objectAzkar.getId());
                    object.setTextAzakar(ed_newsText.getText().toString().trim());
                    object.setSort(Integer.parseInt(ed_sort.getText().toString()));
                    object.setCount(Integer.parseInt(ed_count.getText().toString()));
                    object.setFajr(cb_fajer.isChecked());
                    object.setDhuhr(cb_duhr.isChecked());
                    object.setAsr(cb_asr.isChecked());
                    object.setMagrib(cb_magrib.isChecked());
                    object.setIsha(cb_isha.isChecked());
                    object.setDeleted(false);
                    object.setUpdatedAt(Utils.getFormattedCurrentDate());
                    if (sp.getInt("priority", 0) == 1) {
                        addUpdateAzkar((objectAzkar == null) ? 0 : 1, object);
                    } else {
                        pd = new ProgressDialog(activity);
                        pd.setMessage(getString(R.string.wait));
                        pd.show();
                        pd.setCanceledOnTouchOutside(false);
                        DBOperations db = new DBOperations(activity);
                        ArrayList<Azkar> azkarList = new ArrayList<>();
                        azkarList.add(object);
                        db.insertAzkar(azkarList);
                        if ((objectAzkar == null))
                            Utils.showCustomToast(activity, getString(R.string.success_add));
                        else Utils.showCustomToast(activity, getString(R.string.success_edit));

                        updateAdapter();
                        if (dialog.isShowing()) dialog.dismiss();
                        if (pd.isShowing()) pd.dismiss();

                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (window != null) {
            lp.copyFrom(window.getAttributes());
            window.setAttributes(lp);
        }
    }
}
