package com.example.foodexpress;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class RatingReview extends Activity {
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	// url to create new product
	private static String url_create_product = "http://172.16.16.238///FoodExpress/ratingreview.php";
	private static final String TAG_SUCCESS = "success";
	RatingBar rb;
	EditText name;
	EditText review;
	String station,item,shop;
	Button b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratingreview);
		
		rb = (RatingBar) findViewById(R.id.ratingBar1);
		name = (EditText) findViewById(R.id.editText1);
		review = (EditText) findViewById(R.id.editText2);
		b = (Button) findViewById(R.id.submit_review_button);
		
		station = getIntent().getExtras().getString("Station");
		shop = getIntent().getExtras().getString("Shop");
		item = getIntent().getExtras().getString("Search");
		
		Log.d("Station", getIntent().getExtras().getString("Station"));
		Log.d("Shop", getIntent().getExtras().getString("Shop"));
		Log.d("Search", getIntent().getExtras().getString("Search"));
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AddRatingReview().execute();
			}
		});
	}
	
	class AddRatingReview extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(RatingReview.this);
			pDialog.setMessage("Adding rating and review..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String rate = "" + (int) rb.getRating();
			String n = name.getText().toString();
			String r = review.getText().toString();
			
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("station", station));
			params1.add(new BasicNameValuePair("shop", shop));
			params1.add(new BasicNameValuePair("item", item));
			params1.add(new BasicNameValuePair("rating", rate));
			params1.add(new BasicNameValuePair("name", n));
			params1.add(new BasicNameValuePair("review", r));
		
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,"POST",params1);
			
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(),ThankYou.class);
					startActivity(i);
					
					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
		}
	}
}
