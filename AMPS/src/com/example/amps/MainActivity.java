package com.example.amps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;

public class MainActivity extends BaseActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
	
	/*@Override	
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
    		case R.id.project1:
    		case R.id.project2:
    		case R.id.home:
    			intent = new Intent(this, MainActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		case R.id.admin:
    		case R.id.profile:
    			intent = new Intent(this, ProfileAccountActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		case R.id.theme:
    		case R.id.help:
    		case R.id.logout:
    			intent = new Intent(this, MainActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		default:
    			return false;
		}
    }*/
    
    public void onClick(View view) {
    	Intent intent;
    	try {
    		switch (view.getId()) {
	    		case R.id.login:
	    			intent = new Intent(this, HomeActivity.class);
	    			startActivity(intent);
	    			finish();
    		}
    	}
    	catch (Exception e) {
    	}
    }
}
