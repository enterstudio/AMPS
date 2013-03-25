package com.example.amps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	String userid;
	String tokenid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override	
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
    		case R.id.project1:
    		case R.id.project2:
    			intent = new Intent(this, ProjectActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		case R.id.home:
    			intent = new Intent(this, HomeActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		case R.id.admin:
    		case R.id.profile:
    			intent = new Intent(this, ProfileActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		case R.id.theme:
    		case R.id.help:
    		case R.id.logout:
    			intent = new Intent(this, LoginActivity.class);
    			startActivity(intent);
    			finish();
    			return true;
    		default:
    			return false;
		}
    }

}
