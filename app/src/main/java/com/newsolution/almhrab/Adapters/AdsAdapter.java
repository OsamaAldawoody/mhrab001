package com.newsolution.almhrab.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.R;

import java.util.ArrayList;

/**
 * Created by hp on 8/4/2016.
 */
public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdsViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    private static final int TYPE_FOOTER = 1;
    private final DBOperations DBO;
    private OnRecycleViewItemClicked listener;
    private final SharedPreferences sp;
    private final SharedPreferences.Editor spedit;
    Activity activity;
    ArrayList<Ads> adsList;


    public AdsAdapter(Activity activity, ArrayList<Ads> adsList, OnRecycleViewItemClicked listener) {
        this.activity = activity;
        this.adsList = adsList;
        this.listener = listener;
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        DBO = new DBOperations(activity);
        DBO.createDatabase();
    }

    @Override
    public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_row, parent, false);
        AdsViewHolder contactViewHolder = new AdsViewHolder(itemView, TYPE_HEADER);
        return contactViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final AdsViewHolder holder, final int i) {
        Ads ads = adsList.get(i);
        String days = "";
        holder.tv_adsDays.setText(days);
        holder.tv_adsText.setText(ads.getTitle());
        holder.tv_adsPeriod.setText("من " + ads.getStartDate() + " إلى " + ads.getEndDate());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(view, i);
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, i);
            }
        });
        holder.iv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onView(view, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contact_card;
        int viewType;
        TextView tv_adsText;
        TextView tv_adsPeriod;
        TextView tv_adsDays;
        ImageView iv_delete, iv_edit, iv_view;

        public AdsViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            tv_adsText = (TextView) itemView.findViewById(R.id.tv_adsText);
            tv_adsPeriod = (TextView) itemView.findViewById(R.id.tv_adsPeriod);
            tv_adsDays = (TextView) itemView.findViewById(R.id.tv_adsDays);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            iv_view = (ImageView) itemView.findViewById(R.id.iv_view);


        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    public interface OnRecycleViewItemClicked {
        void onItemClicked(View view, int position);

        void onItemClick(View view, int position);

        void onView(View view, int position);
    }

}
