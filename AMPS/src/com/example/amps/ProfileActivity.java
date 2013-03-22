package com.example.amps;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.*;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class ProfileActivity extends BaseActivity implements TabListener {
	RelativeLayout rl;
	FragmentTransaction fragMentTra = null;
	ProfileAccountFragment fram1;
	ProfilePasswordFragment fram2;
	ProfileSettingsFragment fram3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		try {
			rl = (RelativeLayout) findViewById(R.id.activity_profile);
			fragMentTra = getFragmentManager().beginTransaction();
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
		getMenuInflater().inflate(R.menu.main, menu);
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
				rl.removeAllViews();
			} catch (Exception e) {
			}
			fram1 = new ProfileAccountFragment();
			fragMentTra.addToBackStack(null);
			fragMentTra = getFragmentManager().beginTransaction();
			fragMentTra.add(rl.getId(), fram1);
			fragMentTra.commit();
		} else if (tab.getText().equals("Password")) {
			try {
				rl.removeAllViews();
			} catch (Exception e) {
			}
			fram2 = new ProfilePasswordFragment();
			fragMentTra.addToBackStack(null);
			fragMentTra = getFragmentManager().beginTransaction();
			fragMentTra.add(rl.getId(), fram2);
			fragMentTra.commit();
		} else if (tab.getText().equals("Settings")) {
			try {
				rl.removeAllViews();
			} catch (Exception e) {
			}
			fram3 = new ProfileSettingsFragment();
			fragMentTra.addToBackStack(null);
			fragMentTra = getFragmentManager().beginTransaction();
			fragMentTra.add(rl.getId(), fram3);
			fragMentTra.commit();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}
