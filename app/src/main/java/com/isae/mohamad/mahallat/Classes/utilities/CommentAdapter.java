package com.isae.mohamad.mahallat.Classes.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.R;

import java.util.ArrayList;

/**
 * Created by mohamad on 09/22/2018.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    private static class ViewHolder {
        TextView username;
        TextView text;
        TextView date;
    }

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.list_comment, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            // Get the data item for this position
            Comment comment = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CommentAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                // If there's no view to re-use, inflate a brand new view for row
                viewHolder = new CommentAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_comment, parent, false);
                viewHolder.username = (TextView) convertView.findViewById(R.id.username);
                viewHolder.text = (TextView) convertView.findViewById(R.id.text);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolder);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolder = (CommentAdapter.ViewHolder) convertView.getTag();
            }
            // Populate the data from the data object via the viewHolder object
            // into the template view.
            viewHolder.username.setText(comment.getUsername());
            viewHolder.text.setText(comment.getText());
            viewHolder.date.setText(comment.getCreatedAt());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
