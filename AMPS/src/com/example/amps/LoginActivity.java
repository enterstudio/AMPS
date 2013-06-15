package com.example.amps;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements Settings {
	ProgressDialog dialog;
	String szUsername = "";
	String szPassword = "";
	int error_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/* super.onCreateOptionsMenu(menu); */
		return true;
	}

	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.buttonLogin:
				AuthenticateUser task = new AuthenticateUser();
				task.execute();
				break;
			default:
				finish();
				break;
			}
		} catch (Exception e) {
		}
	}

	public class AuthenticateUser extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this,
					"Authenticating user", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return login();

		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
		}

		public String login() {
			String responseBody = "";
			szUsername = ((EditText) findViewById(R.id.editTextUsername))
					.getText().toString();
			szPassword = ((EditText) findViewById(R.id.editTextPassword))
					.getText().toString();
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "Authenticate");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("username", szUsername));
			postParameters.add(new BasicNameValuePair("password",
					hash(szPassword)));

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

		public String hash(String plaintext) {
			try {
				MessageDigest md = java.security.MessageDigest
						.getInstance("MD5");
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

		public void parseJSONResponse(String responseBody) {
			JSONArray json;
			try {
				json = new JSONArray(responseBody);
				JSONObject job = json.getJSONObject(0);
				error_code = job.getInt("error_code");
				SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
			    SharedPreferences.Editor editor = settings.edit();
		        editor.putString("userid", job.getString("userid"));
		        editor.putString("tokenid", job.getString("tokenid"));
		        editor.commit();
		        if (error_code == 0) {
		        	Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
					startActivity(intent);
		        }
		        else {
					Toast toast = Toast.makeText(
							LoginActivity.this,
							"Incorrect username or password", Toast.LENGTH_LONG);
					toast.show();
		        }
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
