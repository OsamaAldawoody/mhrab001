package com.newsolution.almhrab.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.R;

import java.util.ArrayList;

/**
 * Created by hp on 8/4/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.AdsViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    private static final int TYPE_FOOTER = 1;
    private final DBOperations DBO;
    private  OnRecycleViewItemClicked listener;
    private final SharedPreferences sp;
    private final SharedPreferences.Editor spedit;
    // private ArrayList<Posts> posts;
    Activity activity;
    ArrayList<News> adsList;


    public NewsAdapter(Activity activity, ArrayList<News> adsList, OnRecycleViewItemClicked listener) {
        this.activity = activity;
        this.adsList = adsList;
        this.listener=listener;
        sp = activity.getSharedPreferences(AppConst.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        DBO = new DBOperations(activity);
        DBO.createDatabase();
    }

    @Override
    public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_card, parent, false);
        AdsViewHolder contactViewHolder = new AdsViewHolder(itemView, TYPE_HEADER);
        return contactViewHolder;
        // }
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

    public class AdsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contact_card;
        int viewType;

        TextView tv_adsText;
        ImageView iv_delete,iv_edit;
        public AdsViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            tv_adsText = (TextView) itemView.findViewById(R.id.tv_adsText);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);


        }
    }

    private void deleteAds(final int id) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(activity.getString(R.string.tv_delTitle)).
                    setMessage(activity.getString(R.string.tv_delAttention))
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            delete(id);

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
    private void delete(int id) {
      DBO.open();
      DBO.delAds(id);
      DBO.close();
        Toast.makeText(activity,activity.getString(R.string.success_delete), Toast.LENGTH_SHORT).show();


    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    public interface OnRecycleViewItemClicked{
        public void onItemClicked(View view, int position);
        public void onItemClick(View view, int position);
    }

//    public interface OnItemClickListener {
//        public void onItemClick(View view , int position);
//    }
}
