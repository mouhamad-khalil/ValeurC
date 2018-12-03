package com.isae.mohamad.mahallat.Classes;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohamad on 08/30/2018.
 */

public class Store {
    private String id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String openHour;
    private String closeHour;
    private String image;
    private int likes;
    private double rating;
    private boolean liked;
    private boolean rated;
    private double rate;

    public Store(String id, String name, String description, double lat,double lng,String openHour,String closeHour,
                 String image, int likes, double rating, boolean liked, boolean rated, double rate) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.latitude = lat;
        this.longitude = lng;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.image=image;
        this.likes = likes;
        this.rating = rating;
        this.liked = liked;
        this.rated = rated;
        this.rate = rate;
    }

    // Constructor to convert JSON object into a Java class instance
    public Store(JSONObject object)
    {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.description = object.getString("description");
            this.latitude = object.getDouble("latitude");
            this.longitude = object.getDouble("longitude");
            this.openHour = object.getString("openHour");
            this.closeHour = object.getString("closeHour");
            this.image = object.getString("image");
            this.likes = object.getInt("likeCount");
            this.rating = object.getDouble("averageRating");
            this.liked = object.getBoolean("liked");
            this.rated = object.getBoolean("rated");
            this.rate = object.getDouble("rate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // Product.fromJson(jsonArray);
    public static ArrayList<Store> fromJson(JSONArray jsonObjects) {
        ArrayList<Store> stores = new ArrayList<Store>();

        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                stores.add(new Store(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stores;
    }

    public String getId() {return this.id;}
    public String getName(){return this.name;}
    public String getDescription() {return this.description;}
    public double getLatitude() {return this.latitude;}
    public double getLongitude() {return this.longitude;}
    public String getOpenHour() {return this.openHour;}
    public String getCloseHour() {return this.closeHour;}
    public String getImage() {return this.image;}
    public int getLikes() {return this.likes;}
    public double getRating() {return this.rating;}
    public boolean getLiked() {return this.liked;}
    public boolean getRated() {return this.rated;}
    public double getRate() {return this.rate;}
}
