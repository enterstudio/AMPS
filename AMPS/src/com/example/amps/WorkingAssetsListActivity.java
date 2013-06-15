package com.example.amps;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class WorkingAssetsListActivity extends BaseActivity implements Settings,
		View.OnClickListener {
	ProgressDialog dialog;
	String error_code;
	String project_id;
	ArrayList<Project> projectArray = new ArrayList<Project>();
	ArrayList<Asset> workingAssetsArray = new ArrayList<Asset>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working_assets_list);
		setTitle("Working Assets");
		settings = getSharedPreferences(SETTINGS, 0);
		GetProjectInfo task = new GetProjectInfo();
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		//Details
		if (id < 100) {
		Asset a = workingAssetsArray.get(id);

		Intent intent = new Intent(WorkingAssetsListActivity.this,
				WorkingAssetsActivity.class);
		intent.putExtra("asset_id", a.getAsset_id());
		intent.putExtra("project_id", project_id);
		startActivity(intent);
		}
	}

	public class GetProjectInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(WorkingAssetsListActivity.this,
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

			AlertDialog.Builder alert = new AlertDialog.Builder(
					WorkingAssetsListActivity.this);
			alert.setTitle("Select project");
			final Spinner spinnerProject = new Spinner(
					WorkingAssetsListActivity.this);

			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
					WorkingAssetsListActivity.this,
					android.R.layout.simple_spinner_item, android.R.id.text1);
			spinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerProject.setAdapter(spinnerAdapter);
			for (int i = 0; i < projectArray.size(); i++) {
				spinnerAdapter.add(projectArray.get(i).getName());
			}
			spinnerProject.setSelection(0);
			spinnerAdapter.notifyDataSetChanged();
			alert.setView(spinnerProject);
			alert.setPositiveButton("Select",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							int index = spinnerProject
									.getSelectedItemPosition();
							project_id = projectArray.get(index)
									.getProject_id();
							dialog.dismiss();

							GetAssignedAssetOfUser task = new GetAssignedAssetOfUser();
							task.execute();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
							WorkingAssetsListActivity.this.finish();
						}
					});

			alert.show();
		}

		public String retrieveProjects() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getProjectInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", settings
					.getString("tokenid", null)));
			postParameters.add(new BasicNameValuePair("userid", settings
					.getString("userid", null)));

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
			Project p;
			try {
				json = new JSONArray(responseBody);
				job = json.getJSONObject(0);
				data_array = job.getJSONArray("data_array");
				for (int i = 0; i < data_array.length(); i++) {
					JSONObject dataJob = new JSONObject(data_array.getString(i));

					p = new Project();
					p.setProject_id(dataJob.getString("project_id"));
					p.setName(dataJob.getString("name"));
					projectArray.add(p);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class GetAssignedAssetOfUser extends
			AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(WorkingAssetsListActivity.this,
					"Retrieving Assets", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveAssets();

		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);

			View v = WorkingAssetsListActivity.this.findViewById(
					android.R.id.content).getRootView();
			createTableRow(v);
		}

		public String retrieveAssets() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL
					+ "getAssignedAssetOfUser");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", settings
					.getString("tokenid", null)));
			postParameters.add(new BasicNameValuePair("userid", settings
					.getString("userid", null)));
			postParameters.add(new BasicNameValuePair("projectid", project_id));
			postParameters
					.add(new BasicNameValuePair(
							"select",
							"[asset_id], [name], [ext], [estimated_datestart], [estimated_dateend], [tags], [trash], [created_userid], [created_datetime], [updated_userid], [updated_datetime]"));
			postParameters.add(new BasicNameValuePair("assigned_userid",
					settings.getString("userid", null)));
			postParameters.add(new BasicNameValuePair("start_pos", "1"));
			postParameters.add(new BasicNameValuePair("end_pos", "5"));
			// Instantiate a POST HTTP method
			try {
				httppost.setEntity(new UrlEncodedFormEntity(postParameters));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				responseBody = httpclient.execute(httppost, responseHandler);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			return responseBody;
		}

		public void parseJSONResponse(String responseBody) {
			JSONArray json, data_array;
			JSONObject job;
			Asset a;
			try {
				json = new JSONArray(responseBody);
				job = json.getJSONObject(0);
				data_array = job.getJSONArray("data_array");
				for (int i = 0; i < data_array.length(); i++) {
					JSONObject dataJob = new JSONObject(data_array.getString(i));

					a = new Asset();
					a.setAsset_id(dataJob.getString("asset_id"));
					a.setName(dataJob.getString("name"));
					a.setExt(dataJob.getString("ext"));
					a.setTracking_status(dataJob.getString("tracking_status"));
					a.setWorkflow_step_id(dataJob.getString("workflow_step_id"));
					workingAssetsArray.add(a);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void createTableRow(View v) {
		TableLayout tl = (TableLayout) findViewById(R.id.tableRowWorkingAssets);

		for (int i = 0; i < workingAssetsArray.size(); i++) {
			Asset a = workingAssetsArray.get(i);
			TableRow tr = new TableRow(this);

			if (i % 2 == 0)
				tr.setBackgroundColor(Color.WHITE);

			tr.setPadding(0, 16, 0, 16);
			TextView textViewName = new TextView(this);
			textViewName.setText(a.getName());
			textViewName.setGravity(Gravity.CENTER_VERTICAL);
			textViewName.setPadding(16, 16, 16, 16);
			textViewName.setId(i);
			textViewName.setOnClickListener(this);
			
			
			tr.addView(textViewName, new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, (float) 	1));
			
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

}
