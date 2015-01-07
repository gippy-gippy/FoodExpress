package com.example.foodexpress;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Main extends Activity implements OnItemSelectedListener {


	EditText item;
	Button go;
	Spinner trains;
	ArrayList<Train> trainList;
	ProgressDialog pDialog;
	
	private String URL_getTrains = "http://172.16.16.238///FoodExpress/getTrains.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		item = (EditText) findViewById(R.id.search_item);
		go = (Button) findViewById(R.id.search_button);
		trains = (Spinner) findViewById(R.id.list_of_trains);
		trainList = new ArrayList<Train>();
		
		trains.setOnItemSelectedListener(this);
	
		
		go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),Bogey.class);
				i.putExtra("Search", item.getText().toString());
				i.putExtra("Train", trains.getSelectedItem().toString());
				startActivity(i);
			}
		});
		
		new GetTrains().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class GetTrains extends AsyncTask<Void, Void, Void> {
		 
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(Main.this);
	        pDialog.setMessage("Fetching list of trains..");
	        pDialog.setCancelable(false);
	        pDialog.show();
	 
	    }
	 
	    @Override
	    protected Void doInBackground(Void... v) {
	        ServiceHandler jsonParser = new ServiceHandler();
	        String json = jsonParser.makeServiceCall(URL_getTrains, ServiceHandler.GET);
	 
	        Log.e("Response: ", "> " + json);
	 
	        if (json != null) {
	            try {
	                JSONObject jsonObj = new JSONObject(json);
	                if (jsonObj != null) {
	                    JSONArray categories = jsonObj.getJSONArray("categories");                        
	                   
	                    for (int i = 0; i < categories.length(); i++) {
	                        JSONObject catObj = (JSONObject) categories.get(i);
	                        Train cat = new Train(catObj.getInt("id"),catObj.getString("train"));
	                        trainList.add(cat);
	                    }
	                }
	 
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	 
	        } else {
	            Log.e("JSON Data", "Didn't receive any data from server!");
	        }
	 
	        return null;
	    }
	 
	    
	    @Override
	    protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
	        if (pDialog.isShowing())
	            pDialog.dismiss();
	        populateSpinner();
	    }
	 
	}
	 
	/**
	 * Adding spinner data
	 * */
	private void populateSpinner() {
	    ArrayList<String> lables = new ArrayList<String>();
	 
	    for (int i = 0; i < trainList.size(); i++) {
	        lables.add(trainList.get(i).getName());
	    }
	 
	    // Creating adapter for spinner
	    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
	 
	    // Drop down layout style - list view with radio button
	    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	    // attaching data adapter to spinner
	    trains.setAdapter(spinnerAdapter);
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
}
