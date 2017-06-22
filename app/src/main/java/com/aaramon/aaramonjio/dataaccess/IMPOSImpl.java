package com.aaramon.aaramonjio.dataaccess;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;


import service.IMPOS;

public class IMPOSImpl implements IMPOS {

	public String Response;
	Context context;
	View view;
	String name;
	Activity activity;
	SharedPreferences sp;
	ResponseHandler responseHandler;

	public IMPOSImpl(Activity activity, Context applicationContext, View v,
                     String name) {
		// TODO Auto-generated constructor stub
		this.context = applicationContext;
		this.view = v;
		this.name = name;
		this.activity = activity;
		// this.Response = "";
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setHandler(ResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		Log.e("IMPOS RESPONSE", arg0);
		if (name.equalsIgnoreCase("SEARCH")) {
			responseHandler.notifyActivity(null);
		} else {
			responseHandler.notifyActivity(arg0);
		}
		// Toast.makeText(context, ""+arg0.toString(),
		// Toast.LENGTH_LONG).show();

		// Log.i("Data From JIO", second);
	}

	@Override
	public void onReturnDongleId(String arg0) {
		// TODO Auto-generated method stub
		Log.e("DONGLE ID", arg0);
		responseHandler.notifyActivity(arg0);
	}
}