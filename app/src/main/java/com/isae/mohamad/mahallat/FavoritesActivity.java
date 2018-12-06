package com.isae.mohamad.mahallat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;
import com.isae.mohamad.mahallat.Classes.utilities.ProductAdapter;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FavoritesActivity extends AppCompatActivity {

    APIInterface apiInterface;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            listView = (ListView) findViewById(R.id.lvItems);

            getFavoriteProducts(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getFavoriteProducts( Context context) {

        try {
            final Context c = context;

            String credentials = MyApplication.GetSavedCredentials();
            if (credentials != null)
            {
                apiInterface = APIClient.getClient(credentials).create(APIInterface.class);

                /*Call the method in the interface to get the favorites products list*/
                Call<JsonObject> call = apiInterface.doGetFavoriteProducts();

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            try {
                                JsonElement element = response.body().get("data");
                                Type listType = new TypeToken<List<Product>>() {
                                }.getType();

                                List<Product> products = new Gson().fromJson(element, listType);
                                ProductAdapter productAdapter = new ProductAdapter(c, products);
                                listView.setAdapter(productAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(c, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK, null);
        onBackPressed();
        return true;
    }
}
