package com.skillshot.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skillshot.android.rest.model.Location;

import java.util.Locale;

public class VenueDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private TextView venue_name_tv;
    private TextView venue_age_tv;
    private TextView venue_address_tv;
    private TextView venue_phone_tv;
    private ImageView venue_phone_call;
    private ImageView venue_url_browse;
    private ImageView venue_map_direction;


//    private Location location;


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
//        double age = Double.valueOf(bundle.getString("age allowed"));
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

        venue_phone_call = (ImageView)findViewById(R.id.venue_phone_iv);
        venue_url_browse = (ImageView)findViewById(R.id.venue_url_iv );
        venue_map_direction = (ImageView)findViewById(R.id.venue_map_iv);

        venue_phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.fromParts("tel", "venue_phone", null);
                Intent intent = new Intent(android.content.Intent.ACTION_DIAL, uri);
                startActivity(intent);

            }
        });

        venue_url_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = venue_url;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        venue_map_direction.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v){
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", venue_lat, venue_lng, venue_address );
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }

        });
    }


    @Override
    public void onClick(View v) {

    }
}
