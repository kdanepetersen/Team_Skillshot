package com.skillshot.android.Adapters;
/**
 * Created by Dane on 11/11/2017.
 */
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    // create item list
    private List<Items> itemList;
    Context context;

    //   CREATE THE VIEWHOLDER TEMPLATE - A JAVA CONNECTION TO THE VARIABLES IN THE XML FILE
    public class ViewHolder extends RecyclerView.ViewHolder{

        // assign the variables for the xml file activity_pants_results_list
        public TextView tv_id;
        public TextView tv_name;
        public TextView tv_latitude;
        public TextView tv_longitude;
        public TextView tv_num_games;

        // create the viewholder
        public ViewHolder(View v){
            super(v);
            tv_id = (TextView) v.findViewById(R.id.tv_id);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_latitude = (TextView) v.findViewById(R.id.tv_latitude);
            tv_longitude = (TextView) v.findViewById(R.id.tv_longitude);
            tv_num_games = (TextView) v.findViewById(R.id.tv_num_games);
        }
    }

    // pass the items to the recyclerviewadapter
    public RecyclerViewAdapter(Context context, List<Items> itemList){

        Log.d(TAG, "******************* RecyclerViewAdapter started***************************");
        this.itemList = itemList;
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

        Items k = itemList.get(position);

        Vholder.tv_id.setText(k.id);
        Vholder.tv_id.setTextColor(Color.BLACK);
        Vholder.tv_name.setText(k.name);
        Vholder.tv_name.setTextColor(Color.BLACK);
        Vholder.tv_latitude.setText(k.latitude);
        Vholder.tv_latitude.setTextColor(Color.BLACK);
        Vholder.tv_longitude.setText(k.longitude);
        Vholder.tv_longitude.setTextColor(Color.BLACK);
        Vholder.tv_num_games.setText(k.num_games);
        Vholder.tv_num_games.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount(){
        Log.d(TAG, "******************* getItemCount started***************************");
        return itemList.size();
    }
}