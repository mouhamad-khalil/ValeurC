package com.isae.mohamad.mahallat.Classes;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohamad on 08/30/2018.
 */

public class Category implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("description")
    private String description;

    public Category(int id, String name, String code ,String description ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public String getCode(){return this.code;}
    public String getDescription(){return this.description;}

}
