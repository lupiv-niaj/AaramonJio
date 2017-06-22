package com.aaramon.aaramonjio.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.Constant;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerDueDetails extends Activity implements Constant {

    TextView tv_due_since, tv_order_number, tv_total_order_amount,
            tv_paid_cash, tv_balance, tv_back, tv_done;
    String customer_id = "", name = "", amount = "", order_id = "";
    private SharedPreference_Main sharedPreference;
    private ListView lst_dues_details;
    private String totOrdAmt = "", cust_name = "", orderIdApi = "",
            captureMoneyAPI = "";
    String start_dt = "", end_dt = "";
    private CstomerDuesDetailsAdapter custAdapter;
    private ArrayList<CustomerDuesDetailModel> cstModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.customer_due_detail);

        initView();

        cstModel = new ArrayList<CustomerDuesDetailModel>();

        custAdapter = new CstomerDuesDetailsAdapter(getApplicationContext(),
                cstModel);

        lst_dues_details.setAdapter(custAdapter);

        tv_done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				//
//				if (orderIdApi.contains(",")) {
//					orderIdApi = orderIdApi.substring(0,
//							orderIdApi.length() - 1);
//				}

                // if (captureMoneyAPI.contains(",")) {
                // captureMoneyAPI = captureMoneyAPI.substring(0,
                // captureMoneyAPI.length() - 1);
                // }
                String rs = "";
                orderIdApi = "";
                for (int k = 0; k < cstModel.size(); k++) {
                    Log.e("Capture Money", cstModel.get(k).getCapture_money());
                    if (Double.parseDouble(cstModel.get(k).getTv_balance()) < Double
                            .parseDouble(cstModel.get(k).getCapture_money())) {
                        Toast.makeText(CustomerDueDetails.this,
                                getResources().getString(R.string.validamount),
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (Double.parseDouble(cstModel.get(k).getCapture_money()) <= 0.00) {

                        } else {

                            orderIdApi = orderIdApi + cstModel.get(k).get_order_id() + ",";
                            rs = rs + cstModel.get(k).getCapture_money() + ",";
                        }
                    }
                }
                if (rs.equals("")) {
                    Toast.makeText(CustomerDueDetails.this, getResources().getString(R.string.totaldueblank), Toast.LENGTH_LONG).show();
                } else {
                    if (rs.contains(","))
                        Log.d("Capture Money", rs.toString());
                    Log.d("Order Id", rs.toString());

                    rs = rs.substring(0, rs.length() - 1);
                    orderIdApi = orderIdApi.substring(0, orderIdApi.length() - 1);
                    captureMoneyAPI = rs;
                    sentCaptureMoney();
                }
                // Toast.makeText(CustomerDueDetails.this, captureMoneyAPI,
                // 5000)
                // .show();

            }
        });

        tv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent i=new Intent(CustomerDueDetails.this,DashboardActivity.class);
//                i.putExtra("from","wallet");
//                startActivity(i);
                finish();
            }
        });

        sharedPreference = new SharedPreference_Main(getApplicationContext());

        Intent bundle = getIntent();
        customer_id = bundle.getStringExtra("shopper_id");
        // tv_name.setText("" + bundle.getStringExtra("name"));
        cust_name = bundle.getStringExtra("name");
        // tv_due_amount.setText("Rs. " + bundle.getStringExtra("amount"));
        order_id = bundle.getStringExtra("order_id");
        start_dt = bundle.getStringExtra("strdt");
        end_dt = bundle.getStringExtra("enddt");
        getCustomerDueDetailApi();
    }

    private void sentCaptureMoney() {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(
                CustomerDueDetails.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                sharedPreference.getbaseurl() + "sentCaptureMoney",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();

                        try {
                            JSONObject jsonO = new JSONObject(response);

                            if (jsonO.getString("status").equals("1")) {

                                Toast.makeText(getApplicationContext(),
                                        "" + jsonO.getString("message"),
                                        Toast.LENGTH_SHORT).show();
//                                Intent i=new Intent(CustomerDueDetails.this,DashboardActivity.class);
//                                i.putExtra("from","wallet");
//                                startActivity(i);
                                finish();

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
                pDialog.hide();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", sharedPreference.getDeviceId());
                params.put("deviceType", "2");
                params.put("order_id", orderIdApi);
                params.put("capture_money", "" + captureMoneyAPI);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);

    }

    private void getCustomerDueDetailApi() {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(
                CustomerDueDetails.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                sharedPreference.getbase_inpk_url() + "MerchantWallets/getMerchatnCustomerBalanceDetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();

                        try {
                            Log.e("response", "" + response);
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                JSONArray jsondue_details = jsonO.getJSONArray("Data");

                                cstModel.clear();

                                for (int i = 0; i < jsondue_details.length(); i++) {

                                    JSONObject jsonOoffers = jsondue_details
                                            .getJSONObject(i);

                                    CustomerDuesDetailModel cddm = new CustomerDuesDetailModel();


                                    cddm.setTv_balance(jsonOoffers
                                            .getString("balance"));

                                    cddm.setTv_due_since(jsonOoffers
                                            .getString("due_since"));
                                    cddm.setTv_order_number(jsonOoffers
                                            .getString("order_number"));
                                    cddm.setTv_paid_cash("Rs: "
                                            + jsonOoffers
                                            .getString("paid_cash"));
                                    cddm.setTv_total_order_amount("Rs: "
                                            + jsonOoffers
                                            .getString("total_order_amount"));
                                    cddm.setCust_name(cust_name);
                                    cddm.setCapture_money("0.00");
                                    cddm.setCustomer_chatUserName(jsonOoffers
                                            .getString("shopper_chat_username"));
                                    cddm.setimage_url_320(jsonOoffers
                                            .getString("shopper_image"));
                                    cddm.setcustomer_image(jsonOoffers
                                            .getString("shopper_image"));
                                    cddm.set_order_id(jsonOoffers.getString("order_id"));
                                    totOrdAmt = jsonOoffers
                                            .getString("total_order_amount");

                                    orderIdApi = orderIdApi
                                            + jsonOoffers.getString("order_id")
                                            + ",";
                                    captureMoneyAPI = captureMoneyAPI
                                            + jsonOoffers
                                            .getString("capture_money")
                                            + ",";

                                    cstModel.add(cddm);

                                }
                                custAdapter.notifyDataSetChanged();

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
                pDialog.hide();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "2");
                params.put("merchant_store_id", sharedPreference.getStoreId());
                params.put("shopper_id", "" + customer_id);
                params.put("type", "2");
                params.put("start_date", start_dt);
                params.put("end_date", end_dt);
                Log.d("Request", params.toString());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);

    }

    private void initView() {
        // TODO Auto-generated method stub
        lst_dues_details = (ListView) findViewById(R.id.lst_dues_details);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_back = (TextView) findViewById(R.id.tv_back);
        // tv_name = (TextView) findViewById(R.id.tv_name);
        // tv_due_amount = (TextView) findViewById(R.id.tv_due_amount);
        // tv_balance_money = (EditText) findViewById(R.id.tv_balance_money);
        // tv_remind = (TextView) findViewById(R.id.tv_remind);

        // tv_remind.setTypeface(WidgetProperties
        // .setTextTypefaceRobotoRegular(getApplicationContext()));
        tv_done.setTypeface(WidgetProperties
                .setTextTypefaceRobotoRegular(getApplicationContext()));

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//        Intent i=new Intent(CustomerDueDetails.this,DashboardActivity.class);
//        i.putExtra("from","wallet");
//        startActivity(i);
        finish();
    }

}
