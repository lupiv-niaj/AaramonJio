package com.aaramon.aaramonjio.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.merchant_gst.StateAdapter;
import com.aaramon.aaramonjio.merchant_gst.StateModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerInfo extends AppCompatActivity {

    SharedPreference_Main sharedPreference_main;
    EditText customer_gsttin, customer_confirm_gsttin, customer_business_name, customer_pincode, customer_name, customer_mobile, customer_email, customer_address;
    //    ArrayList<SearchCustomerModel> myList;
    String mode = "", shoppername, shoppermobile, shopper_id;
    TextView add_new_add;
    ImageView img_back;
    String shopper_email, businessname, panno, gsttin, state, pincode;
    int stateid;
    String json_data;
    Spinner spn_state;
    ArrayList<StateModel> State_Model;
    StateAdapter State_Adapter;

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
        setContentView(R.layout.activity_customer_info);
        Bundle bun = getIntent().getExtras();
        mode = bun.getString("Mode");
        shoppername = bun.getString("shoppername");
        shoppermobile = bun.getString("shoppermobile");
        shopper_id = bun.getString("shopperid");
        shopper_email = bun.getString("shopperemail");
        businessname = bun.getString("business");
        gsttin = bun.getString("gst");
        panno = bun.getString("pan");
        state = bun.getString("state");
        pincode = bun.getString("pincode");
        sharedPreference_main = new SharedPreference_Main(CustomerInfo.this);
        customer_name = (EditText) findViewById(R.id.customer_name);
        customer_mobile = (EditText) findViewById(R.id.customer_mobile);
        customer_gsttin = (EditText) findViewById(R.id.edt_gst_no);
        customer_confirm_gsttin = (EditText) findViewById(R.id.edt_confirm_gst_no);
        spn_state = (Spinner) findViewById(R.id.spn_state);
        customer_address = (EditText) findViewById(R.id.customer_address);
        customer_pincode = (EditText) findViewById(R.id.customer_pincode);
        customer_email = (EditText) findViewById(R.id.customer_email);
        add_new_add = (TextView) findViewById(R.id.add_new_add);
        customer_business_name = (EditText) findViewById(R.id.edt_business_name);
        img_back = (ImageView) findViewById(R.id.img_back);
        json_data = sharedPreference_main.get_GSTDetail();
        try {
            JSONObject jsonO = new JSONObject(json_data);
            JSONObject controls = jsonO.getJSONObject("Control");
            if (controls.getString("Status").equals("1")) {
                JSONObject json_data = jsonO.getJSONObject("Data");
                JSONObject GST_Tin_Detail = json_data.getJSONObject("GSTINDetails");
                State_Model = new ArrayList<>();
                JSONArray StateDropdown = GST_Tin_Detail.getJSONArray("StateDropdown");
                for (int i = 0; i < StateDropdown.length(); i++) {
                    JSONObject state = StateDropdown.getJSONObject(i);
                    int StateId = Integer.parseInt(state.getString("StateId"));
                    String StateName = state.getString("StateName");
                    int StateGSTCode = Integer.parseInt(state.getString("StateGSTCode"));
                    int select = Integer.parseInt(state.getString("IsSelected"));
                    boolean IsSelected;
                    if (select == 1) {
                        IsSelected = true;
                    } else {
                        IsSelected = false;
                    }
                    StateModel sdm = new StateModel();
                    sdm.setSelected(IsSelected);
                    sdm.setStateGSTCode(StateGSTCode);
                    sdm.setStateId(StateId);
                    sdm.setStateName(StateName);
                    State_Model.add(sdm);
                }
                State_Adapter = new StateAdapter(this, R.id.drop_down_head, State_Model);
                spn_state.setAdapter(State_Adapter);
                int State_select_position = 0;
                for (int i = 0; i < State_Model.size(); i++) {
                    if (State_Model.get(i).getSelected()) {
                        State_select_position = i;

                        break;
                    }
                }
                spn_state.setSelection(State_select_position);
            }

        } catch (Exception e) {

        }
        customer_name.setText(shoppername);
        customer_mobile.setText(shoppermobile);
        customer_email.setText(shopper_email);
        customer_confirm_gsttin.setText(gsttin);
        customer_gsttin.setText(gsttin);
        customer_pincode.setText(pincode);
        customer_business_name.setText(businessname);

        if (customer_gsttin.getText().toString().equalsIgnoreCase("")) {

        } else {
            spn_state.setEnabled(false);
        }
        customer_gsttin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if (s.length() == 2) {
                if(s.length()==0)
                {
                    spn_state.setEnabled(true);
                }else {
                    spn_state.setEnabled(false);
                    for (int i = 0; i < State_Model.size(); i++) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        String aFormatted = formatter.format(State_Model.get(i).getStateGSTCode());
                        if (s.toString().equalsIgnoreCase(aFormatted)) {
                            State_Model.get(i).setSelected(true);
                            spn_state.setEnabled(false);
                            spn_state.setSelection(i);
                        } else {
                            State_Model.get(i).setSelected(false);
                        }
                    }
                }
                //}

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_new_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer_name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(CustomerInfo.this, getResources().getString(R.string.pleasentercustomername), Toast.LENGTH_LONG).show();
                    customer_name.requestFocus();
                    return;
                } else if (customer_mobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(CustomerInfo.this, getResources().getString(R.string.pleasentercustomermobile), Toast.LENGTH_LONG).show();
                    customer_mobile.requestFocus();
                    return;
                } else if (!isValidEmail(customer_email.getText().toString()) && customer_email.getText().toString().length() > 0) {
                    customer_email.setError(getResources().getString(R.string.invalidemail));
                    customer_email.requestFocus();
                    return;
                } else {
                    if (customer_gsttin.getText().length() > 0) {
                        if (!isValidGSTIN(customer_gsttin.getText().toString())) {
                            Toast.makeText(CustomerInfo.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            customer_gsttin.requestFocus();
                            return;
                        } else if (customer_gsttin.getText().toString().equalsIgnoreCase(customer_confirm_gsttin.getText().toString())) {
                            if (customer_business_name.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(CustomerInfo.this, getResources().getString(R.string.confirmGST), Toast.LENGTH_LONG).show();
                                customer_business_name.requestFocus();
                                return;
                            }
                        } else {
                            Toast.makeText(CustomerInfo.this, getResources().getString(R.string.enterbusinessname), Toast.LENGTH_LONG).show();
                            customer_business_name.requestFocus();
                            return;
                            //Toast GSTING confirm
                        }
                    } else {

                    }

                    if (customer_gsttin.getText().toString().length() == 15) {
                        String gst_no = customer_gsttin.getText().toString();
                        gst_no = gst_no.substring(0, 2);
                        int gstno_valid = Integer.parseInt(gst_no);
                        if (gstno_valid > 37) {
                            Toast.makeText(CustomerInfo.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            customer_gsttin.requestFocus();
                            return;
                        }
                    }

                    try {
                        // Here we convert Java Object to JSON
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("CustomerName", customer_name.getText().toString()); // Set the first name/pair
                        jsonObj.put("CustomerMobile", customer_mobile.getText().toString());
                        jsonObj.put("CustomerEmail", customer_email.getText().toString());
                        jsonObj.put("BusinessName", customer_business_name.getText().toString());
                        jsonObj.put("CustomerAddress", customer_address.getText().toString());
                        jsonObj.put("StateId", stateid);
                        jsonObj.put("Pincode", customer_pincode.getText().toString());
                        jsonObj.put("GSTIN", customer_gsttin.getText().toString());
                        create_customers(jsonObj.toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }


//                    create_customers("2", sharedPreference_main.getStoreId(), customer_name.getText().toString(), customer_mobile.getText().toString(), "", customer_address.getText().toString(), "0", "0", "0");
                }

            }
        });
    }

    private void create_customers(final String json) {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(
                CustomerInfo.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                sharedPreference_main.getbase_inpk_url() + "MerchantCustomers/addMerchantGSTCustomer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        try {
                            Log.d("Response....", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject controls = jsonObject.getJSONObject("Control");
                            if (controls.getString("Status").equals(
                                    "1")) {
                                JSONObject data = jsonObject.getJSONObject("Data");
                                Intent intent = new Intent();
                                intent.putExtra("address_id", data.getString("CustomerAddressId"));
                                intent.putExtra("address", customer_address.getText().toString());
                                intent.putExtra("customername", customer_name.getText().toString());
                                intent.putExtra("customermobile", customer_mobile.getText().toString());
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "" + controls.getString("Message"),
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
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceType", "2");
                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                params.put("DeviceId", sharedPreference_main.getDeviceId());
                params.put("CustomerData", json);
                Log.d("Request...", params.toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    private boolean isValidGSTIN(String gstno) {
        Pattern pattern = Pattern.compile("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[A-Z0-9]{1}[A-Z]{1}[0-9]{1}");
        Matcher matcher = pattern.matcher(gstno);
        return matcher.matches();

    }

    private boolean isValidPAN(String panno) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(panno);
        return matcher.matches();

    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
