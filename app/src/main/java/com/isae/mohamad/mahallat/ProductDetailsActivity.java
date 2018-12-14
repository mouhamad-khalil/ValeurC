package com.isae.mohamad.mahallat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.User;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.CommentAdapter;
import com.isae.mohamad.mahallat.Classes.utilities.GlideApp;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductDetailsActivity extends AppCompatActivity {

    private Product mProduct;
    private TextView txtComment;
    private RatingBar mUserRatingBar;
    private TextView txtLikes;
    private ImageButton btnLike;

    private List<Comment> CommentsList;
    private CommentAdapter adapter;
    private User user;

    private boolean isLoggedIn = false;

    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            mProduct = (Product) intent.getSerializableExtra("Product");

            if(mProduct != null) {

                ((TextView) findViewById(R.id.txtDescription)).setText(mProduct.getDescription());
                ((TextView) findViewById(R.id.txtPrice)).setText(mProduct.getPrice() + "$");
                txtLikes = findViewById(R.id.txtLikes);
                txtLikes.setText(String.valueOf(mProduct.getLikes()));

                final ImageView mBackground = ((ImageView)findViewById(R.id.background));

                GlideApp.with(mBackground.getContext())
                        .asBitmap()
                        .load(mProduct.getImage())
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

                // Product Rating
                ((RatingBar)findViewById(R.id.productRatingBar)).setRating(mProduct.getRating());

                // User Rating Review
                mUserRatingBar = (RatingBar)findViewById(R.id.userRatingBar);
                if(mProduct.getRated())
                    mUserRatingBar.setRating(mProduct.getRate());
                txtComment = (TextView) findViewById(R.id.txtComment);

                mUserRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if(isLoggedIn)
                            SendRate(rating);
                        else
                        {
                            mUserRatingBar.setRating(0);
                            Toast.makeText(ProductDetailsActivity.this, "Please Login first!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final CheckBox chkAddFavorite = (CheckBox) findViewById(R.id.chbkFav);
                chkAddFavorite.setChecked(mProduct.getFavorited());

                chkAddFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isLoggedIn)
                            AddToFavorite();
                        else {
                            Toast.makeText(ProductDetailsActivity.this, "Please Login first!", Toast.LENGTH_SHORT).show();
                            chkAddFavorite.setChecked(false);
                        }
                    }
                });

                btnLike = (ImageButton) findViewById(R.id.btnLike);
                btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isLoggedIn)
                            PutLike();
                        else
                            Toast.makeText(ProductDetailsActivity.this, "Please Login first!", Toast.LENGTH_SHORT).show();
                    }
                });

                if(mProduct.getLiked())
                    btnLike.setBackgroundColor(Color.rgb(249,187,40));

                Button btnSend = (Button) findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isLoggedIn)
                            SendComment(v);
                        else
                            Toast.makeText(ProductDetailsActivity.this, "Please Login first!", Toast.LENGTH_SHORT).show();
                    }
                });

                CommentsList = mProduct.getComments();
                adapter = new CommentAdapter(this, CommentsList);

                ListView listView = (ListView) findViewById(R.id.LvComments);

                listView.setAdapter(adapter);
            }

            String credentials =  MyApplication.GetSavedCredentials();
            if(credentials == null)
                apiInterface = APIClient.getClient().create(APIInterface.class);
            else {
                apiInterface = APIClient.getClient(credentials).create(APIInterface.class);
                isLoggedIn = true;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void SendComment(View v) {
        try {
            if(user == null )
                user = MyApplication.GetUserInfo();

            String text = txtComment.getText().toString();
            final Comment comment = new Comment(0, user, text, "");

            /*Call the method in the interface to post a comment*/
            Call<JSONObject> call  = apiInterface.doPostComment( text,mProduct.getId(),0);

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if (response.isSuccessful() ) {
                        CommentsList.add(comment);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ProductDetailsActivity.this, "Your comment is saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Toast.makeText(ProductDetailsActivity.this, "Sorry, comment not saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){}
    }

    private void SendRate( float rating){
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("rating", rating);

            /*Call the method in the interface to send the rating*/
            Call<JsonObject> call  = apiInterface.doPutProductRate(mProduct.getId(), jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    if (response.isSuccessful() ) {
                        Toast.makeText(getApplicationContext(), "Your rate is saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sorry, rate not saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void AddToFavorite( ){
        try{

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("productId", mProduct.getId());;
            /*Call the method in the interface to add to favorites*/
            Call<JsonObject> call  = apiInterface.doAddFavoriteProduct(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    if (response.isSuccessful() ) {

                        Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sorry, adding Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void PutLike( ){
        try{

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("productId", mProduct.getId());;
            /*Call the method in the interface to add to favorites*/
            Call<JsonObject> call  = apiInterface.doPutProductLike(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    if (response.isSuccessful() ) {
                        String result = response.body().get("success").getAsString();
                        int likes = Integer.parseInt(txtLikes.getText().toString());
                         if(result.contains("removed"))
                         {
                             txtLikes.setText(String.valueOf(likes - 1));
                             btnLike.setBackgroundColor(Color.TRANSPARENT);
                         }
                         else
                         {
                             txtLikes.setText(String.valueOf(likes +1));
                             btnLike.setBackgroundColor(Color.rgb(249,187,40));
                         }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sorry, adding Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
