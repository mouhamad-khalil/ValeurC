package com.isae.mohamad.mahallat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.CommentAdapter;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductDetailsActivity extends AppCompatActivity {

    Product mProduct;
    TextView txtComment;
    RatingBar mProductRatingBar;
    RatingBar mUserRatingBar;

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
                ((TextView) findViewById(R.id.txtPrice)).setText(mProduct.getPrice());

                // Product Rating
                mProductRatingBar = (RatingBar)findViewById(R.id.productRatingBar);

                // User Rating Review
                mUserRatingBar = (RatingBar)findViewById(R.id.userRatingBar);
                if(mProduct.getRated())
                    mUserRatingBar.setRating(mProduct.getRate());
                txtComment = (TextView) findViewById(R.id.txtComment);

                mUserRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        SendRate(rating);
                    }
                });

                Button btnSend = (Button) findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendComment(v);
                    }
                });

                List<Comment> CommentsList = mProduct.getComments();
                CommentAdapter adapter = new CommentAdapter(this, CommentsList);

                ListView listView = (ListView) findViewById(R.id.LvComments);

                listView.setAdapter(adapter);
            }

            /*Create handle for the RetrofitInstance interface*/
            apiInterface = APIClient.getClient().create(APIInterface.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void SendComment(View v) {
        try {
            String text = txtComment.getText().toString();

            /*Call the method in the interface to post a comment*/
            Call<JSONObject> call  = apiInterface.doPostComment( text,mProduct.getId(),0);

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if (response.isSuccessful() ) {
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
            JSONObject object = new JSONObject();
            object.put("rating", rating);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(object.toString()));
            /*Call the method in the interface to send the rating*/
            Call<JSONObject> call  = apiInterface.doPutProductRate(mProduct.getId(), body);

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if (response.isSuccessful() ) {
                        Toast.makeText(getApplicationContext(), "Your rate is saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sorry, rate not saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
