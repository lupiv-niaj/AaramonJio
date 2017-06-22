package com.aaramon.aaramonjio.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.IMPOSImpl;
import com.aaramon.aaramonjio.dataaccess.ResponseHandler;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.merchant_gst.Merchant_register;
import com.aaramon.aaramonjio.reports.DailySummaryReport;
import com.aaramon.aaramonjio.supplier.SupplierList;
import com.aaramon.aaramonjio.syncproduct.GSTIN_StartService;
import com.aaramon.aaramonjio.syncproduct.QueryClass;
import com.aaramon.aaramonjio.syncproduct.StartService;
import com.aaramon.aaramonjio.wallet.Wallet;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.wallet.Payments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import impl.PlugNPlay;
import pojo.Environment;
import service.IMPOS;

public class MainDashboard extends Activity {
    public int Icon[] = {R.mipmap.ic_start_billing, R.mipmap.ic_record_sale};
    public String Text[] = {"Start Billing", "Record Sales"};
    public String TextDetail[] = {"Bill & Get Payments ", "Add all your non billing sales"};
    public String PendinOrderCount[] = {"", "", ""};
    SharedPreference_Main sharedPreference_Main;
    private MainDashboardAdapter DashboardAdapter;
    private ArrayList<MainDashboardModel> ItemsModel;
    ListView LVItems;
    TextView TVTodayDate, TVTotalEarningAmt, TVTotalOrderValue, TVOrderCompletedValue, TVOrderPendingValue;
    HorizontalScrollView more_option_horizontal;
    RelativeLayout more_option_layout;
    ImageView img_up_down;
    int sliding = 0;
    RelativeLayout daily_summary_report, map_layout, pay_layout,purchase_layout,daily_summary_layout;
    String getmerchantdata = "", Response = "";
    String Device_id = "";
    View v;
    String token;
    String data = "";
    SimpleDateFormat dateFormatter;
    String linkdate = "";
    ProgressDialog progress;
    int GST_register = 0;

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
        setContentView(R.layout.activity_main_dashboard);
        v = View.inflate(this.getApplicationContext(), R.layout.activity_main_dashboard, null);
        progress = new ProgressDialog(MainDashboard.this);

