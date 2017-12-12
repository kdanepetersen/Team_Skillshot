package com.skillshot.android;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skillshot.android.rest.model.Location;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.skillshot.android.AppController.TAG;

public class CustomeMachineListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Location> venues;
    private MainActivity mainActivity;

    public CustomeMachineListAdapter(Activity activity, List<Location> venues) {
        this.activity = activity;
        this.venues = venues;
    }


    @Override
    public int getCount() {
        return venues.size();
    }

    @Override
    public Object getItem(int position) {
        return venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.machine_list_row, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.machine_row);

        // getting venues data for the row
//        mainActivity.getTitle();

//        Log.d(TAG, "ghe " +  mainActivity.getTitle());
//        venues.add(location);
        Location l = venues.get(position);

        Log.d(TAG, "gg: " + l);
//        String venueName = venues.set(position, venues.get(position) );


        // name
        locationName.setText(l.getName()); ;



        // genre
//        String genreStr = "";
//        for (String str : m.getGenre()) {
//            genreStr += str + ", ";
//        }
//        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
//                genreStr.length() - 2) : genreStr;
//        genre.setText(genreStr);
//
//        // release year
//        year.setText(String.valueOf(m.getYear()));

        return convertView;
    }
}
