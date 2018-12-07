package com.isae.mohamad.mahallat.Classes;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.isae.mohamad.mahallat.Classes.utilities.Constants.SERVER_IP;

/**
 * Created by mohamad on 08/27/2018.
 */

public class Product implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String image;
    @SerializedName("price")
    private String price;
    @SerializedName("likeCount")
    private int likes;
    @SerializedName("liked")
    private boolean liked;
    @SerializedName("rating")
    private float rating;
    @SerializedName("rated")
    private boolean rated;
    @SerializedName("rate")
    private float rate;
    @SerializedName("favorited")
    private boolean favorited;
    @SerializedName("comments")
    private List<Comment> comments;

    public Product(int id, String name, String description, String image, String price,int likes, float rating ) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.image= SERVER_IP + image;
        this.price = price;
        this.likes = likes;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImage() {return SERVER_IP + this.image;}
    public String getPrice() {
        return price;
    }
    public int getLikes(){return this.likes;}
    public boolean getLiked() {return this.liked;}
    public float getRating(){return this.rating;}
    public boolean getRated() {return this.rated;}
    public float getRate() {return this.rate;}
    public boolean getFavorited() {return this.favorited;}
    public List<Comment> getComments() {return this.comments;}
}
