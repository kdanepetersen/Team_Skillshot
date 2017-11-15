package com.skillshot.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int SKILL_SHOT_YELLOW = 42;
    private static final float DEFAULT_ZOOM = 15;
    public static double SHORTYS_LAT = 47.613834;
    public static double SHORTYS_LONG = -122.345043;
    GoogleMap map;
    List<MarkerData> markerDataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
    public void onMapReady(GoogleMap map) {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(SHORTYS_LAT,
                        SHORTYS_LONG));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);

        map.moveCamera(center);
        map.animateCamera(zoom);

//         marker demo to replace w/ collection of markers from API data
        getMarkers(map);

        MarkerData markerData1 = new MarkerData(47.6120477, -122.3396271, "Location1", "Hello to L1");
        MarkerData markerData2 = new MarkerData(47.616755, -122.303970, "Location2", "Hello to L2");
        MarkerData markerData3 = new MarkerData(47.623033, -122.320469, "Location3", "Hello to L3");
//        List<MarkerData> markerDataList = new ArrayList<>();
        for(int i = 0; i < markerDataList.size(); i++){
            markerDataList.add(markerData1);
            markerDataList.add(markerData2);
            markerDataList.add(markerData3);
        }


        for(int i = 0; i < markerDataList.size(); i++){
            createMarker(map,markerDataList.get(i));
        }
    }

    private void getMarkers(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(47.6120477,
                        -122.3396271))
                .title("Downtown")
                .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
                .snippet("Skill Shot HQ"));
    }

    public Marker createMarker(GoogleMap map, MarkerData markerData){
        return map.addMarker(new MarkerOptions()
                    .position(new LatLng(markerData.getLatitude(), markerData.getLongitude()))
                    .anchor(.5f, .5f)
                    .title(markerData.getTitle())
                    .snippet(markerData.getSnippet())
                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW)));
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

class MarkerData{
    double latitude;
    double longitude;

    String title;
    String snippet;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public MarkerData(double latitude, double longitude, String title, String snippet) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
    }
}
