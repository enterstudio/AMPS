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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ProjectInformationFragment extends Fragment {
	ProgressDialog dialog;
	String userid;
	String tokenid;
	String project_id;
	String name;
	String des;
	String estimated_datestart;
	String estimated_dateend;
	String actual_datestart;
	String actual_dateend;
	String duration;
	String created_userid;
	String created_datetime;
	String updated_userid;
	String updated_datetime;
	EditText editTextProjectName;
	EditText editTextEstimatedStart;
	EditText editTextEstimatedEnd;
	EditText editTextActualStart;

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
		GetProjectInfo task = new GetProjectInfo();
		task.execute();
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public class GetProjectInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					ProjectInformationFragment.this.getActivity(),
					"Retrieving Project", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveProject();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			editTextProjectName.setText(name);
			editTextEstimatedStart.setText(estimated_datestart);
			editTextEstimatedEnd.setText(estimated_dateend);
			editTextActualStart.setText(actual_datestart);
		}

		public String retrieveProject() {
			String szaAPIURL = "http://54.251.38.14/AMPS/ampslt/AMPSAPI/";
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(szaAPIURL + "getProjectInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("condition",
					"project_id LIKE '" + project_id + "'"));

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
				name = dataJob.getString("name");
				des = dataJob.getString("des");
				estimated_datestart = dataJob.getString("estimated_datestart");
				estimated_dateend = dataJob.getString("estimated_dateend");
				actual_datestart = dataJob.getString("actual_datestart");
				actual_datestart = dataJob.getString("actual_datestart");
				actual_dateend = dataJob.getString("actual_dateend");
				duration = dataJob.getString("duration");
				created_userid = dataJob.getString("created_userid");
				created_datetime = dataJob.getString("created_datetime");
				updated_userid = dataJob.getString("updated_userid");
				updated_datetime = dataJob.getString("updated_datetime");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
