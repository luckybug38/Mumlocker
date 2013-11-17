package com.moarcodeplz.moarsensorlogger.network;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.util.Log;

import com.loopj.android.http.*;
import com.moarcodeplz.moarsensorlogger.helper.LogHelper;
import com.moarcodeplz.moarsensorlogger.storage.SensorComparator;

public class UploadRestClient {
	
	private SharedPreferences sp;
	private List<Sensor> sensorList;
	private ArrayList<File> filesBeingOffloaded;
	private final AsyncHttpClient client = new AsyncHttpClient();
	
	private JsonHttpResponseHandler jsonHandler = new JsonHttpResponseHandler() {
		
		@Override 
		public void onSuccess(JSONObject response) {
			
			handleResponse(response);
			
		}
		
		@Override
		public void onFailure(Throwable e, JSONArray errorResponse) {
			
			System.out.println("HELLO!");
			
		}
		
		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			
			System.out.println("HELLO!");
			
		}
		
	};
	
	private AsyncHttpResponseHandler textHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(String response) {
			
			System.out.println("HELLO!");
			
		}
		
		@Override
		public void onFailure(Throwable e, String response) {
			
			System.out.println("HELLO!");
			
		}
		
	};
	
	private boolean isOffloadRunning = false;
	
	public UploadRestClient() {
		
	}
	
	public void setSensorList(List<Sensor> inputSensors) {
		
		sensorList = inputSensors;
		
	}
	
	public void setSharedPrefs(SharedPreferences inputSharedPref) {
		
		sp = inputSharedPref;
		
	}
	
	public void testConnection() {
		
		client.get("https://www.google.com", textHandler);
		
	}
	
	public void offloadCsvFiles(File[] inputFiles) {
		
		isOffloadRunning = true;
		filesBeingOffloaded = new ArrayList<File>(Arrays.asList(inputFiles));
		checkIfOffloadReady();
		
	}
	
	private void checkIfOffloadReady() {
		
		RequestParams toSend = getBasicParams();
		toSend.put("INTENT", "IS_READY_FOR_UPLOAD");
		client.post(sp.getString("targetUrl", ""), toSend, jsonHandler);
		
	}
	
	private void sendDeviceInformation() {
		
		RequestParams toSend = getBasicParams();
		toSend.put("INTENT", "SUPPLY_DEVICE_INFO");
		toSend.put("DEVICE_NAME", android.os.Build.MODEL);
		client.post(sp.getString("targetUrl", ""), toSend, jsonHandler);
		
	}
	
	private void offloadCsvFile() {
		
		RequestParams toSend = getBasicParams();
		toSend.put("INTENT", "OFFLOAD_CSV_FILE");
		try {
			toSend.put("FILE", filesBeingOffloaded.get(0));
		} catch (Exception ex) {
			//TODO handle exception properly
		}
		client.post(sp.getString("targetUrl", ""), toSend, jsonHandler);
		
	}
	
	private void sendSensorInformation(List<Sensor> inputSensors) {
		
		RequestParams toSend = getBasicParams();
		ArrayList<Sensor> sensors = new ArrayList<Sensor>(inputSensors);
		Collections.sort(sensors, new SensorComparator());
		String sensorInfo = "";
		
		for (Sensor curSensor: sensors) {
			sensorInfo += curSensor.getName() + "," + curSensor.getResolution() + "," + curSensor.getVendor() + "," + curSensor.getVersion() + "," + curSensor.getPower() + "," + curSensor.getMaximumRange() + "," + curSensor.getType() + "\n";
		}
		
		sensorInfo = sensorInfo.substring(0, sensorInfo.length() - 1); //Remove trailing new line
		
		toSend.put("SENSOR_INFO", sensorInfo);		
		toSend.put("INTENT", "SUPPLY_SENSOR_INFO");
		client.post(sp.getString("targetUrl", ""), toSend, jsonHandler);
		
	}
	
	private RequestParams getBasicParams() {
		
		RequestParams toReturn = new RequestParams();
		MessageDigest md;
		String hashedPw = "";
		
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(sp.getString("pw", "").getBytes("UTF-8"));
			hashedPw = LogHelper.byesToHexString(md.digest());
		} catch (Exception ex) {
			//TODO do something here
		}
		
		toReturn.put("EMAIL", sp.getString("email",""));
		toReturn.put("PASS", hashedPw);
		toReturn.put("DEVICE_ID", sp.getString("deviceId", ""));
		
		return toReturn;
		
	}
	
	public boolean isOffloadRunning() {
		
		return isOffloadRunning;
		
	}
	
	private void handleResponse(JSONObject response) {
		
		try {
			String sentIntent = response.getString("RESPONSE_TO");
			String serverResponse = response.getString("RESPONSE");
			
			if (serverResponse.equals("FAIL")) {
				throw new Exception(response.getString("DETAILS"));
			} else if (sentIntent.equals("IS_READY_FOR_UPLOAD")) {
				if (serverResponse.equals("YES")) {
					if (filesBeingOffloaded.size() > 0) {
						offloadCsvFile();
					}
				} else if (serverResponse.equals("NO")){
					String missingInfo = response.getString("INFO_NEEDED");
					if (missingInfo.equals("DEVICE")) {
						sendDeviceInformation();
					} else if (missingInfo.equals("SENSORS")) {
						sendSensorInformation(sensorList);
					} else {
						throw new Exception("missingInfo supplied for INFO_NEEDED did not match any of the pre-defined cases");
					}
				} else {
					throw new Exception("serverResponse for sentIntent of " + sentIntent + " did not match any of the pre-defined cases");
				}
			} else if (sentIntent.equals("SUPPLY_DEVICE_INFO")) {
				String deviceId = serverResponse;
				SharedPreferences.Editor edit = sp.edit();
				edit.putString("deviceId", deviceId);
				edit.commit();
				checkIfOffloadReady();
			} else if (sentIntent.equals("SUPPLY_SENSOR_INFO")) {
				checkIfOffloadReady();
			} else if (sentIntent.equals("OFFLOAD_CSV_FILE")) {
				String uploadedFile = response.getString("FILE_NAME");
				for (File curFile : filesBeingOffloaded) {
					if (curFile.getName().equals(uploadedFile)) {
						curFile.delete();
						filesBeingOffloaded.remove(curFile);
						break;
					}
				}
				if (filesBeingOffloaded.size() > 0) {
					offloadCsvFile();
				} else {
					isOffloadRunning = false;
				}
			} else {
				throw new Exception("sentIntent did not match any of the pre-defined cases");
			}
			
		} catch (Exception ex) {
			//TODO proper error handling
			isOffloadRunning = false;
			Log.d("UploadRestClient", "Exception thrown while parsing JSONObject response");
		}
		
	}

}
