package com.skillshot.android.Adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.skillshot.android.R;
import com.skillshot.android.rest.model.Location;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private static final String TAG = CustomAdapter.class.getSimpleName();

    // create item list
    private Location[] locations;
    private Context context;

    //   CREATE THE VIEWHOLDER TEMPLATE - A JAVA CONNECTION TO THE VARIABLES IN THE XML FILE
    public class ViewHolder extends RecyclerView.ViewHolder{
        // assign the variables for the xml file activity_pants_results_list
        public TextView tv_id;
//        public TextView tv_name;
//        public TextView tv_latitude;
//        public TextView tv_longitude;
        public TextView tv_num_games;
        public  TextView tv_map;

        private ImageView venue_map_direction;

        // create the viewholder
        public ViewHolder(View v){
            super(v);
            tv_id = (TextView) v.findViewById(R.id.tv_id);
//            tv_name = (TextView) v.findViewById(R.id.tv_name);
//            tv_latitude = (TextView) v.findViewById(R.id.tv_latitude);
//            tv_longitude = (TextView) v.findViewById(R.id.tv_longitude);
            tv_num_games = (TextView) v.findViewById(R.id.tv_num_games);
            tv_map = (TextView)v.findViewById(R.id.tv_map);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Toast.makeText(context, "Location #: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    // pass the items to the recyclerviewadapter
    public CustomAdapter(Context context, Location[] locations){

        Log.d(TAG, "******************* RecyclerViewAdapter started***************************");
        this.locations = locations;
        // recyclerview has to have access to the context
        this.context = context;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d(TAG, "******************* RecyclerViewAdapter.ViewHolder started***************************");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_result,parent,false);
        return new ViewHolder(view);
    }

    // BIND THE DATE TO THE POSITION ON THE SCREEN

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position){
        Log.d(TAG, "******************* onBindViewHolder started***************************");

        Location location = locations[position];
        for(int j = 1; j <= locations.length-1; j++){
            Vholder.tv_id.setText(position + 1 + ") "  + location.getId());
            Vholder.tv_id.setTextColor(Color.BLACK);
//            Vholder.tv_name.setText(location.getName());
//            Vholder.tv_name.setTextColor(Color.BLACK);
//        Vholder.tv_latitude.setText(location.getLatitude().);
//            Vholder.tv_latitude.setTextColor(Color.BLACK);
//        Vholder.tv_longitude.setText(location.getLongitude());
//            Vholder.tv_longitude.setTextColor(Color.BLACK);
            Vholder.tv_num_games.setText(" " + location.getNum_games() + " games");
            Vholder.tv_num_games.setTextColor(Color.BLACK);

            Vholder.tv_map.setText("MAP");
            Vholder.tv_map.setTextColor(Color.BLACK);


            final String address = (" " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code()).toString();
            //on click of the map marker icon, map of that particular locations opens
            Vholder.tv_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    String uriBegin = "geo:0,0";
                    String query = address;
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery;
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }

            });

        }

//        Vholder.tv_num_games.setText(location.getNum_games());
//        Vholder.tv_num_games.setTextColor(Color.BLACK);


    }

    @Override
    public int getItemCount(){
        Log.d(TAG, "******************* getItemCount started***************************");
        if (locations != null) {
            Log.d(TAG, String.valueOf(locations.length));
            return locations.length;
        } else {
            Log.d(TAG, "no locations");
            return 0;
        }
    }

    public void setItems(Location[] items) {
        this.locations = items;
    }

}