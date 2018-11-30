package com.isae.mohamad.mahallat.Classes.utilities;

import com.isae.mohamad.mahallat.Classes.*;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mohamad on 10/12/2018.
 */

public interface  APIInterface {

    // Get Stores
    @GET(Constants.Get_All_Stores_API)
    Call<List<Store>> doGetStoreList();

    // Get Stores
    @GET(Constants.Get_Store_Products)
    Call<List<Product>> doGetProductList(@Path(value = "store_id", encoded = true) String storeId);

    // Post Comment
    @POST(Constants.Post_Comment_API)
    @FormUrlEncoded
    Call<JSONObject> doPostComment(@Field("text") String text,
                                   @Field("productId") int productId,
                                   @Field("storeId") int storeId);

    // Put Store Rate
    @POST(Constants.Put_Store_Rate_API)
    Call<JSONObject> doPutStoreRate(@Path(value = "store_id", encoded = true) int storeId, @Body RequestBody rating);

    // Put Product Rate
    @POST(Constants.Put_Product_Rate_API)
    Call<JSONObject> doPutProductRate(@Path(value = "product_id", encoded = true) int productId, @Body RequestBody rating);

    // User Registration
    @POST(Constants.User_Register_API)
    Call<JSONObject> doUserRegister(@Body User user);

    // User Login
    @POST(Constants.User_login_API)
    Call<Void> doLogin();

    // User Forget Password
    @POST(Constants.User_Forget_Password_API)
    Call<JSONObject> doForgetPassword(@Body String email);

}
