package com.skillshot.android.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skillshot.android.R;

import java.util.List;

/**
 * Created by Dane on 11/25/2017.
 */
public class rva_VenueList {


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skillshot.android.R;

import java.util.List;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {
        private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

        // create item list
        private List<locations> venueList;
        Context context;

        //   CREATE THE VIEWHOLDER TEMPLATE - A JAVA CONNECTION TO THE VARIABLES IN THE XML FILE
        public class ViewHolder extends RecyclerView.ViewHolder{

            // assign the variables for the xml file activity_pants_results_list

            public TextView venue_name;
            public TextView distance;
            public TextView num_games;

            // create the viewholder
            public ViewHolder(View v){
                super(v);
                venue_name = (ImageView) v.findViewById(R.id.venue_name);
                distance = (TextView) v.findViewById(R.id.distance);
                num_games = (TextView) v.findViewById(R.id.num_games);
            }
        }

        // pass the items to the recyclerviewadapter
        public RecyclerViewAdapter(Context context, List<locations> venueList){

            Log.d(TAG, "******************* RecyclerViewAdapter started***************************");
            this.venueList = venueList;
            // recyclerview has to have access to the context
            this.context = context;
        }


        // override the recyclerviewadapter in the Android library
        // return a recyclerViewAdapter viewholder after calling the onCreateViewHolder method, sending in ViewGroup parent and viewType
        // EVERY TIME THE CARD IS CREATED, RUN ON CREATEVIEWHOLDER TO DISPLAY THE CARD OF DATA IN THE VIEWHOLDER

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            Log.d(TAG, "******************* RecyclerViewAdapter.ViewHolder started***************************");

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_result,parent,false);
            return new ViewHolder(view);
        }

        // BIND THE DATE TO THE POSITION ON THE SCREEN

        @Override
        public void onBindViewHolder(ViewHolder Vholder, int position){
            Log.d(TAG, "******************* onBindViewHolder started***************************");

            Items k = venueList.get(position);

            Vholder.venue_name.setText(k.venueName);
            Vholder.distance.setTextColor(Color.#F7AE00);
            Vholder.num_games.setText(k.num_games);

            Glide
                    .with(context)
                    .load(k.xxx)
                    .placeholder(R.drawable.xxx)
                    .into(Vholder.xxx);
        }

        @Override
        public int getItemCount(){
            Log.d(TAG, "******************* getItemCount started***************************");
            return venueList.size();
        }
    }
