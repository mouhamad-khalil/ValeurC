package com.isae.mohamad.mahallat.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohamad on 08/30/2018.
 */

public class Category {
    private String id;
    private String name;
    private String code;
    private String description;

    public Category(String id, String name, String code ,String description ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
    }

    // Constructor to convert JSON object into a Java class instance
    public Category(JSONObject object)
    {
        try {
            this.id = object.getString("id");
            this.name = object.getString("name");
            this.description = object.getString("description");
            this.code = object.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // Category.fromJson(jsonArray);
    public static ArrayList<Category> fromJson(JSONArray jsonObjects) {
        ArrayList<Category> categories = new ArrayList<Category>();

        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                categories.add(new Category(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }

    public String getId(){return this.id;}
    public String getName(){return this.name;}
    public String getCode(){return this.code;}
    public String getDescription(){return this.description;}

}