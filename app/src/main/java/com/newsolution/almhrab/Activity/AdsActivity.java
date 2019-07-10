package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsolution.almhrab.Adapters.AdsAdapter;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AdsActivity extends AppCompatActivity {
    Activity activity;
    private RecyclerView rv_ads;
    AdsAdapter adsAdapter;
    ArrayList<Ads> adsArrayList;
    private DBOperations DBO;
    private SharedPreferences sp;

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
        setContentView(R.layout.activity_ads);
        DBO = new DBOperations(this);
        DBO.createDatabase();
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        SharedPreferences.Editor spedit = sp.edit();
        TextView tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        tv_tittle.setText(getString(R.string.advertisement));
        rv_ads = (RecyclerView) findViewById(R.id.rv_ads);
        ImageView iv_addAds = (ImageView) findViewById(R.id.iv_addAds);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_addAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, AddAdsActivity.class));

            }
        });
        rv_ads.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_ads.setLayoutManager(llm);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        DBO.open();
        adsArrayList = DBO.getAdsList(sp.getInt("masjedId", -1));
        Log.i("+++ads", adsArrayList.size() + "  ");
        DBO.close();
        setAdapter(adsArrayList);
    }

    private void setAdapter(final ArrayList<Ads> list) {
        adsAdapter = new AdsAdapter(this, list, new AdsAdapter.OnRecycleViewItemClicked() {
            @Override
            public void onItemClicked(View view, int position) {
                deleteAds(position);
            }

            @Override
            public void onItemClick(View view, int position) {
                editAds(list.get(position));
            }

            @Override
            public void onView(View view, int position) {
                showAds(list.get(position));
            }
        });
        rv_ads.setAdapter(adsAdapter);
        adsAdapter.notifyDataSetChanged();
    }

    private void showAds(Ads ads) {
        Intent intent = new Intent(activity, ShowAdsActivity.class);
        intent.setAction("view");
        intent.putExtra("ads", ads);
        startActivity(intent);
    }

    private void editAds(Ads ads) {
        Intent intent = new Intent(activity, EditAdsActivity.class);
        intent.putExtra("ads", ads);
        startActivity(intent);
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
        DBO.open();
        DBO.delAdvertisement(adsArrayList.get(pos).getId(), sp.getInt("masjedId", -1));
        adsArrayList.remove(pos);
        adsAdapter.notifyDataSetChanged();
        DBO.close();
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

}
