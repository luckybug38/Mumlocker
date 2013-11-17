package com.moarcodeplz.moarsensorlogger.activity;

import com.example.moarsensorlogger.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SensorPreferenceActivity extends PreferenceActivity {
	
	//TODO write stackoverflow question
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

}
