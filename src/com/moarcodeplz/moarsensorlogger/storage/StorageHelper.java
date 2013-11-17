package com.moarcodeplz.moarsensorlogger.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.moarcodeplz.moarsensorlogger.application.LoggerApplication;

import android.hardware.Sensor;
import android.util.Log;
import android.util.SparseBooleanArray;

//TODO bring error handling out to LoggerApplication

public class StorageHelper {

	public static boolean writePreferencesToFile(SparseBooleanArray inputArray, List<Sensor> inputSensors) {
		
		String toWrite = "";
		ArrayList<Sensor> sortedList = new ArrayList<Sensor>(inputSensors);
		Collections.sort(sortedList, new SensorComparator());
		
		for (Sensor curSensor : sortedList) {
			toWrite += (inputArray.get(curSensor.getName().hashCode()) ? 1 : 0) + "," + curSensor.getName().hashCode() + "\n";
		}
		
		toWrite = toWrite.substring(0, toWrite.length() - 1);
		
		boolean writeSuccess = writeStringToFile(LoggerApplication.sensorPreferenceUri, toWrite);
		
		Log.d("StorageHelper", "StorageHelper writePreferences to file " + ((writeSuccess) ? "succeeded." : "failed."));
		
		return writeSuccess;
		
	}
	
	public static boolean writeDefaultPreferenceFile(List<Sensor> inputSensors) {
		
		String toWrite = "";
		ArrayList<Sensor> sortedList = new ArrayList<Sensor>(inputSensors);
		Collections.sort(sortedList, new SensorComparator());
		
		for (Sensor curSensor: sortedList) {
			toWrite += "0," + curSensor.getName().hashCode() + "\n";
		}
		
		toWrite = toWrite.substring(0, toWrite.length() - 1);
		
		boolean writeSuccess = writeStringToFile(LoggerApplication.sensorPreferenceUri, toWrite);

		Log.d("StorageHelper", "StorageHelper writeDefaultPreferenceFile " + ((writeSuccess) ? "succeeded." : "failed."));
		
		return writeSuccess;
		
	}
	
	public static boolean doesFileExist(String inputUri) {
		
		return new File(inputUri).exists();
		
	}
	
	public static boolean writeStringToFile(String fileUri, String toWrite) {
		System.out.println(toWrite);
		/*
		try {
			File curFile = new File(fileUri);
			if (!curFile.exists()) {
				curFile.getParentFile().mkdirs();
				curFile.createNewFile();
			}
			OutputStreamWriter ows = new OutputStreamWriter(new FileOutputStream(curFile));
			ows.write(toWrite);
			ows.flush();
			ows.close();
			boolean writeSuccess = true;
			Log.d("StorageHelper", "StorageHelper writeStringToFile succeeded.");
			return writeSuccess;
		} catch (Exception ex) {
			Log.d("StorageHelper", "StorageHelper writeStringToFile failed.");
			return false;
		}
		*/
		return true;
		
		
	}
	
	public static boolean appendStringToFile(String fileUri, String toWrite) {
		
		try {
			FileWriter f = new FileWriter(fileUri, true);
			f.write(toWrite);
			f.flush();
			f.close();
			return true;
		} catch (Exception ex) {
			Log.d("StorageHelper", "StorageHelper appendStringToFile failed.");
			return false;
		}
		
	}
	
	public static SparseBooleanArray getSensorPreferencesFromFile() {
		
		SparseBooleanArray toReturn = new SparseBooleanArray();
		String prefsFromDisk = getStringFromFile(LoggerApplication.sensorPreferenceUri);
		String[] splitPrefs = prefsFromDisk.split("\\r?\\n");
		String[] curSplit;
		
		for (String curString : splitPrefs) {
			curSplit = curString.split(",");
			toReturn.put(Integer.valueOf(curSplit[1]), curSplit[0].equals("1"));
		}
		
		return toReturn;
		
	}
	
	public static boolean writeNoteToFile(String inputNote) {
		
		return appendStringToFile(LoggerApplication.noteLogUri, System.currentTimeMillis() + ":" + inputNote + "\n");
		
		
	}
	
	//http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
	public static String getStringFromFile(String fileUri) {
		
		//TODO close stream properly
		
		try {
			FileInputStream stream = new FileInputStream(new File(fileUri));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			stream.close();
			Log.d("StorageHelper", "StorageHelper getStringFromFile succeeded.");
			return Charset.defaultCharset().decode(bb).toString();
		} catch (Exception ex) {
			Log.d("StorageHelper", "StorageHelper writeDefaultPreferenceFile failed.");
			return null;
		} 
	
	}
	
	public static boolean writeSensorEventsToFile() {
		
		boolean writeSuccess = writeStringToFile(LoggerApplication.eventLogDirectory + System.currentTimeMillis() + ".csv", LoggerApplication.logToWrite);
		
		Log.d("StorageHelper", "StorageHelper writeSensorEventsToFile " + ((writeSuccess) ? "succeeded." : "failed."));
		
		return writeSuccess;
		
	}
	
}
