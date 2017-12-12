package com.skillshot.android;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    private GoogleMap map;
    private Location userLocation = null;
    private static String TAG = VenueListActivity.class.getSimpleName();
    public static final float MILES_PER_METER = (float) 0.000621371192;

//    private JSONObject locationData;


    private Location[] locations;
    private JSONObject locationData;


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
                locations = new Location[response.length()];
                try {
                    for(int i = 0; i < response.length(); i++){
                        Location location = new Location();

                        locationData = (JSONObject) response
                                .get(i);

                        location.setId(locationData.getString("id"));
                        location.setName(locationData.getString("name"));
                        location.setAddress(locationData.getString("address"));
                        location.setCity(locationData.getString("city"));
                        location.setPostal_code(locationData.getString("postal_code"));
                        location.setLatitude((float)locationData.getDouble("latitude"));
                        location.setLongitude((float)locationData.getDouble("longitude"));
                        location.setPhone(locationData.getString("phone"));
                        location.setUrl(locationData.getString("url"));
                        location.setAll_ages(locationData.getBoolean("all_ages"));
                        location.setNum_games(locationData.getInt("num_games"));

 
                        locations[i] = location; // add to locations list for later use

                        addMarker(location);


                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                int index = Integer.parseInt(marker.getId().substring(1));
                                Location location = locations[index];

                                Intent intent = new Intent(MainActivity.this,VenueDetailActivity.class);
                                intent.putExtra("name", location.getName());
                                intent.putExtra("address", location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code());
                                intent.putExtra("phone", location.getPhone());
                                intent.putExtra("website", location.getUrl());
                                intent.putExtra("latlng", new LatLng(location.getLatitude(), location.getLongitude()));
                                intent.putExtra("age allowed", location.isAll_ages());
                                startActivity(intent);

                            }
                        });


                    }

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

    private class DistanceSort implements Comparator<Location> {

        @Override
        public int compare(Location a, Location b) {
            return Float.compare(userDistance(a), userDistance(b));
        }

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

//                    .snippet(location.getNum_games()  + " games \n\n"  + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code() + "\n" + location.getPhone())
//                    .title(location.getName()))
//                    .showInfoWindow();

                    .snippet(location.getNum_games() + " games " + location.getName() + location.getId() + ", " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code())
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
//        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_venue_detail:

//                startActivity(new Intent(this, VenueDetailActivity.class));
//                return true;
                Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return  true;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    /*
     * Check whether Google Play Services are available.
	 * If not, then display dialog allowing user to update Google Play Services 
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
