package com.aaramon.aaramonjio.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.IMPOSImpl;
import com.aaramon.aaramonjio.dataaccess.ResponseHandler;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import impl.PlugNPlay;
import pojo.Environment;
import service.IMPOS;

public class Jio_Money_Card extends Activity {
	EditText edt_amt;
	Button btn_save;
	ImageView img_swipe;
	// SharedPreferences sp;
	// String mid, tid;
	View v;
	SharedPreference_Main sharedPreference_Main;
	String Response = "";
//	AlertDialogManager alert = new AlertDialogManager();
	// ConnectionDetector cd;
	String unique_no;
	String IMEI_Number_Holder;
	TelephonyManager telephonyManager;
	String amount;
	String android_id, token;
	String data;
	String getencryptdata = "", mermobile;
	private ProgressDialog progress;
	int requesttype = Integer.parseInt("1001");
	String Result, exception;
	String emailid, rrefno, printdata, authcode, acquirename, mobileno,
			transid, response_code, message, statuscode, status_message;
	int status;
	String SendResponse = "", transaction_Date;
	double amount_check = 0.00;
	String strDate;
	SimpleDateFormat dateFormatter;
	ImageView img_back;
	String order_id, via;
	String card_amount;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// finally change the color
			window.setStatusBarColor(getResources().getColor(R.color.status_bar_color));
		}
		setContentView(R.layout.activity_jio__money__card);
		Bundle bun = getIntent().getExtras();
		via = bun.getString("VIA");
		order_id = bun.getString("order_id");
		card_amount = bun.getString("Card_Amount");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		sharedPreference_Main = new SharedPreference_Main(Jio_Money_Card.this);
		// sp =
		// PreferenceManager.getDefaultSharedPreferences(Jio_Money_Card.this);
		// mid = sp.getString("mid", mid);
		// tid = sp.getString("tid", tid);
		// Deliveryboy_id = sp.getString("Delivery_Boy_ID", Deliveryboy_id);
		// Store_id = sp.getString("Store_id", Store_id);
		// mermobile = sp.getString("jioPrepaidNum", mermobile);
		edt_amt = (EditText) findViewById(R.id.jio_amount_card_recieve);
		img_swipe = (ImageView) findViewById(R.id.jio_scan_card_image);
		btn_save = (Button) findViewById(R.id.jio_money_card_save);
		edt_amt.setText(card_amount);
		edt_amt.setEnabled(false);
		v = View.inflate(this.getApplicationContext(),
				R.layout.activity_jio__money__card, null);

		btn_save.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("HardwareIds")
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (DataStatic.isInternetAvailable(Jio_Money_Card.this)) {
					telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					IMEI_Number_Holder = telephonyManager.getDeviceId();
					android_id = Secure.getString(getContentResolver(),
							Secure.ANDROID_ID);
					amount = edt_amt.getText().toString();
					if (amount.isEmpty()) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								Jio_Money_Card.this);
						// builder.setTitle("AaramOn");
						builder.setMessage(getResources().getString(R.string.enteramount));
						builder.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										edt_amt.requestFocus();
										return;
									}
								});

						builder.show();
					} else {
						amount_check = Double.parseDouble(edt_amt.getText()
								.toString());
						if (amount_check <= 0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									Jio_Money_Card.this);
							// builder.setTitle("AaramOn");
							builder.setMessage(getResources().getString(R.string.checkamount));
							builder.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											edt_amt.requestFocus();
											return;
										}
									});

							builder.show();
						} else {

							long currenttime = System.currentTimeMillis() / 1000;
							unique_no = "AS" + currenttime + "JM";

							token = sharedPreference_Main.getdatastored();
							progress = new ProgressDialog(Jio_Money_Card.this,
									ProgressDialog.THEME_TRADITIONAL);
							progress.setCancelable(false);
							progress.setMessage("Please Wait...");
							progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progress.show();
							String blank = "";
							// android_id = android_id.substring(0, 12);
							String mobileid = android_id + "126";
							data = "requestType=" + requesttype + "&imei="
									+ IMEI_Number_Holder.toString()
									+ "&amount=" + String.valueOf(amount_check)

									+ "&longitude=" + blank + "&latitude="
									+ blank
									+ "&transactionType=Sale&totalAmount="
									+ String.valueOf(amount_check) + "&mid="
									+ sharedPreference_Main.getmid() + "&tid="
									+ sharedPreference_Main.gettid()
									+ "&dibUsername=mPOSSDK" + "&mobileId="
									+ mobileid +

									"&version=v2&merchantInstitutionId="
									+ sharedPreference_Main.getStoreId()
									+ "&merchantAddress="
									+ sharedPreference_Main.getStoreAddress()
									+ "&merchantCity="
									+ sharedPreference_Main.getCity()
									+ "&merchantState="
									+ sharedPreference_Main.getState()
									+ "&merchantPincode="
									+ sharedPreference_Main.getPincode()
									+ "&merchantCategory="
									+ sharedPreference_Main.getmccCode()
									+ "&invoiceNumber=" + unique_no
									+ "&originatedTransactionId=" + unique_no
									+ "&dealerId="
									+ sharedPreference_Main.getStoreId()
									+ "&dealerSubId="
									+ sharedPreference_Main.getdealerSubId()
									+ "&dealerName="
									+ sharedPreference_Main.getmerchantName()
									+ "";
							Log.d("Data Foe Request", data);
							Cipher cipher;
							try {
								cipher = Cipher
										.getInstance("AES/ECB/PKCS5Padding");
								SecretKeySpec secretKey = new SecretKeySpec(
										token.getBytes(), "AES");
								cipher.init(Cipher.ENCRYPT_MODE, secretKey);
								byte[] encryptedText = cipher.doFinal(data
										.getBytes());
								getencryptdata = Base64.encodeToString(
										encryptedText, Base64.DEFAULT);
								IMPOSImpl impos1;
								IMPOS impos = impos1 = new IMPOSImpl(
										Jio_Money_Card.this,
										getApplicationContext(), v, "Card");

								impos1.setHandler(new ResponseHandler() {
									@Override
									public void notifyActivity(String response) {
										// TODO Auto-generated method stub
										Response = response;
										Log.d("REsponse.........",
												Response);
										try {

											// {"StatusCode":"0","result":[{"message":"TIMED OUT. Please try again later.","responseCode":"9999"}],"StatusMessage":"success message 0"}

											JSONObject json = new JSONObject(
													Response);

											Object result_type = json
													.get("result");
											JSONObject newjson = null;
											if (result_type instanceof JSONArray) {
												// It's an array
												JSONArray jsonarray = json
														.getJSONArray("result");
												newjson = jsonarray
														.getJSONObject(0);
												response_code = newjson
														.getString("responseCode");
												message = newjson
														.getString("message");
												statuscode = json
														.getString("StatusCode");
												status_message = json
														.getString("StatusMessage");
											} else if (result_type instanceof JSONObject) {
												// It's an object

												JSONObject jsonarray = json
														.getJSONObject("result");
												response_code = jsonarray
														.getString("responseCode");
												message = jsonarray
														.getString("message");
												statuscode = json
														.getString("StatusCode");
												status_message = json
														.getString("StatusMessage");

											}

											// {"StatusCode":"0","StatusMessage":"success message 0","result":[{"message":"SUCCESS","responseCode":"0000","cardHolderName":"AARAMSHOP SUPER",
											// "AuthCode":"033134","signatureRequired":"N","issuerData":"612717028523","pinEncryptKey":"20160506 174352","pinBlock":"N","txnId":"TC160506174352A18219",
											// "transactionDateTime":"20160506 174352"}],"newToken":"EMxWon6SfyvKNAJcae5tYqWAT9xDIrCqAadpVyKlzME="}
											if (response_code.equals("0000")) {

												progress.dismiss();
												emailid = newjson
														.getString("emailId");
												rrefno = newjson
														.getString("rrefNo");
												printdata = newjson
														.getString("printData");
												authcode = newjson
														.getString("AuthCode");
												acquirename = newjson
														.getString("acquirerName");
												mobileno = newjson
														.getString("mobileNumber");
												transid = newjson
														.getString("transid");

												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(getResources().getString(R.string.paymentsuccess));
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// add_transaction();
																new get_add_transaction()
																		.execute();
															}
														});

												builder.show();
											} else if (response_code
													.equals("7001")) {
												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(message);
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
															}
														});

												builder.show();
											} else if (response_code
													.equals("05")) {
												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(message);
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
															}
														});

												builder.show();
											} else if (response_code
													.equals("58")) {
												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(message);
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
															}
														});

												builder.show();
											} else if (response_code
													.equals("7006")) {
												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(message);
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {

															}
														});

												builder.show();
											}
											// {"StatusCode":"0","StatusMessage":"success message 0","result":[{"message":"Invalid Auth Code (7006)","responseCode":"7006","emailId":"null","rrefNo":"null","printData":"null","AuthCode":"null","acquirerName":"null","mobileNumber":"null","transid":"null"}]}
											else if (response_code
													.equals("9999")) {
												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(message);
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {

															}
														});

												builder.show();

											} else if (response_code
													.equals("5112")) {

												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramGo");
												builder.setCancelable(false);
												builder.setMessage(getResources().getString(R.string.paymentcancel));
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																edt_amt.setText("0.00");
																edt_amt.setEnabled(true);
																btn_save.setEnabled(true);

															}
														});

												builder.show();

											} else {
												progress.dismiss();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Jio_Money_Card.this);
												// builder.setTitle("AaramOn");
												builder.setMessage(getResources().getString(R.string.carderror));
												builder.setPositiveButton(
														android.R.string.ok,
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {

															}
														});

												builder.show();
											}
										} catch (Exception e) {
											// TODO: handle exception
											progress.dismiss();
										}
									}
								});
								PlugNPlay plugNPlay = PlugNPlay.getInstance(
										impos, getApplicationContext());
								plugNPlay.doTransaction(getencryptdata,
										sharedPreference_Main.getmid(),
										sharedPreference_Main.gettid(),
										requesttype, amount_check,
										Environment.PROD, null);
							} catch (Exception e) {
								// TODO: handle exception
								progress.dismiss();
							}
						}
					}
				} else {
					Toast.makeText(Jio_Money_Card.this,
							getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	// class get_update_token extends AsyncTask<Void, Void, Void> {
	// ProgressDialog pb;
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// pb = new ProgressDialog(Jio_Money_Card.this,
	// ProgressDialog.THEME_TRADITIONAL);
	// pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);//
	// pb.setTitle("AaramGo");
	// pb.setMessage("Please Wait...");
	// pb.setCanceledOnTouchOutside(false);
	// pb.show();
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// update_token();
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// if (pb.isShowing()) {
	// pb.dismiss();
	// }
	// // adp.clear();
	// if (exception.equals("API")) {
	// alert.showAlertDialog(
	// Jio_Money_Card.this,
	// "AaramOn",
	// "We are unable to establish connection with server.Please try again after sometime",
	// false);
	// } else if (exception.equals("Json")) {
	// alert.showAlertDialog(
	// Jio_Money_Card.this,
	// "AaramOn",
	// "We are unable to establish connection with server.Please try again after sometime",
	// false);
	// } else if (exception.equals("Socket")) {
	// alert.showAlertDialog(
	// Jio_Money_Card.this,
	// "AaramOn",
	// "No Internet Connection Detected.Please check your connectivity",
	// false);
	// } else {
	// if (status == 1) {
	// Toast.makeText(Jio_Money_Card.this,
	// "Token Update Successfully", 5000).show();
	// }
	// }
	//
	// }
	// }

	// public void update_token() {
	//
	// try {
	// exception = "";
	// InputStream isr = null;
	// // To getting Data
	//
	// HttpClient httpclient = new DefaultHttpClient();
	//
	// HttpPost httppost = new HttpPost(
	// "http://www.aaramon.com:80/api/index.php/web/updateJioToken");
	//
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	// nameValuePairs.add(new BasicNameValuePair("mid",
	// sharedPreference_Main.getmid()));
	//
	// nameValuePairs.add(new BasicNameValuePair("tid",
	// sharedPreference_Main.gettid()));
	//
	// nameValuePairs.add(new BasicNameValuePair("oldToken", decryptedData
	// .toString()));
	//
	// nameValuePairs.add(new BasicNameValuePair("newToken", New_troken
	// .toString()));
	//
	// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	//
	// HttpResponse response = httpclient.execute(httppost);
	// if (response != null)
	// System.out.println("Connection created");
	// HttpEntity entity = response.getEntity();
	// isr = entity.getContent();
	//
	// // convert response to string
	//
	// InputStreamReader isre = new InputStreamReader(isr, "iso-8859-1");
	// BufferedReader reader = new BufferedReader(isre, 8);
	// StringBuilder sb = new StringBuilder();
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// if (line.startsWith("<"))
	// continue;
	// sb.append(line + "\n");
	// }
	// isr.close();
	// Result = sb.toString();
	// // tv.setText(result);
	// // Log.d("Result....", Result);
	//
	// JSONObject jobj = new JSONObject(Result);
	// Log.d("REsult", Result);
	// status = Integer.parseInt(jobj.getString("status"));
	//
	// // System.out.println(jobj);
	// } catch (SocketTimeoutException e) {
	// Log.e("Exception Found", e.toString());
	// exception = "Socket";
	// // TODO: handle exception
	// } catch (SocketException e) {
	// Log.e("Exception Found", e.toString());
	// exception = "Socket";
	//
	// } catch (JSONException e) {
	// exception = "Json";
	// }
	//
	// catch (Exception e) {
	// Log.e("log_tag", "Error converting result " + e.toString());
	// exception = "API";
	//
	// }
	// }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent();
//		intent.putExtra(
//				"SuccessValue",
//				"Success");
//		setResult(
//				RESULT_OK,
//				intent);
		finish();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.jio__money__card, menu);
		return true;
	}

	class get_add_transaction extends AsyncTask<Void, Void, Void> {
		ProgressDialog pb;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb = new ProgressDialog(Jio_Money_Card.this,
					ProgressDialog.THEME_TRADITIONAL);
			pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			pb.setMessage("Please Wait...");
			pb.setCanceledOnTouchOutside(false);
			pb.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Calendar c = Calendar.getInstance();
			dateFormatter = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.US);

			transaction_Date = dateFormatter.format(c.getTime());

			add_transaction();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pb.isShowing()) {
				pb.dismiss();
			}
			// adp.clear();
			if (exception.equals("API")) {
				Toast.makeText(Jio_Money_Card.this,getResources().getString(R.string.jiomoneyerror), Toast.LENGTH_LONG).show();
			}  else {
				if (status == 1) {

					edt_amt.setText("0.00");
					edt_amt.setText("");
					edt_amt.requestFocus();
					show_notify_dailog(rrefno);
				}
				// SharedPreferences.Editor se = sp.edit();
				// se.putString("masterNewToken", masterNewToken);
				// se.putString("masterOldToken", masterOldToken);
				// se.putString("newToken", newToken);
				// se.putString("oldToken", oldToken);
				// se.putBoolean("CONFIG", true);
				// se.commit();
				// // Toast.makeText(Order_status.this, oldToken, 5000).show();
				//
				// } else {
				// SharedPreferences.Editor se = sp.edit();
				// se.putString("masterNewToken", "");
				// se.putString("masterOldToken", "");
				// se.putString("newToken", "");
				// se.putString("oldToken", "");
				// se.putBoolean("CONFIG", true);
				// se.commit();
				// }
			}
		}
	}

	public void add_transaction() {

		try {
			exception = "";
			InputStream isr = null;
			// To getting Data

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					"http://www.aaramon.com:80/api/index.php/web/addJioTransaction");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("mid",
					sharedPreference_Main.getmid()));

			nameValuePairs.add(new BasicNameValuePair("tid",
					sharedPreference_Main.gettid()));

			nameValuePairs.add(new BasicNameValuePair("StatusCode", statuscode
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("StatusMessage",
					status_message.toString()));
			nameValuePairs.add(new BasicNameValuePair("message", message
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("responseCode",
					response_code.toString()));
			nameValuePairs.add(new BasicNameValuePair("cardHolderName",
					acquirename.toString()));

			nameValuePairs.add(new BasicNameValuePair("AuthCode", authcode
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("signatureRequired", ""));

			nameValuePairs.add(new BasicNameValuePair("rrefNo", rrefno
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("pinEncryptKey", ""));

			nameValuePairs.add(new BasicNameValuePair("pinBlock", ""));
			nameValuePairs.add(new BasicNameValuePair("txnId", transid
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("transactionDateTime",
					transaction_Date.toString()));
			nameValuePairs.add(new BasicNameValuePair("transactionAmount",
					String.valueOf(amount_check)));

			nameValuePairs.add(new BasicNameValuePair("barcode", ""));
			nameValuePairs.add(new BasicNameValuePair("order_id", ""));
			nameValuePairs.add(new BasicNameValuePair("delivery_boy_id", ""));
			nameValuePairs.add(new BasicNameValuePair("merchant_store_id",
					sharedPreference_Main.getStoreId()));

			nameValuePairs.add(new BasicNameValuePair("invoiceNumber",
					unique_no));
			nameValuePairs.add(new BasicNameValuePair("dealerSubId",
					sharedPreference_Main.getdealerSubId()));
			nameValuePairs.add(new BasicNameValuePair(
					"originatedTransactionId", unique_no));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			if (response != null)
				System.out.println("Connection created");
			HttpEntity entity = response.getEntity();
			isr = entity.getContent();

			// convert response to string

			InputStreamReader isre = new InputStreamReader(isr, "iso-8859-1");
			BufferedReader reader = new BufferedReader(isre, 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("<"))
					continue;
				sb.append(line + "\n");
			}
			isr.close();

			Result = sb.toString();
			// tv.setText(result);
			// Log.d("Result....", Result);

			JSONObject jobj = new JSONObject(Result);
			Log.d("REsult", Result);
			status = Integer.parseInt(jobj.getString("status"));

			// System.out.println(jobj);
		}
		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
			exception = "API";

		}
	}

	void show_notify_dailog(final String issuerdata_value) {
		final Dialog login = new Dialog(this);
		// Set GUI of login screen
		login.setContentView(R.layout.layout_custom_dialog);
		login.setTitle("Send Notification");
		login.setCanceledOnTouchOutside(false);
		login.setCancelable(false);
		// Init button of login GUI
		Button send_btn = (Button) login.findViewById(R.id.ad_send);
		Button cancel_btn = (Button) login.findViewById(R.id.ad_cancel);
		final EditText edt_mobile = (EditText) login
				.findViewById(R.id.ad_mobile);
		final EditText edt_email = (EditText) login.findViewById(R.id.ad_email);

		// Attached listener for login GUI button
		send_btn.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("HardwareIds")
			public void onClick(View v) {
				String mobile = edt_mobile.getText().toString();
				String email = edt_email.getText().toString();

				if (mobile.equals("") && email.equals("")) {
					edt_mobile.setError(getResources().getString(R.string.entermobile));
					edt_mobile.requestFocus();
				} else if (mobile.length() != 10) {
					edt_mobile.setError(getResources().getString(R.string.entermobile));
					edt_mobile.requestFocus();
				} else if (!isValidEmail(email) && email.length() > 0) {
					edt_email.setError(getResources().getString(R.string.invalidemail));
					edt_email.requestFocus();
				} else {
					if (DataStatic.isInternetAvailable(v.getContext())) {

						progress = new ProgressDialog(v.getContext());
						progress.setCancelable(false);
						progress.setMessage("Please Wait...");
						progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progress.show();
						String rrno = issuerdata_value.toString();
						String destination = mobile + ";" + email;
						telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						IMEI_Number_Holder = telephonyManager.getDeviceId();
						data = "requestType=1007&destination="
								+ destination.toString() + "&imei="
								+ IMEI_Number_Holder.toString() + "&rrn="
								+ rrno.toString() + "&mid="
								+ sharedPreference_Main.getmid() + "&tid="
								+ sharedPreference_Main.gettid();
						Log.d("Data Foe Request", data);
						Cipher cipher;
						try {
							cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
							SecretKeySpec secretKey = new SecretKeySpec(token
									.getBytes(), "AES");
							cipher.init(Cipher.ENCRYPT_MODE, secretKey);
							byte[] encryptedText = cipher.doFinal(data
									.getBytes());
							getencryptdata = Base64.encodeToString(
									encryptedText, Base64.DEFAULT);
							Log.d("Encrypt Data", getencryptdata);
							IMPOSImpl impos1;
							IMPOS impos = impos1 = new IMPOSImpl(
									Jio_Money_Card.this,
									getApplicationContext(), v, "Notification");

							impos1.setHandler(new ResponseHandler() {

								@Override
								public void notifyActivity(String response) {
									// TODO Auto-generated method stub
									SendResponse = response;
									Log.d("REsponse of Send", SendResponse);
									JSONObject json1;
									try {
										json1 = new JSONObject(SendResponse);
										JSONArray jsonarray1 = json1
												.getJSONArray("result");
										JSONObject newjson2 = jsonarray1
												.getJSONObject(0);

										Log.d("Json Array", newjson2.toString());
										String response_code = newjson2
												.getString("responseCode");
										if (response_code.equals("0000")) {
											AlertDialog.Builder builder = new AlertDialog.Builder(
													Jio_Money_Card.this);
											// builder.setTitle("AaramGo");
											builder.setMessage(getResources().getString(R.string.notificationsentsucces));

											builder.setPositiveButton(
													android.R.string.ok,
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															login.dismiss();
															progress.dismiss();
															Intent intent = new Intent();
															intent.putExtra(
																	"SuccessValue",
																	"Success");
															setResult(
																	RESULT_OK,
																	intent);
															finish();
														}
													});

											builder.show();

										} else {
											AlertDialog.Builder builder = new AlertDialog.Builder(
													Jio_Money_Card.this);
											// builder.setTitle("AaramGo");
											builder.setMessage(getResources().getString(R.string.tryagain));

											builder.setPositiveButton(
													android.R.string.ok,
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {

														}
													});

											builder.show();
										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// {"StatusCode":"0","StatusMessage":"success message 0","result":[{"responseMessage":"Notification request received received and it is in progress","responseCode":"0000","status":"null","remarks":"null"}],"newToken":"a41souIQkWvvdMJrgEWAbhLOZ7JVlEh159JadfoLkCs="}
								}
							});
							PlugNPlay plugNPlay = PlugNPlay.getInstance(impos,
									getApplicationContext());
							plugNPlay.doTransaction(getencryptdata,
									sharedPreference_Main.getmid(),
									sharedPreference_Main.gettid(), 1007, 0.00,
									Environment.PROD, null);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}

					else {
						Toast.makeText(v.getContext(),
								getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
		cancel_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				login.dismiss();
				Intent intent = new Intent();
				intent.putExtra("SuccessValue", "Success");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		// Make dialog box visible.
		login.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
