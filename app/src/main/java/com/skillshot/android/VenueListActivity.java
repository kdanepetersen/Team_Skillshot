package com.skillshot.android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;
import com.skillshot.android.Adapters.CustomAdapter;
import com.skillshot.android.rest.model.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VenueListActivity extends AppCompatActivity {
    private final int SKILL_SHOT_YELLOW = 42;
    private static final String TAG = VenueListActivity.class.getSimpleName();
    private Location[] locations;
    private Location userLocation = null;
    private JSONObject locationData;
    public static final float MILES_PER_METER = (float) 0.000621371192;
    RecyclerView recyclerView;
    Context context;

    // declare recyclerViewAdapter variable to display venue list in
    private CustomAdapter recyclerViewAdapter;

    // declaring how the item in the recyclerview will be layed out
    RecyclerView.LayoutManager recylerViewLayoutManager;

    // create the list of items to display in the Image Adapter List - creates the itemList array private to this class
//    private List<Items> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLocationData();


        // context (interface to the global information about your application) is your app, and is what connects to the other classes.  Get application context gets all the back end connection information
        context = getApplicationContext();

        // set the reecyclerView variable to the RecyclerView data type - look in xml files to find recyclerview layout to be able to display the data in the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Log.d(TAG, "************************set the reecyclerView variable to the RecyclerView data type - look in xml files to find recyclerview layout to be able to display the data in the recycler view*******************");

        // use a linear layout manager
        recylerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recylerViewLayoutManager);

        // giving RecyclerViewAdapter permission to display the private itemList array
        recyclerViewAdapter = new CustomAdapter(context, locations);
        recyclerView.setAdapter(recyclerViewAdapter);


    }    /*   end of on create  */

    public void getLocationData() {
        // check to see if the mobile phone is connected to the internet
        if(isNetworkConnected(getApplicationContext()))
        {
			/* json object request for locations  */
            Log.d("JSON", "loadVenueLocations");

            /* json array response url  */
            String url_locations = "https://skill-shot-dev.herokuapp.com/locations.json";

            final RequestQueue queue = Volley.newRequestQueue(this);

            JsonArrayRequest jsonReq = new JsonArrayRequest(url_locations, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("JSON", "onResponse");
                    locations = new Location[response.length()];
                    try {
                        for(int i = 0; i < response.length(); i++){

                            locationData = (JSONObject) response.get(i);

                            Location location = new Location();
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

                        }   /*  end of for loop  */
                        // trigger refresh of recycler view
                        recyclerViewAdapter.setItems(locations);
                        recyclerViewAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.d("JSON", e.getMessage());
                    }  /* end of try-catch  */

                }   /*  end of OnResponse  */

            },   /*  end of JSON Array request */

                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("JSON", "Error: " + error.getMessage());

                        }   /*  end of onErrorResponse*/

                    }); /* }end of Response.ErrorListener )endofJsonArrayRequest with errorlistener   */

			/* Add the request to the RequestQueue.  */

			queue.add(jsonReq);

        }    /*   end of if is network connected = true  */
        else {
            // display toast indicating no internet connection, connect to the internet
            Toast.makeText(VenueListActivity.this, "No Internet Connection.  Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }  /*  end of is network connected false */
    }


    public static boolean isNetworkConnected(Context cntx){
        // look for the network connection and return a boolean if network is connected (True = 1) or not (false = 0)
        ConnectivityManager cm = (ConnectivityManager)
                cntx.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo nti = cm.getActiveNetworkInfo();
        return nti != null && nti.isConnectedOrConnecting();
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

/*
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
*/


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
// An Activity represents a single screen in an app.
    // You can start a new instance of an Activity by passing an Intent to startActivity().
    // The Intent describes the activity to start and carries any necessary data.
    // If you want to receive a result from the activity when it finishes, call startActivityForResult().
    // Your activity receives the result as a separate Intent object in your activity's onActivityResult() callback.
    // Intent main = new Intent(this, Index_Activity.class);
    // startActivity(main);

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
}
