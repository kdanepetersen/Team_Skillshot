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

//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;

import android.widget.Toast;
import android.widget.ImageButton;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    private GoogleMap map;
    private Location userLocation = null;


    private static String TAG = VenueListActivity.class.getSimpleName();

    public static final float MILES_PER_METER = (float) 0.000621371192;

    private Location[] locations;
    private JSONObject locationData;

    List<Location> machineList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton footer_map=(ImageButton)findViewById(R.id.footer_map);
        ImageButton footer_list=(ImageButton)findViewById(R.id.footer_list);
        ImageButton footer_description=(ImageButton)findViewById(R.id.footer_description);
        ImageButton backarrow=(ImageButton)findViewById(R.id.backarrow);
        ImageButton skillshotlogo=(ImageButton)findViewById(R.id.skillshotlogo);
        ImageButton allages=(ImageButton)findViewById(R.id.allages);
        ImageButton list_search=(ImageButton)findViewById(R.id.list_search);

        footer_map.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        footer_list.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

       Intent intent = new Intent(getApplicationContext(), VenueListActivity.class);
                startActivity(intent);

            }
        });

        footer_description.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), VenueDetailActivity.class);
                startActivity(intent);
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        skillshotlogo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        allages.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
//                allAges();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        list_search.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
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




 /*
     * Method to make json object request where json response starts wtih {
     * */
    public void loadMarkers() {
        Log.d("JSON", "loadMarkers");
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

//                                try {
//                                    if(locationData.getBoolean("all_ages")){
                                        Intent intent = new Intent(MainActivity.this,VenueDetailActivity.class);

                                        intent.putExtra("name", location.getName());
                                        intent.putExtra("address", location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code());
                                        intent.putExtra("phone", location.getPhone());
                                        intent.putExtra("website", location.getUrl());
                                        intent.putExtra("latlng", new LatLng(location.getLatitude(), location.getLongitude()));
                                        intent.putExtra("age allowed", location.isAll_ages());
                                        startActivity(intent);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }


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



//        //setting and adding marker on to the map page
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
//
//        switch (item.getItemId()){
////            case R.id.action_settings:
////                return true;
////            case R.id.action_venue_detail:
////
////////                startActivity(new Intent(MainActivity.this, VenueDetailActivity.class));
////////                break;
////////                openVenueDetailPage();
//////                Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
////////                return true;
////
////
//////                startActivity(new Intent(this, VenueDetailActivity.class));
//////                return true;
////                Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
////                return  true;
////
////            default:
////                break;
//
//        }
//
////        return true;
//
//        return super.onOptionsItemSelected(item);
//
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backarrow:
                // User chose the index item, show the search UI...
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.footer_list:
                // User chose the "login" item, show the login UI...
                Intent venue_list = new Intent(this, VenueListActivity.class);
                startActivity(venue_list);
                return true;
            case R.id.list_search:
                // User chose the "display categories" action, display the fashion categories
                Intent dc = new Intent(this, VenueListActivity.class);
                startActivity(dc);
                return true;
            case R.id.action_map:
                // User chose the "maps" action, display the map UI
                Intent map = new Intent(this, MainActivity.class);
                startActivity(map);
                return true;
            default:
                // The user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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



//    private void openVenueDetailPage() {
//        Intent intent = new Intent(this, VenueDetailActivity.class);
//        startActivity(intent);
//    }
//
//
////    //...............
////    @Override
////    public void onDestroy() {
////        super.onDestroy();
////        hidePDialog()MachineList;
////    }
////
//    private void hidePDialog() {
//        if (pDialog != null) {
//            pDialog.dismiss();
//            pDialog = null;
//        }
//    }
////
////    //.................
//
//    }




}

