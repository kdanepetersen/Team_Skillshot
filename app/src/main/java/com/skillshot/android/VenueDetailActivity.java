package com.skillshot.android;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VenueDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Location location;
    private String locationId = null;

    private Context context;
    private TextView venue_name_tv;
    private TextView venue_age_tv;
    private TextView venue_address_tv;
    private TextView venue_phone_tv;
    private ImageView venue_phone_call;
    private ImageView venue_url_browse;
    private ImageView venue_map_direction;


    //..................

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Location> machineList = new ArrayList<>();
    private ListView listView;
    private CustomeMachineListAdapter machineAdapter;

    //.................


    ArrayList<JSONObject> venues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        final String venue_name = bundle.getString("name");
        final String venue_address = bundle.getString("address");
        final String venue_phone = bundle.getString("phone");
        boolean age = bundle.getBoolean("age allowed");
        final String venue_url = bundle.getString("website");
        final float venue_lat = bundle.getFloat("lat");
        final float venue_lng = bundle.getFloat("lng");

        venue_name_tv = findViewById(R.id.venue_name);
        venue_age_tv = findViewById(R.id.age);
        venue_address_tv = findViewById(R.id.venue_address);
        venue_phone_tv = findViewById(R.id.venue_phone);

        venue_name_tv.setText(venue_name);
        venue_address_tv.setText(venue_address);
        venue_phone_tv.setText(venue_phone);
        if(age){
            venue_age_tv.setText("No age restriction");
        }else{
            venue_age_tv.setText("21+");
        }

        venue_phone_call = (ImageView)findViewById(R.id.venue_phone_iv);
        venue_url_browse = (ImageView)findViewById(R.id.venue_url_iv );
        venue_map_direction = (ImageView)findViewById(R.id.venue_map_iv);


        //onclicking the phone icon, call can be made
        venue_phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.fromParts("tel", venue_phone, null);
                Intent intent = new Intent(android.content.Intent.ACTION_DIAL, uri);
                startActivity(intent);

            }
        });

        //on click of the globe icon, a url opens the website of the business
        venue_url_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = venue_url;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        //on click of the map marker icon, map of that particular locations opens
        venue_map_direction.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v){
                String uriBegin = "geo:0,0";
                String query = venue_address;
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });



//        //........................
            listView = (ListView) findViewById(R.id.machine_list);
            machineAdapter = new CustomeMachineListAdapter(this, venues );
//            machineAdapter = new CustomeMachineListAdapter(this, v);
            listView.setAdapter(machineAdapter);

            pDialog = new ProgressDialog(this);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();
//
//


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public void onClick(View v) {

    }
}
