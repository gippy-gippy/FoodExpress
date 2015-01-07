package com.example.foodexpress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


@SuppressLint("NewApi")
public class BestNearestAll extends Activity {
	static String nearestStation = "Mars";
	ProgressDialog pDialog;
	Button best,nearest,all;
	String URL_getStations = "http://172.16.16.238///FoodExpress/getStations.php";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.best_nearest_all);
		
		new GetStations().execute();
		
		
        best = (Button) findViewById(R.id.best_option);
        nearest = (Button) findViewById(R.id.nearest_option);
        all = (Button) findViewById(R.id.button3);
        
        all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), AllShops.class);
				i.putExtra("Search", getIntent().getExtras().getString("Search"));
				i.putExtra("Train", getIntent().getExtras().getString("Train"));
				i.putExtra("Bogey", getIntent().getExtras().getString("Bogey"));
		        i.putExtra("Station", nearestStation);
		        startActivity(i);
			}
		});
        
        best.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent j = new Intent(getApplicationContext(), BestShop.class);
				j.putExtra("Search", getIntent().getExtras().getString("Search"));
				j.putExtra("Train", getIntent().getExtras().getString("Train"));
				j.putExtra("Bogey", getIntent().getExtras().getString("Bogey"));
		        j.putExtra("Station", nearestStation);
		        startActivity(j);
			}
		});
        
        nearest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent k = new Intent(getApplicationContext(), NearestShop.class);
				k.putExtra("Search", getIntent().getExtras().getString("Search"));
				k.putExtra("Train", getIntent().getExtras().getString("Train"));
				k.putExtra("Bogey", getIntent().getExtras().getString("Bogey"));
		        k.putExtra("Station", nearestStation);
		        startActivity(k);
			}
		});
	}
	
	private class GetStations extends AsyncTask<Void, Void, Void> {
		 
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(BestNearestAll.this);
	        pDialog.setMessage("Determining the nearest station..");
	        pDialog.setCancelable(false);
	        pDialog.show();
	 
	    }
	 
	    @Override
	    protected Void doInBackground(Void... v) {
	        ServiceHandler jsonParser = new ServiceHandler();
	        String json = jsonParser.makeServiceCall(URL_getStations, ServiceHandler.GET);
	 
	        Log.e("Response: ", "> " + json);
	 
	        if (json != null) {
	            try {
	                JSONObject jsonObj = new JSONObject(json);
	                if (jsonObj != null) {
	                    JSONArray categories = jsonObj.getJSONArray("categories");                        
	                    double currDistance,minDistance = 100000.00,currLatitude,currLongitude;
	                    GPSTracker gps = new GPSTracker(BestNearestAll.this);
	       
	                    while(true){
	                    	if(gps.canGetLocation())
	                    		break;
	                    	else
	                    		gps.showSettingsAlert();
	                    }
	                    
	                    currLatitude = gps.getLatitude();
	                    currLongitude = gps.getLongitude();
	                    Log.d("Current location", "latitude "+ currLatitude + " longitude "+ currLongitude);
	                    for (int i = 0; i < categories.length(); i++) {
	                        JSONObject catObj = (JSONObject) categories.get(i);
	                        currDistance = calculteDistance(Double.parseDouble(catObj.getString("latitude")), Double.parseDouble(catObj.getString("longitude")), currLatitude, currLongitude);
	                        Log.d("location", "minDistance: "+minDistance+" station: "+catObj.getString("station")+" currDistance: "+currDistance);
	                        if(currDistance < minDistance){
	                        	minDistance = currDistance;
	                        	nearestStation = catObj.getString("station");
	                        }
	                    }
	                    Log.d("Nearest Station", nearestStation);
	                }
	 
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	 
	        } else {
	            Log.e("JSON Data", "Didn't receive any data from server!");
	        }
	 
	        return null;
	    }
	 
	    
	    private double calculteDistance(double lat1, double lon1, double lat2, double lon2) {
			// TODO Auto-generated method stub
	    	double theta = lon1 - lon2;
	    	double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta)); 
	    	dist = Math.acos(dist);
	    	dist = rad2deg(dist);
	    	dist = dist * 60 * 1.1515;
	    	dist = dist * 1.609344;
			return dist;
		}

		private double rad2deg(double rad) {
			// TODO Auto-generated method stub
			return (rad * 180 / Math.PI);
		}

		private double deg2rad(double deg) {
			// TODO Auto-generated method stub
			return (deg * Math.PI / 180.0);
		}

		@Override
	    protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        Log.d("Near", nearestStation);
	        
	        	
	    }
	 
	}
}
