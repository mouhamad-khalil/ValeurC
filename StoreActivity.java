package com.isae.mohamad.mahallat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.isae.mohamad.mahallat.Classes.Store;
import com.isae.mohamad.mahallat.Classes.utilities.GlideApp;
import com.isae.mohamad.mahallat.Classes.utilities.ViewPagerAdapter;

import org.json.JSONArray;

public class StoreActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Store mStore;

    private ImageView mBackground;
    private Toolbar mToolbar;
    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            //Here you get actionbar features if you
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Get the Store
            Intent intent = getIntent();

            mStore = (Store) intent.getSerializableExtra("Store");
            // Initialize Tabs
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            mBackground = findViewById(R.id.background);
            mRatingBar = findViewById(R.id.ratingBar);

            LoadStore(mStore);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            StoreDetailsFragment storeDetailsFragment = new StoreDetailsFragment();
            StoreProductsFragment storeProductsFragment = new StoreProductsFragment();

            adapter.addFrag( storeDetailsFragment.newInstance(mStore), "Details");
            adapter.addFrag( storeProductsFragment.newInstance(mStore.getId()), "Products");

            viewPager.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void LoadStore(Store store)
    {
        try {
            GlideApp.with(mBackground.getContext())
                    .asBitmap()
                    .load(store.getImage())
                    .fitCenter()
                    .placeholder(R.drawable.spinner)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            mBackground.setImageBitmap(resource);
                            return false;
                        }

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                    })
                    .into(mBackground);

            mRatingBar.setRating(store.getRating());
            mToolbar.setTitle(store.getName());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
