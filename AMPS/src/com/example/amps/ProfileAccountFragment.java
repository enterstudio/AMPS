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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileAccountFragment extends Fragment implements Settings {
	ProgressDialog dialog;
	String userid;
	String tokenid;
	String password;
	String username;
	String displayname;
	String firstname;
	String lastname;
	String email;
	String title;
	String gender;
	String dob;
	String ic;
	int error_code;
	EditText editTextUsername;
	EditText editTextDisplayName;
	EditText editTextFirstName;
	EditText editTextLastName;
	EditText editTextEmail;
	Spinner spinnerTitle;
	Spinner spinnerGender;
	EditText editTextBirthDate;
	EditText editTextIC;

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile_account, container,
				false);

		return v;
	}

	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.buttonUpdate:
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ProfileAccountFragment.this.getActivity());
				alert.setTitle("Verify password");
				final EditText editTextPassword = new EditText(
						ProfileAccountFragment.this.getActivity());
				editTextPassword.setHint("Password");
				editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				alert.setView(editTextPassword);
				alert.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								password = editTextPassword.getText().toString();
								EditUser task = new EditUser();
								task.execute();
								dialog.dismiss();
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});

				alert.show();
				break;
			case R.id.buttonCancel:
				break;
			default:
				getActivity().finish();
				break;
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		editTextUsername = (EditText) getActivity().findViewById(
				R.id.editTextUsername);
		editTextDisplayName = (EditText) getActivity().findViewById(
				R.id.editTextDisplayName);
		editTextFirstName = (EditText) getActivity().findViewById(
				R.id.editTextFirstName);
		editTextLastName = (EditText) getActivity().findViewById(
				R.id.editTextLastName);
		editTextEmail = (EditText) getActivity().findViewById(
				R.id.editTextEmail);
		spinnerTitle = (Spinner) getActivity().findViewById(R.id.spinnerTitle);
		ArrayAdapter<String> spinnerTitleAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1);
		spinnerTitleAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTitle.setAdapter(spinnerTitleAdapter);
		spinnerTitleAdapter.add("Mr");
		spinnerTitleAdapter.add("Ms");
		spinnerTitleAdapter.add("Mrs");
		spinnerTitleAdapter.add("Not specified");
		// spinner.setSelection(0);
		spinnerTitleAdapter.notifyDataSetChanged();

		spinnerGender = (Spinner) getActivity()
				.findViewById(R.id.spinnerGender);
		ArrayAdapter<String> spinnerGenderAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1);
		spinnerGenderAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGender.setAdapter(spinnerGenderAdapter);
		spinnerGenderAdapter.add("Male");
		spinnerGenderAdapter.add("Female");
		spinnerGenderAdapter.add("Not specified");
		// spinner.setSelection(0);
		spinnerGenderAdapter.notifyDataSetChanged();

		editTextBirthDate = (EditText) getActivity().findViewById(
				R.id.editTextBirthDate);
		editTextIC = (EditText) getActivity().findViewById(R.id.editTextIC);
		GetUserInfo task = new GetUserInfo();
		task.execute();
	}

	public class GetUserInfo extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					ProfileAccountFragment.this.getActivity(),
					"Retrieving Account Information", "Please wait...", true);
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return retrieveProject();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			editTextUsername.setText(username);
			editTextDisplayName.setText(displayname);
			editTextFirstName.setText(firstname);
			editTextLastName.setText(lastname);
			editTextEmail.setText(email);
			if (title.equals("Mr")) {
				spinnerTitle.setSelection(0);
			} else if (title.equals("Ms")) {
				spinnerTitle.setSelection(1);
			} else if (title.equals("Mrs")) {
				spinnerTitle.setSelection(2);
			} else {
				spinnerTitle.setSelection(3);
			}

			if (gender.equals("0")) {
				spinnerGender.setSelection(0);
			} else if (gender.equals("1")) {
				spinnerGender.setSelection(1);
			} else {
				spinnerGender.setSelection(2);
			}
			editTextBirthDate.setText(dob);
			editTextIC.setText(ic);
		}

		public String retrieveProject() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "getUserInfo");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("condition",
					"[userid] = " + userid));

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
				username = dataJob.getString("username");
				displayname = dataJob.getString("displayname");
				firstname = dataJob.getString("firstname");
				lastname = dataJob.getString("lastname");
				email = dataJob.getString("email");
				title = dataJob.getString("title");
				gender = dataJob.getString("gender");
				dob = dataJob.getString("dob");
				ic = dataJob.getString("ic");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class EditUser extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(
					ProfileAccountFragment.this.getActivity(),
					"Updating Account Information", "Please wait...", true);

			username = editTextUsername.getText().toString();
			displayname = editTextDisplayName.getText().toString();
			firstname = editTextFirstName.getText().toString();
			lastname = editTextLastName.getText().toString();
			email = editTextEmail.getText().toString();
			title = spinnerTitle.getSelectedItem().toString();
			gender = Integer.toString(spinnerGender.getSelectedItemPosition());
			dob = editTextBirthDate.getText().toString();
			ic = editTextIC.getText().toString();
		}

		@Override
		protected String doInBackground(Object... arg0) {
			return editUser();
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();
			parseJSONResponse((String) result);
			if (error_code == 0) {
				Toast toast = Toast.makeText(
						ProfileAccountFragment.this.getActivity(),
						"Updated account succesfully!", Toast.LENGTH_LONG);
				toast.show();
			} else {
				Toast toast = Toast.makeText(
						ProfileAccountFragment.this.getActivity(),
						"Updating failed!", Toast.LENGTH_LONG);
				toast.show();
			}
		}

		public String editUser() {
			String responseBody = "";
			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SZAAPIURL + "editUser");

			// Post parameters
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("tokenid", tokenid));
			postParameters.add(new BasicNameValuePair("userid", userid));
			postParameters.add(new BasicNameValuePair("verify_userid", userid));
			postParameters.add(new BasicNameValuePair("verify_password",
					hash(password)));
			postParameters.add(new BasicNameValuePair("username", username));
			postParameters.add(new BasicNameValuePair("displayname",
					displayname));
			postParameters.add(new BasicNameValuePair("firstname", firstname));
			postParameters.add(new BasicNameValuePair("lastname", lastname));
			postParameters.add(new BasicNameValuePair("email", email));
			postParameters.add(new BasicNameValuePair("title", title));
			postParameters.add(new BasicNameValuePair("gender", gender));
			postParameters.add(new BasicNameValuePair("dob", dob));
			postParameters.add(new BasicNameValuePair("ic", ic));

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
			JSONArray json;
			JSONObject job;
			try {
				json = new JSONArray(responseBody);
				job = json.getJSONObject(0);
				error_code = job.getInt("error_code");
			} catch (JSONException e) {
				e.printStackTrace();
			}
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
	}
}