package com.moarcodeplz.moarsensorlogger.storage;

import java.util.Comparator;

import android.hardware.Sensor;

public class SensorComparator implements Comparator<Sensor> {

	public int compare(Sensor s1, Sensor s2) {
		return s1.getName().compareTo(s2.getName());
	}

}
