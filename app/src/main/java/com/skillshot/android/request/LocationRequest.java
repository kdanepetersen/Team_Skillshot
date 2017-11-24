package com.skillshot.android.request;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skillshot.android.MainActivity;
import com.skillshot.android.rest.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fiori on 11/21/17.
 */

public class LocationRequest extends AppCompatActivity {


//    private final int SKILL_SHOT_YELLOW = 42;
//    private static final float DEFAULT_ZOOM = 15;
//    public static double SHORTYS_LAT = 47.613834;
//    public static double SHORTYS_LONG = -122.345043;
//    private GoogleMap map;
//
////    private Location userLocation = null;
//
//
//
//    private static String TAG = MainActivity.class.getSimpleName();
//    /**
//     * Method to make json object request where json response starts wtih {
//     * */
//
//
//    public void loadMarkers() {
//        // json array response url
//        String url_locations = "https://skill-shot-dev.herokuapp.com/locations.json";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonReq = new JsonArrayRequest(url_locations, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d("JSON", "onResponse");
//                try {
//                    Location location = new Location();
////                    settingLocationData(response, location);
//
//                    for (int i = 0; i < response.length(); i++) {
//
//                        JSONObject locObject = (JSONObject) response
//                                .get(i);
//
//                        location.setId(locObject.getString("id"));
//                        location.setName(locObject.getString("name"));
//                        location.setAddress(locObject.getString("address"));
//                        location.setCity(locObject.getString("city"));
//                        location.setPostal_code(locObject.getString("postal_code"));
//                        location.setLatitude((float) locObject.getDouble("latitude"));
//                        location.setLongitude((float) locObject.getDouble("longitude"));
//                        location.setPhone(locObject.getString("phone"));
//                        location.setUrl(locObject.getString("url"));
//                        location.setAll_ages(locObject.getBoolean("all_ages"));
//                        location.setNum_games(locObject.getInt("num_games"));
//
//                        addMarker(location);
//
//                    }
//
//                    // trigger refresh of recycler view
//
//                } catch (JSONException e) {
//                    Log.d("JSON", e.getMessage());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("JSON", "Error: " + error.getMessage());
//            }
//
//        });
//        // Add the request to the RequestQueue.
//        queue.add(jsonReq);
//        System.out.println("The List: " + queue.add(jsonReq));
//
//    }
//
////    private void settingLocationData(JSONArray response, Location location) throws JSONException {
////        for (int i = 0; i < response.length(); i++) {
////
////            JSONObject locObject = (JSONObject) response
////                    .get(i);
////
////            location.setId(locObject.getString("id"));
////            location.setName(locObject.getString("name"));
////            location.setAddress(locObject.getString("address"));
////            location.setCity(locObject.getString("city"));
////            location.setPostal_code(locObject.getString("postal_code"));
////            location.setLatitude((float) locObject.getDouble("latitude"));
////            location.setLongitude((float) locObject.getDouble("longitude"));
////            location.setPhone(locObject.getString("phone"));
////            location.setUrl(locObject.getString("url"));
////            location.setAll_ages(locObject.getBoolean("all_ages"));
////            location.setNum_games(locObject.getInt("num_games"));
////
////            addMarker(location);
////
////        }
////    }
//
//    private void addMarker(Location location) {
//        LatLng lt = new LatLng(location.getLatitude(), location.getLongitude());
//
//        if (location.getCity().equals("Seattle"))
//        {
//            map.addMarker(new MarkerOptions()
//                    .position(lt)
//                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
//                    .snippet(location.getNum_games() + " games " + location.getName() + ", " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code())
//                    .title(location.getName())).showInfoWindow();
//
////            .snippet(locationsActivity.numGamesString(location.getNum_games()) + " games " + location.getName() + ", " + location.getAddress() + ", " + location.getCity() + ", " + location.getPostal_code())
//        }
//        else
//        {
//            map.addMarker(new MarkerOptions()
//                    .position(lt)
//                    .icon(BitmapDescriptorFactory.defaultMarker(SKILL_SHOT_YELLOW))
//                    .title(location.getName())).showInfoWindow();
//        }
//    }
}
