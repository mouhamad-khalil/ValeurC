package com.isae.mohamad.mahallat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;
import com.isae.mohamad.mahallat.Classes.utilities.ProductAdapter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

            getFavoriteProducts(this);

            listView = (ListView) findViewById(R.id.lvItems);
            // When the user clicks on the ListItem
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    try {
                        Product product = (Product) listView.getItemAtPosition(position);
                        Intent ProductIntent = new Intent(v.getContext(), ProductDetailsActivity.class);
                        ProductIntent.putExtra("Product", (Serializable) product);
                        startActivity(ProductIntent);
                    }
                    catch(Exception e){}
                }
            });
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
