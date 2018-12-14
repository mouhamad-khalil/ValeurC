package com.isae.mohamad.mahallat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.Classes.Store;
import com.isae.mohamad.mahallat.Classes.User;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.CommentAdapter;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class StoreDetailsFragment extends Fragment {

    private static final String ARG_PARAM = "Store";
    private Store mStore;
    private TextView txtComment;
    private RatingBar mRatingBar;
    private TextView txtLikes;
    private ImageButton btnLike;

    private CommentAdapter adapter;
    private List<Comment> CommentsList;
    private User user;

    boolean isLoggedIn = false;

    APIInterface apiInterface;

    public StoreDetailsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param store Parameter 1.
     * @return A new instance of fragment StoreDetailsFragment.
     */
    public static StoreDetailsFragment newInstance(Store store) {
        StoreDetailsFragment fragment = new StoreDetailsFragment();
        try {
            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM, store);
            fragment.setArguments(args);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                mStore = (Store) getArguments().getSerializable(ARG_PARAM);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_details, container, false);

        try {
            if(mStore != null) {

                ((TextView) view.findViewById(R.id.txtDescription)).setText(mStore.getDescription());

                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat formatShort = new SimpleDateFormat("hh:mm a");
                Date openHour = inputFormat.parse(mStore.getOpenHour());
                Date closeHour = inputFormat.parse(mStore.getCloseHour());
                ((TextView) view.findViewById(R.id.txtOpen)).setText(formatShort.format(openHour));
                ((TextView) view.findViewById(R.id.txtClose)).setText(formatShort.format(closeHour));

                txtLikes = (TextView) view.findViewById(R.id.txtLikes);
                txtLikes.setText(String.valueOf(mStore.getLikes()));

                // User Review
                mRatingBar = (RatingBar)view.findViewById(R.id.ratingBar);
                if(mStore.getRated())
                    mRatingBar.setRating(mStore.getRate());
                txtComment = (TextView) view.findViewById(R.id.txtComment);

                Button btnSend = (Button) view.findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isLoggedIn)
                            SendComment(v);
                        else
                            Toast.makeText(getContext(), "Please Login first!", Toast.LENGTH_SHORT).show();
                    }
                });

                mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if(isLoggedIn)
                            SendRate(rating);
                        else
                        {
                            mRatingBar.setRating(0);
                            Toast.makeText(getContext(), "Please Login first!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnLike = (ImageButton) view.findViewById(R.id.btnLike);
                btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isLoggedIn)
                            PutLike();
                        else
                            Toast.makeText(getContext(), "Please Login first!", Toast.LENGTH_SHORT).show();
                    }
                });

                if(mStore.getLiked())
                    btnLike.setBackgroundColor(Color.rgb(249,187,40));


                CommentsList = mStore.getComments();
                adapter = new CommentAdapter(this.getContext(), CommentsList);

                ListView listView = (ListView) view.findViewById(R.id.LvComments);

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

        // Inflate the layout for this fragment
        return view;
    }

    private void SendComment(View v) {
        try {
            if(user == null)
                user = MyApplication.GetUserInfo();

            String text = txtComment.getText().toString();
            final Comment comment = new Comment(0,user,text, "");

            /*Call the method in the interface to post a comment*/
            Call<JSONObject> call  = apiInterface.doPostComment( text,0,mStore.getId());

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if (response.isSuccessful() ) {
                        CommentsList.add(comment);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Your comment is saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, comment not saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SendRate( float rating){
        try{

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("rating", rating);

            /*Call the method in the interface to send the rating*/
            Call<JsonObject> call  = apiInterface.doPutStoreRate(mStore.getId(), jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    if (response.isSuccessful() ) {
                        Toast.makeText(getContext(), "Your rate is saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, rate not saved!", Toast.LENGTH_SHORT).show();
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
            jsonObject.addProperty("storeId", mStore.getId());;

            Call<JsonObject> call  = apiInterface.doPutStoreLike(jsonObject);

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
                    Toast.makeText(getContext(), "Sorry, adding Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
