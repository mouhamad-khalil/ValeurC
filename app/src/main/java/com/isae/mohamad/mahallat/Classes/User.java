package com.isae.mohamad.mahallat.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohamad on 08/30/2018.
 */

public class User {
    private String id;
    private String username;
    private String email;
    private String name;
    private String lastname;
    private String password;
    private String image;

    public User(String id, String username, String email, String name, String lastname,String password ,String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.image = image;
    }

    // Constructor to convert JSON object into a Java class instance
    public User(JSONObject object)
    {
        try {
            this.id = object.getString("id");
            this.username = object.getString("username");
            this.email = object.getString("email");
            this.name = object.getString("name");
            this.lastname = object.getString("lastname");
            this.password = object.getString("password");
            this.image = object.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject ToJson()
    {
        JSONObject user = new JSONObject();
        try {
            user.put("username", this.getUsername());
            user.put("name", this.getName());
            user.put("lastname", this.getLastname());
            user.put("password", this.getPassword());
            user.put("email", this.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return user;
    }

    public String getId(){return this.id;}
    public String getUsername(){return  this.username;}
    public String getEmail(){return this.email;}
    public String getName(){return this.name;}
    public String getLastname(){return  this.lastname;}
    public String getPassword(){return this.password;}
    public String getImage(){return this.image;}
}
