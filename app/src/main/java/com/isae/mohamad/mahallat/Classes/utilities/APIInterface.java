package com.isae.mohamad.mahallat.Classes.utilities;

import com.google.gson.JsonObject;
import com.isae.mohamad.mahallat.Classes.*;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mohamad on 10/12/2018.
 */

public interface  APIInterface {

    // Get Categories
    @GET(Constants.Get_Categories_API)
    Call<List<Category>> doGetCategories();

    // Get ALll Stores
    @GET(Constants.Get_All_Stores_API)
    Call<List<Store>> doGetAllStores();

    // Get Stores by Category
    @GET(Constants.Get_Stores_By_Category_API)
    Call<JsonObject> doGetStoreList(@Path(value = "category_id", encoded = true) int categoryId);

    // Get Stores
    @GET(Constants.Get_Store_Products)
    Call<JsonObject> doGetProductList(@Path(value = "store_id", encoded = true) String storeId);

    // Post Comment
    @POST(Constants.Post_Comment_API)
    @FormUrlEncoded
    Call<JSONObject> doPostComment(@Field("text") String text,
                                   @Field("productId") int productId,
                                   @Field("storeId") int storeId);

    // Put Store Rate
    @Headers("Content-Type: application/json")
    @POST(Constants.Put_Store_Rate_API)
    Call<JsonObject> doPutStoreRate(@Path(value = "store_id", encoded = true) int storeId, @Body JsonObject rating);

    // Put Product Rate
    @Headers("Content-Type: application/json")
    @POST(Constants.Put_Product_Rate_API)
    Call<JsonObject> doPutProductRate(@Path(value = "product_id", encoded = true) int productId, @Body JsonObject rating);

    // Add Product to Favorites
    @POST(Constants.Put_Product_Favorite_API)
    Call<JsonObject> doAddFavoriteProduct( @Body JsonObject productId);

    // Get Favorite Products
    @GET(Constants.Get_Product_Favorite_API)
    Call<JsonObject> doGetFavoriteProducts();

    // User Registration
    @POST(Constants.User_Register_API)
    Call<JSONObject> doUserRegister(@Body User user);

    // User Login
    @POST(Constants.User_login_API)
    Call<Void> doLogin();

    // User Login
    @GET(Constants.User_Details_API)
    Call<JsonObject> doGetUser();

    // User Forget Password
    @POST(Constants.User_Forget_Password_API)
    Call<JSONObject> doForgetPassword(@Body String email);

}
