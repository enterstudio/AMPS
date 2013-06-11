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

import com.example.amps.FolderActivity.GetOneLevelChild;

import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class WorkingAssetsPreviewFragment extends Fragment implements Settings {
	ProgressDialog dialog;
	Asset a = new Asset();
	String userid;
	String tokenid;
	String asset_id;
	String project_id;
	ImageView imageViewPreview;
	TextView textViewRevisionNo2;
	TextView textViewUploadedBy2;
	TextView textViewUploadedDate2;
	TextView textViewComment2;

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_working_assets_preview,
				container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageViewPreview = (ImageView) getActivity().findViewById(
				R.id.imageViewPreview);
		textViewRevisionNo2 = (TextView) getActivity().findViewById(
				R.id.textViewRevisionNo2);
		textViewUploadedBy2 = (TextView) getActivity().findViewById(
				R.id.textViewUploadedBy2);
		textViewUploadedDate2 = (TextView) getActivity().findViewById(
				R.id.textViewUploadedDate2);
		textViewComment2 = (TextView) getActivity().findViewById(
				R.id.textViewComment2);
		GetAssetInfo task = new GetAssetInfo();
		task.execute();
	}

	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.imageButtonDownload:
				DownloadAsset task = new DownloadAsset();
				task.execute();
				break;
			case R.id.imageButtonDelete:
				DeleteAsset taskDel = new DeleteAsset();
				taskDel.execute();
				break;
			default:
				getActivity().finish();
				break;
			}
		} catch (Exception e) {
		}
	}

	public class GetAssetInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					WorkingAssetsPreviewFragment.this.getActivity(),
					"Retrieving Asset Information", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveProject();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);

			byte[] decodedString = Base64.decode(a.getBase64_thumbnail(),
					Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
					0, decodedString.length);
			imageViewPreview.setImageBitmap(decodedByte);
			textViewRevisionNo2.setText("#1");
			textViewUploadedDate2.setText(a.getCreated_datetime());
			textViewComment2.setText("Comment");
			GetCreatedUserInfo task = new GetCreatedUserInfo();
			task.execute();
		}

		public String retrieveProject() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getAsset");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("projectid", project_id));
			postParameters
					.add(new BasicNameValuePair(
							"select",
							"[asset_id], [name], [ext], [created_userid], [created_datetime], [base64_thumbnail]"));
			postParameters.add(new BasicNameValuePair("condition",
					"[asset_id] IN ('" + asset_id + "')"));

			postParameters.add(new BasicNameValuePair("start_pos", "1"));
			postParameters.add(new BasicNameValuePair("end_pos", "5"));

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
				a.setAsset_id(dataJob.getString("asset_id"));
				a.setName(dataJob.getString("name"));
				a.setExt(dataJob.getString("ext"));
				a.setCreated_userid(dataJob.getString("created_userid"));
				a.setCreated_datetime(dataJob.getString("created_datetime"));
				a.setBase64_thumbnail(dataJob.getString("base64_thumbnail"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class GetCreatedUserInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					WorkingAssetsPreviewFragment.this.getActivity(),
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
					"[userid] = '" + a.getCreated_userid() + "'"));

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
				textViewUploadedBy2.setText(dataJob.getString("username"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class DownloadAsset extends AsyncTask<Object, Object, Object> {
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					WorkingAssetsPreviewFragment.this.getActivity(),
					"Downloading Asset", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return downloadAsset();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			Toast toast = Toast.makeText(
					WorkingAssetsPreviewFragment.this.getActivity(),
					"Downloaded successfully!", Toast.LENGTH_LONG);
			toast.show();
		}

		public String downloadAsset() {
			String req = SZAAPIURL + "downloadAsset?tokenid=" + tokenid +
					"&userid=" + userid +
					"&projectid=" + project_id +
					"&assetid=" + asset_id;
			try {
				DownloadManager downloadManager;
				downloadManager = (DownloadManager)WorkingAssetsPreviewFragment.this.getActivity().getSystemService("download");
				Uri uri = Uri.parse(req);
				DownloadManager.Request request = new DownloadManager.Request(uri);
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, a.getName() + "." + a.getExt());
				downloadManager.enqueue(request);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public class DeleteAsset extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(WorkingAssetsPreviewFragment.this.getActivity(),
					"Deleting Asset", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return deleteAsset();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			
		}

		public String deleteAsset() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "delAsset");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("projectid", project_id));
			postParameters.add(new BasicNameValuePair("assetid_list", asset_id));

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
			Intent intent = new Intent(WorkingAssetsPreviewFragment.this.getActivity(), WorkingAssetsListActivity.class);
			startActivity(intent);
		}
	}
}
