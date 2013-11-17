package com.moarcodeplz.moarsensorlogger.helper;

import java.math.BigInteger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class LogHelper {

	public static boolean isWifiAvailable(Context inputContext) {

		ConnectivityManager connManager = (ConnectivityManager) inputContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d("LogHelper", "Is connManager null? " + ((connManager == null) ? " YES!" : " NO!"));
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.d("LogHelper", "Is mWifi null? " + ((mWifi == null) ? " YES!" : " NO!"));

		return mWifi.isConnected();
		
	}
	
	public static String byesToHexString(byte[] bytes) {
		return String.format("%0" + (bytes.length * 2) + 'x', new BigInteger(1, bytes));
	}
	
}
