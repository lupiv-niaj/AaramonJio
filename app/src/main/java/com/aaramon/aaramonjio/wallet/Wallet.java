package com.aaramon.aaramonjio.wallet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Wallet extends Activity implements
        Constant {
    SharedPreference_Main sharedPreference;
    SimpleDateFormat dateFormatter;
    Calendar from_calender, to_calender;
    TextView txt_aarammoney_fromdate, txt_aarammoney_todate, txt_aarammoney_datebtn, txt_aarammoney_payment_amount, txt_aarammoney_outstanding_amount, txt_aarammoney_advance_amount, txt_aarammoney_customerdue_amount, tv_cust_due_text, tv_aaramshop_money;
    ;
    ImageView img_aarammmoney_payment, img_aarammmoney_outstanding, img_balance_advance, img_balance_customerdue, img_customer_dues, img_aaramshop_money;
    ListView list_payment, list_outstanding, list_advance, list_customerdue;
    String From_date, to_date;
    RelativeLayout rl_aarammoney_payment, rl_aarammoney_outstanding, rl_balance_advance, rl_balance_customerdue;
    private ArrayList<AaramMoneyModel> Aarammoney_payment_model;
    private AaramMoneyOutstandingAdapter Aarammoney_outstanding_adapter;
    private ArrayList<AaramMoneyOutstandingModel> Aarammoney_outstanding_model;
    private AaramMoneyAdapter Aarammoney_payment_adapter;
    int countpayment = 0, countoutstanding = 0, countadvance = 0, countcustomerdue = 0;
    LinearLayout ll_aarammoney, ll_balance, ll_header_money, ll_header_balance;

    private CustomerDueAdapter customer_due_adapter;
    private ArrayList<CustomerAdvanceModel> Balance_advance_model;
    private CustomerAdvanceAdapter customer_Advance_adapter;
    private ArrayList<CustomerBalanceModel> Balance_due_model;
    ImageView imageView2;

    public Wallet() {

    }

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
        setContentView(R.layout.wallet_fragment);
        sharedPreference = new SharedPreference_Main(Wallet.this);
        dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        to_calender = Calendar.getInstance();
        from_calender = Calendar.getInstance();
        initView();
        txt_aarammoney_todate.setText(dateFormatter.format(to_calender.getTime()) + "");
        from_calender.add(Calendar.MONTH, -1);
        txt_aarammoney_fromdate.setText(dateFormatter.format(from_calender.getTime()) + "");

        Aarammoney_payment_model = new ArrayList<AaramMoneyModel>();
        Aarammoney_payment_adapter = new AaramMoneyAdapter(Wallet.this,
                Aarammoney_payment_model, txt_aarammoney_fromdate, txt_aarammoney_todate);

        Aarammoney_outstanding_model = new ArrayList<AaramMoneyOutstandingModel>();
        Aarammoney_outstanding_adapter = new AaramMoneyOutstandingAdapter(
                Wallet.this, Aarammoney_outstanding_model, txt_aarammoney_fromdate, txt_aarammoney_todate);


        Balance_advance_model = new ArrayList<CustomerAdvanceModel>();
        customer_Advance_adapter = new CustomerAdvanceAdapter(Wallet.this,
                Balance_advance_model);

        Balance_due_model = new ArrayList<CustomerBalanceModel>();
        customer_due_adapter = new CustomerDueAdapter(
                Wallet.this, Balance_due_model, txt_aarammoney_fromdate, txt_aarammoney_todate);
        txt_aarammoney_datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_date = txt_aarammoney_todate.getText().toString();
                From_date = txt_aarammoney_fromdate.getText().toString();
                list_customerdue.setVisibility(View.GONE);
                list_outstanding.setVisibility(View.GONE);
                list_payment.setVisibility(View.GONE);
                list_advance.setVisibility(View.GONE);
                img_aarammmoney_outstanding.setBackgroundResource(R.drawable.home_detail_addition_circle);
                img_aarammmoney_payment.setBackgroundResource(R.drawable.home_detail_addition_circle);
                img_balance_advance.setBackgroundResource(R.drawable.home_detail_addition_circle);
                img_balance_customerdue.setBackgroundResource(R.drawable.home_detail_addition_circle);
                Date date1 = null, date2 = null;
                try {
                    date1 = dateFormatter.parse(From_date);
                    date2 = dateFormatter.parse(to_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date2.compareTo(date1) < 0) {
                    Toast.makeText(Wallet.this,
                            getResources().getString(R.string.lessthandate),
                            Toast.LENGTH_LONG).show();
                } else {
                    getAaramMoney(From_date, to_date);
                    get_Balance(From_date, to_date);
                }
            }
        });
        txt_aarammoney_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                        Wallet.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txt_aarammoney_fromdate.setText(dateFormatter.format(newDate
                                .getTime()));
                        From_date = txt_aarammoney_fromdate.getText().toString();
                    }

                }, to_calender.get(Calendar.YEAR), to_calender
                        .get(Calendar.MONTH), to_calender
                        .get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getDatePicker().setMaxDate(
                        to_calender.getTimeInMillis());
                fromDatePickerDialog.show();

            }
        });
        txt_aarammoney_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog toDatePickerDialog = new DatePickerDialog(
                        Wallet.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txt_aarammoney_todate.setText(dateFormatter.format(newDate
                                .getTime()));
                        to_date = txt_aarammoney_todate.getText().toString();
                    }
                }, to_calender.get(Calendar.YEAR), to_calender
                        .get(Calendar.MONTH), to_calender
                        .get(Calendar.DAY_OF_MONTH));
                toDatePickerDialog.getDatePicker().setMaxDate(
                        to_calender.getTimeInMillis());
                toDatePickerDialog.show();
            }
        });
