package com.skillshot.android.Adapters;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**= om URL, take JSON data and convert it into a list of Objects of type items (from Adapters/Items)
 */

public class JsonParse {

    private static final String TAG = JsonParse.class.getSimpleName();

    public List<Items> JsonParse(org.json.JSONArray jDataArray) {
        List<Items> itemsList = new ArrayList<>();
        try {
            for (int k = 0; k < jDataArray.length(); k++) {
                JSONObject data = jDataArray.getJSONObject(k);
                // put the data into variables
                String id = data.getString("id");
                String name = data.getString("name");
                String latitude = data.getString("latitude");
                String longitude = data.getString("longitude");
                String num_games = data.getString("num_games");
                Items items = new Items(id, name, latitude, longitude, num_games);
                itemsList.add(items);
            }
            return itemsList;

        } catch (final JSONException e) {
            Log.e(TAG, "JSON Parsing failed.  Error: " + e.getMessage());
        }
        return null;
    }
}
