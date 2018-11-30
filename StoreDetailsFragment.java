package com.isae.mohamad.mahallat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.Classes.Store;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.CommentAdapter;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;

import org.json.JSONObject;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class StoreDetailsFragment extends Fragment {

    private static final String ARG_PARAM = "Store";
    private Store mStore;
    TextView txtComment;
    RatingBar mRatingBar;

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
                ((TextView) view.findViewById(R.id.txtOpen)).setText(mStore.getOpenHour());
                ((TextView) view.findViewById(R.id.txtClose)).setText(mStore.getCloseHour());

                // User Review
                mRatingBar = (RatingBar)view.findViewById(R.id.ratingBar);
                if(mStore.getRated())
                    mRatingBar.setRating(mStore.getRate());
                txtComment = (TextView) view.findViewById(R.id.txtComment);

                Button btnSend = (Button) view.findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendComment(v);
                    }
                });

                mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        SendRate(rating);
                    }
                });

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendComment(v);
                    }
                });

                List<Comment> CommentsList = mStore.getComments();
                CommentAdapter adapter = new CommentAdapter(this.getContext(), CommentsList);

                ListView listView = (ListView) view.findViewById(R.id.LvComments);

                listView.setAdapter(adapter);
            }

            String credentials =  MyApplication.GetSavedCredentials();
            if(credentials == null)
                apiInterface = APIClient.getClient().create(APIInterface.class);
            else
                apiInterface = APIClient.getClient(credentials).create(APIInterface.class);

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
            String text = txtComment.getText().toString();

            /*Call the method in the interface to post a comment*/
            Call<JSONObject> call  = apiInterface.doPostComment( text,0,mStore.getId());

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if (response.isSuccessful() ) {
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
            JSONObject object = new JSONObject();
            object.put("rating", rating);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(object.toString()));
            /*Call the method in the interface to send the rating*/
            Call<JSONObject> call  = apiInterface.doPutStoreRate(mStore.getId(), body);

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                    if (response.isSuccessful() ) {
                        Toast.makeText(getContext(), "Your rate is saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Sorry, rate not saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