//        ll_header_money.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_aaramshop_money.setTextColor(Color.parseColor("#FFFFFF"));
//                tv_cust_due_text.setTextColor(Color.parseColor("#EACEB2"));
//                img_customer_dues
//                        .setBackgroundResource(R.drawable.dues_money_icon_inactive);
//                img_aaramshop_money
//                        .setBackgroundResource(R.drawable.aaram_money_icon_active);
//                ll_aarammoney.setVisibility(View.VISIBLE);
//                ll_balance.setVisibility(View.GONE);
//
//            }
//        });
        ll_header_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_aaramshop_money.setTextColor(Color.parseColor("#EACEB2"));
                tv_cust_due_text.setTextColor(Color.parseColor("#FFFFFF"));
                img_customer_dues
                        .setBackgroundResource(R.drawable.dues_money_icon_active);
                img_aaramshop_money
                        .setBackgroundResource(R.drawable.aaram_money_icon_inactive);
                ll_aarammoney.setVisibility(View.GONE);
                ll_balance.setVisibility(View.VISIBLE);
            }
        });
        rl_aarammoney_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Aarammoney_payment_model.size() > 0) {
                    if (countpayment == 0) {
                        img_aarammmoney_payment.setBackgroundResource(R.drawable.minus_circle);
                        list_payment.setVisibility(View.VISIBLE);
                        list_outstanding.setVisibility(View.GONE);
                        img_aarammmoney_outstanding.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        countpayment++;
                    } else {
                        img_aarammmoney_payment.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        list_payment.setVisibility(View.GONE);
                        countpayment = 0;
                    }
                }
            }
        });
        rl_aarammoney_outstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Aarammoney_outstanding_model.size() > 0) {
                    if (countoutstanding == 0) {
                        img_aarammmoney_outstanding.setBackgroundResource(R.drawable.minus_circle);
                        list_outstanding.setVisibility(View.VISIBLE);
                        list_payment.setVisibility(View.GONE);
                        img_aarammmoney_payment.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        countoutstanding++;
                    } else {
                        img_aarammmoney_outstanding.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        list_outstanding.setVisibility(View.GONE);
                        countoutstanding = 0;
                    }
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });

        rl_balance_customerdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Balance_due_model.size() > 0) {
                    if (countcustomerdue == 0) {
                        img_balance_customerdue.setBackgroundResource(R.drawable.minus_circle);
                        list_customerdue.setVisibility(View.VISIBLE);
                        list_advance.setVisibility(View.GONE);
                        img_balance_advance.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        countcustomerdue++;
                    } else {
                        img_balance_customerdue.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        list_customerdue.setVisibility(View.GONE);
                        countcustomerdue = 0;
                    }
                }
            }
        });
        rl_balance_advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Balance_advance_model.size() > 0) {
                    if (countadvance == 0) {
                        img_balance_advance.setBackgroundResource(R.drawable.minus_circle);
                        list_advance.setVisibility(View.VISIBLE);
                        list_customerdue.setVisibility(View.GONE);
                        img_balance_customerdue.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        countadvance++;
                    } else {
                        img_balance_advance.setBackgroundResource(R.drawable.home_detail_addition_circle);
                        list_advance.setVisibility(View.GONE);
                        countadvance = 0;
                    }
                }
            }
        });
    }


    protected void getAaramMoney(final String dFrom2, final String dTo2) {
        // TODO Auto-generated method stub

        final ProgressDialog pDialog = new ProgressDialog(Wallet.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                sharedPreference.getbase_inpk_url() + "MerchantWallets/getMerchantAaramMoneyNew",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        Log.e("response", "" + response.toString());

                        try {
                            Aarammoney_payment_model.clear();

                            JSONObject jsonO = new JSONObject(response);

                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                JSONObject json_data = jsonO.getJSONObject("Data");
                                String payment_done_total_amount = json_data
                                        .getString("total_payment_done");
                                String outstanding_total_amount = json_data
                                        .getString("total_outstanding");
                                txt_aarammoney_outstanding_amount.setText("Rs: "
                                        + outstanding_total_amount);
                                txt_aarammoney_payment_amount.setText("Rs: "
                                        + payment_done_total_amount);
                                JSONArray payment_data_array = json_data.getJSONArray("payment_done");

                                for (int i = 0; i < payment_data_array.length(); i++) {

                                    JSONObject json_payment = payment_data_array
                                            .getJSONObject(i);

                                    AaramMoneyModel cdm = new AaramMoneyModel();
                                    cdm.setActivity_id(json_payment
                                            .getString("activity_id"));
                                    cdm.setActivity_name(json_payment
                                            .getString("activity_name"));
                                    cdm.setAmount(json_payment
                                            .getString("amount"));
                                    cdm.setBrand_name(json_payment
                                            .getString("brand_name"));
                                    cdm.setEnd_date(json_payment
                                            .getString("end_date"));
                                    cdm.setImage(json_payment.getString("image"));

                                    Aarammoney_payment_model.add(cdm);

                                    list_payment
                                            .setAdapter(Aarammoney_payment_adapter);

                                    Aarammoney_payment_adapter
                                            .notifyDataSetChanged();
                                    // listview_hieght(Aarammoney_payment_adapter,list_payment);
                                }

                                Aarammoney_outstanding_model.clear();

                                JSONArray outstanding_data_array = json_data.getJSONArray("outstanding");


                                for (int i = 0; i < outstanding_data_array.length(); i++) {

                                    JSONObject json_outstanding = outstanding_data_array
                                            .getJSONObject(i);

                                    AaramMoneyOutstandingModel cdm = new AaramMoneyOutstandingModel();

                                    cdm.setActivity_id(json_outstanding
                                            .getString("activity_id"));
                                    cdm.setActivity_name(json_outstanding
                                            .getString("activity_name"));
                                    cdm.setAmount(json_outstanding
                                            .getString("amount"));
                                    cdm.setBrand_name(json_outstanding
                                            .getString("brand_name"));
                                    cdm.setEnd_date(json_outstanding
                                            .getString("end_date"));
                                    cdm.setImage(json_outstanding.getString("image"));


                                    Aarammoney_outstanding_model.add(cdm);

                                    list_outstanding
                                            .setAdapter(Aarammoney_outstanding_adapter);
                                    Aarammoney_outstanding_adapter
                                            .notifyDataSetChanged();
                                }

                            } else {
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
                params.put("start_date", dFrom2);
                params.put("end_date", dTo2);
                Log.d("AaramMoney Request", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);

    }


    protected void get_Balance(final String dFrom2, final String dTo2) {
        // TODO Auto-generated method stub

        final ProgressDialog pDialog = new ProgressDialog(Wallet.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST,
                sharedPreference.getbase_inpk_url() + "MerchantWallets/getMerchantCustomerBalance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        Log.e("response", "" + response.toString());

                        try {
                            Balance_advance_model.clear();

                            JSONObject jsonO = new JSONObject(response);

                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                JSONObject json_data = jsonO.getJSONObject("Data");
                                String payment_done_total_amount = json_data
                                        .getString("total_advance");
                                String outstanding_total_amount = json_data
                                        .getString("total_outstanding");
                                txt_aarammoney_customerdue_amount.setText("Rs: "
                                        + outstanding_total_amount);
                                txt_aarammoney_advance_amount.setText("Rs: "
                                        + payment_done_total_amount);
                                JSONArray payment_data_array = json_data.getJSONArray("advance");

                                for (int i = 0; i < payment_data_array.length(); i++) {

                                    JSONObject json_payment = payment_data_array
                                            .getJSONObject(i);


                                    CustomerAdvanceModel cdm = new CustomerAdvanceModel();
                                    cdm.set_shopper_id(json_payment
                                            .getString("shopper_id"));
                                    cdm.set_shopper_name(json_payment
                                            .getString("shopper_name"));
                                    cdm.set_shopper_image(json_payment
                                            .getString("shopper_image"));
                                    cdm.setOrder_id(json_payment
                                            .getString("order_id"));
                                    cdm.set_order_date(json_payment
                                            .getString("order_date"));
                                    cdm.set_outstanding_amount(json_payment.getString("outstanding_amount"));

                                    cdm.set_type(Integer.parseInt(json_payment.getString("type")));

                                    Balance_advance_model.add(cdm);

                                    list_advance
                                            .setAdapter(customer_Advance_adapter);

                                    customer_Advance_adapter
                                            .notifyDataSetChanged();
                                    // listview_hieght(Aarammoney_payment_adapter,list_payment);
                                }

                                Balance_due_model.clear();

                                JSONArray outstanding_data_array = json_data.getJSONArray("outstanding");


                                for (int i = 0; i < outstanding_data_array.length(); i++) {

                                    JSONObject json_outstanding = outstanding_data_array
                                            .getJSONObject(i);

                                    CustomerBalanceModel cdm = new CustomerBalanceModel();
                                    cdm.set_shopper_id(json_outstanding
                                            .getString("shopper_id"));
                                    cdm.set_shopper_name(json_outstanding
                                            .getString("shopper_name"));
                                    cdm.set_shopper_image(json_outstanding
                                            .getString("shopper_image"));
                                    cdm.setOrder_id(json_outstanding
                                            .getString("order_id"));
                                    cdm.set_order_date(json_outstanding
                                            .getString("order_date"));
                                    cdm.set_outstanding_amount(json_outstanding.getString("outstanding_amount"));

                                    cdm.set_type(Integer.parseInt(json_outstanding.getString("type")));

                                    Balance_due_model.add(cdm);

                                    list_customerdue
                                            .setAdapter(customer_due_adapter);

                                    customer_due_adapter
                                            .notifyDataSetChanged();
                                }

                            } else {
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
                params.put("start_date", dFrom2);
                params.put("end_date", dTo2);
                Log.d("AaramMoney Request", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);

    }


    private void initView() {
        // TODO Auto-generated method stub
        ll_aarammoney = (LinearLayout) findViewById(R.id.wallet_ll_aarammoney);
        txt_aarammoney_datebtn = (TextView) findViewById(R.id.wallet_aarammoney_date_btn);
        txt_aarammoney_fromdate = (TextView) findViewById(R.id.wallet_aarammoney_fromdate);
        txt_aarammoney_todate = (TextView) findViewById(R.id.wallet_aarammoney_todate);
        txt_aarammoney_outstanding_amount = (TextView) findViewById(R.id.wallet_aarammoney_outstanding_amount);
        txt_aarammoney_payment_amount = (TextView) findViewById(R.id.wallet_aarammoney_payment_amount);
        img_aarammmoney_payment = (ImageView) findViewById(R.id.wallet_expand_payment);
        img_aarammmoney_outstanding = (ImageView) findViewById(R.id.wallet_expand_outstanding);
        list_outstanding = (ListView) findViewById(R.id.wallet_aarammoney_outstandinglist);
        list_payment = (ListView) findViewById(R.id.wallet_aarammmoney_paymentlist);
        rl_aarammoney_outstanding = (RelativeLayout) findViewById(R.id.wallet_rl_outstanding);
        rl_aarammoney_payment = (RelativeLayout) findViewById(R.id.wallet_rl_aarammoney_payment);


        txt_aarammoney_advance_amount = (TextView) findViewById(R.id.wallet_balance_advance_amount);
        txt_aarammoney_customerdue_amount = (TextView) findViewById(R.id.wallet_aarammoney_customerdue_amount);
        img_balance_advance = (ImageView) findViewById(R.id.wallet_expand_advance);
        img_balance_customerdue = (ImageView) findViewById(R.id.wallet_expand_customerdue);
        list_advance = (ListView) findViewById(R.id.wallet_advance_list);
        list_customerdue = (ListView) findViewById(R.id.wallet_customerdue_list);
        rl_balance_advance = (RelativeLayout) findViewById(R.id.wallet_rl_balance_advance);
        rl_balance_customerdue = (RelativeLayout) findViewById(R.id.wallet_rl_customer_due);
        ll_aarammoney = (LinearLayout) findViewById(R.id.aarammoney_layout);
        ll_balance = (LinearLayout) findViewById(R.id.balance_layout);
        ll_header_money = (LinearLayout) findViewById(R.id.wallet_ll_aarammoney);
        ll_header_balance = (LinearLayout) findViewById(R.id.wallet_ll_customerbalance);
        tv_cust_due_text = (TextView) findViewById(R.id.tv_cust_due_text);
        tv_aaramshop_money = (TextView) findViewById(R.id.tv_aaram_money_text);
        img_customer_dues = (ImageView) findViewById(R.id.img_customer_dues);
        img_aaramshop_money = (ImageView) findViewById(R.id.img_aaramshop_money);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        to_date = txt_aarammoney_todate.getText().toString();
        From_date = txt_aarammoney_fromdate.getText().toString();
        getAaramMoney(From_date, to_date);
//        get_Balance(From_date, to_date);

    }

}
