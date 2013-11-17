package com.moarcodeplz.moarsensorlogger.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;

@SuppressLint("ViewConstructor")
public class SensorCheckBox extends CheckBox {

	private int containedSensorHash;
	
	public SensorCheckBox(Context context, int inputSensorHash) {
		
		super(context);
		containedSensorHash = inputSensorHash;
		
	}
	
	public int getSensorHash() {
		
		return containedSensorHash;
		
	}

}
