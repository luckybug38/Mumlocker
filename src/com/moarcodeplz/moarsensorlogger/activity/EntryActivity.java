package com.moarcodeplz.moarsensorlogger.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.moarsensorlogger.R;
import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;
import com.moarcodeplz.moarsensorlogger.service.LoggerService;
import com.moarcodeplz.moarsensorlogger.view.SensorCheckBox;

public class EntryActivity extends LoggerBaseActivityMenu implements OnClickListener {
	
	Button serviceStarter;
	List<SensorCheckBox> checkBoxes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		checkBoxes = new ArrayList<SensorCheckBox>();
		serviceStarter = (Button) findViewById(R.id.buttonToggleService);
		serviceStarter.setOnClickListener(this);
		setButtonText(LoggerApplication.isServiceRunning);
		addSensorBoxes();
		if (!isACheckBoxChecked()) {
			toggleButton(false);
		}
		if (LoggerApplication.isServiceRunning) {
			toggleCheckBoxes(false);
		}
		
	}
	
	@Override
	public void onPause() {
		
		super.onPause();
		app.writeOutSensorPreferences();
		
	}

	public void onClick(View v) {
		
		switch(v.getId()) {
			case R.id.buttonToggleService:
				if (LoggerApplication.isServiceRunning) {
					stopService(new Intent(this, LoggerService.class));
					toggleCheckBoxes(true);
					setButtonText(false);
				} else {
					startService(new Intent(this, LoggerService.class));
					toggleCheckBoxes(false);
					setButtonText(true);
				}
				break;
			default: //This will get hit for programmatically added views
				SensorCheckBox clickedBox = (SensorCheckBox) v;
				LoggerApplication.loggingSensors.put(clickedBox.getSensorHash(), clickedBox.isChecked());
				toggleButton(isACheckBoxChecked());
				break;
		}
		
	}
	
	private boolean isACheckBoxChecked() {
		
		for (SensorCheckBox curBox : checkBoxes) {
			if (curBox.isChecked()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	private void toggleButton(boolean inputToggle) {
		
		serviceStarter.setEnabled(inputToggle);
		
	}
	
	private void toggleCheckBoxes(boolean inputToggle) {
		
		for (SensorCheckBox curBox : checkBoxes) {
			curBox.setEnabled(inputToggle);
		}
		
	}
	
	private void setButtonText(boolean inputServiceRunning) {
		
		if (inputServiceRunning) {
			serviceStarter.setText("Stop Logging");
		} else {
			serviceStarter.setText("Start Logging");			
		}
		
	}
	
	private void addSensorBoxes() {

		RelativeLayout toAddTo = (RelativeLayout) findViewById(R.id.layoutMainRelative);
		List<Sensor> sensorsToAdd = app.getAllSensors();
		Sensor curSensor;
		int idOffset = 89571;
		
		for (int i=0; i<sensorsToAdd.size(); i++) {
    		curSensor = sensorsToAdd.get(i);
    		SensorCheckBox newBox = new SensorCheckBox(this, curSensor.getName().hashCode());
    		newBox.setText(curSensor.getName());
    		newBox.setId(idOffset + i);
    		RelativeLayout.LayoutParams checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    		if (i==0) {
    			checkParams.addRule(RelativeLayout.BELOW, R.id.buttonToggleService);
    		} else {
    			checkParams.addRule(RelativeLayout.BELOW, idOffset + i - 1);
    		}
    		newBox.setOnClickListener(this);
    		newBox.setChecked(LoggerApplication.loggingSensors.get(curSensor.getName().hashCode()));
    		toAddTo.addView(newBox, checkParams);
    		checkBoxes.add(newBox);
		}
		
	}
	
}
