package com.isae.mohamad.mahallat.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohamad on 08/27/2018.
 */

public class Product {
    private String name;
    private String category;
    private String description;
    private String img;

    public Product(String name, String category, String description, String img ) {
        this.name=name;
        this.category=category;
        this.description=description;
        this.img=img;
    }

    // Constructor to convert JSON object into a Java class instance
    public Product(JSONObject object)
    {
        try {
            this.name = object.getString("name");
            this.category = object.getString("category");
            this.description = object.getString("description");
            this.img = object.getString("img");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
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
}
