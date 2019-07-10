package com.newsolution.almhrab.Adapters;

import android.app.Activity;
import android.content.Context;
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
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.AdsViewHolder> {
    private static final int TYPE_HEADER = 0;
    private  OnRecycleViewItemClicked listener;
    private ArrayList<News> adsList;


    public NewsAdapter(Activity activity, ArrayList<News> adsList, OnRecycleViewItemClicked listener) {
        this.adsList = adsList;
        this.listener=listener;
        SharedPreferences sp = activity.getSharedPreferences(Utils.PREFS, Context.MODE_PRIVATE);
        DBOperations DBO = new DBOperations(activity);
        DBO.createDatabase();
    }

    @Override
    public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_card, parent, false);
        AdsViewHolder contactViewHolder = new AdsViewHolder(itemView, TYPE_HEADER);
        return contactViewHolder;

    }

    @Override
    public void onBindViewHolder(final AdsViewHolder holder, final int i) {
        holder.tv_adsText.setText(adsList.get(i).getTextAds());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(view, i);
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              listener.onItemClick(view,i);
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

    class AdsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contact_card;
        int viewType;

        TextView tv_adsText;
        ImageView iv_delete,iv_edit;
        AdsViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            tv_adsText = (TextView) itemView.findViewById(R.id.tv_adsText);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);


        }
    }



    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    public interface OnRecycleViewItemClicked{
         void onItemClicked(View view, int position);
         void onItemClick(View view, int position);
    }

}
