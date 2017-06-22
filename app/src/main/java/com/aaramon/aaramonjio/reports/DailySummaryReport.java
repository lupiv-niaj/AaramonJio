package com.aaramon.aaramonjio.reports;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
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
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.order.MainDashboard;
import com.aaramon.aaramonjio.order.ScanProducts;
import com.aaramon.aaramonjio.wallet.AaramMoneyAdapter;
import com.aaramon.aaramonjio.wallet.AaramMoneyModel;
import com.aaramon.aaramonjio.wallet.AaramMoneyOutstandingAdapter;
import com.aaramon.aaramonjio.wallet.AaramMoneyOutstandingModel;
import com.aaramon.aaramonjio.wallet.CustomerAdvanceAdapter;
import com.aaramon.aaramonjio.wallet.CustomerAdvanceModel;
import com.aaramon.aaramonjio.wallet.CustomerBalanceModel;
import com.aaramon.aaramonjio.wallet.CustomerDueAdapter;
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

public class DailySummaryReport extends Activity implements AbsListView.OnScrollListener {

    private ImageView img_back;
    private TextView tv_date, TotalAmountTextViewId, add_new_add, NoDailySummaryId;
    SimpleDateFormat dateFormatter,dateFormatter1;
    Calendar from_calender,from_calender1;
    ArrayList<DailySummaryreportListModel> rowItems;
    private ListView summaryList;
    SharedPreference_Main sharedPreference_Main;
    private SummaryAdapter adaptersummary;

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
        setContentView(R.layout.daily_sale_summary);
        sharedPreference_Main = new SharedPreference_Main(DailySummaryReport.this);
        img_back = (ImageView) findViewById(R.id.img_back_account);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        from_calender = Calendar.getInstance();
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(dateFormatter.format(from_calender.getTime()) + "");
        TotalAmountTextViewId = (TextView) findViewById(R.id.TotalAmountTextViewId);
        add_new_add = (TextView) findViewById(R.id.add_new_add);
        NoDailySummaryId = (TextView) findViewById(R.id.NoDailySummaryId);
        dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        from_calender1 = Calendar.getInstance();
        summaryList = (ListView) findViewById(R.id.listview_summary);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_new_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DailySummaryReport.this, ScanProducts.class);
                i.putExtra("from", "");
                i.putExtra("cartid", "");
                i.putExtra("OrderTypePage", "1");
                startActivity(i);
                finish();
            }
        });

        summaryList.setOnScrollListener(this);

    }
    int x = 0;
    boolean isloading;
    protected void onResume() {
        super.onResume();
        if (DataStatic.isInternetAvailable(DailySummaryReport.this)) {
            rowItems = new ArrayList<DailySummaryreportListModel>();
            x = 0;
            adaptersummary = new SummaryAdapter(DailySummaryReport.this, rowItems);
            summaryList.setAdapter(adaptersummary);
            getsummary(x);
        } else {
            Toast.makeText(DailySummaryReport.this,
                    getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void getsummary(final int x) {

        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "Order/getRetailerSales";
        //http://www.aaramshop.co.in/api/index.php/
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                sharedPreference_Main.getbase_inpk_url() + tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response ", response);
                            pDialog.cancel();
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                JSONObject json_data = jsonO.getJSONObject("Data");
                                String total_sale = json_data.getString("TotalSales");
                                TotalAmountTextViewId.setText("₹" + total_sale);
                                JSONArray jsonArray = json_data.getJSONArray("Sales");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject sales = jsonArray.getJSONObject(i);
                                    int OrderId = Integer.parseInt(sales.getString("OrderId"));
                                    String InvoiceNo = sales.getString("InvoiceNo");
                                    String OrderTiming = sales.getString("OrderTiming");
                                    String TotalAmount = sales.getString("TotalAmount");
                                    String PaymentType = sales.getString("PaymentType");
                                    int TotalItems=Integer.parseInt(sales.getString("TotalItems"));

                                    DailySummaryreportListModel dlm = new DailySummaryreportListModel();
                                    dlm.setOrderId(OrderId);
                                    dlm.setInvoiceNo(InvoiceNo);
                                    dlm.setOrderTiming(OrderTiming);
                                    dlm.setTotalAmount(TotalAmount);
                                    dlm.setPaymentType(PaymentType);
                                    dlm.setTotalItems(TotalItems);
                                    rowItems.add(dlm);
                                }
                                if(jsonArray.length()<=0 && x == 0)
                                {
                                    NoDailySummaryId.setVisibility(View.VISIBLE);
                                    summaryList.setVisibility(View.GONE);
                                }else{
                                    NoDailySummaryId.setVisibility(View.GONE);
                                    summaryList.setVisibility(View.VISIBLE);
                                }
                                if(jsonArray.length()<=0)
                                {
                                    isloading = false;
                                }
                                else {
                                    isloading = true;
                                    int currentPosition = summaryList.getLastVisiblePosition();
                                    Log.d("Current Position......", "" + currentPosition);
                                    adaptersummary.notifyDataSetChanged();
                                    summaryList.setSelectionFromTop(currentPosition, 0);
                                }
                            } else {
                                Toast.makeText(DailySummaryReport.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.cancel();
                pDialog.cancel();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceId", sharedPreference_Main.getDeviceId());
                params.put("DeviceType", "2");
                params.put("MerchantStoreId", sharedPreference_Main.getStoreId());
                params.put("PageNo", "" + x);
                params.put("StartDate", dateFormatter1.format(from_calender1.getTime()));
                params.put("EndDate", dateFormatter1.format(from_calender1.getTime()));
                //params.put("StartDate", "2017-06-25");
                //params.put("EndDate", "2017-06-25");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        Log.d("On Scroll", String.valueOf(view.getId()));
        switch (view.getId()) {
            case R.id.listview_summary:
                if (DataStatic.isInternetAvailable(DailySummaryReport.this)) {
                    int threshold = 1;
                    int count = summaryList.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (summaryList.getLastVisiblePosition() >= count - threshold
                                && x < 100 && (isloading)) {
                            x++;
                            Log.e("Last", "Last" + x);
                            getsummary(x);
                        }
                    }
                } else {
                    Toast.makeText(DailySummaryReport.this, getResources().getString(R.string.internetnotavailable),
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onScroll(AbsListView view, int i, int i1, int i2) {

    }
}