        img_up_down = (ImageView) findViewById(R.id.up_down_arrow);
        map_layout = (RelativeLayout) findViewById(R.id.map_layout);
        more_option_layout = (RelativeLayout) findViewById(R.id.more_options);
        more_option_horizontal = (HorizontalScrollView) findViewById(R.id.more_option_horizontal);
        pay_layout = (RelativeLayout) findViewById(R.id.pay_layout);
        daily_summary_report = (RelativeLayout) findViewById(R.id.wallet_layout);
        LVItems = (ListView) findViewById(R.id.LVItems);
        TVTodayDate = (TextView) findViewById(R.id.TVTodayDate);
        TVTotalEarningAmt = (TextView) findViewById(R.id.TVTotalEarningAmt);
        TVTotalOrderValue = (TextView) findViewById(R.id.TVTotalOrderValue);
        TVOrderCompletedValue = (TextView) findViewById(R.id.TVOrderCompletedValue);
        TVOrderPendingValue = (TextView) findViewById(R.id.TVOrderPendingValue);
        purchase_layout=(RelativeLayout)findViewById(R.id.purchase_layout);
        daily_summary_layout=(RelativeLayout)findViewById(R.id.daily_summary_layout);
        TVTotalEarningAmt.setTypeface(WidgetProperties.setTextTypefaceRobotoMedium(this));
        TVTotalOrderValue.setTypeface(WidgetProperties.setTextTypefaceRobotoMedium(this));
        TVOrderCompletedValue.setTypeface(WidgetProperties.setTextTypefaceRobotoMedium(this));
        TVOrderPendingValue.setTypeface(WidgetProperties.setTextTypefaceRobotoMedium(this));
        sharedPreference_Main = new SharedPreference_Main(MainDashboard.this);
        map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapmydevice();
            }
        });
        more_option_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliding == 0) {
                    sliding++;
                    img_up_down.setImageResource(R.mipmap.ic_down_chevron);
                    more_option_horizontal.setVisibility(View.VISIBLE);
                } else {
                    sliding--;
                    img_up_down.setImageResource(R.mipmap.ic_up_chevron);
                    more_option_horizontal.setVisibility(View.GONE);
                }
            }
        });
        LVItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (sharedPreference_Main.get_turnover().equalsIgnoreCase("2")) {
                        //Alert Message
                        final Dialog multipleBatch = new Dialog(MainDashboard.this);
                        multipleBatch.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        multipleBatch.setContentView(R.layout.layout_alert_dialog);
                        multipleBatch.setCanceledOnTouchOutside(true);
                        multipleBatch.setCancelable(false);
                        multipleBatch.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        multipleBatch.show();
                        final TextView text_message = (TextView) multipleBatch.findViewById(R.id.alert_message);
                        final Button send_btn = (Button) multipleBatch.findViewById(R.id.ad_send);
                        final Button cancel_btn = (Button) multipleBatch.findViewById(R.id.ad_cancel);
                        send_btn.setText("GSTN Registration");
                        cancel_btn.setText("Later");
                        text_message.setText("You have existed you turnover.Your sales transactions have been blocked Please update your GSTN Registration to continue sales.");
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                multipleBatch.dismiss();
                            }
                        });

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Intent i = new Intent(MainDashboard.this, Merchant_register.class);
//                                startActivity(i);
                                multipleBatch.dismiss();
                            }
                        });

                    } else {
                        Intent i = new Intent(MainDashboard.this, ScanProducts.class);
                        i.putExtra("from", "");
                        i.putExtra("cartid", "");
                        i.putExtra("OrderTypePage", "0");
                        startActivity(i);
                    }
                } else if (position == 1) {
                    Intent i = new Intent(MainDashboard.this, ScanProducts.class);
                    i.putExtra("from", "");
                    i.putExtra("cartid", "");
                    i.putExtra("OrderTypePage", "1");
                    startActivity(i);
                }
                /*else if (position == 2) {
                    Intent i = new Intent(MainDashboard.this, SupplierList.class);
                    startActivity(i);
                }*/
            }
        });
        daily_summary_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainDashboard.this, DailySummaryReport.class);
                startActivity(i);
            }
        });
        purchase_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainDashboard.this, SupplierList.class);
                startActivity(i);
            }
        });
        pay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.jio.bapp");
                if (intent != null) {
                    // We found the activity now start the activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Bring user to the market or let them choose an app?
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "com.jio.bapp"));
                    startActivity(intent);
                }
            }
        });
        daily_summary_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainDashboard.this, Wallet.class);
                startActivity(i);
            }
        });
        DatabaseHandlerService.InitializeDataBase(MainDashboard.this);
    }


    protected void onResume() {
        super.onResume();

        sharedPreference_Main = new SharedPreference_Main(MainDashboard.this);
        ItemsModel = new ArrayList<MainDashboardModel>();
        if (DataStatic.isInternetAvailable(MainDashboard.this)) {
            try {
                getDashboardDetails();
            } catch (Exception e) {
                Log.e("API", e.getMessage());
            }
        } else {
            TVTotalEarningAmt.setText("₹0.00");
            TVTotalOrderValue.setText("0");
            TVOrderCompletedValue.setText("0");
            TVOrderPendingValue.setText("0");
            TVTodayDate.setText(sharedPreference_Main.getStoreName());
            for (int i = 0; i < Text.length; i++) {
                MainDashboardModel Items = new MainDashboardModel();
                Items.setIcon(Icon[i]);
                Items.setText(Text[i]);
                Items.setTextDetail(TextDetail[i]);
                Items.setPendingOrderCount(PendinOrderCount[i]);
                ItemsModel.add(Items);
            }
            DashboardAdapter = new MainDashboardAdapter(getApplicationContext(), ItemsModel);
            LVItems.setAdapter(DashboardAdapter);
            sharedPreference_Main.setIsRegistered("true");
            Toast.makeText(MainDashboard.this, getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void getDashboardDetails() { // FROM API
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "MerchantStore/getStoreDashboard";
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, sharedPreference_Main.getbase_inpk_url()
                + tag_json_obj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response ", response);
                    pDialog.cancel();
                    JSONObject jsonO = new JSONObject(response);
                    JSONObject controls = jsonO.getJSONObject("Control");
                    if (controls.getString("Status").equals("1")) {
                        JSONObject json_data = jsonO.getJSONObject("Data");
                        TVTotalEarningAmt.setText("₹" + json_data.getString("TotalEarnings").toString());
                        TVTotalOrderValue.setText(json_data.getString("TotalOrders").toString());
                        TVOrderCompletedValue.setText(json_data.getString("OrderCompleted").toString());
                        TVOrderPendingValue.setText(json_data.getString("OrderPending").toString());
                        TVTodayDate.setText(json_data.getString("HeaderText").toString());
                        String PendinOrderCount[] = {"", "", json_data.getString("OrderPending").toString()};
                        ItemsModel.clear();
                        for (int i = 0; i < Text.length; i++) {
                            MainDashboardModel Items = new MainDashboardModel();
                            Items.setIcon(Icon[i]);
                            Items.setText(Text[i]);
                            Items.setTextDetail(TextDetail[i]);
                            Items.setPendingOrderCount(PendinOrderCount[i]);
                            ItemsModel.add(Items);
                        }
                        DashboardAdapter = new MainDashboardAdapter(getApplicationContext(), ItemsModel);
                        LVItems.setAdapter(DashboardAdapter);
                        sharedPreference_Main.setIsRegistered("true");
                        sharedPreference_Main.set_GSTDetail(response.toString());
                        GST_register = Integer.parseInt(json_data.getString("GSTINRegistered"));
                        sharedPreference_Main.setGST_REG(GST_register + "");
                        Intent serviceIntent = new Intent(MainDashboard.this, StartService.class);
                        startService(serviceIntent);
                        Intent serviceIntent1 = new Intent(MainDashboard.this, GSTIN_StartService.class);
                        startService(serviceIntent1);
                        if (GST_register == 1) {
                        } else {
                            Intent i = new Intent(MainDashboard.this, Merchant_register.class);
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(MainDashboard.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final Calendar from_calender = Calendar.getInstance();
                Map<String, String> params = new HashMap<String, String>();
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceId", sharedPreference_Main.getDeviceId());
                params.put("DeviceType", "2");
                params.put("MerchantStoreId", sharedPreference_Main.getStoreId());
                params.put("StartDate", sdf.format(from_calender.getTime()));
                params.put("EndDate", sdf.format(from_calender.getTime()));
                Log.d("Params", params.toString());
                return params;

            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    public void mapmydevice() {
        try {
            IMPOSImpl impos1;
            IMPOSImpl impos = impos1 = new IMPOSImpl(MainDashboard.this,
                    MainDashboard.this, v, "SEARCH");
            impos1.setHandler(new ResponseHandler() {
                public void notifyActivity(String response) {
                    try {
                        Device_id = response;
                        if (Device_id == null) {
                            Toast.makeText(MainDashboard.this, getResources().getString(R.string.deviceswitchoff), Toast.LENGTH_LONG).show();
                        } else if (Device_id.equalsIgnoreCase("")) {
                            Toast.makeText(MainDashboard.this,
                                    getResources().getString(R.string.dongleidnot), Toast.LENGTH_LONG).show();
                        } else {
                            sharedPreference_Main
                                    .setpaymentcarddeviceId(Device_id);
                            dongle_id(Device_id);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            PlugNPlay plugNPlay = PlugNPlay.getInstance(impos, MainDashboard.this);
            plugNPlay.getDongleId();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Error..........", e.toString());
        }
    }


    void dongle_id(final String device) {

        final Dialog login = new Dialog(MainDashboard.this);
        // Set GUI of login screen
        login.setContentView(R.layout.layout_custom_dialog_mapdevice);
        login.setTitle(getResources().getString(R.string.mappinpaddevice));
        login.setCanceledOnTouchOutside(false);
        login.setCancelable(false);

        // Init button of login GUI
        Button send_btn = (Button) login.findViewById(R.id.ad_mapdevice_btn);
        Button cancel_btn = (Button) login
                .findViewById(R.id.ad_mapdevice_cancel);
        final EditText edt_mobile = (EditText) login
                .findViewById(R.id.ad_mapdevice_edt);
        edt_mobile.setText(device);
        send_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check if Internet present
                if (DataStatic.isInternetAvailable(MainDashboard.this)) {
                    Calendar c = Calendar.getInstance();
                    dateFormatter = new SimpleDateFormat("dd-MM-yyyy",
                            Locale.US);

                    linkdate = dateFormatter.format(c.getTime());
                    progress = new ProgressDialog(MainDashboard.this,
                            ProgressDialog.THEME_TRADITIONAL);
                    progress.setCancelable(false);
                    progress.setCanceledOnTouchOutside(false);
                    progress.setMessage("Please Wait...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    token = sharedPreference_Main.getdatastored();
                    data = "requestType=1008&mid="
                            + sharedPreference_Main.getmid()
                            + "&deviceId="
                            + device
                            + "&manufacturer=aaaaaaaa&deviceStatus=A&linkDate="
                            + linkdate
                            + "&deLinkDate=&superMerchantId="
                            + sharedPreference_Main.getsuperMerchantId()
                            + "&userName=Vipul&businessLegalName=AaramShop&tid="
                            + sharedPreference_Main.gettid() + "";
                    Log.d("Data.....", data);
                    Cipher cipher;
                    try {
                        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        SecretKeySpec secretKey = new SecretKeySpec(token
                                .getBytes(), "AES");
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        byte[] encryptedText = cipher.doFinal(data.getBytes());
                        getmerchantdata = Base64.encodeToString(encryptedText,
                                Base64.DEFAULT);
                        IMPOSImpl impos1;
                        IMPOS impos = impos1 = new IMPOSImpl(MainDashboard.this,
                                getApplicationContext(), v, "Mapping");
                        impos1.setHandler(new ResponseHandler() {
                            @Override
                            public void notifyActivity(String response) {
                                if (progress != null && progress.isShowing()) {
                                    progress.cancel();
                                }
                                Response = response;
                                try {
                                    JSONObject json = new JSONObject(Response);
                                    JSONArray jsonarray = json
                                            .getJSONArray("result");
                                    JSONObject newjson = jsonarray
                                            .getJSONObject(0);

                                    if (Response.contains("responseCode")
                                            && Response.contains("message")) {
                                        if (progress != null
                                                && progress.isShowing()) {
                                            progress.cancel();
                                        }

                                        String message = newjson
                                                .getString("message");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                MainDashboard.this);
                                        // builder.setTitle("AaramGo");
                                        builder.setMessage(message);
                                        builder.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                    }
                                                });

                                        builder.show();
                                    } else {
                                        if (progress != null
                                                && progress.isShowing()) {
                                            progress.cancel();
                                        }
                                        // {"StatusCode":"0","StatusMessage":"success message 0","result":[{"message":"Invalid token","responseCode":"0200"}]}
                                        String message_code = newjson
                                                .getString("messageCode");
                                        String message = newjson
                                                .getString("message");
                                        if (message_code
                                                .equals("RPSL_MAS_BUS_SUCCESS_001")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                                    MainDashboard.this);
                                            // builder.setTitle("AaramGo");
                                            builder.setMessage(getResources().getString(R.string.devicesuccess));
                                            builder.setPositiveButton(
                                                    android.R.string.ok,
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {

                                                            login.dismiss();
                                                        }
                                                    });

                                            builder.show();

                                        } else if (message_code
                                                .equals("RPSL_MAS_BUS_ALREADY_EXISTS_001")) {
                                            if (progress != null
                                                    && progress.isShowing()) {
                                                progress.cancel();
                                            }
                                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                                    MainDashboard.this);
                                            // builder.setTitle("AaramGo");
                                            builder.setMessage(getResources().getString(R.string.devicealready));
                                            builder.setPositiveButton(
                                                    android.R.string.ok,
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            login.dismiss();
                                                        }
                                                    });

                                            builder.show();
                                        } else if (message_code
                                                .equals("RPSL_MAS_BUS_INVALID_DATA_002")) {
                                            if (progress != null
                                                    && progress.isShowing()) {
                                                progress.cancel();
                                            }
                                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                                    MainDashboard.this);
                                            // builder.setTitle("AaramGo");
                                            builder.setMessage(message);
                                            builder.setPositiveButton(
                                                    android.R.string.ok,
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            login.dismiss();
                                                        }
                                                    });

                                            builder.show();
                                        } else {
                                            if (progress != null
                                                    && progress.isShowing()) {
                                                progress.cancel();
                                            }
                                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                                    MainDashboard.this);
                                            // builder.setTitle("AaramGo");
                                            builder.setMessage(getResources().getString(R.string.tryagain));
                                            builder.setPositiveButton(
                                                    android.R.string.ok,
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {

                                                            login.dismiss();
                                                        }
                                                    });

                                            builder.show();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("MposDevice ", Response + "");
                            }
                        });
                        PlugNPlay plugNPlay = PlugNPlay.getInstance(impos,
                                getApplicationContext());
                        plugNPlay.doTransaction(getmerchantdata,
                                sharedPreference_Main.getmid(),
                                sharedPreference_Main.gettid(), 1008, 0.00,
                                Environment.PROD, null);

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } else {
                    Toast.makeText(MainDashboard.this,
                            getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login.dismiss();
            }
        });
        // Make dialog box visible.
        login.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
