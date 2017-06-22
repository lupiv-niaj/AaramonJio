package com.aaramon.aaramonjio.dataaccess;

import android.util.Log;


import com.aaramon.aaramonjio.controller.UIController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class HttpJsonThread extends Thread {
	String urlhit;
	byte event_fail, event_success;
	ArrayList<NameValuePair> namevaluepairs;

	public HttpJsonThread(byte event_success, byte event_fail, String urlhit,
                          ArrayList<NameValuePair> namevaluepairs) {

		this.urlhit = urlhit;
		this.event_fail = event_fail;
		this.event_success = event_success;
		this.namevaluepairs = namevaluepairs;

	}

	public void run() {
		Log.i("HIT URL", urlhit);
		try {

			HttpClient httpclient = new DefaultHttpClient();

			// client.
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					60000); // Timeout
			HttpResponse response;

			// try {
			HttpPost httppost = new HttpPost(urlhit);
			// HttpGet httpget = new HttpGet(new URI(urlhit));

			// Execute HTTP Post Request
			httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

			response = httpclient.execute(httppost);

			HttpEntity resEntityGet = response.getEntity(); // It will process
															// and take time to
															// execute
			if (resEntityGet != null) {
				// do something with the wresponse

				String sres = EntityUtils.toString(resEntityGet);
				Log.i("GET RESPONSE", sres);
				UIController.getController().sendMessage(event_success, sres);

			} else {
				UIController.getController().sendMessage(event_fail);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Exception",e.toString());
			//UIController.getController().sendMessage(event_fail);
		}
	}

}
