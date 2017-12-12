package com.skillshot.android;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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


    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Location> machineList = new ArrayList<>();
    private ListView listView;
    private CustomeMachineListAdapter machineAdapter;



    List<Location> venues = new ArrayList<>();
    // json array response url
    String url = "https://skill-shot-dev.herokuapp.com/";
    String url_locations = String.format("%s/locations.json", url);

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

//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest locReq = new JsonArrayRequest(url_locations, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d(TAG, response.toString());
//                hidePDialog();
//                //parsing json
////                locations = new Location[response.length()];
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject obj = response.getJSONObject(i);
//                        Location location = new Location();
//                        location.setName(obj.getString("name"));
//                        venues.add(location);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                machineAdapter.notifyDataSetChanged();
//            }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("JSON", "Error: " + error.getMessage());
//                }
//
//            });
//
//        queue.add(locReq);


//        venues.add();
//        //........................
            listView = (ListView) findViewById(R.id.machine_list);

             machineAdapter = new CustomeMachineListAdapter(this, (ArrayList<Location>) venues);
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
    public boolean onCreateOptionsMenu (Menu menu) {
        // inflate my menu as a menu
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // look for id in the menu and set it to invisible
        menu.findItem(R.id.action_venue_list).setVisible(false);

        // https://developer.android.com/guide/topics/search/search-dialog.html
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_bar).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // User chose the index item, show the search UI...
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.action_venue_list:
                // User chose the "login" item, show the login UI...
                Intent venue_list = new Intent(this, VenueListActivity.class);
                startActivity(venue_list);
                return true;
            case R.id.action_venue_detail:
                // User chose the "about" item, show the about UI...
                Intent venue_detail = new Intent(this, VenueDetailActivity.class);
                startActivity(venue_detail);
                return true;
            case R.id.action_settings:
                // User chose the "display categories" action, display the fashion categories
                Intent dc = new Intent(this, MainActivity.class);
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
