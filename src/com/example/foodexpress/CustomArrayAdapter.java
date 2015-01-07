package com.example.foodexpress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class CustomArrayAdapter extends ArrayAdapter<Review> {
	private final Review[] values;
	private final Context context;
	
	public CustomArrayAdapter(Context context, Review[] values) {
		super(context, R.layout.review_list_item, values);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.review_list_item, parent, false);
		    
		    TextView name = (TextView) rowView.findViewById(R.id.name); 
		    RatingBar rating = (RatingBar) rowView.findViewById(R.id.rating);
		    TextView review = (TextView) rowView.findViewById(R.id.review);
		    
		    name.setText(values[position].name);
		    rating.setRating(values[position].rating);
		    review.setText(values[position].review);
		    
		return rowView;
	}
}
