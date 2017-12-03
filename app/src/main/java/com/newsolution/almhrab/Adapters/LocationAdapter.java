package com.newsolution.almhrab.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.R;

import java.util.ArrayList;

/**
 * Created by hp on 7/28/2016.
 */
public class LocationAdapter extends ArrayAdapter<City> {

        // Initialise custom font, for example:

        ArrayList<City> items;
        public LocationAdapter(Context context, ArrayList<City> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            items=new ArrayList<>();
            items = objects;
        }

    @Override
    public int getCount() {
        return items.size();
    }

    // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(getContext().getResources().getColor(R.color.light_black));
            view.setTextSize(14);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setText(items.get(position).getName());
            return view;
        }

//        // Affects opened state of the spinner
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
//            view.setTypeface(font);
//            view.setTextColor(Color.BLACK);
//            view.setTextSize(14);
//            // view.setBackgroundColor(getContext().getResources().getColor(R.color.primary_color_dark));
//            view.setText(items.get(position).getName());
//            return view;
//        }
    }

