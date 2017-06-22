package com.aaramon.aaramonjio.dataaccess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> <br/>
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 */
public class ConnectivityUtils {

	private static final String LOG_TAG	= "ConnectivityUtils";

	/**
	 * @param pContext
	 * @return
	 */
	public static boolean isNetworkEnabled(Context pContext) {
		NetworkInfo activeNetwork = getActiveNetwork(pContext);
		return activeNetwork != null && activeNetwork.isConnected();
	}

	/**
	 * @param pContext
	 * @return
	 */
	public static void logNetworkState(Context pContext) {
		NetworkInfo activeNetwork = getActiveNetwork(pContext);
		if (activeNetwork == null) {
			Log.i(LOG_TAG, "No any active network found.");
			return;
		}
		Log.i(LOG_TAG, "Active Network. Type: " + activeNetwork.getTypeName());
		Log.i(LOG_TAG, "Active Network. isConnected: " + activeNetwork.isConnected());
		Log.i(LOG_TAG, "Active Network. isConnectedOrConnecting: " + activeNetwork.isConnectedOrConnecting());
		Log.i(LOG_TAG, "Active Network. N/W State Reason: " + activeNetwork.getReason());
	}

	/**
	 * @param pContext
	 * @return
	 */
	public static NetworkInfo getActiveNetwork(Context pContext) {
		ConnectivityManager conMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		return conMngr == null ? null : conMngr.getActiveNetworkInfo();
	}

	/**
	 * @param pContext
	 * @return
	 */
	@SuppressLint("HardwareIds")
	public static String getMobileNumber(Context pContext) {
		TelephonyManager telephonyMngr = (TelephonyManager) pContext.getSystemService(TELEPHONY_SERVICE);
		return telephonyMngr.getLine1Number();
	}

}