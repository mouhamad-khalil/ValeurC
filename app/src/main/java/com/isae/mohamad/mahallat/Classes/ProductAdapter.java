package com.isae.mohamad.mahallat.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isae.mohamad.mahallat.R;

import java.util.ArrayList;

/**
 * Created by mohamad on 08/27/2018.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private static class ViewHolder {
        TextView name;
        TextView home;
    }

    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, R.layout.list_item, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.title);
            viewHolder.home = (TextView) convertView.findViewById(R.id.description);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(product.getName());
        viewHolder.home.setText(product.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
