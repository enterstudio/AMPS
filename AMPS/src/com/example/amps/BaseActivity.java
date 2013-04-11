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
        
		return true;
	}
	
	@Override	
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			case R.id.workingassets:
				intent = new Intent(this, WorkingAssetsListActivity.class);
    			startActivity(intent);
    			finish();
				break;
			case R.id.projects:
				intent = new Intent(this, ProjectListActivity.class);
    			startActivity(intent);
    			finish();
				break;
			case R.id.folder:
				break;
			case R.id.search:
				intent = new Intent(this, SearchActivity.class);
    			startActivity(intent);
    			finish();
				break;
			case R.id.report:
				intent = new Intent(this, ReportActivity.class);
    			startActivity(intent);
    			finish();
				break;
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
		return true;
    }

}
