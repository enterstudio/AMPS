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
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.*;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class ProjectActivity extends BaseActivity implements TabListener {
	RelativeLayout r;
	FragmentTransaction fragmentTra = null;
	ProjectInformationFragment infoFragment;
	ProjectBulletinsFragment bulletinsFragment;
	ProjectChartsFragment chartsFragment;
	String project_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userid = extras.getString("userid");
			tokenid = extras.getString("tokenid");
			project_id = extras.getString("project_id");
		}

		try {
			r = (RelativeLayout) findViewById(R.id.activity_profile);
			fragmentTra = getFragmentManager().beginTransaction();
			ActionBar bar = getActionBar();
			bar.addTab(bar.newTab().setText("Information").setTabListener(this));
			bar.addTab(bar.newTab().setText("Bulletins").setTabListener(this));
			bar.addTab(bar.newTab().setText("Charts").setTabListener(this));

			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_USE_LOGO);
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			bar.setDisplayShowHomeEnabled(true);
			bar.setDisplayShowTitleEnabled(false);
			bar.show();

		} catch (Exception e) {
			e.getMessage();
		}
		/**
		 * Hiding Action Bar
		 */
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

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		if (tab.getText().equals("Information")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			infoFragment = new ProjectInformationFragment();
			infoFragment.setUserid(userid);
			infoFragment.setTokenid(tokenid);
			infoFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), infoFragment);
			CommitFragment task = new CommitFragment();
			task.execute();
		} else if (tab.getText().equals("Bulletins")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			bulletinsFragment = new ProjectBulletinsFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), bulletinsFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Charts")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			chartsFragment = new ProjectChartsFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), chartsFragment);
			fragmentTra.commit();
		}
	}
	

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	public class CommitFragment extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			/*dialog = ProgressDialog.show(
					ProjectInformationFragment.this.getActivity(),
					"Retrieving Project", "Please wait...", true);*/
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			return fragmentTra.commit();
		}

		@Override
		protected void onPostExecute(Object result) {
			/*dialog.dismiss();*/
		}
	}
}
