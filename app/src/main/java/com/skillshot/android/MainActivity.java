package com.skillshot.android;

import android.app.Dialog;
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
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback //, GoogleMap.OnMyLocationChangeListener
    {

//    private static final float METERS_100 = 100;
    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    private GoogleMap map;

    private static final String LOG_TAG = "Locations";

    private static String TAG = MainActivity.class.getSimpleName();

    Location location = new Location();

    float distance;

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
                    Location location = new Location();
                    for(int i = 0; i < response.length(); i++){

                        JSONObject locObject = (JSONObject) response
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


                        float  latitude = location.getLatitude();
                        float longitude = location.getLongitude();

//                        onMyLocationChange(location);


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

    private void addMarker(Location location) {
        LatLng lt = new LatLng(location.getLatitude(), location.getLongitude());

        if (location.getCity().equals("Seattle"))
        {
            map.addMarker(new MarkerOptions()
            .position(lt)
            .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
            .snippet(distance + ": " + location.getName() + ", " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code())
            .title(location.getName())).showInfoWindow();
        }
        else
        {
            map.addMarker(new MarkerOptions()
            .position(lt)
            .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
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
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(SHORTYS_LAT,
                        SHORTYS_LONG));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

//    float distanceBetween(Location l1, Location l2)
//    {
//        float lat1= (float)l1.getLatitude();
//        float lon1=(float)l1.getLongitude();
//        float lat2=(float)l2.getLatitude();
//        float lon2=(float)l2.getLongitude();
//        float R = 6371; // km
//        float dLat = (float)((lat2-lat1)*Math.PI/180);
//        float dLon = (float)((lon2-lon1)*Math.PI/180);
//        lat1 = (float)(lat1*Math.PI/180);
//        lat2 = (float)(lat2*Math.PI/180);
//
//        float a = (float)(Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2));
//        float c = (float)(2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
//        float d = R * c * 1000;
//
//        return d;
//    }

//    @Override
//    public void onMyLocationChange(android.location.Location location) {
//        Location target = new Location("target");
//        List<LatLng> myLatlng = new ArrayList<>();
//        for(int i = 0; i < myLatlng.size(); i++){
//            myLatlng.add(new LatLng(location.getLatitude(), location.getLongitude()));
//        }
//        for(LatLng point : myLatlng) {
//            target.setLatitude((float)point.latitude);
//            target.setLongitude((float)point.longitude);
//            if(location.distanceTo(myLatlng) < METERS_100) {
//                // bingo!
//            }
//        }
//    }
}
