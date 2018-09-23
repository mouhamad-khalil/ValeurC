package com.isae.mohamad.mahallat;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.isae.mohamad.mahallat.Classes.utilities.ViewPagerAdapter;


public class StoreActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new StoreDetailsFragment(), "Details");
            adapter.addFrag(new StoreCategoriesFragment(), "Categories");
            viewPager.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
