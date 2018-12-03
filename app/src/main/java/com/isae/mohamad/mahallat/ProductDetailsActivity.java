package com.isae.mohamad.mahallat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.utilities.ViewPagerAdapter;

public class ProductDetailsActivity extends AppCompatActivity {

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            product = (Product) intent.getSerializableExtra("Product");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
