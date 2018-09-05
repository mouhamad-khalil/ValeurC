package com.isae.mohamad.mahallat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.ProductAdapter;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Add item to adapter
        ArrayList<Product> arrayOfProducts = new ArrayList<Product>();
        Product newProduct = new Product("Product1", "San Diego1","description 1","img1");
        Product newProduct2 = new Product("Product2", "San Diego2","description 2","img2");
        arrayOfProducts.add(newProduct);
        arrayOfProducts.add(newProduct2);
        ProductAdapter adapter = new ProductAdapter(this, arrayOfProducts);

        //adapter.add(newProduct);

// Or even append an entire new collection
// Fetching some data, data has now returned

// If data was JSON, convert to ArrayList of User objects.

        //**JSONArray jsonArray = ...;
        //ArrayList<User> newUsers = User.fromJson(jsonArray)
        //adapter.addAll(newUsers);
        ListView listView = (ListView) findViewById(R.id.lvItems);

        listView.setAdapter(adapter);
    }
}
