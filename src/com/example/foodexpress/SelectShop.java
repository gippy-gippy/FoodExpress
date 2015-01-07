package com.example.foodexpress;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectShop extends Activity implements OnItemSelectedListener {
	Button rate;
	JSONParser jsonParser = new JSONParser();
	Spinner shops;
	TextView tv;
	//String item = getIntent().getExtras().getString("Search"); 
	String item;
	String station;
	String[] shopList;
	ProgressDialog pDialog;
	private String URL_getShops = "http://172.16.16.238///FoodExpress/getShops.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_shop);
		
		item = getIntent().getExtras().getString("Search");
		station = getIntent().getExtras().getString("Station");
		
		Log.d("Search", item);
		Log.d("Station", station);
		
		tv = (TextView) findViewById(R.id.selectshoptv);
		rate = (Button) findViewById(R.id.rate);
		shops = (Spinner) findViewById(R.id.spinner1);
		
		tv.setText("Select the shop you bought "+ item + " from");
		
		shops.setOnItemSelectedListener(this);
		
		rate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), RatingReview.class);
				i.putExtra("Station", getIntent().getExtras().getString("Station"));
				i.putExtra("Search", getIntent().getExtras().getString("Search"));
				i.putExtra("Shop", shops.getSelectedItem().toString());
				startActivity(i);
				finish();
			}
		});
		
		new GetShops().execute();
	}
	
	class GetShops extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SelectShop.this);
			pDialog.setMessage("Fetching the list of shops..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String id1 = "1";
			
			//String st = getIntent().getExtras().getString("Station");
			//String it = getIntent().getExtras().getString("Search");
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("bogey", "dummy"));
			params1.add(new BasicNameValuePair("station", station));
			params1.add(new BasicNameValuePair("item", item));
			params1.add(new BasicNameValuePair("train", "dummy"));
		
			JSONObject json = jsonParser.makeHttpRequest(URL_getShops,"POST",params1);
			
			Log.d("Create Response", json.toString());

			// check for success tag
			if (json != null) {
	            try {
	                
	                if (json != null) {
	                    JSONArray ja = json.getJSONArray("categories");
	                    shopList = new String[ja.length()];
	                    Log.d("ja size", Integer.toString(ja.length()));
	                    for (int i = 0; i < ja.length(); i++) {
	                        JSONObject catObj = (JSONObject) ja.get(i);
	                        Log.d("i", Integer.toString(i));
	                        Log.d("Jsjo", catObj.toString());
	                        Log.d("name", catObj.getString("shop"));
	                        //Shop shop = new Shop(catObj.getString("shop"));
	                        //Log.d("i", Integer.toString(i));
	                        shopList[i] = catObj.getString("shop");
	                    }
	                }
	 
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
			}
	 
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
	        
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        Log.d("list size", Integer.toString(shopList.length));
	        populateSpinner();
	        
		}
	}
	private void populateSpinner() {
	    ArrayList<String> lables = new ArrayList<String>();
	    Log.d("size list", Integer.toString(shopList.length));
	    for (int i = 0; i < shopList.length; i++) {
	        lables.add(shopList[i]);
	    }
	 
	    // Creating adapter for spinner
	    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
	 
	    // Drop down layout style
	    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	    // attaching data adapter to spinner
	    shops.setAdapter(spinnerAdapter);
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
