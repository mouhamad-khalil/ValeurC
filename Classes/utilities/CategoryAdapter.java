package com.isae.mohamad.mahallat.Classes.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isae.mohamad.mahallat.Classes.Category;
import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.R;

import java.util.ArrayList;

/**
 * Created by mohamad on 09/23/2018.
 */

public class CategoryAdapter  extends ArrayAdapter<Category> {
    private static class ViewHolder {
        TextView title;
        TextView description;
    }

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, R.layout.grid_item, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            // Get the data item for this position
            Category category = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CategoryAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                // If there's no view to re-use, inflate a brand new view for row
                viewHolder = new CategoryAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.grid_item, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.description = (TextView) convertView.findViewById(R.id.description);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolder);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolder = (CategoryAdapter.ViewHolder) convertView.getTag();
            }
            // Populate the data from the data object via the viewHolder object
            // into the template view.
            viewHolder.title.setText(category.getName());
            viewHolder.description.setText(category.getDescription());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
