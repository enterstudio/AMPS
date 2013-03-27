package com.example.amps;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.*;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class ProjectActivity extends BaseActivity implements TabListener, Settings {
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
		
		settings = getSharedPreferences(SETTINGS, 0);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
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
			infoFragment.setUserid(settings.getString("userid", null));
			infoFragment.setTokenid(settings.getString("tokenid", null));
			infoFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), infoFragment);
			fragmentTra.commit();
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
}
