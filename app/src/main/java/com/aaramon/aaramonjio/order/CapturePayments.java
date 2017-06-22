package com.aaramon.aaramonjio.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.GPSTracker;
import com.aaramon.aaramonjio.dataaccess.IMPOSImpl;
import com.aaramon.aaramonjio.dataaccess.ResponseHandler;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import impl.PlugNPlay;
import pojo.Environment;
import service.IMPOS;

public class CapturePayments extends Activity implements OnScanListener {
    ImageView img_back;
    EditText et_scancode;
    TextView tv_paymentss;
    String IMEI_Number_Holder;
    private ProgressDialog progress;
    EditText et_amount;
    String data = "";
    SharedPreference_Main sharedPreference_Main;
    GPSTracker gps;
    TelephonyManager telephonyManager;
    String SendResponse = "";
    String contents = "";
    View v;
    String unique_no = "";
    String getencryptdata = "";
    int d = 0;
    String status_query_messagecode, status_query_rrefno;
    Cipher cipher;
    String decryptedData = "", StatusCode = "", responseCode = "",
            cardHolderName = "", AuthCode = "", signatureRequired = "",
            issuerData = "", pinEncryptKey = "", pinBlock = "", txnId = "",
            transactionDateTime = "", StatusMessage = "";
    String messageresposeserver;
    String response_code = "0";
    String via, order_id;
    FrameLayout zbarLayout;
    private BarcodePicker mBarcodePicker;
    private final int CAMERA_PERMISSION_REQUEST = 0;
    private boolean mDeniedCameraAccess = false;
    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "aANmnk0bEeSRnFMYy8LZZeZsEEgB3s3HP6tOQqJejgs";
    private boolean mPaused = true;
    Toast mToast = null;
    String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        setContentView(R.layout.capturepayments);
        v = View.inflate(this.getApplicationContext(),
                R.layout.capturepayments, null);
        gps = new GPSTracker(CapturePayments.this);
        sharedPreference_Main = new SharedPreference_Main(
                getApplicationContext());
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_amount = (EditText) findViewById(R.id.et_amount);
        tv_paymentss = (TextView) findViewById(R.id.tv_paymentss);
        zbarLayout = (FrameLayout) findViewById(R.id.frmQr);
        Bundle bun = getIntent().getExtras();
        via = bun.getString("VIA");
        order_id = bun.getString("order_id");
        et_amount.setText(bun.getString("Barcode_Amount"));
        if (et_amount.getText().length() > 0) {
            et_amount.setEnabled(false);
        } else {
            et_amount.setEnabled(true);
        }
        ScanditLicense.setAppKey(sScanditSdkAppKey);
        initializeAndStartBarcodeScanning();
        tv_paymentss.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (et_amount.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.entermobile), Toast.LENGTH_SHORT).show();
                } else if (et_scancode.getText().toString()
                        .equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.pleaseenterbarcode), Toast.LENGTH_SHORT)
                            .show();
                } else {

                    if (DataStatic.isInternetAvailable(CapturePayments.this)) {
                        callapibarcodepayment();
                    } else {
                        Toast.makeText(CapturePayments.this,
                                getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                                .show();
                    }
                }

            }
        });
        // getjiotoken();
        et_scancode = (EditText) findViewById(R.id.et_scancode);
        img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        // getJioToken("0");

    }

    public void initializeAndStartBarcodeScanning() {
        // The scanning behavior of the barcode picker is configured through scan
        // settings. We start with empty scan settings and enable a very generous
        // set of symbologies. In your own apps, only enable the symbologies you
        // actually need.
        ScanSettings settings = ScanSettings.create();
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN13, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCA, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN8, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCE, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE93, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_QR, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE39, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE128, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_GS1_DATABAR_EXPANDED, true);

        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_INTERLEAVED_2_OF_5, true);
        settings.setCameraFacingPreference(ScanSettings.CAMERA_FACING_BACK);

        settings.setCodeDuplicateFilter(3000);
        // Some Android 2.3+ devices do not support rotated camera feeds. On these devices, the
        // barcode picker emulates portrait mode by rotating the scan UI.
        boolean emulatePortraitMode = !BarcodePicker.canRunPortraitPicker();
        if (emulatePortraitMode) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        BarcodePicker picker = new BarcodePicker(this, settings);
        zbarLayout.addView(picker);
        mBarcodePicker = picker;
        mBarcodePicker.setOnScanListener(this);
    }

    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();
        mPaused = true;
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void grantCameraPermissionsThenStartScanning() {
        if (this.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (mDeniedCameraAccess == false) {
                // it's pretty clear for why the camera is required. We don't need to give a
                // detailed reason.
                this.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }

        } else {
            // we already have the permission
            mBarcodePicker.startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mDeniedCameraAccess = false;
                if (!mPaused) {
                    mBarcodePicker.startScanning();
                }
            } else {
                mDeniedCameraAccess = true;
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
        // handle permissions for Marshmallow and onwards...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantCameraPermissionsThenStartScanning();
        } else {
            // Once the activity is in the foreground again, restart scanning.
            mBarcodePicker.startScanning();
        }
    }

    @SuppressLint("HardwareIds")
    private void
    callapibarcodepayment() {
        // TODO Auto-generated method stub
        progress = new ProgressDialog(CapturePayments.this,
                ProgressDialog.THEME_TRADITIONAL);
        progress.setCancelable(false);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {

            long currenttime = System.currentTimeMillis() / 1000;
            unique_no = "AS" + currenttime + "JM";
            data = "requestType=" + "1004" + "&mid="
                    + sharedPreference_Main.getmid() + "&tid="
                    + sharedPreference_Main.gettid() + "" + "&imei=" + ""
                    + telephonyManager.getDeviceId() + "&scanData="
                    + et_scancode.getText().toString() + "&longitude="
                    + gps.getLongitude() + "&latitude=" + gps.getLatitude()
                    + "&transactionType=" + "sale" + "&amount="
                    + et_amount.getText().toString() + "&mobileId="
                    + "9990928456678907654567" + "&merchantName="
                    + sharedPreference_Main.getmerchantName()
                    + "&merchantMobileNumber="
                    + sharedPreference_Main.getmerchantMobileNumber()
                    + "&tipPermission="
                    + sharedPreference_Main.gettipPermission()
                    + "&merchantBusinessName="
                    + sharedPreference_Main.getmerchantBusinessName()
                    + "&tipPercent=" + sharedPreference_Main.gettipPercent()
                    + "&version=v2&merchantInstitutionId="
                    + sharedPreference_Main.getStoreId() + "&merchantAddress="
                    + sharedPreference_Main.getStoreAddress()
                    + "&merchantCity=" + sharedPreference_Main.getCity()
                    + "&merchantState=" + sharedPreference_Main.getStoreName()
                    + "&merchantPincode=" + sharedPreference_Main.getPincode()
                    + "&merchantCategory=" + sharedPreference_Main.getmccCode()
                    + "&invoiceNumber=" + unique_no
                    + "&originatedTransactionId=" + unique_no + "&dealerId="
                    + sharedPreference_Main.getStoreId() + "&dealerSubId="
                    + sharedPreference_Main.getdealerSubId() + "&dealerName="
                    + sharedPreference_Main.getmerchantName() + "";
            double amount = Double.parseDouble(et_amount.getText().toString());
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(sharedPreference_Main
                    .getdatastored().getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedText = cipher.doFinal(data.getBytes());
            String getmerchantdata = Base64.encodeToString(encryptedText,
                    Base64.DEFAULT);

            IMPOSImpl impos1;
            IMPOS impos = impos1 = new IMPOSImpl(CapturePayments.this,
                    getApplicationContext(), v, "Barcode");
            PlugNPlay plugNPlay = PlugNPlay.getInstance(impos,
                    getApplicationContext());
            plugNPlay.doTransaction(getmerchantdata,
                    sharedPreference_Main.getmid(),
                    sharedPreference_Main.gettid(), 1004, amount,
                    Environment.PROD, null);
            impos1.setHandler(new ResponseHandler() {

                @Override
                public void notifyActivity(String response) {
                    // TODO Auto-generated method stub
                    Log.e("response arises", "" + response.toString());
                    if (response != null) {
                        try {

                            JSONObject jsonO = new JSONObject(response);
                            if (jsonO.getString("StatusMessage").equals(
                                    "success message 0")) {
                                JSONArray result = jsonO.getJSONArray("result");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject prod = result.getJSONObject(i);
                                    response_code = prod.getString("responseCode");
                                    messageresposeserver = prod
                                            .getString("message");
                                }
                            }
                            progress.dismiss();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    if (response != null) {
                        try {
                            JSONObject jsonO = new JSONObject(response);
                            if (response_code
                                    .equalsIgnoreCase("0000")) {
                                progress.dismiss();
                                String newToken = jsonO.getString("newToken");
                                StatusCode = jsonO.getString("StatusCode");
                                StatusMessage = jsonO
                                        .getString("StatusMessage");
                                JSONObject jsonO2 = new JSONObject(response);
                                if (jsonO2.getString("StatusMessage").equals(
                                        "success message 0")) {
                                    JSONArray result2 = jsonO2
                                            .getJSONArray("result");
                                    for (int i = 0; i < result2.length(); i++) {
                                        JSONObject prod2 = result2
                                                .getJSONObject(i);
                                        messageresposeserver = prod2
                                                .getString("message");
                                        responseCode = prod2
                                                .getString("responseCode");
                                        cardHolderName = prod2
                                                .getString("cardHolderName");
                                        AuthCode = prod2.getString("AuthCode");
                                        signatureRequired = prod2
                                                .getString("signatureRequired");
                                        issuerData = prod2.getString("rrefNo");
                                        pinEncryptKey = prod2
                                                .getString("pinEncryptKey");
                                        pinBlock = prod2.getString("pinBlock");
                                        txnId = prod2.getString("txnId");
                                        transactionDateTime = prod2
                                                .getString("transactionDateTime");
                                    }
                                    addJioTransaction();
                                }
                                // finish();
                            } else if (response_code.equalsIgnoreCase("9999")) {
                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        CapturePayments.this);
                                // builder.setTitle("AaramOn");
                                builder.setMessage(messageresposeserver);
                                builder.setPositiveButton(
                                        android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                try {
                                                    progress = new ProgressDialog(CapturePayments.this,
                                                            ProgressDialog.THEME_TRADITIONAL);
                                                    progress.setCancelable(false);
                                                    progress.setMessage("Please Wait...");
                                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progress.show();


                                                    data = "requestType=1003&mid=" + sharedPreference_Main.getMID() + "&tid=" + sharedPreference_Main.gettid() + "&version=V2&originatedTransactionId=" + unique_no + "";

                                                    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                                                    SecretKeySpec secretKey = new SecretKeySpec(sharedPreference_Main
                                                            .getdatastored().getBytes(), "AES");
                                                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                                                    byte[] encryptedText = cipher.doFinal(data.getBytes());
                                                    String getmerchantdata = Base64.encodeToString(encryptedText,
                                                            Base64.DEFAULT);

                                                    IMPOSImpl impos1;
                                                    IMPOS impos = impos1 = new IMPOSImpl(CapturePayments.this,
                                                            getApplicationContext(), v, "StatusQuery");
                                                    PlugNPlay plugNPlay = PlugNPlay.getInstance(impos,
                                                            getApplicationContext());
                                                    plugNPlay.doTransaction(getmerchantdata,
                                                            sharedPreference_Main.getmid(),
                                                            sharedPreference_Main.gettid(), 1003, 0.00,
                                                            Environment.PROD, null);

                                                    impos1.setHandler(new ResponseHandler() {
                                                        @Override
                                                        public void notifyActivity(String response) {
                                                            Log.e("response arises", "" + response.toString());
                                                            try {
                                                                progress.dismiss();
                                                                JSONObject jsonO2 = new JSONObject(response);
                                                                JSONArray result2 = jsonO2
                                                                        .getJSONArray("result");
                                                                JSONObject prod2 = result2
                                                                        .getJSONObject(0);
                                                                status_query_messagecode = prod2.getString("messageCode");
                                                                if (status_query_messagecode.equalsIgnoreCase("RPSL_NAVI_BUS_SUCCESS_001") || status_query_messagecode.equalsIgnoreCase("RPSL_NAVI_BUS_RETURNED_ALL_001")) {
                                                                    JSONObject rref_obj = result2.getJSONObject(2);
                                                                    status_query_rrefno = rref_obj.getString("rrefNo");
                                                                    if (status_query_rrefno == null || status_query_rrefno.isEmpty()) {
                                                                        progress.dismiss();
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                                                CapturePayments.this);
                                                                        // builder.setTitle("AaramOn");
                                                                        builder.setMessage("Transaction Failed.Please Try Again");
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
                                                                    } else {
                                                                        messageresposeserver = prod2
                                                                                .getString("message");
                                                                        transactionDateTime = rref_obj
                                                                                .getString("date") + " " + rref_obj.getString("time");
                                                                        cardHolderName = rref_obj
                                                                                .getString("customerName");
                                                                        AuthCode = rref_obj.getString("authCode");
                                                                        issuerData = rref_obj.getString("rrefNo");
                                                                        responseCode = prod2
                                                                                .getString("messageCode");

                                                                        signatureRequired = "";
                                                                        pinEncryptKey = "";
                                                                        pinBlock = "";
                                                                        addJioTransaction();
                                                                    }
                                                                }
                                                            } catch (JSONException e) {

                                                            }
                                                        }
                                                    });
                                                } catch (Exception ex) {

                                                }

                                            }
                                        });

                                builder.show();
                            } else {
                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        CapturePayments.this);
                                // builder.setTitle("AaramOn");
                                builder.setMessage(messageresposeserver);
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
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            progress.dismiss();
            Log.e("message", e.getMessage() + "");
        }

    }

    private void showpaymentsuccessfulldialog() {

        final Dialog dialog = new Dialog(CapturePayments.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.paymentsuccessfuldialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        final TextView tv_successfull = (TextView) dialog
                .findViewById(R.id.tv_successfull);
        tv_successfull.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (via.equalsIgnoreCase("payments")) {
                    Intent intent = new Intent();
                    intent.putExtra("SuccessValue", "Success");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    show_notify_dailog(issuerData);
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }

    private void addJioTransaction() {
        // TODO Auto-generated method stub

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                "http://www.aaramon.com:80/api/index.php/web/addJioTransaction",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", "" + response.toString());

                        try {
                            JSONObject jsonO = new JSONObject(response);
                            if (jsonO.getString("status").equals("1")) {
                                Log.e("response", "" + response.toString());
                                Toast.makeText(getApplicationContext(),
                                        "" + jsonO.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                                showpaymentsuccessfulldialog();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "" + jsonO.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.hide();
                VolleyLog.e("getStoreCustomers",
                        "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", "" + sharedPreference_Main.getmid());
                params.put("tid", "" + sharedPreference_Main.gettid());
                params.put("StatusCode", sharedPreference_Main.getdatastored());
                params.put("StatusMessage", "" + messageresposeserver);
                params.put("message", "" + StatusMessage);
                params.put("responseCode", "" + responseCode);
                params.put("cardHolderName", "" + cardHolderName);
                params.put("AuthCode", "" + AuthCode);
                params.put("signatureRequired", "" + signatureRequired);
                params.put("rrefNo", "" + issuerData);
                params.put("pinEncryptKey", "" + pinEncryptKey);
                params.put("pinBlock", "" + pinBlock);
                params.put("txnId", "" + txnId);
                params.put("transactionDateTime", "" + transactionDateTime);
                params.put("transactionAmount", ""
                        + et_amount.getText().toString() + "");
                params.put("barcode", "" + et_scancode.getText().toString());

                params.put("order_id", "" + "");
                params.put("delivery_boy_id", "" + "");
                params.put("merchant_store_id",
                        "" + sharedPreference_Main.getStoreId());
                params.put("invoiceNumber", "" + unique_no);
                params.put("dealerSubId",
                        "" + sharedPreference_Main.getdealerSubId());
                params.put("originatedTransactionId", "" + unique_no);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }

    private static AlertDialog showDialog(final Activity act,
                                          CharSequence title, CharSequence message, CharSequence buttonYes,
                                          CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:"
                                + "com.google.zxing.client.android");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            act.startActivity(intent);
                        } catch (ActivityNotFoundException anfe) {

                        }
                    }
                });
        downloadDialog.setNegativeButton(buttonNo,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        return downloadDialog.show();

    }

    void show_notify_dailog(final String issuerdata_value) {
        final Dialog login = new Dialog(this);
        // Set GUI of login screen
        login.setContentView(R.layout.layout_custom_dialog);
        login.setTitle(getResources().getString(R.string.sendnotification));
        login.setCanceledOnTouchOutside(false);
        login.setCancelable(false);
        // Init button of login GUI
        Button send_btn = (Button) login.findViewById(R.id.ad_send);
        Button cancel_btn = (Button) login.findViewById(R.id.ad_cancel);
        final EditText edt_mobile = (EditText) login
                .findViewById(R.id.ad_mobile);
        final EditText edt_email = (EditText) login.findViewById(R.id.ad_email);

        // Attached listener for login GUI button
        send_btn.setOnClickListener(new OnClickListener() {
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
                    if (DataStatic.isInternetAvailable(CapturePayments.this)) {

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
                            SecretKeySpec secretKey = new SecretKeySpec(
                                    sharedPreference_Main.getdatastored()
                                            .getBytes(), "AES");
                            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                            byte[] encryptedText = cipher.doFinal(data
                                    .getBytes());
                            getencryptdata = Base64.encodeToString(
                                    encryptedText, Base64.DEFAULT);
                            Log.d("Encrypt Data", getencryptdata);
                            IMPOSImpl impos1;
                            IMPOS impos = impos1 = new IMPOSImpl(
                                    CapturePayments.this,
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
                                                    CapturePayments.this);
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
                                                    CapturePayments.this);
                                            // builder.setTitle("AaramOn");
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

                    } else {
                        Toast.makeText(CapturePayments.this,
                                getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
        cancel_btn.setOnClickListener(new OnClickListener() {
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void didScan(ScanSession scanSession) {
        String message = "";

        for (Barcode code : scanSession.getNewlyRecognizedCodes()) {
            String data = code.getData();
            // truncate code to certain length
            String cleanData = data;
            if (data.length() > 30) {
                cleanData = data.substring(0, 25) + "[...]";
            }
            if (message.length() > 0) {
                message += "\n\n\n";
            }
            message += cleanData;
        }
        barcode = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (barcode.length() > 0) {
                    try {
                        MediaPlayer mp;
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.barcodescanner);
                        mp.setVolume(3, 3);
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                // TODO Auto-generated method stub
                                mp.reset();
                                mp.release();
                                mp = null;
                            }
                        });
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                        et_scancode.setText("" + barcode);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        Intent intent = new Intent();
//        intent.putExtra(
//                "SuccessValue",
//                "Success");
//        setResult(
//                RESULT_OK,
//                intent);
        finish();
    }
}