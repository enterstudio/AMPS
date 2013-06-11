package com.example.amps;

import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class WorkingAssetsActivity extends BaseActivity implements TabListener,
		Settings {
	RelativeLayout r;
	FragmentTransaction fragmentTra = null;
	WorkingAssetsPreviewFragment previewFragment;
	WorkingAssetsPropertiesFragment propertiesFragment;
	WorkingAssetsCommentsFragment commentsFragment;
	WorkingAssetsRevisionsFragment revisionsFragment;
	WorkingAssetsLogHistoryFragment logHistoryFragment;
	String asset_id;
	String project_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working_assets);
		setTitle("Working Assets");
		settings = getSharedPreferences(SETTINGS, 0);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			asset_id = extras.getString("asset_id");
			project_id = extras.getString("project_id");
		}

		try {
			r = (RelativeLayout) findViewById(R.id.activity_working_assets);
			fragmentTra = getFragmentManager().beginTransaction();
			ActionBar bar = getActionBar();
			bar.addTab(bar.newTab().setText("Preview").setTabListener(this));
			bar.addTab(bar.newTab().setText("Properties").setTabListener(this));
			bar.addTab(bar.newTab().setText("Comments").setTabListener(this));
			bar.addTab(bar.newTab().setText("Revisions").setTabListener(this));
			bar.addTab(bar.newTab().setText("Log History").setTabListener(this));

			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_USE_LOGO);
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			bar.setDisplayShowHomeEnabled(true);
			bar.setDisplayShowTitleEnabled(false);
			bar.show();

		} catch (Exception e) {
			e.getMessage();
		}
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
	public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		if (tab.getText().equals("Properties")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			propertiesFragment = new WorkingAssetsPropertiesFragment();
			propertiesFragment.setUserid(settings.getString("userid", null));
			propertiesFragment.setTokenid(settings.getString("tokenid", null));
			propertiesFragment.setAsset_id(asset_id);
			propertiesFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), propertiesFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Preview")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			previewFragment = new WorkingAssetsPreviewFragment();
			previewFragment.setUserid(settings.getString("userid", null));
			previewFragment.setTokenid(settings.getString("tokenid", null));
			previewFragment.setAsset_id(asset_id);
			previewFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), previewFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Comments")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			commentsFragment = new WorkingAssetsCommentsFragment();
			commentsFragment.setUserid(settings.getString("userid", null));
			commentsFragment.setTokenid(settings.getString("tokenid", null));
			commentsFragment.setAsset_id(asset_id);
			commentsFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), commentsFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Revisions")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			revisionsFragment = new WorkingAssetsRevisionsFragment();
			revisionsFragment.setUserid(settings.getString("userid", null));
			revisionsFragment.setTokenid(settings.getString("tokenid", null));
			revisionsFragment.setAsset_id(asset_id);
			revisionsFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), revisionsFragment);
			fragmentTra.commit();
		} else if (tab.getText().equals("Log History")) {
			try {
				r.removeAllViews();
			} catch (Exception e) {
			}
			logHistoryFragment = new WorkingAssetsLogHistoryFragment();
			logHistoryFragment.setUserid(settings.getString("userid", null));
			logHistoryFragment.setTokenid(settings.getString("tokenid", null));
			logHistoryFragment.setAsset_id(asset_id);
			logHistoryFragment.setProject_id(project_id);
			fragmentTra.addToBackStack(null);
			fragmentTra = getFragmentManager().beginTransaction();
			fragmentTra.add(r.getId(), logHistoryFragment);
			fragmentTra.commit();
		}
	}
	
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.imageButtonDownload:
				previewFragment.onClick(view);
				break;
			default:
				finish();
				break;
			}
		} catch (Exception e) {
		}
	}
}
