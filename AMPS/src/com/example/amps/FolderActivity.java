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

public class FolderActivity extends BaseActivity implements Settings,
		View.OnClickListener {
	ProgressDialog dialog;
	String error_code;
	String project_id;
	String folder_id;
	ArrayList<Project> projectArray = new ArrayList<Project>();
	ArrayList<Folder> foldersArray = new ArrayList<Folder>();

	// ArrayList<Asset> workingAssetsArray = new ArrayList<Asset>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_folder);
		setTitle("Folder");
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public class GetProjectInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FolderActivity.this,
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
					FolderActivity.this);
			alert.setTitle("Select project");
			final Spinner spinnerProject = new Spinner(FolderActivity.this);

			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
					FolderActivity.this, android.R.layout.simple_spinner_item,
					android.R.id.text1);
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

							GetRootFolder task = new GetRootFolder();
							task.execute();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
							FolderActivity.this.finish();
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

	public class GetRootFolder extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FolderActivity.this,
					"Retrieving Folder", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveAssets();

		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
		}

		public String retrieveAssets() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getRootFolder");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", settings
					.getString("tokenid", null)));
			postParameters.add(new BasicNameValuePair("userid", settings
					.getString("userid", null)));
			postParameters.add(new BasicNameValuePair("projectid", project_id));

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
			JSONArray json;
			JSONObject job;
			try {
				json = new JSONArray(responseBody);
				job = json.getJSONObject(0);
				folder_id = job.getString("folder_id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			GetOneLevelChild task = new GetOneLevelChild();
			task.execute();
			
			
		}
	}

	public class GetOneLevelChild extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FolderActivity.this,
					"Loading contents", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveAssets();

		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
		}

		public String retrieveAssets() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getOneLevelChild");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", settings
					.getString("tokenid", null)));
			postParameters.add(new BasicNameValuePair("userid", settings
					.getString("userid", null)));
			postParameters.add(new BasicNameValuePair("projectid", project_id));
			postParameters.add(new BasicNameValuePair("parent_id", folder_id));

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
			Folder f;
			try {
				json = new JSONArray(responseBody);
				job = json.getJSONObject(0);
				data_array = job.getJSONArray("data_array");
				for (int i = 0; i < data_array.length(); i++) {
					JSONObject dataJob = new JSONObject(data_array.getString(i));

					f = new Folder();
					f.setFolder_id(dataJob.getString("folder_id"));
					f.setReferenced_folder(dataJob.getString("referenced_folder"));
					f.setName(dataJob.getString("name"));
					f.setDes(dataJob.getString("des"));
					f.setCreated_userid(dataJob.getString("created_userid"));
					f.setCreated_datetime(dataJob.getString("created_datetime"));
					f.setUpdated_userid(dataJob.getString("updated_userid"));
					f.setUpdated_datetime(dataJob.getString("updated_datetime"));
					foldersArray.add(f);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			View v = FolderActivity.this.findViewById(
			android.R.id.content).getRootView();
			createTableRow(v);
			
		}
	}
	
	public void createTableRow(View v) {
		TableLayout tl = (TableLayout) findViewById(R.id.tableRowFolder);

		if (foldersArray.size() == 0) {
			TableRow tr = new TableRow(this);
			tr.setBackgroundColor(Color.WHITE);

			tr.setPadding(0, 16, 0, 16);
			TextView textViewName = new TextView(this);
			textViewName.setText("No files retrieved.");
			textViewName.setGravity(Gravity.CENTER_VERTICAL);
			
			
			tr.addView(textViewName, new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, (float) 1.0));
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		for (int i = 0; i < foldersArray.size(); i++) {
			Folder f = foldersArray.get(i);
			TableRow tr = new TableRow(this);
			tr.setId(i);
			tr.setOnClickListener(this);

			if (i % 2 == 0)
				tr.setBackgroundColor(Color.WHITE);

			tr.setPadding(0, 16, 0, 16);
			TextView textViewName = new TextView(this);
			textViewName.setText(f.getName());
			textViewName.setGravity(Gravity.CENTER_VERTICAL);
			/*TextView textViewStatus = new TextView(this);
			textViewStatus.setText(f.getTracking_status());
			textViewStatus.setGravity(Gravity.CENTER_VERTICAL);*/
			
			ImageButton imageButtonPreview = new ImageButton(this);
			imageButtonPreview.setBackgroundColor(Color.TRANSPARENT); 
			imageButtonPreview.setImageDrawable(getResources().getDrawable(R.drawable.content_picture));
			imageButtonPreview.setId(i + 100);
			imageButtonPreview.setOnClickListener(this);
			
			ImageButton imageButtonDetails = new ImageButton(this);
			imageButtonDetails.setBackgroundColor(Color.TRANSPARENT); 
			imageButtonDetails.setImageDrawable(getResources().getDrawable(R.drawable.action_about));
			imageButtonDetails.setId(i);
			imageButtonDetails.setOnClickListener(this);
			
			
			tr.addView(textViewName, new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, (float) 0.8));
			/*tr.addView(textViewStatus, new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, (float) 0.3));*/
			tr.addView(imageButtonDetails, new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, (float) 0.2));
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
}
