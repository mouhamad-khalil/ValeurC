package com.isae.mohamad.mahallat.Classes;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.isae.mohamad.mahallat.Classes.utilities.Constants.SERVER_IP;

/**
 * Created by mohamad on 08/30/2018.
 */

public class Store implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("openHour")
    private String openHour;
    @SerializedName("closeHour")
    private String closeHour;
    @SerializedName("image")
    private String image;
    @SerializedName("comments")
    private List<Comment> comments;
    @SerializedName("likeCount")
    private int likes;
    @SerializedName("averageRating")
    private float rating;
    @SerializedName("liked")
    private boolean liked;
    @SerializedName("rated")
    private boolean rated;
    @SerializedName("rate")
    private float rate;
    @SerializedName("category")
    private Category category;

    public Store(int id, String name, String description, double lat,double lng,String openHour,String closeHour,
                 String image,List<Comment> comments, int likes, float rating, boolean liked, boolean rated, float rate,Category category) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.latitude = lat;
        this.longitude = lng;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.image= SERVER_IP + image;
        this.comments = comments;
        this.likes = likes;
        this.liked = liked;
        this.rating = rating;
        this.rated = rated;
        this.rate = rate;
        this.category = category;
    }


    public int getId() {return this.id;}
    public String getName(){return this.name;}
    public String getDescription() {return this.description;}
    public double getLatitude() {return this.longitude;}
    public double getLongitude() {return this.latitude;}
    public String getOpenHour() {return this.openHour;}
    public String getCloseHour() {return this.closeHour;}
    public String getImage() {return SERVER_IP + this.image;}
    public int getLikes() {return this.likes;}
    public float getRating() {return this.rating;}
    public boolean getLiked() {return this.liked;}
    public boolean getRated() {return this.rated;}
    public float getRate() {return this.rate;}
    public Category getCategory() {return this.category;}
    public List<Comment> getComments() {return this.comments;}
}
