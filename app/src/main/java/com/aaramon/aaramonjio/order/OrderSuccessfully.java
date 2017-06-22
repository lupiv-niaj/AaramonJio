package com.aaramon.aaramonjio.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL STORE on 22-May-17.
 */

public class OrderSuccessfully extends Activity {

    String items, data, customeremail = "", orderid = "", customermobile = "";
    TextView next_bill, OrderSummary;
    RelativeLayout RLSharingLayout;
    SharedPreference_Main sharedPreference_main;
    ArrayList<OrderSummaryModel> alist;
    ListView order_summary;

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
        setContentView(R.layout.order_complete_successfully);
        order_summary = (ListView) findViewById(R.id.summay_list);
        Bundle bun = getIntent().getExtras();
        items = bun.getString("items");
        data = bun.getString("data");
        customermobile = bun.getString("customermobile");
        MediaPlayer mp;
        mp = MediaPlayer.create(getApplicationContext(), R.raw.push_sound);
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


        alist = new ArrayList<>();
        try {
            JSONObject jsonO = new JSONObject(data);
            JSONObject controls = jsonO.getJSONObject("Control");
            if (controls.getString("Status").equals("1")) {
                //Toast.makeText(CartAdditionalInfo.this,controls.getString("me"))
                JSONObject data_object = jsonO.getJSONObject("Data");
                orderid = data_object.getString("OrderId");
                alist.add(new OrderSummaryModel("Order No", data_object.getString("OrderNo")));
                alist.add(new OrderSummaryModel("Data & Time", data_object.getString("DateTime")));
                JSONObject earned = data_object.getJSONObject("EarnedData");
                if (earned.getString("SchemeName").equalsIgnoreCase("")) {

                } else {
                    alist.add(new OrderSummaryModel(earned.getString("SchemeName"), "₹" + earned.getString("EarnedPoints")));
                }
                JSONArray jsonArray = data_object.getJSONArray("PaymentModes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String payment_mode = json.getString("PaymentMode");
                    String amount = json.getString("Amount");
                    alist.add(new OrderSummaryModel(payment_mode, "₹" + amount));
                }
                alist.add(new OrderSummaryModel("Total Amount", "₹" + data_object.getString("TotalAmount")));
                OrderSummaryAdapter adp = new OrderSummaryAdapter(OrderSuccessfully.this, alist);
                order_summary.setAdapter(adp);
                adp.notifyDataSetChanged();
            }

        } catch (Exception e) {

        }

        next_bill = (TextView) findViewById(R.id.next_bill);
        OrderSummary = (TextView) findViewById(R.id.OrderSummary);
        RLSharingLayout = (RelativeLayout) findViewById(R.id.RLSharingLayout);
        sharedPreference_main = new SharedPreference_Main(OrderSuccessfully.this);
//        if (customername.equalsIgnoreCase("")) {
//            OrderSummary.setText("Sold " + items + " items worth ₹" + total_amount);
//        } else {
//            OrderSummary.setText(items + " items worth ₹" + total_amount + " sold to " + customername);
//        }
        OrderSummary.setText("Total No. of Items :" + items + " Items");
        next_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSuccessfully.this, ScanProducts.class);
                i.putExtra("from", "");
                i.putExtra("cartid", "");
                i.putExtra("OrderTypePage","0");
                startActivity(i);
                finish();
            }
        });
        RLSharingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_notify_dailog(customermobile, customeremail);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void show_notify_dailog(final String customermobile, final String customeremail) {
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
        edt_email.setText(customeremail);
        edt_mobile.setText(customermobile);
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
                    if (DataStatic.isInternetAvailable(OrderSuccessfully.this)) {
                        final ProgressDialog pDialog = new ProgressDialog(OrderSuccessfully.this);
                        pDialog.setMessage("Please Wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        String tag_json_obj = "Order/sendNotification";
                        StringRequest sr = new StringRequest(
                                com.android.volley.Request.Method.POST,
                                sharedPreference_main.getbase_inpk_url() + tag_json_obj,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            pDialog.cancel();
                                            JSONObject jsonO = new JSONObject(response);
                                            JSONObject controls = jsonO.getJSONObject("Control");
                                            if (controls.getString("Status").equals("1")) {
                                                //Toast.makeText(CartAdditionalInfo.this,controls.getString("me"))
                                                finish();
                                            } else {
                                                Toast.makeText(OrderSuccessfully.this, controls.getString("Message"), Toast.LENGTH_LONG).show();
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
                                params.put("DeviceId", sharedPreference_main.getDeviceId());
                                params.put("DeviceType", "2");
                                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                                params.put("OrderId", orderid);
                                params.put("MobileNo", edt_mobile.getText().toString());
                                params.put("EmailId", edt_email.getText().toString());
                                Log.d("Billing Response", params.toString());
                                return params;

                            }
                        };

                        AppController.getInstance().addToRequestQueue(sr);

                    } else {
                        Toast.makeText(OrderSuccessfully.this,
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public class OrderSummaryAdapter extends BaseAdapter {
        ArrayList<OrderSummaryModel> itemlist;
        Context context;
        LayoutInflater inflater;

        public OrderSummaryAdapter(Context context, ArrayList<OrderSummaryModel> itemlist) {
            // super(context, R.layout.payment_mode_layout, itemlist);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.itemlist = itemlist;
            inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.order_summary_layout,
                        parent, false);
                holder.txt_order = (TextView) convertView.findViewById(R.id.order_text);
                holder.txt_order_detail = (TextView) convertView.findViewById(R.id.order_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_order_detail.setText(":" + itemlist.get(position).getDetail());
            holder.txt_order.setText(itemlist.get(position).getName());
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return itemlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return itemlist.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        private class ViewHolder {
            // CheckBox cb;
            TextView txt_order, txt_order_detail;
        }
    }


}
