package com.skillshot.android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    private GoogleMap map;
    private Location userLocation = null;
    public static final float MILES_PER_METER = (float) 0.000621371192;

    // json array response url
    String url = "https://skill-shot-dev.herokuapp.com/";
    String url_locations = String.format("%s/locations.json", url);

    private Location[] locations;
    private JSONObject locationData;

    //..................
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Location> MachineList = new ArrayList<>();
    private ListView listView;
    private CustomeMachineListAdapter machineAdapter;

    //.................


//    ArrayList<JSONObject> venues = new ArrayList<>();

    List<Location> machineList = new ArrayList<>();
    
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



//        //.....................
//
////        listView = (ListView) findViewById(R.id.machine_list);
////        machineAdapter = new CustomeMachineListAdapter(this, MachineList);
////        listView.setAdapter(machineAdapter);
////
////        pDialog = new ProgressDialog(this);
////        // Showing progress dialog before making http request
////        pDialog.setMessage("Loading...");
////        pDialog.show();
//
////            // changing action bar color
////            getActionBar().setBackgroundDrawable(
////                    new ColorDrawable(Color.parseColor("#1b1b1b")));
//
//        // Creating volley request obj
//        JsonArrayRequest machineReq = new JsonArrayRequest(url,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        hidePDialog();
//
//                        // Parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//
//                                JSONObject obj = null;
//                                try {
//                                    obj = response.getJSONObject(i);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                Location location = new Location();
////                                    location.setName(obj.getString("name"));
//
//
//                                location.setId(obj.getString("id"));
//                                location.setName(obj.getString("name"));
//                                location.setAddress(obj.getString("address"));
//                                location.setCity(obj.getString("city"));
//                                location.setPostal_code(obj.getString("postal_code"));
//                                location.setLatitude((float)obj.getDouble("latitude"));
//                                location.setLongitude((float)obj.getDouble("longitude"));
//                                location.setPhone(obj.getString("phone"));
//                                location.setUrl(obj.getString("url"));
//                                location.setAll_ages(obj.getBoolean("all_ages"));
//                                location.setNum_games(obj.getInt("num_games"));
//
//
//
////                                    movie.setThumbnailUrl(obj.getString("image"));
////                                    movie.setRating(((Number) obj.get("rating"))
////                                            .doubleValue());
////                                    movie.setYear(obj.getInt("releaseYear"));
//
//                                // Genre is json array
////                                    JSONArray genreArry = obj.getJSONArray("genre");
////                                    ArrayList<String> genre = new ArrayList<String>();
////                                    for (int j = 0; j < genreArry.length(); j++) {
////                                        genre.add((String) genreArry.get(j));
////                                    }
////                                    movie.setGenre(genre);
////
////                                    // adding movie to movies array
////                                    movieList.add(movie);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        // notifying list adapter about data changes
//                        // so that it renders the list view with updated data
//                        machineAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hidePDialog();
//
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(machineReq);
//
//        //...................
    }



    /**
     * Method to make json object request where json response starts wtih {
     * */
    public void loadMarkers() {
        Log.d("JSON", "loadMarkers");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonReq = new JsonArrayRequest(url_locations, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON", "onResponse");
                locations = new Location[response.length()];
                try {
                    for(int i = 0; i < response.length(); i++){

                        Location location = new Location();

                        final JSONObject locObject = (JSONObject) response.get(i);

                        //adding each element to an arraylist
//                        venues.add(locObject);
//
//                        Log.d(TAG, "Ghebrehiwet " + locObject.getString("name"));
////                        getting all objects in a list
//                        if (venues != null) {
//                            for (i = 0; i < venues.size(); i++) {
//                                JSONObject venue = venues.get(i);
////                                Log.d(TAG, "Ghebre "+ venue);
//                                String nameObject = venue.getString("name");
//                                Intent intent = new Intent(MainActivity.this,VenueDetailActivity.class);
//                                intent.putExtra("nameObject",nameObject);
//                                Log.d(TAG, "Ghebre "+ nameObject);
//                            }
//                        }


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


                        locations[i] = location;

                        addMarker(location);

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                int index = Integer.parseInt(marker.getId().substring(1));
                                Location location = locations[index];
                                Intent intent = new Intent(MainActivity.this,VenueDetailActivity.class);


//                                intent.putExtra("name", marker.getTitle() );
//
//                                intent.putExtra("address", marker.getSnippet());


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


        //setting and adding marker on to the map page
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
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_venue_detail:
//                startActivity(new Intent(MainActivity.this, VenueDetailActivity.class));
//                break;
                openVenueDetailPage();
                Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
//        return true;
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

    private void openVenueDetailPage() {
        Intent intent = new Intent(this, VenueDetailActivity.class);
        startActivity(intent);
    }


//    //...............
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        hidePDialog()MachineList;
//    }
//
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
//
//    //.................

    }
