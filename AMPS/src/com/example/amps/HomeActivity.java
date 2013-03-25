package com.example.amps;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    userid = extras.getString("userid");
		    tokenid = extras.getString("tokenid");
		}
		
		TextView textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
		textViewWelcome.setText(tokenid);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 public boolean onOptionsItemSelected(MenuItem item) {
		 return super.onOptionsItemSelected(item);
	 }
	

}
