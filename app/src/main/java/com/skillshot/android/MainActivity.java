package com.skillshot.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    GoogleMap map;
//    List<MarkerData> markerDataList = new ArrayList<>();

    private static final String LOG_TAG = "Locations";

    // json array response url
    private String urlJsonArry = "https://skill-shot-dev.herokuapp.com/locations.json";

    private static String TAG = MainActivity.class.getSimpleName();

    Location location = new Location();

    // temporary string to show the parsed response
    private String jsonResponse;
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

//        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        map.getUiSettings();
//        map.getUiSettings().setZoomControlsEnabled(true);
         redirect();


    }


    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void redirect() {
        //showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        try {
                            Location location = new Location();
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject locObject = (JSONObject) response
                                        .get(i);

                                String idLoc = locObject.getString("id");
                                String name = locObject.getString("name");
                                String address = locObject.getString("address");
                                String city = locObject.getString("city");
                                String postal_code = locObject.getString("postal_code");
//                                JSONArray latlng = person
//                                        .getJSONArray("latlng");
                                float lat=(float)locObject.getDouble("latitude");
                                float lng=(float)locObject.getDouble("longitude");
                                String phone = locObject.getString("phone");


                                location.setName(idLoc);
                                location.setName(name);
                                location.setAddress(address);
                                location.setCity(city);
                                location.setPostal_code(postal_code);
                                location.setLatitude(lat);
                                location.setLongitude(lng);
                                location.setPhone(phone);


//                                System.out.println("id = "+location.getId());
//                                System.out.println("name = "+location.getName());
//                                System.out.println("address = "+location.getAddress());
//                                System.out.println("city = "+location.getCity());
//                                System.out.println("Postal Code = "+location.getPostal_code());
//                                System.out.println("Latitude = "+location.getLatitude());
//                                System.out.println("Longitude = "+location.getLongitude());
//                                System.out.println("Phone = "+location.getPhone());

                                LatLng lt = new LatLng(location.getLatitude(), location.getLongitude());

                                Log.d(TAG, location.getAddress());
//                                location.setLatlng(lt);

                                jsonResponse += "id: " + idLoc + "\n\n";
                                jsonResponse += "name: " + name + "\n\n";
                                jsonResponse += "address: " + address + "\n\n";
                                jsonResponse += "city: " + city + "\n\n";
                                jsonResponse += "Postal Code: " + postal_code + "\n\n";
                                jsonResponse += "Latitude: " + lat + "\n\n";
                                jsonResponse += "Longitude: " + lng + "\n\n\n";
                                jsonResponse += "Phone" + phone +"\n\n\n";

                                if(location.getCity().equals("Seattle"))
                                {
                                    map.addMarker(new MarkerOptions()
                                            .position(lt)
                                            .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
                                            .snippet(name + ", " + address + ", " + city + ", " + postal_code)
                                            .title(location.getName())).showInfoWindow();

                                }
                                else
                                {
                                    map.addMarker(new MarkerOptions()
                                            .position(lt)
                                            .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
                                            .title(location.getName())).showInfoWindow();

                                }





                                CircleOptions circleOptions = new CircleOptions()
                                        .center(lt)
                                        .radius(500) //in meter

                                        .strokeWidth(2)

                                        .strokeColor(Color.parseColor("#E7C8D8"))
                                        .fillColor(Color.parseColor("#DAE6E2"));
                                map.addCircle(circleOptions);




                                CameraPosition cameraPosition = new CameraPosition.Builder().target(lt).zoom(15.0f).build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                map.moveCamera(cameraUpdate);



                            }

                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        //hidepDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        Log.d(TAG, location.getAddress());
    }

    @SuppressWarnings("null")
    private void initializeMap() {
        // TODO Auto-generated method stub

        System.out.println("map");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        map.getUiSettings();
//        map.getUiSettings().setZoomControlsEnabled(false);

        //googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        //googleMap.setOnInfoWindowClickListener(this);


        if (map !=null)
        {


            float lat = location.getLatitude();
            float lng = location.getLongitude();
            LatLng position = new LatLng(lat,lng);
            System.out.println("latlong="+position);
            Marker marker =map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(location.getName())
                    .alpha(0.7f)
                    .snippet(location.getName() + ", " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code())

                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW)));

            CircleOptions circleOptions = new CircleOptions()
                    .center(position)
                    .radius(500) //in meter

                    .strokeWidth(2)

                    .strokeColor(Color.parseColor("#E7C8D8"))
                    .fillColor(Color.parseColor("#DAE6E2"));
            map.addCircle(circleOptions);

            marker.showInfoWindow();


            CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);



        }

        else
        {
            Log.d("myMap", "map is null");
        }


    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//    }

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
    public void onMapReady(GoogleMap map) {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(SHORTYS_LAT,
                        SHORTYS_LONG));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);

        map.moveCamera(center);
        map.animateCamera(zoom);

        redirect();

////         marker demo to replace w/ collection of markers from API data
//        addMarker(map);

//        List<Map<String, Object>> markers = {
//                {"id":"418-public-house", "name":"418 Public House", "address":
//        "418 NW 65th St", "city":"Seattle", "postal_code":"98117", "latitude":
//        47.6761639, "longitude":-122.362352, "phone":"(206) 783-0418", "url":
//        "http://www.418publichouse.com/", "all_ages":true, "num_games":4}
//        };

//        MarkerData[] markers = {
//                new MarkerData(47.6120477, -122.3396271, "Location1", "Hello to L1"),
//                new MarkerData(47.616755, -122.303970, "Location2", "Hello to L2"),
//                new MarkerData(47.623033, -122.320469, "Location3", "Hello to L3")
//        };

//        for (MarkerData marker : markers) {
//            Log.d("Marker",String.valueOf(marker.getLatitude()));
//
//            map.addMarker(new MarkerOptions()
//                    .position(new LatLng(marker.getLatitude(),
//                            marker.getLongitude()))
//                    .title(marker.getTitle())
//                    .snippet(marker.getSnippet())
//                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW)));
//        }
//    }

//    private void addMarker(GoogleMap map) {
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(47.6120477,
//                        -122.3396271))
//                .title("Downtown")
//                .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
//                .snippet("Skill Shot HQ"));
//    }

//    public void addMarkers(GoogleMap map, MarkerData markerData){
//        map.addMarker(new MarkerOptions()
//                    .position(new LatLng(markerData.getLatitude(), markerData.getLongitude()))
//                    .title(markerData.getTitle())
//                    .snippet(markerData.getSnippet())
//                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW)));
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
}

//class MarkerData{
//    float latitude;
//    float longitude;
//
//    String title;
//    String snippet;
//
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getSnippet() {
//        return snippet;
//    }
//
//    public void setSnippet(String snippet) {
//        this.snippet = snippet;
//    }
//
//
//    public float getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(float latitude) {
//        this.latitude = latitude;
//    }
//
//    public float getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(float longitude) {
//        this.longitude = longitude;
//    }
//
//    public MarkerData(float latitude, float longitude, String title, String snippet) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.title = title;
//        this.snippet = snippet;
//    }

