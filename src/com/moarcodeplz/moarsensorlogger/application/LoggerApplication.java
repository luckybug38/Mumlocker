package com.moarcodeplz.moarsensorlogger.application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.moarcodeplz.moarsensorlogger.network.UploadRestClient;
import com.moarcodeplz.moarsensorlogger.service.LoggerService;
import com.moarcodeplz.moarsensorlogger.storage.SensorComparator;
import com.moarcodeplz.moarsensorlogger.storage.StorageHelper;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

//TODO make sure all logs are being written correctly
//TODO remove static reference to activity
//Reduce logBuilder size

public class LoggerApplication extends Application {
	
	public static final String rootDataDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "data" + File.separator + "MoarSensorLogger" + File.separator;
	public static final String eventLogDirectory = rootDataDirectory + "logs" + File.separator;
	public static final String sensorPreferenceUri = rootDataDirectory + "sprefs.xml";
	public static final String noteLogUri = rootDataDirectory + "notes.txt";
	public static final UploadRestClient restClient = new UploadRestClient();

	public static StringBuilder logBuilder = new StringBuilder();
	public static LoggerService loggerService;
	public static SparseBooleanArray loggingSensors;
	public static SparseIntArray sensorNumbers;
	public static boolean isServiceRunning = false;
	public static int numLogged = 0;
	public static String logToWrite;
	
	@Override
	public void onCreate() {
		
		if (!StorageHelper.doesFileExist(LoggerApplication.sensorPreferenceUri)) {
			StorageHelper.writeDefaultPreferenceFile(getAllSensors());
		}
		
		LoggerApplication.loggingSensors = StorageHelper.getSensorPreferencesFromFile();
		LoggerApplication.sensorNumbers = getNumberedSensors();
		
		Log.d("LoggerApplication", "LoggerApplication onCreate fired.");
		
	}
	
	@Override
	public void onTerminate() {
		
		super.onTerminate();
		writeOutSensorPreferences();
		
	}
	
	public List<Sensor> getAllSensors() {
		
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		return sm.getSensorList(Sensor.TYPE_ALL);
		
	}
	
	public void writeOutSensorPreferences() {
		
		//TODO check for errorrrsss
		boolean writeSuccess = StorageHelper.writePreferencesToFile(loggingSensors, getAllSensors());
		Log.d("LoggerApplication", "LoggerApplication writeOutSensorPreferences fired.");
		Log.d("LoggerApplication", (writeSuccess) ? "LoggerApplication successfully wrote out sensor preferences." : "LoggerApplication did not write out sensor preferences correctly.");
		
	}
	
	private SparseIntArray getNumberedSensors() {
		
		ArrayList<Sensor> sensors = new ArrayList<Sensor>(getAllSensors());
		Collections.sort(sensors, new SensorComparator());
		SparseIntArray toReturn = new SparseIntArray(sensors.size());
		for (int i=0; i<sensors.size(); i++) {
			toReturn.put(sensors.get(i).getName().hashCode(), i);
		}
		
		return toReturn;
	
	}
	
	public static void prepForCSVWrite() {
		
		LoggerApplication.logToWrite = LoggerApplication.logBuilder.deleteCharAt(LoggerApplication.logBuilder.length() - 1).toString(); //Remove trailing new line from String
		LoggerApplication.logBuilder = new StringBuilder();
		
	}

}
