package com.example.amps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ProfileTabMenu extends RelativeLayout {

	private LayoutInflater inflater;
	
	public ProfileTabMenu(Context context, AttributeSet attr)
	{
		super(context, attr);
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.profile_tab_menu, this, true);
		createTabMenu();
	}
	
	//Method to create the tabbed menu
	public void createTabMenu()
	{
		TabHost tabHost = (TabHost)findViewById(R.id.profileTabHost);
		tabHost.setup();
		
		//Account tab
		TabSpec spec1 = tabHost.newTabSpec("Account");
		spec1.setContent(R.id.accountTab);
		spec1.setIndicator("Account");
		
		//Settings tab
		TabSpec spec2 = tabHost.newTabSpec("Password");
		spec2.setContent(R.id.passwordTab);
		spec2.setIndicator("Password");
		
		//Settings tab
				TabSpec spec3 = tabHost.newTabSpec("Settings");
				spec2.setContent(R.id.settingsTab);
				spec2.setIndicator("Settings");
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
	}

}
