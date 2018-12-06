package com.isae.mohamad.mahallat.Classes.utilities;

import java.nio.file.FileAlreadyExistsException;
import java.security.PublicKey;

/**
 * Created by mohamad on 10/21/2018.
 */

public class Constants {

    public static final String TAG = "MahallatApp";

    public static final String SERVER_IP = "http://142.93.100.254:8080/";

    public static final String SP_NAME = "MahallatLogin";
    public static final String SP_CREDENTIALS = "Credentials";
    public static final String SP_USER_INFO = "UserInfo";
    public static final String SP_LOGGEDIN = "LoggedIn";

    //************************************************************************
    // Store
    //************************************************************************
    public static final String Global_API = SERVER_IP + "api/";
    public static final String Get_All_Stores_API = Global_API + "stores";
    public static final String Get_Stores_By_Category_API = Global_API + "category/{category_id}/stores";
    public static final String Get_Store_By_Id_API = Global_API + "store/";
    public static final String Put_Store_Rate_API = Global_API + "store/rate/{store_id}";
    public static final String Put_Store_Like_API = Global_API + "store/like";
    public static final String Get_Store_Products = Global_API + "store/{store_id}/products";

    //************************************************************************
    // Product
    //************************************************************************
    public static final String Get_Product_By_Id_API = Global_API + "product/";
    public static final String Put_Product_Rate_API = Global_API + "product/rate/{product_id}";
    public static final String Put_Product_Like_API = Global_API + "product/like";
    public static final String Put_Product_Favorite_API = Global_API + "product/add-favorite";
    public static final String Get_Product_Favorite_API = Global_API + "product/get-favorite";

    //************************************************************************
    // Category
    //************************************************************************
    public static final String Get_Categories_API = Global_API + "categories";

    //************************************************************************
    // User
    //************************************************************************
    public static final String User_Register_API = Global_API + "user/register";
    public static final String User_login_API = SERVER_IP + "login";
    public static final String User_Details_API = Global_API + "user-details";
    public static final String User_Forget_Password_API = Global_API + "forgot-password";

    //************************************************************************
    // Comment
    //************************************************************************
    public static final String Post_Comment_API = Global_API + "comment/add";


    //************************************************************************
    // Geofence
    //************************************************************************
    public static final float GEOFENCE_RADIUS = 500.0f; // in meters


}
