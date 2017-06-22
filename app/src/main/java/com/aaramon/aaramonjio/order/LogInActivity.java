package com.aaramon.aaramonjio.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.Constant;
import com.aaramon.aaramonjio.controller.IScreenView;
import com.aaramon.aaramonjio.controller.UIController;
import com.aaramon.aaramonjio.controller.UIMessage;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.ConnectivityUtils;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.HttpJsonThread;
import com.aaramon.aaramonjio.dataaccess.NameValuePairClass;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.google.gson.JsonArray;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.crypto.Cipher;

public class LogInActivity extends Activity implements OnClickListener,
        IScreenView, Constant {

    private EditText etCode;
    private EditText etPwd;
    private Button btnSignIn;
    private SharedPreference_Main sharedPreference;
    private ProgressDialog progressDialog;
    String data = "";
    String getmerchantdata = "";
    Cipher cipher;
    String decryptedData = "";
    //    View v;
    String store_latitude = "", store_longitude = "";

    @Override
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
        setContentView(R.layout.login);
//        v = View.inflate(this.getApplicationContext(), R.layout.login, null);

        progressDialog = new ProgressDialog(this);

        sharedPreference = new SharedPreference_Main(this);

        etCode = (EditText) findViewById(R.id.et_code);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        btnSignIn = (Button) findViewById(R.id.btn_signin);

        etPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    if (!ConnectivityUtils.isNetworkEnabled(LogInActivity.this)) {
                        Toast.makeText(
                                LogInActivity.this,
                                getResources()
                                        .getString(R.string.network_check),
                                Toast.LENGTH_SHORT).show();
                        // return;
                    }
                    if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
                        Toast.makeText(LogInActivity.this, getResources().getString(R.string.enteraaramshopcode),
                                Toast.LENGTH_SHORT).show();
                        // return;
                    }
                    if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                        Toast.makeText(LogInActivity.this,
                                getResources().getString(R.string.enterpassword), Toast.LENGTH_SHORT)
                                .show();
                        // return;
                    }
                    if (DataStatic
                            .isInternetAvailable(getApplicationContext())) {
                        getSignIn();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.internetnotavailable),
                                Toast.LENGTH_SHORT).show();
                    }

                }
                // TODO Auto-generated method stub
                return false;
            }
        });

        btnSignIn.setOnClickListener(this);

        setFont();


    }

    private void setFont() {
        etCode.setTypeface(WidgetProperties.setTextTypefaceRobotoMedium(this));
        etPwd.setTypeface(WidgetProperties.setTextTypefaceRobotoMedium(this));
        btnSignIn.setTypeface(WidgetProperties.setTextTypefaceRobotoBold(this));
    }

    private ArrayList<NameValuePair> signInParameters() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new NameValuePairClass("aaramshopId", etCode
                .getText().toString().trim()));
        nameValuePairs.add(new NameValuePairClass("deviceId", sharedPreference
                .getDeviceId()));
        nameValuePairs.add(new NameValuePairClass("deviceType", "2"));
        nameValuePairs.add(new NameValuePairClass("password", etPwd.getText()
                .toString().trim()));
        Log.e("Request", nameValuePairs.toString());
        return nameValuePairs;
    }

    private void getSignIn() {

        ArrayList<NameValuePair> nameValuePairs = signInParameters();

        HttpJsonThread httpJson = new HttpJsonThread(LOGIN_SUCCESS,
                LOGIN_FAILED, sharedPreference.getbaseurl() + "login",
                nameValuePairs);

        httpJson.start();
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_signin: {

                if (!ConnectivityUtils.isNetworkEnabled(LogInActivity.this)) {
                    Toast.makeText(LogInActivity.this,
                            getResources().getString(R.string.network_check),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
                    Toast.makeText(LogInActivity.this, getResources().getString(R.string.enteraaramshopcode),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    Toast.makeText(LogInActivity.this, getResources().getString(R.string.enterpassword),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                getSignIn();
            }

            break;

            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        UIController.getController().setiScreenView(this);
    }

    @Override
    public boolean updateScreen(UIMessage message) {

        if (message.getCommand() == LOGIN_SUCCESS) {

            String msg = (String) message.getScreenData();
            Log.e("Existing user : ", "" + msg);

            try {

                JSONObject jsonO = new JSONObject(msg);

                if (jsonO.getString("status").equals("1")) {

                    String store_id = jsonO.getString("store_id");
                    sharedPreference.setStoreId(store_id);

                    String mobile_verified = jsonO.getString("mobile_verified");
                    String isValid = jsonO.getString("isValid");
                    String messageFrmSrvr = jsonO.getString("message");
                    String store_code = jsonO.getString("store_code");
                    String store_image = jsonO.getString("store_image");
                    //String chat_username ="14007215046706";// jsonO.getString("chat_username");
                    String chat_username = jsonO.getString("chat_username");
                    store_latitude = jsonO.getString("store_latitude");
                    store_longitude = jsonO.getString("store_longitude");
                    sharedPreference
                            .setstorelatitudegloballogin(store_latitude);
                    sharedPreference
                            .setstorelongitudegloballogin(store_longitude);

                    try {
                        String is_jio = jsonO.getString("is_jio");
                        sharedPreference.setisjio(is_jio);
                        String tid = jsonO.getString("tid");
                        sharedPreference.settid(tid);
                        String mid = jsonO.getString("mid");
                        sharedPreference.setmid(mid);
                        String token = jsonO.getString("token");
                        sharedPreference.setdatastored(token);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    try {
                        String country = jsonO.getString("store_country");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
//                    try {
//                        String country = jsonO.getString("country");
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
                    String mobile = jsonO.getString("store_mobile");
                    String store_name = jsonO.getString("store_name");
                    String full_name = jsonO.getString("fullName");
                    String email_id = jsonO.getString("email_id");
                    String distance = jsonO.getString("distance");
                    String home_delivery = jsonO.getString("homeDelivery");
                    String store_address = jsonO.getString("store_address");
                    String locality = jsonO.getString("locality");
                    String city = jsonO.getString("store_city");
                    String state = jsonO.getString("store_state");
                    String pincode = jsonO.getString("store_pincode");
                    String image_url_320 = jsonO.getString("image_url_100");
                    String gst_register = jsonO.getString("GSTINRegistered");
                    JSONObject GSTJson = jsonO.getJSONObject("GSTINDetails");
                    String get_business_name = GSTJson.getString("BusinessName");
                    String get_gst_tin = GSTJson.getString("GSTIN");
                    String get_gst_category_id="";
                    JSONArray gst_category = GSTJson.getJSONArray("GSTCategory");
                    String get_gst_category_name = "";
                    for (int i = 0; i < gst_category.length(); i++) {
                        JSONObject category_json = gst_category.getJSONObject(i);
                        //if (get_gst_category_id.equalsIgnoreCase(category_json.getString("GSTDealerCategoryId"))) {
                            if (category_json.getString("IsSelected").equalsIgnoreCase("1")) {
                                get_gst_category_name = category_json.getString("GSTDealerCategoryName");
                                get_gst_category_id=category_json.getString("GSTDealerCategoryId");
                            }
                       // }
                    }


                    JSONArray gst_business = GSTJson.getJSONArray("BusinessCategory");
                    String get_gst_business_name = "",get_gst_business_id="";
                    for (int i = 0; i < gst_business.length(); i++) {
                        JSONObject business_json = gst_business.getJSONObject(i);
                        if (business_json.getString("IsSelected").equalsIgnoreCase("1")) {
                            get_gst_business_name = business_json.getString("GSTBusinessCategoryName");
                            get_gst_business_id=business_json.getString("GSTBusinessCategoryId");
                        }
                    }




                    if (get_business_name != "" && get_business_name != null) {
                        sharedPreference.setGSTBusinessCategoryName(get_business_name);
                    }


                    if (gst_register != "" && gst_register != null) {
                        sharedPreference.setGST_REG(gst_register);
                    }

                    if (get_gst_tin != "" && get_gst_tin != null) {
                        sharedPreference.setGSTIN(get_gst_tin);
                    }
                    if (get_gst_category_name != "" && get_gst_category_name != null) {
                        sharedPreference.setGSTDealerCategoryName(get_gst_category_name);
                    }

                    if (get_gst_category_id != "" && get_gst_category_id != null) {
                        sharedPreference.setGSTCategoryId(get_gst_category_id);
                    }

                    if (get_gst_business_id != "" && get_gst_business_id != null) {
                        sharedPreference.setGSTBusinessCategoryId(get_gst_business_id);
                    }

                    if (get_gst_business_name != "" && get_gst_business_name != null) {
                        sharedPreference.setGSTBusinessCategoryName(get_gst_business_name);
                    }

                    String get_stateid=GSTJson.getString("StateId");
                    if (get_stateid != "" && get_stateid != null) {
                        sharedPreference.setStateId(get_stateid);
                    }

                    if (store_id != "" && store_id != null) {
                        sharedPreference.setStoreId(store_id);
                    }



                    if (store_id != "" && store_id != null) {
                        sharedPreference.setStoreId(store_id);
                    }

                    if (image_url_320 != null && image_url_320 != "") {
                        sharedPreference.setImageUrl320(image_url_320);
                    }

                    if (store_code != null && store_code != "") {
                        sharedPreference.setStoreCode(store_code);
                    }
                    if (store_image != null && store_image != "") {
                        sharedPreference.setStoreImage(store_image);
                    }
                    if (chat_username != null && chat_username != "") {
                        sharedPreference.setChatUserName(chat_username);

                    }
                    if (mobile_verified != null && mobile_verified != "") {
                        sharedPreference.setMobileVerified(Integer
                                .parseInt(mobile_verified));
                    }
                    if (mobile != null && mobile != "") {
                        sharedPreference.setMobile(mobile);
                    }
                    if (store_name != null && store_name != "") {
                        sharedPreference.setStoreName(store_name);
                    }
                    if (full_name != null && full_name != "") {
                        sharedPreference.setFullName(full_name);
                    }
                    if (email_id != null && email_id != "") {
                        sharedPreference.setEmailId(email_id);
                    }
                    if (distance.length() > 0) {
                        sharedPreference.setDistance(distance);
                    }
                    if (home_delivery != null && home_delivery != "") {
                        sharedPreference.setHomeDelivery(home_delivery);
                    }
                    if (store_address != null && store_address != "") {
                        sharedPreference.setStoreAddress(store_address);
                    }
                    if (locality != null && locality != "") {
                        sharedPreference.setLocality(locality);
                    }
                    if (city != null && city != "") {
                        sharedPreference.setCity(city);
                    }
                    if (state != null && state != "") {
                        sharedPreference.setState(state);
                    }
                    if (pincode != null && pincode != "") {
                        sharedPreference.setPincode(pincode);
                    }


                    Intent intent = new Intent(LogInActivity.this,
                            MainDashboard.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LogInActivity.this,
                            "" + jsonO.getString("message"), Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            return true;
        }

        if (message.getCommand() == LOGIN_FAILED) {
            Toast.makeText(LogInActivity.this,
                    getResources().getString(R.string.network_check),
                    Toast.LENGTH_SHORT).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            return true;
        }
        return false;
    }

    // private void updateJioToken(final String newToken, final String oldToken)
    // {
    // // TODO Auto-generated method stub
    //
    // StringRequest sr = new StringRequest(Request.Method.POST,
    // "http://www.aaramon.com:80/api/index.php/web/updateJioToken",
    // new Response.Listener<String>() {
    // @Override
    // public void onResponse(String response) {
    // Log.e("response", "" + response.toString());
    //
    // try {
    // JSONObject jsonO = new JSONObject(response);
    //
    // if (jsonO.getString("status").equals("1")) {
    // Log.e("response", "" + response.toString());
    // Toast.makeText(getApplicationContext(),
    // "" + jsonO.getString("message"),
    // Toast.LENGTH_SHORT).show();
    // } else {
    // Toast.makeText(getApplicationContext(),
    // "" + jsonO.getString("message"),
    // Toast.LENGTH_SHORT).show();
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // }
    // }, new Response.ErrorListener() {
    // @Override
    // public void onErrorResponse(VolleyError error) {
    // // pDialog.hide();
    // VolleyLog.e("getStoreCustomers",
    // "Error: " + error.getMessage());
    // }
    // }) {
    // @Override
    // protected Map<String, String> getParams() {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("mid", "" + sharedPreference.getmid());
    // params.put("tid", "" + sharedPreference.gettid());
    // params.put("oldToken",""+oldToken );
    // params.put("newToken", "" + newToken);
    //
    // return params;
    // }
    // };
    //
    // AppController.getInstance().addToRequestQueue(sr);
    // }

}
