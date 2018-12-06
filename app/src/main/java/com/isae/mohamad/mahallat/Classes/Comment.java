package com.isae.mohamad.mahallat.Classes;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.StreamHandler;

import javax.sql.StatementEvent;

/**
 * Created by mohamad on 08/30/2018.
 */

public class Comment implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private User user;
    @SerializedName("text")
    private String text;
    @SerializedName("createdAt")
    private String createdAt;

    public Comment(int id, User user, String text,String createdAt) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getId() {return this.id;}
    public User getUser() {return this.user;}
    public String getText() {return this.text;}
    public String getCreatedAt() {return this.createdAt;}
}
