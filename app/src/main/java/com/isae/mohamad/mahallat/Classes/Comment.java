package com.isae.mohamad.mahallat.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.StreamHandler;

import javax.sql.StatementEvent;

/**
 * Created by mohamad on 08/30/2018.
 */

public class Comment {
    private String id;
    private String productId;
    private String storeId;
    private String userId;
    private String username;
    private String text;
    private String createdAt;

    public Comment(String id, String productId, String storeId, String userId, String username, String text,String createdAt) {
        this.id = id;
        this.productId = productId;
        this.storeId = storeId;
        this.userId = userId;
        this.username = username;
        this.text = text;
        this.createdAt = createdAt;
    }

    // Constructor to convert JSON object into a Java class instance
    public Comment(JSONObject object)
    {
        try {
            this.id = object.getString("id");
            this.productId = object.getString("product_id");
            this.storeId = object.getString("store_id");
            this.userId = object.getString("user_id");
            this.username = object.getString("username");
            this.text = object.getString("text");
            this.createdAt = object.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // Comment.fromJson(jsonArray);
    public static ArrayList<Comment> fromJson(JSONArray jsonObjects) {
        ArrayList<Comment> comments = new ArrayList<Comment>();

        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                comments.add(new Comment(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return comments;
    }

    public String getId() {return this.id;}
    public String getProductId() {return this.productId;}
    public String getStoreId() {return this.storeId;}
    public String getUserId() {return this.userId;}
    public String getUsername() {return this.username;}
    public String getText() {return this.text;}
    public String getCreatedAt() {return this.createdAt;}
}
