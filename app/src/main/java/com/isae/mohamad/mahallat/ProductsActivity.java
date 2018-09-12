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

        // Add item to adapter
        ArrayList<Product> arrayOfProducts = new ArrayList<Product>();
        Product newProduct = new Product("1","Product1", "description 1","https://www.erasfashion.net/wp-content/uploads/2015/12/Mens-Shirts-001.jpg","price 1","1","2");
        Product newProduct2 = new Product("2","Product2", "description 2","https://www.dhresource.com/0x0s/f2-albu-g1-M00-0B-87-rBVaGFXu3ROACNaLAAGLGxlx4mI093.jpg/tee-shirts-en-ligne-en-ligne-tee-shirts-pour.jpg","price 2","1","2");
        Product newProduct3 = new Product("3","Product3", "description 3","https://gloimg.rowcdn.com/ROSE/pdm-product-pic/Clothing/2017/09/08/goods-img/1510291560954063330.jpg","price 3","2","3");

        arrayOfProducts.add(newProduct);
        arrayOfProducts.add(newProduct2);
        arrayOfProducts.add(newProduct3);
        ProductAdapter adapter = new ProductAdapter(this, arrayOfProducts);

        //adapter.add(newProduct);

        // Or even append an entire new collection
        // Fetching some data, data has now returned

        // If data was JSON, convert to ArrayList of Product objects.

        //**JSONArray jsonArray = ...;
        //ArrayList<Product> Products = Product.fromJson(jsonArray)
        //adapter.addAll(Products);
        ListView listView = (ListView) findViewById(R.id.lvItems);

        listView.setAdapter(adapter);
    }
}
