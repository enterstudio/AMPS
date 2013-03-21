package com.example.amps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class MainTabMenu extends RelativeLayout {
	
	private LayoutInflater inflater;
	
	public MainTabMenu(Context context, AttributeSet attr)
	{
		super(context, attr);
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.main_tab_menu, this, true);
		createTabMenu();
	}
	
	//Method to create the tabbed menu
	public void createTabMenu()
	{
		TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
		tabHost.setup();
		
		//Asset tab
		TabSpec spec1 = tabHost.newTabSpec("Assets");
		spec1.setContent(R.id.assetTab);
		spec1.setIndicator("Assets");
		
		//Project tab
		TabSpec spec2 = tabHost.newTabSpec("Projects");
		spec2.setContent(R.id.projectTab);
		spec2.setIndicator("Projects");
		
		//Folder tab
		TabSpec spec3 = tabHost.newTabSpec("Folder");
		spec3.setContent(R.id.folderTab);
		spec3.setIndicator("Folder");
		
		//Search tab
		TabSpec spec4 = tabHost.newTabSpec("Search");
		spec4.setContent(R.id.searchTab);
		spec4.setIndicator("Search");
		
		//Report tab
		TabSpec spec5 = tabHost.newTabSpec("Report");
		spec5.setContent(R.id.reportTab);
		spec5.setIndicator("Report");
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabHost.addTab(spec4);
		tabHost.addTab(spec5);
	}



}
