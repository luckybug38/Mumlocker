package com.moarcodeplz.moarsensorlogger.activity.fragment;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.moarsensorlogger.R;
import com.moarcodeplz.moarsensorlogger.activity.EntryActivity;
import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;
import com.moarcodeplz.moarsensorlogger.service.LoggerService;
import com.moarcodeplz.moarsensorlogger.view.SensorCheckBox;

public class SensorFragment extends Fragment implements OnClickListener {
	View view;
	Button serviceStarter;
	List<SensorCheckBox> checkBoxes;
	EntryActivity activity;
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View mainView = inflater.inflate(R.layout.layout_main, container, false);
		
		return mainView;
	}		
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		activity = (EntryActivity)getActivity() ;
		checkBoxes = new ArrayList<SensorCheckBox>();
		serviceStarter = (Button) view.findViewById(R.id.buttonToggleService);
		setButtonText(LoggerApplication.isServiceRunning);
		this.view=view;
		serviceStarter.setOnClickListener(this);
		addSensorBoxes();
		if (!isACheckBoxChecked()) {
			toggleButton(false);
		}
		if (LoggerApplication.isServiceRunning) {
			toggleCheckBoxes(false);
		}
	}

	public boolean isACheckBoxChecked() {
		
		for (SensorCheckBox curBox : checkBoxes) {
			if (curBox.isChecked()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	
	
	public void toggleCheckBoxes(boolean inputToggle) {
		
		for (SensorCheckBox curBox : checkBoxes) {
			curBox.setEnabled(inputToggle);
		}
		
	}
public void toggleButton(boolean inputToggle) {
		
		serviceStarter.setEnabled(inputToggle);
		
	}
	public void setButtonText(boolean inputServiceRunning) {
		
		if (inputServiceRunning) {
			serviceStarter.setText("Stop");
		} else {
			serviceStarter.setText("Run");			
		}
		
	}
	
	public void addSensorBoxes() {

		RelativeLayout toAddTo = (RelativeLayout) view.findViewById(R.id.layoutMainRelative);
		List<Sensor> sensorsToAdd = activity.app.getAllSensors();
		Sensor curSensor;
		int idOffset = 89571;
		
		for (int i=0; i<sensorsToAdd.size(); i++) {
    		curSensor = sensorsToAdd.get(i);
    		SensorCheckBox newBox = new SensorCheckBox(activity, curSensor.getName().hashCode());
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
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonToggleService:

			//activity.startFragmentTransaction(EntryActivity.GRAPH);
			
			
			if (LoggerApplication.isServiceRunning) {
				getActivity().stopService(new Intent(getActivity(), LoggerService.class));
				toggleCheckBoxes(true);
				setButtonText(false);
				
			} else {
				getActivity().startService(new Intent(getActivity(), LoggerService.class));
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
	
	
}
