package com.example.foodexpress;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ShowReviews extends ListActivity {
	JSONParser jsonParser = new JSONParser();
	Review[] reviewList;
	ProgressDialog pDialog;
	private String URL_getReviews = "http://172.16.16.238///FoodExpress/getReviews.php";
	CustomArrayAdapter adapter;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_reviews);
		listView = (ListView) findViewById(android.R.id.list);
		
		new GetReviews().execute();
		
	}
	
	class GetReviews extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ShowReviews.this);
			pDialog.setMessage("Fetching reviews..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String shop = getIntent().getExtras().getString("Shop");
			String station = "Patna";
			//String station = getIntent().getExtras().getString("Station");
			String item = getIntent().getExtras().getString("Search");
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("shop", shop));
			params1.add(new BasicNameValuePair("station", station));
			params1.add(new BasicNameValuePair("item", item));
		
			JSONObject json = jsonParser.makeHttpRequest(URL_getReviews,"POST",params1);
			
			Log.d("Create Response", json.toString());

			// check for success tag
			if (json != null) {
	            try {
	                
	                if (json != null) {
	                    JSONArray ja = json.getJSONArray("categories");                        
	                    reviewList = new Review[ja.length()];
	                    
	                    for (int i = 0; i < ja.length(); i++) {
	                        JSONObject catObj = (JSONObject) ja.get(i);
	                        Review review = new Review(catObj.getString("name"),catObj.getInt("rating"),catObj.getString("review"));
	                        reviewList[i] = review;
	                    }
	                }
	 
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
			}
	 
			return null;
		}
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        adapter = new CustomArrayAdapter(getApplicationContext(), reviewList);
	        //setListAdapter(adapter);
	        listView = (ListView) findViewById(android.R.id.list);
	        listView.setAdapter(adapter);
	        
	        // setting up notification
	        
	        
	        String ns = Context.NOTIFICATION_SERVICE;
		 	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		 	int NOT_ID = 1;
	
		 	int icon = android.R.drawable.stat_notify_chat;
		 	CharSequence tickerText = "Hello ";
		 	long when = System.currentTimeMillis();
		
		 	int requestID = (int) System.currentTimeMillis(); 
		 	
		 	Notification notification = new Notification(icon, tickerText, when + 30000);
		 	Context context = getApplicationContext();
		 	Intent notificationIntent = new Intent(getApplicationContext(), AfterNotification.class); 
		 	notificationIntent.putExtra("Station", "Patna");
		 	notificationIntent.putExtra("Search", getIntent().getExtras().getString("Search"));
		 	notificationIntent.setAction("myString"+ requestID);
			PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestID, notificationIntent, 0);
			notificationIntent.setData((Uri.parse("mystring"+requestID)));
		    notification.setLatestEventInfo(context, "Just a moment", requestID + "", contentIntent);
		    notification.flags += Notification.FLAG_ONGOING_EVENT;
		    notification.flags += Notification.FLAG_AUTO_CANCEL;
		    mNotificationManager.notify(NOT_ID, notification);
			
		}
	}

}
