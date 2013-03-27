package com.example.amps;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.*;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class ProfileActivity extends BaseActivity implements TabListener, Settings {
	RelativeLayout r;
	FragmentTransaction fragmentTra = null;
	ProfileAccountFragment accountFragment;
	ProfilePasswordFragment passwordFragment;
	ProfileSettingsFragment settingsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		settings = getSharedPreferences(SETTINGS, 0);

		try {
			r = (RelativeLayout) findViewById(R.id.activity_profile);
			fragmentTra = getFragmentManager().beginTransaction();
			ActionBar bar = getActionBar();
			bar.addTab(bar.newTab().setText("Account").setTabListener(this));
			bar.addTab(bar.newTab().setText("Password").setTabListener(this));
			bar.addTab(bar.newTab().setText("Settings").setTabListener(this));

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
		if (tab.getText().equals("Account")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			accountFragment = new ProfileAccountFragment();
			accountFragment.setUserid(settings.getString("userid", null));
			accountFragment.setTokenid(settings.getString("tokenid", null));
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), accountFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Password")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			passwordFragment = new ProfilePasswordFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), passwordFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Settings")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			settingsFragment = new ProfileSettingsFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), settingsFragment);
			fragmentTra.commit();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}
