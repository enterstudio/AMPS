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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.TextView;

public class ProjectInformationFragment extends Fragment implements Settings {
	ProgressDialog dialog;
	Project p = new Project();
	String userid;
	String tokenid;
	String project_id;
	EditText editTextProjectName;
	EditText editTextEstimatedStart;
	EditText editTextEstimatedEnd;
	EditText editTextActualStart;
	EditText editTextEstimatedDuration;
	EditText editTextDescription;
	TextView textViewCreatedBy;
	TextView textViewCreationDate;
	TextView textViewLastUpdatedBy;
	TextView textViewLastUpdatedDate;
	
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_project_information, container,
				false);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		editTextProjectName = (EditText) getActivity().findViewById(R.id.editTextProjectName);
		editTextEstimatedStart = (EditText) getActivity().findViewById(R.id.editTextEstimatedStart);
		editTextEstimatedEnd = (EditText) getActivity().findViewById(R.id.editTextEstimatedEnd);
		editTextActualStart = (EditText) getActivity().findViewById(R.id.editTextActualStart);
		editTextEstimatedDuration = (EditText) getActivity().findViewById(R.id.editTextEstimatedDuration);
		editTextDescription = (EditText) getActivity().findViewById(R.id.editTextDescription);
		textViewCreatedBy = (TextView) getActivity().findViewById(R.id.textViewCreatedBy);
		textViewCreationDate = (TextView) getActivity().findViewById(R.id.textViewCreationDate);
		textViewLastUpdatedBy = (TextView) getActivity().findViewById(R.id.textViewLastUpdatedBy);
		textViewLastUpdatedDate = (TextView) getActivity().findViewById(R.id.textViewLastUpdatedDate);
		GetProjectInfo task = new GetProjectInfo();
		task.execute();
	}

	public class GetProjectInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					ProjectInformationFragment.this.getActivity(),
					"Retrieving Project Information", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveProject();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			editTextProjectName.setText(p.getName());
			editTextEstimatedStart.setText(p.getEstimated_datestart());
			editTextEstimatedEnd.setText(p.getEstimated_dateend());
			editTextActualStart.setText(p.getActual_datestart());
			editTextEstimatedDuration.setText(p.getDuration());
			editTextDescription.setText(p.getDes());
			textViewCreationDate.setText("Created On: " + p.getCreated_datetime());
			textViewLastUpdatedDate.setText("Updated On: " + p.getUpdated_datetime());
			GetCreatedUserInfo task = new GetCreatedUserInfo();
			task.execute();
		}

		public String retrieveProject() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getProjectInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("condition",
					"[project_id] LIKE '" + project_id + "'"));

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
				p.setName(dataJob.getString("name"));
				p.setDes(dataJob.getString("des"));
				p.setEstimated_datestart(dataJob.getString("estimated_datestart"));
				p.setEstimated_dateend(dataJob.getString("estimated_dateend"));
				p.setActual_datestart(dataJob.getString("actual_datestart"));
				p.setActual_dateend(dataJob.getString("actual_dateend"));
				p.setDuration(dataJob.getString("duration"));
				p.setCreated_userid(dataJob.getString("created_userid"));
				p.setCreated_datetime(dataJob.getString("created_datetime"));
				p.setUpdated_userid(dataJob.getString("updated_userid"));
				p.setUpdated_datetime(dataJob.getString("updated_datetime"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class GetCreatedUserInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					ProjectInformationFragment.this.getActivity(),
					"Retrieving User Information", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveUser();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
		}

		public String retrieveUser() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getUserInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("condition",
					"[userid] = '" + p.created_userid + "'"));

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
				p.setCreated_userid(dataJob.getString("username"));
				textViewCreatedBy.setText("Created By: " + p.getCreated_userid());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class GetUpdatedUserInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					ProjectInformationFragment.this.getActivity(),
					"Retrieving User Information", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveUser();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			
		}

		public String retrieveUser() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getUserInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("condition",
					"[userid] = '" + p.getUpdated_userid() + "'"));

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
				p.setUpdated_userid(dataJob.getString("username"));
				textViewLastUpdatedBy.setText("Updated By: " + p.getUpdated_userid());;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
