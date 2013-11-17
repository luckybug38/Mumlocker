package com.moarcodeplz.moarsensorlogger.activity;

import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LoggerBaseActivity extends FragmentActivity {

	public LoggerApplication app;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	app = (LoggerApplication) getApplication();
    	
    }
    
    @Override
    public void onResume() {
    	
    	super.onResume();
    	app = (LoggerApplication) getApplication();
    	
    	
    }
    
}
