package com.moarcodeplz.moarsensorlogger.service;

import java.io.File;

import com.moarcodeplz.moarsensorlogger.activity.EntryActivity;
import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;
import com.moarcodeplz.moarsensorlogger.helper.LogHelper;
import com.moarcodeplz.moarsensorlogger.storage.StorageHelper;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class LoggerService extends Service implements SensorEventListener {

	//TODO move this value to preferences
	private final int LOG_FILE_CAPACITY = 1;
	private final int MAX_LOCAL_LOG_FILES = 100;
	private final int foregroundId = 80085;
	//private WriteRunnable writeFileThread = new WriteRunnable();
	
	private boolean isWriteThreadRunning = false;
	private int logSpeed = SensorManager.SENSOR_DELAY_FASTEST;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		Log.d("LoggerService", "LoggerService onCreate fired.");
		LoggerApplication.loggerService = this;
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		super.onStartCommand(intent, flags, startId);
		Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 001");
		LoggerApplication.isServiceRunning = true;
		Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 002");
		registerSensorListeners();
		Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 003");
		Log.d("LoggerService", "LoggerService onStartCommand fired.");
		
		//TODO put all of this into a getNotice method and put values in @strings accordingly
		
		Intent i = new Intent(this, EntryActivity.class);
		Log.d("LoggerService", "Is the intent null?!" + ((i == null) ? " YES!" : " NO!"));
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		Notification n = new Notification(android.R.drawable.presence_online, "Sensor logging ACTIVATED! PEWPEW", System.currentTimeMillis());
		n.setLatestEventInfo(this, "Sensor Logging", "Tap to configure", pi);
		n.flags |= Notification.FLAG_NO_CLEAR;
		startForeground(foregroundId, n);
		
		return START_STICKY;
		
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();
		LoggerApplication.isServiceRunning = false;
		unregisterSensorListeners();
		LoggerApplication.loggerService = null;
		
		stopForeground(true);
		
		Log.d("LoggerService", "LoggerService onDestroy fired.");
		
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	public void onSensorChanged(SensorEvent event) {
		
		LoggerApplication.numLogged++;
		//Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 011 - " + event.sensor.getName());
		LoggerApplication.logBuilder.append(LoggerApplication.sensorNumbers.get(event.sensor.getName().hashCode()) + "," + System.currentTimeMillis() + ",");
		//Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 012");
		for (int i=0; i<6; i++) {
			if (i < event.values.length) {
				LoggerApplication.logBuilder.append(event.values[i]);
			}
			LoggerApplication.logBuilder.append(",");
		}
		//Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 013");
		LoggerApplication.logBuilder.deleteCharAt(LoggerApplication.logBuilder.length() - 1).append("\n"); //Remove trailing comma
		//Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 014");
		
		if (LoggerApplication.numLogged >= LOG_FILE_CAPACITY
			&& !isWriteThreadRunning) {
			Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 015");
			isWriteThreadRunning = true;
			LoggerApplication.prepForCSVWrite();
			new WriteRunnable().run();
			
			File[] loggedFiles = new File(LoggerApplication.eventLogDirectory).listFiles();
			File checkFile = new File(LoggerApplication.eventLogDirectory);
			Log.d("LoggerService", "Is the file array null? " + ((loggedFiles == null)? " YES!" : " NO!"));
			Log.d("LoggerService", "Is checkFile null? " + ((checkFile == null) ? " YES!" : " NO!"));
			Log.d("LoggerService", "Does checkFile exist? " + (checkFile.exists() ? " YES!" : " NO!"));
			Log.d("LoggerService", "Is checkFile a directory? " + (checkFile.isDirectory() ? " YES!" : " NO!"));
			Log.d("LoggerService", "ZOMG PL0Z HALP MAI - 016");
			
			if (loggedFiles != null
				&& loggedFiles.length > MAX_LOCAL_LOG_FILES
				&& LogHelper.isWifiAvailable(this)
				&& !LoggerApplication.restClient.isOffloadRunning()) {
				LoggerApplication.restClient.setSharedPrefs(PreferenceManager.getDefaultSharedPreferences(this));
				LoggerApplication.restClient.setSensorList(((LoggerApplication)getApplication()).getAllSensors());
				LoggerApplication.restClient.offloadCsvFiles(loggedFiles);
			}
		}
		
	}
	
	private void registerSensorListeners() {
		
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		for (Sensor curSensor : sm.getSensorList(Sensor.TYPE_ALL)) {
			if (LoggerApplication.loggingSensors.get(curSensor.getName().hashCode())) {
				sm.registerListener(this, curSensor, logSpeed);
			}
		}
		
	}
	
	private void unregisterSensorListeners() {
		
		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.unregisterListener(this);
		
	}
	
	private class WriteRunnable implements Runnable {

		
		public void run() {
			Log.d("WriteRunnable", "WriteRunnable run method started.");
			boolean writeSucceed = StorageHelper.writeSensorEventsToFile();
			if (!writeSucceed) {
				Log.d("LoggerService", "File write failed :'(");
				//TODO throw error or log or something
			} else {
				Log.d("LoggerService", "File write succeeded!");
				LoggerApplication.numLogged -= LOG_FILE_CAPACITY;
			}
			isWriteThreadRunning = false;
		}
		
	}

}
