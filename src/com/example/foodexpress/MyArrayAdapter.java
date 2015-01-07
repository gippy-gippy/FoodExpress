package com.example.foodexpress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<Shop> {
	private final Shop[] values;
	private final Context context;
	public MyArrayAdapter(Context context, Shop[] values) {
		super(context, R.layout.shop_list_item, values);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = values;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.shop_list_item, parent, false);
		    
		    TextView rating = (TextView) rowView.findViewById(R.id.icon);
		    TextView name = (TextView) rowView.findViewById(R.id.firstLine);
		    TextView location = (TextView) rowView.findViewById(R.id.secondLine);
		    
		    rating.setText(Double.toString(values[position].getRating()));
		    name.setText(values[position].getName());
		    
		    int off = values[position].getOffset();
		    String s = (off >= 0) ? "m towards the engine" : "m away from the engine";
		    		    
		    location.setText(Integer.toString(Math.abs(values[position].getOffset())) + s);
		    
		return rowView;
	}
}
