package com.isae.mohamad.mahallat.Classes.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.isae.mohamad.mahallat.R;

/**
 * Created by mohamad on 09/15/2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.txtDetails);
        final ImageView image = view.findViewById(R.id.pic);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView txtLikes = view.findViewById(R.id.txtLikes);
        TextView txtCategory = view.findViewById(R.id.txtCategory);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        ratingBar.setRating(Float.parseFloat(infoWindowData.getRating()));
        txtLikes.setText(infoWindowData.getLikes());
        txtCategory.setText(infoWindowData.getCategory());
        GlideApp.with(image.getContext())
                .asBitmap()
                .load(infoWindowData.getImage())
                .fitCenter()
                .placeholder(R.drawable.spinner)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        image.setImageBitmap(resource);
                        return false;
                    }

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                })
                .into(image);
        return view;
    }
}
