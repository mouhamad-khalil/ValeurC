package com.isae.mohamad.mahallat.Classes.utilities;

import java.nio.file.FileAlreadyExistsException;
import java.security.PublicKey;

/**
 * Created by mohamad on 10/21/2018.
 */

public class Constants {

    //************************************************************************
    // Store
    //************************************************************************
    public static final String Global_API = "http://142.93.100.254:8080/api/";
    public static final String Get_All_Stores_API = Global_API + "stores";
    public static final String Get_Store_By_Id_API = Global_API + "store/";
    public static final String Put_Store_Rate = Global_API + "store/rate/";
    public static final String Get_Store_Products = Global_API + "store/{0}/products";

    //************************************************************************
    // Product
    //************************************************************************
    public static final String Get_Product_By_Id_API = Global_API + "product/";
    public static final String Put_Product_Rate = Global_API + "product/rate/";

    //************************************************************************
    // Category
    //************************************************************************
    public static final String Get_Categories_API = Global_API + "categories";
    public static final String Get_Category_Products_API = Global_API + "/category/{0}/products";

    //************************************************************************
    // User
    //************************************************************************
    public static final String User_Register_API = Global_API + "user/register";
    public static final String User_login_API = Global_API + "user/login";
    public static final String User_Reset_Password_API = Global_API + "user/reset";

    //************************************************************************
    // Comment
    //************************************************************************


    //************************************************************************
    // Geofence
    //************************************************************************
    public static final String KEY_GEOFENCE_ID = "geofence_id";

    // Keys for flattened geofences stored in SharedPreferences.
    public static final String KEY_LATITUDE = "com.isae.mahallat.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "com.isae.mahallat.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "com.isae.mahallat.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION = "com.isae.mahallat.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE = "com.isae.mahallat.KEY_TRANSITION_TYPE";
    // The prefix for flattened geofence keys.
    public static final String KEY_PREFIX = "com.isae.mahallat.KEY";

    // Invalid values, used to test geofence storage when retrieving geofences.
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;

}
