package com.example.amps;

import java.security.MessageDigest;
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

import com.example.amps.LoginActivity.AuthenticateUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {
	ProgressDialog dialog;
	private ArrayList<String> spinnerArray;
	String error_code;
	ArrayList<String> nameArray = new ArrayList<String>();
	ArrayList<String> projectIdArray = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userid = extras.getString("userid");
			tokenid = extras.getString("tokenid");
		}

		GetProjectInfo task = new GetProjectInfo();
		task.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
		
		
	}
	
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.buttonView:
				Spinner spinner = (Spinner) findViewById(R.id.spinnerProjects);
				int index = spinner.getSelectedItemPosition();
				Intent intent = new Intent(HomeActivity.this, ProjectActivity.class);
				intent.putExtra("userid", userid);
				intent.putExtra("tokenid", tokenid);
				intent.putExtra("project_id", projectIdArray.get(index));
				startActivity(intent);
				break;
			default:
				finish();
				break;
			}
		} catch (Exception e) {
		}
	}

	public class GetProjectInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(HomeActivity.this,
					"Retrieving Projects", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveProjects();

		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);

			Spinner spinner = (Spinner) findViewById(R.id.spinnerProjects);
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
					HomeActivity.this, android.R.layout.simple_spinner_item,
					android.R.id.text1);
			spinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(spinnerAdapter);
			for (int i = 0; i < nameArray.size(); i++) {
				spinnerAdapter.add(nameArray.get(i));
			}
			spinner.setSelection(0);
			spinnerAdapter.notifyDataSetChanged();
		}

		public String retrieveProjects() {
			String szaAPIURL = "http://54.251.38.14/AMPS/ampslt/AMPSAPI/";
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(szaAPIURL + "getProjectInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));

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
				for (int i = 0; i < data_array.length(); i++) {
					JSONObject dataJob = new JSONObject(data_array.getString(i));
					String name = dataJob.getString("name");
					nameArray.add(name);
					String project_id = dataJob.getString("project_id");
					projectIdArray.add(project_id);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
