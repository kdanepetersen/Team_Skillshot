package com.skillshot.android;

import android.app.Dialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.skillshot.android.request.LocationRequest;



/**
 * Created by fiori on 11/21/17.
 */

public class LocationsActivity extends AppCompatActivity {

    //implements LocationListener

    public static final String LOCATION_ID = "com.skillshot.android.LOCATION_ID";
    public static final String LOCATIONS_ARRAY = "com.skillshot.android.LOCATIONS_ARRAY";
    public static final String FILTER_ALL_AGES = "com.skillshot.android.FILTER_ALL_AGES";
    public static final String FILTER_SORT = "com.skillshot.android.FILTER_SORT";

    private Location userLocation = null;


    private static final int PREFERRED_UPDATE_INTERVAL_MS = 5000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000;
    public static final float MILES_PER_METER = (float) 0.000621371192;

    protected GoogleApiClient mLocationClient;



    // Debugging tag for the application
    public static final String APPTAG = "SkillShot";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*
	 * Called by Location Services when the request to connect the
	 * client finishes successfully. At this point, you can
	 * request the current location or start periodic updates
	 */

//    @Override
//    public void onConnected(Bundle dataBundle) {
//        super.onConnected(dataBundle);
//
//        setUserLocation(getLocation());
//        if (getUserLocation() != null) {
//        } else {
//            Toast.makeText(this, R.string.location_unavailable, Toast.LENGTH_SHORT).show();
//        }
//
//    }

    public String numGamesString(int num) {
        String formatString = num == 1
                ? getResources().getString(R.string.n_game)
                : getResources().getString(R.string.n_games);
        return String.format(formatString, num);
    }

//    public String userDistanceString(double latitude, double longitude) {
//        return metersToMilesString(userDistance(latitude, longitude));
//    }
//
//    public String userDistanceString(com.skillshot.android.rest.model.Location location) {
//        return metersToMilesString(userDistance(location));
//    }
//
//    private String metersToMilesString(float meters) {
//        float distanceMiles = meters * MILES_PER_METER;
//        String formatString = distanceMiles > 1
//                ? distanceMiles >= 10
//                ? "%.0f"
//                : "%.1f"
//                : "%.2f";
//        return String.format(formatString + " mi", distanceMiles);
//    }
//
//    public float userDistance(double latitude, double longitude) {
//        float[] aDistance = new float[1];
//        Location.distanceBetween(getUserLocation().getLatitude(),
//                getUserLocation().getLongitude(), latitude, longitude, aDistance);
//        return aDistance[0];
//    }
//
//    public float userDistance(com.skillshot.android.rest.model.Location location) {
//        return userDistance(location.getLatitude(), location.getLongitude());
//    }
//
//    public Location getUserLocation() {
//        return userLocation;
//    }
//
//    public void setUserLocation(Location userLocation) {
//        this.userLocation = userLocation;
//    }
//
//     /* Invoked by the "Get Location" button.
//     *
//             * Calls getLastLocation() to get the current location
//     *
//             * @param v The view object associated with this method, in this case a Button.
//            */
//    public Location getLocation() {
//
//        // If Google Play Services is available
//        if (googleServicesAvailable()) {
//
//            // Get the current location
//            Location currentLocation = mLocationClient.getLastLocation();
//            return currentLocation;
//        }
//
//        return null;
//    }
//
//    public boolean googleServicesAvailable(){
//        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
//        int isAvailable = api.isGooglePlayServicesAvailable(this);
//        if(isAvailable == ConnectionResult.SUCCESS){
//            return true;
//        }else if (api.isUserResolvableError(isAvailable)){
//            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
//            dialog.show();
//        }else{
//            Toast.makeText(this, "can't connect to play service", Toast.LENGTH_LONG).show();
//        }
//        return  false;
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        // Report to the UI that the location was updated
//        setUserLocation(location);
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
