package com.skillshot.android.Adapters;

/**
 * Created by Dane on 11/11/2017.
 */

public class Items {

    protected String id;
    protected String name;
    protected String latitude;
    protected String longitude;
    protected int num_games;


    // encapsulating these variables - so that once the variables are set, you can't change them (to secure the variables)
    public Items(String id, String name, String latitude, String longitude, int num_games)
    {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.num_games = num_games;
    }



}
