package com.isae.mohamad.mahallat.Classes.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isae.mohamad.mahallat.Classes.Category;

import java.util.List;

/**
 * Created by mohamad on 12/05/2018.
 */

public class SpinnerAdapter extends ArrayAdapter<Category> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Category)
    private Category[] values;

    public SpinnerAdapter(Context context, int textViewResourceId, List<Category> values) {
        super(context, textViewResourceId, values);
        try {
            this.context = context;
            this.values = new Category[values.size()];
            this.values = values.toArray(this.values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public Category getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.WHITE);
        // Then you can get the current item using the values array (Categories array) and the current position
        // You can NOW reference each method you has created in your bean object (Category class)
        label.setText(values[position].getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getName());

        return label;
    }
}