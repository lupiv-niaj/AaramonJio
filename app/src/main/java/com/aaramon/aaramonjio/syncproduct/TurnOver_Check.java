package com.aaramon.aaramonjio.syncproduct;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.merchant_gst.Merchant_register;
import com.aaramon.aaramonjio.order.StaticVariable;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.internal.zzhl.runOnUiThread;

/**
 * Created by DELL STORE on 16-May-17.
 */

public class TurnOver_Check extends Service implements StaticVariable {
    SharedPreference_Main sharedPreference_Main;
    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressLint("LongLogTag")
    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        context = this.getApplicationContext();
        // Bundle bun = intent.getExtras();
        // msgstr = bun.getString("date");
        // Check if Internet present
        sharedPreference_Main = new SharedPreference_Main(context);

        if (DataStatic.isInternetAvailable(context)) {
            Log.d("TurnOver", "Call");
            readWebPage();
        } else {
            Log.d("TurnOver without internet", "NotCall");
            return;
        }
    }

    public void readWebPage() {
        // TODO Auto-generated method stub
        //http://www.aaramshop.co.in/api/index.php/
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST, sharedPreference_Main.getbase_inpk_url() + "" +
                "MerchantStore/checkRetailerTurnOverLimit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response ", response);
                            JSONObject jsonO = new JSONObject(response);
                            final JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                JSONObject data = jsonO.getJSONObject("Data");
                                int turnover = Integer.parseInt(data.getString("Status"));
                                sharedPreference_Main.set_turnover(String.valueOf(turnover));
                                if (turnover == 1) {
                                    final Dialog multipleBatch = new Dialog(context);
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
                                    text_message.setText("You have exited your existing turnover thrashold.Please do GSTN Registration");
                                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            multipleBatch.dismiss();
                                            stopSelf();
                                        }
                                    });

                                    send_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(context, Merchant_register.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                            stopSelf();
                                            multipleBatch.dismiss();
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("Service Exception", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.cancel();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceId", sharedPreference_Main.getDeviceId());
                params.put("MerchantStoreId", sharedPreference_Main.getStoreId());
                params.put("DeviceType", "2");
                Log.d("Sync Response", params.toString());
                //   params.put("key_update", "{\"products\":[{\"store_product_id\":\"\",\"server_merchant_store_product_id\":\"\"}]}");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }
}
