package com.example.amps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
			    SharedPreferences.Editor editor = settings.edit();
		        editor.putString("userid", job.getString("userid"));
		        editor.putString("tokenid", job.getString("tokenid"));
		        editor.commit();
        settings.getString("tokenid", null))*/
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
        
		return (super.onCreateOptionsMenu(menu));
	}
	
	@Override	
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {/*
    		case R.id.project1:
    			intent = new Intent(this, ProjectActivity.class);
    			startActivity(intent);
    			finish();
    			break;
    		case R.id.project2:
    			intent = new Intent(this, ProjectActivity.class);
    			startActivity(intent);
    			finish();
    			break;*/
    		case R.id.home:
    			intent = new Intent(this, HomeActivity.class);
    			startActivity(intent);
    			finish();
    			break;
    		case R.id.admin:
    			break;
    		case R.id.profile:
    			intent = new Intent(this, ProfileActivity.class);
    			startActivity(intent);
    			finish();
    			break;
    		case R.id.theme:
    			break;
    		case R.id.help:
    			break;
    		case R.id.logout:
    			intent = new Intent(this, LoginActivity.class);
    			startActivity(intent);
    			finish();
    			break;
    		default:
    			break;
		}
		return false;
    }

}
