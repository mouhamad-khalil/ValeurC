package com.isae.mohamad.mahallat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.utilities.Constants;
import com.isae.mohamad.mahallat.Classes.utilities.ProductAdapter;

import org.json.JSONArray;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
public class ProductsActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    ListView listView;
    String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        try {
            final Context context = this.getApplicationContext();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            requestQueue = Volley.newRequestQueue(this);
            listView = (ListView) findViewById(R.id.lvItems);
            Intent intent = getIntent();
            categoryId = intent.getStringExtra("CategoryId");
            getProducts(context);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    try {
                        Product product = (Product) listView.getItemAtPosition(position);
                        Intent ProductIntent = new Intent(v.getContext(), ProductDetailsActivity.class);
                        ProductIntent.putExtra("Product", (Serializable) product);
                        startActivity(ProductIntent);
                    } catch (Exception e) {
                    }
                }
            });
        }catch (Exception e ){}

        // Add item to adapter
//        ArrayList<Product> arrayOfProducts = new ArrayList<Product>();
//        Product newProduct = new Product("1","Product1", "description 1","https://www.erasfashion.net/wp-content/uploads/2015/12/Mens-Shirts-001.jpg","price 1","1","2");
//        Product newProduct2 = new Product("2","Product2", "description 2","https://www.dhresource.com/0x0s/f2-albu-g1-M00-0B-87-rBVaGFXu3ROACNaLAAGLGxlx4mI093.jpg/tee-shirts-en-ligne-en-ligne-tee-shirts-pour.jpg","price 2","1","2");
//        Product newProduct3 = new Product("3","Product3", "description 3","https://gloimg.rowcdn.com/ROSE/pdm-product-pic/Clothing/2017/09/08/goods-img/1510291560954063330.jpg","price 3","2","3");
//
//        arrayOfProducts.add(newProduct);
//        arrayOfProducts.add(newProduct2);
//        arrayOfProducts.add(newProduct3);
//        ProductAdapter adapter = new ProductAdapter(this, arrayOfProducts);
//
//
//        listView.setAdapter(adapter);
    }

    private void getProducts(Context context) {
        final Context c = context;
        String url = MessageFormat.format(Constants.Get_Category_Products_API, categoryId);
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() > 0) {
                            ArrayList<Product> products = new ArrayList<Product>();
                            products = Product.fromJson(response);
                            ProductAdapter adapter = new ProductAdapter(c, products);
                            listView.setAdapter(adapter);
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        //setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
    }
}
