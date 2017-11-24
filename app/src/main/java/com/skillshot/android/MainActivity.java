package com.skillshot.android;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skillshot.android.request.LocationRequest;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.skillshot.android.LocationsActivity.LOCATION_ID;
import static com.skillshot.android.LocationsActivity.MILES_PER_METER;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    private GoogleMap map;
    private Location userLocation = null;
    private static String TAG = MainActivity.class.getSimpleName();
    public static final float MILES_PER_METER = (float) 0.000621371192;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializeMap();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(googleServicesAvailable()){
            Toast.makeText(this, "Good", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void loadMarkers() {
        Log.d("JSON", "loadMarkers");

        // json array response url
        String url_locations = "https://skill-shot-dev.herokuapp.com/locations.json";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonReq = new JsonArrayRequest(url_locations, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON", "onResponse");
                try {
                    final Location location = new Location();
                    for(int i = 0; i < response.length(); i++){

                        final JSONObject locObject = (JSONObject) response
                                .get(i);

                        location.setId(locObject.getString("id"));
                        location.setName(locObject.getString("name"));
                        location.setAddress(locObject.getString("address"));
                        location.setCity(locObject.getString("city"));
                        location.setPostal_code(locObject.getString("postal_code"));
                        location.setLatitude((float)locObject.getDouble("latitude"));
                        location.setLongitude((float)locObject.getDouble("longitude"));
                        location.setPhone(locObject.getString("phone"));
                        location.setUrl(locObject.getString("url"));
                        location.setAll_ages(locObject.getBoolean("all_ages"));
                        location.setNum_games(locObject.getInt("num_games"));

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent intent = new Intent(MainActivity.this,VenueDetailActivity.class);
                                intent.putExtra("name", location.getName());
                                intent.putExtra("address", location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code());
                                intent.putExtra("phone", location.getPhone());
                                intent.putExtra("website", location.getUrl());

//                intent.putExtra("age allowed", location.getNum_games());
                                startActivity(intent);

                            }
                        });
 

                        addMarker(location);



                    }
                    // trigger refresh of recycler view

                } catch (JSONException e) {
                    Log.d("JSON", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSON", "Error: " + error.getMessage());
            }

        });
        // Add the request to the RequestQueue.
        queue.add(jsonReq);

    }

        public float userDistance(double latitude, double longitude) {
            float[] aDistance = new float[1];
            android.location.Location.distanceBetween(getUserLocation().getLatitude(),
                    getUserLocation().getLongitude(), latitude, longitude, aDistance);
            return aDistance[0];
        }



        public String userDistanceString(com.skillshot.android.rest.model.Location location) {
            return metersToMilesString(userDistance(location.getLatitude(), location.getLongitude()));
        }

        private String metersToMilesString(float meters) {
            float distanceMiles = meters * MILES_PER_METER;
            String formatString = distanceMiles > 1
                    ? distanceMiles >= 10
                    ? "%.0f"
                    : "%.1f"
                    : "%.2f";
            return String.format(formatString + " mi", distanceMiles);
        }

        public float userDistance(com.skillshot.android.rest.model.Location location) {
            return userDistance(location.getLatitude(), location.getLongitude());
        }

        public Location getUserLocation() {
            return userLocation;
        }

        public void setUserLocation(Location userLocation) {
            this.userLocation = userLocation;
        }

    private void addMarker(final Location location) {
        LatLng lt = new LatLng(location.getLatitude(), location.getLongitude());

//        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Intent intent = new Intent(MainActivity.this,VenueDetailActivity.class);
//                intent.putExtra("name", location.getName());
//                intent.putExtra("address", location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code());
//                intent.putExtra("phone", location.getPhone());
//                intent.putExtra("website", location.getUrl());
//
////                intent.putExtra("age allowed", location.getNum_games());
//                startActivity(intent);
//
//            }
//        });
        if (location.getCity().equals(" ")){
            map.addMarker(new MarkerOptions()
                    .position(lt)
                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
                    .title(location.getName())).showInfoWindow();
        }
        else {
            map.addMarker(new MarkerOptions()
                    .position(lt)
                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
                    .snippet(location.getNum_games() + " games " + location.getName() + ", " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code())
                    .title(location.getName())).showInfoWindow();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker centered on downtown Seattle.
     */
    @Override
    public void onMapReady(GoogleMap mapView) {
        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(SHORTYS_LAT, SHORTYS_LONG));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);

        mapView.moveCamera(center);
        mapView.animateCamera(zoom);
        map = mapView;

        loadMarkers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.venue_detail:
                startActivity(new Intent(MainActivity.this, VenueDetailActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }


    /*
     * Check whether Google Play Services are available.
	 *
	 * If not, then display dialog allowing user to update Google Play Services
	 *
	 * @return true if available, or false if not
     */
    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }else{
            Toast.makeText(this, "can't connect to play service", Toast.LENGTH_LONG).show();
        }
        return  false;
    }
    }
