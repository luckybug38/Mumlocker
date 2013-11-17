package com.moarcodeplz.moarsensorlogger.activity;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.moarsensorlogger.R;
import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;
import com.moarcodeplz.moarsensorlogger.storage.StorageHelper;

public class LoggerBaseActivityMenu extends LoggerBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
	    	case R.id.menu_settings:
	    		startActivity(new Intent(this, SensorPreferenceActivity.class));
	    		return true;
	    	case R.id.menu_dev:
	    		Log.v("Utils","Max Mem in MB:"+(Runtime.getRuntime().maxMemory()/1024/1024));
	    		File[] loggedFiles = new File(LoggerApplication.eventLogDirectory).listFiles();
				LoggerApplication.restClient.setSharedPrefs(PreferenceManager.getDefaultSharedPreferences(this));
				LoggerApplication.restClient.setSensorList(((LoggerApplication)getApplication()).getAllSensors());
				LoggerApplication.restClient.offloadCsvFiles(loggedFiles);
	    		return true;
	    	case R.id.menu_note:
	    		writeNote();
	    		return true;
	    	case R.id.menu_quicknote:
	    		StorageHelper.writeNoteToFile("Quick Note");
	    		return true;
    	}
    	
    	return false;
    	
    }
    
    private void writeNote() {
		
		final EditText edit = new EditText(this);
		new AlertDialog.Builder(this)
			.setTitle("Write a note")
			.setView(edit)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					StorageHelper.writeNoteToFile(edit.getText().toString());
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//Do nothing
				}
			}).show();
		
    }
    
}
