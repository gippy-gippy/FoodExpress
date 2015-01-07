package com.example.foodexpress;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class NearestShop extends ListActivity {
	JSONParser jsonParser = new JSONParser();
	Shop[] shopList;
	Shop[] nearestShopList = new Shop[1];
	ProgressDialog pDialog;
	private String URL_getShops = "http://172.16.16.238///FoodExpress/getShops.php";
	MyArrayAdapter adapter;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_shops);
		
		
		new GetNearestShop().execute();
	}
	
	class GetNearestShop extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(NearestShop.this);
			pDialog.setMessage("Fetching the list of shops..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String bogey = getIntent().getExtras().getString("Bogey");
			String train = getIntent().getExtras().getString("Train");
			String st = "Patna";
			//String st = getIntent().getExtras().getString("Station");
			String it = getIntent().getExtras().getString("Search");
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("bogey", bogey));
			params1.add(new BasicNameValuePair("train", train));
			params1.add(new BasicNameValuePair("station", st));
			params1.add(new BasicNameValuePair("item", it));
		
			JSONObject json = jsonParser.makeHttpRequest(URL_getShops,"POST",params1);
			
			Log.d("Create Response", json.toString());

			// check for success tag
			if (json != null) {
	            try {
	                
	                if (json != null) {
	                    JSONArray ja = json.getJSONArray("categories");                        
	                    shopList = new Shop[ja.length()];
	                    int bogeyoffset = 0;
	                    for (int i = 0; i < ja.length(); i++) {
	                        JSONObject catObj = (JSONObject) ja.get(i);
	                        Shop shop = new Shop(catObj.getString("shop"),catObj.getInt("offset"),catObj.getInt("ratingsum"),catObj.getInt("count"), catObj.getInt("bogeyoffset"));
	                        shopList[i] = shop;
	                        bogeyoffset = catObj.getInt("bogeyoffset");
	                    }
	                    
	                    int nearest = 0, minDist = 100000;
	                    for(int i=0; i<shopList.length; i++){
	                    	if(Math.abs(bogeyoffset - shopList[i].offset) < minDist){
	                    		nearest = i;
	                    	}
	                    }
	                    
	                    nearestShopList[0] = shopList[nearest];
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
	        adapter = new MyArrayAdapter(getApplicationContext(), nearestShopList);
	        //setListAdapter(adapter);
	        listView = (ListView) findViewById(android.R.id.list);
	        listView.setAdapter(adapter);
	        
		}
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		TextView tv = (TextView) v.findViewById(R.id.firstLine); 
		String shop = tv.getText().toString();
		Log.v("Shop", shop);
		
		Intent i = new Intent(getApplicationContext(), ShowReviews.class);
		i.putExtra("Search", getIntent().getExtras().getString("Search"));
		i.putExtra("Station", getIntent().getExtras().getString("Station"));
		i.putExtra("Shop", shop);
		startActivity(i);
	}

}
