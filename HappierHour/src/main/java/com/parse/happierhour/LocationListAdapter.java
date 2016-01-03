package com.parse.happierhour;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import im.delight.android.location.SimpleLocation;

/**
 * Created by Misanthropic on 1/2/16.
 */
public class LocationListAdapter extends BaseAdapter {

    public Context mContext;
    public int layoutResourceId;
    LatLng location;
    public ArrayList<ParseObject> data = new ArrayList<>();


    public LocationListAdapter(Context c, int id, ArrayList<ParseObject> dataIn, LatLng loc) {
        mContext = c;
        layoutResourceId = id;
        if (dataIn != null) {
            this.data = dataIn;
        }
        if(loc != null){
            location = loc;
        }
    }

    @Override
    public int getCount() {
        if (data.size() > 0) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.location_list_item, parent, false);
        }
        try {
            if (data != null) {
                //Uri uri = Uri.parse(data.get(position).getUrl());
                ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);
                //image.setImageURI(uri);
                //Picasso.with(mContext).load(data.get(position).url).into(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                double d = SimpleLocation.calculateDistance(location.latitude, location.longitude, data.get(position).getDouble("Lat"), data.get(position).getDouble("Long")) * 0.00062137;
                String dist = String.format("%.2f", d);
                TextView name = (TextView) convertView.findViewById(R.id.location_list_name);
                TextView hours = (TextView) convertView.findViewById(R.id.hours_list_field);
                TextView rating = (TextView) convertView.findViewById(R.id.ratings_list_field);
                TextView distance = (TextView) convertView.findViewById(R.id.distance_field);

                name.setText(data.get(position).getString("Name"));
                hours.setText("Hours: " + data.get(position).getString("StartTime") + " - " + data.get(position).getString("EndTime"));
                rating.setText("Ratings: " + String.valueOf(data.get(position).getDouble("Rating")).substring(0, 4));
                distance.setText("Distance: " + dist + " miles");
            }
        } catch (Exception e) {

        }

        return convertView;
    }
}
