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

public class Bogey extends Activity implements OnItemSelectedListener{
	Button go;
	JSONParser jsonParser = new JSONParser();
	Spinner bogeys;
	ArrayList<Bog> bogeyList;
	ProgressDialog pDialog;
	private String URL_getBogey = "http://172.16.16.238///FoodExpress/getBogey.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bogey);
		go = (Button) findViewById(R.id.select_bogey);
		bogeys = (Spinner) findViewById(R.id.bogey);
		bogeyList = new ArrayList<Bog>();
		
		bogeys.setOnItemSelectedListener(this);
	
		
		go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),BestNearestAll.class);
				i.putExtra("Search", getIntent().getExtras().getString("Search"));
				i.putExtra("Train", getIntent().getExtras().getString("Train"));
				i.putExtra("Bogey", bogeys.getSelectedItem().toString());
				Log.d("Item", getIntent().getExtras().getString("Search"));
				Log.d("Train", getIntent().getExtras().getString("Train"));
				Log.d("Bogey", bogeys.getSelectedItem().toString());
				startActivity(i);
			}
		});
	
		Log.d("Train", getIntent().getExtras().getString("Train"));
		new GetBogey().execute();
	}
	
	private void populateSpinner() {
	    ArrayList<String> lables = new ArrayList<String>();
	 
	    for (int i = 0; i < bogeyList.size(); i++) {
	        lables.add(bogeyList.get(i).getName());
	    }
	 
	    // Creating adapter for spinner
	    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
	 
	    // Drop down layout style
	    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	    // attaching data adapter to spinner
	    bogeys.setAdapter(spinnerAdapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> a0, View a1, int a2,
			long a3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> a0) {
		// TODO Auto-generated method stub
		
	}
	
	class GetBogey extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Bogey.this);
			pDialog.setMessage("Fetching the list of bogeys..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String id1 = "1";
			String id2 = "2";
			String tr = getIntent().getExtras().getString("Train");
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("id1", id1));
			params1.add(new BasicNameValuePair("id2", id2));
			params1.add(new BasicNameValuePair("train", tr));
		
			JSONObject json = jsonParser.makeHttpRequest(URL_getBogey,"POST",params1);
			
			Log.d("Create Response", json.toString());

			// check for success tag
			if (json != null) {
	            try {
	                
	                if (json != null) {
	                    JSONArray categories = json.getJSONArray("categories");                        
	                    
	                    for (int i = 0; i < categories.length(); i++) {
	                        JSONObject catObj = (JSONObject) categories.get(i);
	                        Bog cat = new Bog(catObj.getInt("id"),catObj.getString("bogey"));
	                        bogeyList.add(cat);
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
	        	populateSpinner();
		}
	}
}
