package com.isae.mohamad.mahallat.Classes;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.isae.mohamad.mahallat.Classes.utilities.Constants.SERVER_IP;

/**
 * Created by mohamad on 08/30/2018.
 */

public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("password")
    private String password;
    @SerializedName("image")
    private String image;

    public User(int id, String username, String email, String name, String lastname,String password ,String image) {
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
            this.id = object.getInt("id");
            this.username = object.getString("username");
            this.email = object.getString("email");
            this.name = object.getString("name");
            this.lastname = object.getString("lastname");
            this.password = object.getString("password");
            this.image = SERVER_IP + object.getString("image");
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

    public int getId(){return this.id;}
    public String getUsername(){return  this.username;}
    public String getEmail(){return this.email;}
    public String getName(){return this.name;}
    public String getLastname(){return  this.lastname;}
    public String getPassword(){return this.password;}
    public String getImage(){return this.image;}
}
