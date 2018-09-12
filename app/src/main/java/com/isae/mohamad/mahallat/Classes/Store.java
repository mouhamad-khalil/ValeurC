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
    private LatLng latLng;
    private String openHour;
    private String closeHour;
    private String image;
    private String likes;
    private String rating;

    public Store(String id, String name, String description, String lat,String lng,String openHour,String closeHour, String image, String likes, String rating ) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.latLng = new LatLng( Double.parseDouble(lat),Double.parseDouble(lng));
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.image=image;
        this.likes = likes;
        this.rating = rating;
    }

    // Constructor to convert JSON object into a Java class instance
    public Store(JSONObject object)
    {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.description = object.getString("description");
            Double lat = Double.parseDouble(object.getString("description"));
            Double lng = Double.parseDouble(object.getString("description"));
            this.latLng = new LatLng(lat,lng);
            this.openHour = object.getString("image");
            this.closeHour = object.getString("image");
            this.image = object.getString("image");
            this.likes = object.getString("likes");
            this.rating = object.getString("rating");
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
    public LatLng getLatLng(){return this.latLng;}
    public String getOpenHour() {return this.openHour;}
    public String getCloseHour() {return this.closeHour;}
    public String getImage() {return this.image;}
    public String getLikes() {return this.likes;}
    public String getRating() {return this.rating;}

}
