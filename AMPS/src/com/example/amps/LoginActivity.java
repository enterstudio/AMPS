package com.example.amps;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.security.*;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {

	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		dialog = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (dialog != null)
			dialog.dismiss();
	}

	@SuppressWarnings("unchecked")
	public void onClick(View view) {
		Intent intent;
		try {
			switch (view.getId()) {
			case R.id.button_login:
				CallAMPSAPI task = new CallAMPSAPI();
				task.applicationContext = LoginActivity.this;
				task.execute();
				intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		} catch (Exception e) {
		}
	}

	public class CallAMPSAPI extends AsyncTask {
		protected Context applicationContext;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(applicationContext, "Calling",
					"AMPS API...", true);
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			return LoginActivity.login();

		}

		protected void onPostExecute(String result) {
			dialog.dismiss();
			String sZuserID = LoginActivity.parseJSONResponse(result);
		}

	}

	@SuppressWarnings("finally")
	public static String login() {

		String username = "Admin";
		String password = "fosdfdoffi9489ui9uf9u934uf94u598";
		String sZUserID = null;
		String sZTokenID;
		try {
			String szaAPIURL = "http//54.251.38.14/AMPS/ampslt/AMPSAPI";

			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(szaAPIURL + "Authenticate");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("username", username));
			postParameters.add(new BasicNameValuePair("password",
					hash(password)));

			// Instantiate a POST HTTP method
			try {
				httppost.setEntity(new UrlEncodedFormEntity(postParameters));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				String responseBody = httpclient.execute(httppost,
						responseHandler);
				return responseBody;
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		} finally {
			return null;
		}
	}

	public static String hash(String plaintext) {
		try {
			MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(plaintext.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;

	}

	public static String parseJSONResponse(String responseBody) {
		String sZUserID = "";
		String sZTokenID = "";

		JSONArray json;
		try {
			json = new JSONArray(responseBody);
			JSONObject job = json.getJSONObject(0);
			sZUserID = job.getString("userid");
			sZTokenID = job.getString("tokenid");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return sZUserID;
	}
}
