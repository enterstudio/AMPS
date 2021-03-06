package com.example.amps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.amps.ProfileAccountFragment.EditUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity implements Settings  {
	ProgressDialog dialog;
	SharedPreferences settings;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(SETTINGS, 0);
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
		
		switch (item.getItemId()) {
			
    		case R.id.home:
    			intent = new Intent(this, HomeActivity.class);
    			startActivity(intent);
    			finish();
    			break;
    		case R.id.projects:
				intent = new Intent(this, ProjectListActivity.class);
    			startActivity(intent);
    			finish();
				break;
    		case R.id.workingassets:
				intent = new Intent(this, WorkingAssetsListActivity.class);
    			startActivity(intent);
    			finish();
				break;
			case R.id.folder:
				intent = new Intent(this, FolderActivity.class);
    			startActivity(intent);
    			finish();
				break;
    		case R.id.profile:
    			intent = new Intent(this, ProfileActivity.class);
    			startActivity(intent);
    			finish();
    			break;
    		case R.id.logout:
    			Logout task = new Logout();
				task.execute();
    			intent = new Intent(this, LoginActivity.class);
    			break;
    		default:
    			break;
		}
		return true;
    }

	public class Logout extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					BaseActivity.this,
					"Logging out", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return logout();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			Toast toast = Toast.makeText(
					BaseActivity.this,
					"Logged out successfully!", Toast.LENGTH_LONG);
			toast.show();
			startActivity(intent);
			finish();
		}

		public String logout() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "logout");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", settings.getString("tokenid", null)));
			postParameters.add(new BasicNameValuePair("userid", settings.getString("userid", null)));

			// Instantiate a POST HTTP method
			try {
				httppost.setEntity(new UrlEncodedFormEntity(postParameters));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				responseBody = httpclient.execute(httppost, responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return responseBody;
		}

		public void parseJSONResponse(String responseBody) {
			JSONArray json, data_array;
			JSONObject job;
			try {
				json = new JSONArray(responseBody);
				job = json.getJSONObject(0);
				data_array = job.getJSONArray("data_array");
				JSONObject dataJob = new JSONObject(data_array.getString(0));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
