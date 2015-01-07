package com.example.foodexpress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AfterNotification extends Activity {
	Button button;
	RadioGroup rg;
	TextView tv;
	String station = "pluto";
	String item = "dosa";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.after_notification);
		
		tv = (TextView) findViewById(R.id.afternotificationtextView);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			station = extras.getString("Station");
			item = extras.getString("Search");
		}
		
		tv.setText("Have you purchased " + item + " from " + station + " station?");
		button = (Button) findViewById(R.id.rate);
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		
		button.setOnClickListener(new OnClickListener() {
			
			

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int selectId = rg.getCheckedRadioButtonId();
				
				if(selectId == R.id.radio0){
					Intent i = new Intent(getApplicationContext(), SelectShop.class);
					i.putExtra("Station", station);
					i.putExtra("Search", item);
					startActivity(i);
				}
				else if(selectId == R.id.radio1){
					startActivity(new Intent(getApplicationContext(), ThankYou.class));
				}
				else{
					// setting up notification after sometimes
					
					String ns = Context.NOTIFICATION_SERVICE;
				 	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
				 	int NOT_ID = 1;
			
				 	int icon = android.R.drawable.stat_notify_chat;
				 	CharSequence tickerText = "Hello ";
				 	long when = System.currentTimeMillis();
				
				 	int requestID = (int) System.currentTimeMillis(); 
				 	
				 	Notification notification = new Notification(icon, tickerText, when+30000);
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
				    
				    startActivity(new Intent(getApplicationContext(), ThankYou.class).putExtra("Flag", "later"));
				}
				
				finish();
			}
		});
	}

}
