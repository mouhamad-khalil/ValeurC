package com.isae.mohamad.mahallat.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohamad on 08/27/2018.
 */

public class Product {
    private String id;
    private String name;
    private String description;
    private String image;
    private String price;
    private String likes;
    private String rating;

    public Product(String id, String name, String description, String image, String price,String likes, String rating ) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.image=image;
        this.price = price;
        this.likes = likes;
        this.rating = rating;
    }

    // Constructor to convert JSON object into a Java class instance
    public Product(JSONObject object)
    {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.description = object.getString("description");
            this.image = object.getString("image");
            this.price = object.getString("price");
            this.likes = object.getString("likes");
            this.rating = object.getString("rating");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // Product.fromJson(jsonArray);
    public static ArrayList<Product> fromJson(JSONArray jsonObjects) {
        ArrayList<Product> products = new ArrayList<Product>();

        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                products.add(new Product(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImage() {
        return image;
    }
    public String getPrice() {
        return price;
    }
    public String getLikes(){return this.likes;}
    public String getRating(){return this.rating;}
}
