package com.aaramon.aaramonjio.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.Constant;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WalletAaramShopDetails extends Activity implements Constant {

    ImageView img_product, img_back;
    TextView tv_amount, tv_date, tv_name;
    WebView webV_coupon_redeem, webV_payment_status;
    SharedPreference_Main sharedPreference;
    String activityId = "", type = "", startdt = "", enddt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.wallet_aaramshop_details);

        sharedPreference = new SharedPreference_Main(getApplicationContext());

        initView();

        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        type = intent.getStringExtra("type");
        startdt = intent.getStringExtra("start_date");
        enddt = intent.getStringExtra("end_date");

        getAaramMoneyDetail();

        img_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				 Intent i=new Intent(WalletAaramShopDetails.this,DashboardActivity.class);
//				i.putExtra("from","wallet");
//				startActivity(i);
                finish();
            }
        });
    }

    private void getAaramMoneyDetail() {
        // TODO Auto-generated method stub
        String tag_json_obj = "getAaramMoneyDetail";

        final ProgressDialog pDialog = new ProgressDialog(
                WalletAaramShopDetails.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                sharedPreference.getbase_inpk_url() +"MerchantWallets/getMerchantAaramMoneyDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                try {
                    JSONObject jsonO = new JSONObject(response);

                    JSONObject controls = jsonO.getJSONObject("Control");
                    if (controls.getString("Status").equals("1")) {
                        JSONObject json_data = jsonO.getJSONObject("Data");

                        String brand_name = json_data.getString("brand_name");
                        String image = json_data.getString("image");
                        String start_date = json_data.getString("start_date");
                        String end_date = json_data.getString("end_date");
                        String amount = json_data.getString("amount");
                        String table1 = json_data.getString("table1");
                        String table2 = json_data.getString("table2");

                        Picasso.with(getApplicationContext()).load(image)
                                .into(img_product);
                        tv_name.setText(brand_name);
                        tv_amount.setText(amount);

                        tv_date.setText(start_date + " to " + end_date);

                        webV_coupon_redeem.loadData(table1, "text/html",
                                "UTF-8");
                        webV_coupon_redeem.getSettings().setJavaScriptEnabled(true);
                        webV_payment_status.loadData(table2, "text/html",
                                "UTF-8");
                        webV_payment_status.getSettings().setJavaScriptEnabled(true);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "" + controls.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "2");
                params.put("merchant_store_id", sharedPreference.getStoreId());
                params.put("activity_id", "" + activityId);
                params.put("type", type);
                params.put("start_date", startdt);
                params.put("end_date", enddt);
                Log.d("Response", params.toString());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }

    private void initView() {
        // TODO Auto-generated method stub
        img_product = (ImageView) findViewById(R.id.img_product);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_date = (TextView) findViewById(R.id.tv_date);
        webV_coupon_redeem = (WebView) findViewById(R.id.webV_coupon_redeem);
        webV_payment_status = (WebView) findViewById(R.id.webV_payment_status);
        img_back = (ImageView) findViewById(R.id.img_back);
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MMM-yyyy", cal).toString();
        return date;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//		Intent i=new Intent(WalletAaramShopDetails.this,DashboardActivity.class);
//		i.putExtra("from","wallet");
//		startActivity(i);
        finish();
    }

}
