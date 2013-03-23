package com.example.amps;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.*;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class ProjectActivity extends BaseActivity implements TabListener {
	RelativeLayout r;
	FragmentTransaction fragmentTra = null;
	ProjectInformationFragment frag1;
	ProjectBulletinsFragment frag2;
	ProjectChartsFragment frag3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

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
		if (tab.getText().equals("Information")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			frag1 = new ProjectInformationFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), frag1);
			fragmentTra.commit();
		} else if (tab.getText().equals("Bulletins")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			frag2 = new ProjectBulletinsFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), frag2);
			fragmentTra.commit();
		} else if (tab.getText().equals("Charts")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			frag3 = new ProjectChartsFragment();
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), frag3);
			fragmentTra.commit();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}
