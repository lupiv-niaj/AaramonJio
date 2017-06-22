package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DelayAutoCompleteTextView;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.merchant_gst.CityAdapter;
import com.aaramon.aaramonjio.merchant_gst.CityModel;
import com.aaramon.aaramonjio.merchant_gst.GSTCategoryAdapter;
import com.aaramon.aaramonjio.merchant_gst.GSTCategoryModel;
import com.aaramon.aaramonjio.merchant_gst.Merchant_register;
import com.aaramon.aaramonjio.merchant_gst.StateAdapter;
import com.aaramon.aaramonjio.merchant_gst.StateModel;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSupplier extends Activity {
    private EditText company_name, company_address, contact_person, contact_phone;
    private EditText bank_account_number, bank_ifsc_code, gstn_number, pan_number, edit_text_pincode;
    private DelayAutoCompleteTextView add_company;
    private TextView add_supplier_button_submit;
    ImageView backbtn;
    SharedPreference_Main sharedPreference_Main;
    ProgressBar pb_indicator;
    State_Search_Adapter adapter;
    int state_id;
    ArrayList<StateListModel> rowItems;
    City_Search_Adapter adapter1;
    int city_id;
    ArrayList<CityListModel> rowItems1;
    Company_Search_Adapter adapter2;
    int company_id;
    ArrayList<CompanyListModel> rowItems2;
    Spinner spn_gst_category;
    ArrayList<GSTCategoryModel> GSTCategory_Model;
    GSTCategoryAdapter GSTCategory_Adapter;
    Spinner spn_state, spn_city;
    int get_city_id;
    ArrayList<StateModel> State_Model;
    StateAdapter State_Adapter;
    String json_data;
    ArrayList<CityModel> City_Model;
    CityAdapter City_Adapter;

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
        setContentView(R.layout.activity_supplier_add_edit);

        sharedPreference_Main = new SharedPreference_Main(AddSupplier.this);

        company_name = (EditText) findViewById(R.id.edit_text_company_name);
        company_address = (EditText) findViewById(R.id.edit_text_address);
        contact_person = (EditText) findViewById(R.id.edit_text_contact_person);
        contact_phone = (EditText) findViewById(R.id.edit_text_contact_phone);
        bank_account_number = (EditText) findViewById(R.id.edit_text_account_number);
        bank_ifsc_code = (EditText) findViewById(R.id.edit_text_ifsc_code);
        gstn_number = (EditText) findViewById(R.id.edit_text_gstn_number);
        pan_number = (EditText) findViewById(R.id.edit_text_pan_number);
        spn_state = (Spinner) findViewById(R.id.spn_state);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        add_company = (DelayAutoCompleteTextView) findViewById(R.id.addbrand);
        add_supplier_button_submit = (TextView) findViewById(R.id.add_supplier_button);
        backbtn = (ImageView) findViewById(R.id.img_back_account);
        spn_gst_category = (Spinner) findViewById(R.id.spn_gst_category);
        edit_text_pincode = (EditText) findViewById(R.id.edit_text_pincode);
//        company_state.setThreshold(3);
//        company_state.setLoadingIndicator(pb_indicator);
//        adapter = new State_Search_Adapter(AddSupplier.this,
//                android.R.layout.simple_dropdown_item_1line, pb_indicator);
//        company_state.setAdapter(adapter);

//        company_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int id = adapter.getItem(i).getStateId();
//                state_id = id;
//                company_state.setText(adapter.getItem(i).getStateName());
//                DecimalFormat formatter = new DecimalFormat("00");
//                String aFormatted = formatter.format(state_id);
//                gstn_number.setText(aFormatted);
//            }
//        });

        json_data = sharedPreference_Main.get_GSTDetail();
        try {
            JSONObject jsonO = new JSONObject(json_data);
            JSONObject controls = jsonO.getJSONObject("Control");
            if (controls.getString("Status").equals("1")) {
                JSONObject json_data = jsonO.getJSONObject("Data");
                JSONObject GST_Tin_Detail = json_data.getJSONObject("GSTINDetails");
                get_city_id = Integer.parseInt(GST_Tin_Detail.getString("CityId"));

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
        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int stateId = State_Model.get(position).getStateId();
                state_id = stateId;
                for (int i = 0; i < State_Model.size(); i++) {
                    if (state_id == State_Model.get(i).getStateId()) {
                        State_Model.get(i).setSelected(true);
                    } else {
                        State_Model.get(i).setSelected(false);
                    }
                }
                GetCity(String.valueOf(state_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add_company.setThreshold(3);
        add_company.setLoadingIndicator(pb_indicator);
        adapter2 = new Company_Search_Adapter(AddSupplier.this,
                android.R.layout.simple_dropdown_item_1line, pb_indicator);
        add_company.setAdapter(adapter2);

        add_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = adapter2.getItem(i).getCompanyId();
                company_id = id;
                add_company.setText(adapter2.getItem(i).getCompanyName());
            }
        });
        spn_gst_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spn_state.setEnabled(true);
                if (position == 0) {
                    pan_number.setEnabled(true);
                    pan_number.setText("");
                    gstn_number.setText("");
                } else if (position > 0) {
                    pan_number.setEnabled(false);
                    spn_state.setEnabled(false);
                    if (gstn_number.length() < 2) {
                        pan_number.setText("");
                    }
                    if (gstn_number.length() == 2) {
                        for (int i = 0; i < State_Model.size(); i++) {
                            DecimalFormat formatter = new DecimalFormat("00");
                            String aFormatted = formatter.format(State_Model.get(i).getStateGSTCode());
                            if (gstn_number.toString().equalsIgnoreCase(aFormatted)) {
                                State_Model.get(i).setSelected(true);
                                spn_state.setSelection(i);
                                spn_state.setEnabled(false);
                                spn_city.setSelection(0);
                            } else {
                                State_Model.get(i).setSelected(false);
                            }
                        }
                    }

                    if (gstn_number.length() > 2 && gstn_number.length() < 14) {
                        String pannumber = gstn_number.getText().toString().substring(2, gstn_number.length());
                        spn_state.setEnabled(false);
                        pan_number.setText(pannumber);
                    } else if (gstn_number.length() > 2 && gstn_number.length() >= 14) {
                        String pannumber = gstn_number.getText().toString().substring(2, 13);
                        spn_state.setEnabled(false);
                        pan_number.setText(pannumber);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = City_Model.get(position).getCityid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gstn_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                int position = spn_gst_category.getSelectedItemPosition();
//                if (position > 0) {
                spn_state.setEnabled(true);
                if (s.length() < 2) {
                    pan_number.setText("");
                }
                //  if (s.length() == 2) {
                for (int i = 0; i < State_Model.size(); i++) {
                    DecimalFormat formatter = new DecimalFormat("00");
                    String aFormatted = formatter.format(State_Model.get(i).getStateGSTCode());
                    if (s.toString().equalsIgnoreCase(aFormatted)) {
                        State_Model.get(i).setSelected(true);
                        spn_state.setSelection(i);
                        spn_state.setEnabled(false);
                        spn_city.setSelection(0);
                    } else {
                        State_Model.get(i).setSelected(false);
                    }
                }
                //}

                if (s.length() > 2 && s.length() < 14) {
                    String pannumber = gstn_number.getText().toString().substring(2, gstn_number.length());
                    pan_number.setText(pannumber);
                    spn_state.setEnabled(false);
                }else if (s.length() > 2 && s.length() >= 14) {
                    String pannumber = gstn_number.getText().toString().substring(2, 13);
                    pan_number.setText(pannumber);
                    pan_number.setEnabled(false);
                    spn_state.setEnabled(false);
                }

                //}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add_supplier_button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (company_name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.entercompanyname), Toast.LENGTH_LONG).show();
                    company_name.requestFocus();
                    return;
                } else if (company_address.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.entercompanyaddress), Toast.LENGTH_LONG).show();
                    company_address.requestFocus();
                    return;
                } else if (spn_city.getSelectedItemPosition() == 0) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.selectcity), Toast.LENGTH_LONG).show();
                    spn_city.requestFocus();
                    return;
                } else if (contact_person.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.entercontactperson), Toast.LENGTH_LONG).show();
                    contact_person.requestFocus();
                    return;
                } else if (contact_phone.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.entercontactphone), Toast.LENGTH_LONG).show();
                    contact_phone.requestFocus();
                    return;
                } else if (edit_text_pincode.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.enterpincode), Toast.LENGTH_LONG).show();
                    edit_text_pincode.requestFocus();
                    return;
                }
//                else if (gstn_number.getText().toString().equalsIgnoreCase("")) {
//                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.entergstnnumber), Toast.LENGTH_LONG).show();
//                    gstn_number.requestFocus();
//                } else if (pan_number.getText().toString().equalsIgnoreCase("")) {
//                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.enterpannumber), Toast.LENGTH_LONG).show();
//                    pan_number.requestFocus();
//                }
                else if (add_company.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.selectcompany), Toast.LENGTH_LONG).show();
                    add_company.requestFocus();
                    return;
                } else if (company_id <= 0) {
                    Toast.makeText(AddSupplier.this, getResources().getString(R.string.selectcompany), Toast.LENGTH_LONG).show();
                    add_company.requestFocus();
                    return;
                } else {

                    if (spn_gst_category.getSelectedItemPosition() == 0) {
                        if (pan_number.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.enterpannumber), Toast.LENGTH_LONG).show();
                            pan_number.requestFocus();
                            return;
                        } else if (pan_number.getText().length() < 10) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.enterpannumber), Toast.LENGTH_LONG).show();
                            pan_number.requestFocus();
                            return;
                        } else if (!isValidPAN(pan_number.getText().toString())) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.invalidpannumber), Toast.LENGTH_LONG).show();
                            pan_number.requestFocus();
                            return;
                        } else if (!isValidGSTIN(gstn_number.getText().toString()) && gstn_number.getText().toString().length() > 0) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            gstn_number.requestFocus();
                            return;
                        }
                    } else if (spn_gst_category.getSelectedItemPosition() > 0) {
                        if (gstn_number.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.entergstnnumber), Toast.LENGTH_LONG).show();
                            gstn_number.requestFocus();
                            return;
                        } else if (gstn_number.getText().length() < 15) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            gstn_number.requestFocus();
                            return;
                        } else if (!isValidGSTIN(gstn_number.getText().toString())) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            gstn_number.requestFocus();
                            return;
                        }
                    }
                    if (gstn_number.getText().toString().length() == 15) {
                        String gst_no = gstn_number.getText().toString();
                        gst_no = gst_no.substring(0, 2);
                        int gstno_valid = Integer.parseInt(gst_no);
                        if (gstno_valid > 37) {
                            Toast.makeText(AddSupplier.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            gstn_number.requestFocus();
                            return;
                        }
                    }


                    if(edit_text_pincode.getText().toString().length()<6)
                    {
                        Toast.makeText(AddSupplier.this, getResources().getString(R.string.invalidpincode), Toast.LENGTH_LONG).show();
                        edit_text_pincode.requestFocus();
                        return;
                    }
                    if (gstn_number.getText().toString().equalsIgnoreCase("")) {
                        final Dialog multipleBatch = new Dialog(AddSupplier.this);
                        multipleBatch.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        multipleBatch.setContentView(R.layout.layout_alert_dialog);
                        multipleBatch.setCanceledOnTouchOutside(true);
                        multipleBatch.setCancelable(false);
                        multipleBatch.show();
                        final TextView text_message = (TextView) multipleBatch.findViewById(R.id.alert_message);
                        final Button send_btn = (Button) multipleBatch.findViewById(R.id.ad_send);
                        final Button cancel_btn = (Button) multipleBatch.findViewById(R.id.ad_cancel);
                        send_btn.setText("Enter GSTIN");
                        cancel_btn.setText("Later");
                        text_message.setText("You have not entered supplier's GSTIN.For purchase transactions with the supplier.There will be a tax payable on reverse charge");
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                multipleBatch.dismiss();
                                try {
                                    // Here we convert Java Object to JSON
                                    JSONObject jsonObj = new JSONObject();
                                    jsonObj.put("CompanyName", company_name.getText()); // Set the first name/pair
                                    jsonObj.put("CompanyAddress", company_address.getText());
                                    jsonObj.put("StateId", state_id);
                                    jsonObj.put("CityId", city_id);
                                    jsonObj.put("SupplierName", contact_person.getText());
                                    jsonObj.put("SupplierMobile", contact_phone.getText());
                                    jsonObj.put("AccountNo", bank_account_number.getText());
                                    jsonObj.put("IFSCCode", bank_ifsc_code.getText());
                                    jsonObj.put("GSTNNo", gstn_number.getText());
                                    jsonObj.put("PANNo", pan_number.getText());
                                    jsonObj.put("Pincode", edit_text_pincode.getText().toString());
                                    int i = spn_gst_category.getSelectedItemPosition();
                                    jsonObj.put("GSTCategoryId", GSTCategory_Model.get(i).getGSTDealerCategoryId());
                                    String[] ABC = new String[1];
                                    ABC[0] = String.valueOf(company_id);
                                    jsonObj.put("SupplierCompanies", new JSONArray().put(ABC[0]));
                                    //Log.d("vikash",jsonObj.toString());
                                    addsupplier(jsonObj.toString());
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        });

                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                multipleBatch.dismiss();
                                gstn_number.requestFocus();
                            }
                        });
                    } else {
                        try {
                            // Here we convert Java Object to JSON
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("CompanyName", company_name.getText()); // Set the first name/pair
                            jsonObj.put("CompanyAddress", company_address.getText());
                            jsonObj.put("StateId", state_id);
                            jsonObj.put("CityId", city_id);
                            jsonObj.put("SupplierName", contact_person.getText());
                            jsonObj.put("SupplierMobile", contact_phone.getText());
                            jsonObj.put("AccountNo", bank_account_number.getText());
                            jsonObj.put("IFSCCode", bank_ifsc_code.getText());
                            jsonObj.put("GSTNNo", gstn_number.getText());
                            jsonObj.put("PANNo", pan_number.getText());
                            jsonObj.put("Pincode", edit_text_pincode.getText().toString());
                            int i = spn_gst_category.getSelectedItemPosition();
                            jsonObj.put("GSTCategoryId", GSTCategory_Model.get(i).getGSTDealerCategoryId());
                            String[] ABC = new String[1];
                            ABC[0] = String.valueOf(company_id);
                            jsonObj.put("SupplierCompanies", new JSONArray().put(ABC[0]));
                            //Log.d("vikash",jsonObj.toString());
                            addsupplier(jsonObj.toString());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        getGSTCategory();
    }

    private void addsupplier(final String jsonList) {
        Log.d("vikash", jsonList);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "Supplier/addStoreSupplier";
        //http://www.aaramshop.co.in/api/index.php/
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
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
                                JSONArray json_data = jsonO.getJSONArray("Data");
                                Intent m = new Intent(getApplicationContext(), SupplierAddUpdateSuccess.class);
                                m.putExtra("SupplierCompany", company_name.getText());
                                m.putExtra("message", "Supplier Successfully Added");
                                startActivity(m);
//                                Toast.makeText(AddSupplier.this, "Supplier Added Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddSupplier.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
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
                params.put("Supplier", jsonList);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }


    public class State_Search_Adapter extends ArrayAdapter<StateListModel> implements
            Filterable {
        private ArrayList<StateListModel> State_model;
        Context context;
        SharedPreference_Main sharedPreferenceMain, sharedPreferenceMain1;
        ProgressBar pb_indicator;

        public State_Search_Adapter(Context context, int resource, ProgressBar pb_indicator
        ) {
            super(context, resource);
            this.context = context;
            State_model = new ArrayList<StateListModel>();
            this.pb_indicator = pb_indicator;
            sharedPreferenceMain = new SharedPreference_Main(context);
        }

        @Override
        public int getCount() {
            return State_model.size();
        }

        @Override
        public StateListModel getItem(int position) {
            return State_model.get(position);
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            String term = constraint.toString();
                            State_model = new Search_state().execute(term).get();
                        } catch (Exception e) {
                            Log.d("HUS", "EXCEPTION " + e);
                        }
                        filterResults.values = State_model;
                        filterResults.count = State_model.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return myFilter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.search_layout, parent,
                    false);

            // get Country
            final StateListModel state = State_model.get(position);

            TextView tv_cust_name;

            tv_cust_name = (TextView) view.findViewById(R.id.tv_cust_name);
            tv_cust_name.setText(state.getStateName());
            //Log.d("SID", String.valueOf(supplier.getSupplierId()));
            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    company_state.setText("");
                    Intent intent = new Intent(AddSupplier.this,
                            SupplierHistoryActivity.class);
                    intent.putExtra("supplier_id", String.valueOf(state.getStateId()));
                    startActivity(intent);
                    //finish();
                }
            });*/
            return view;
        }

        // download mCountry list
        private class Search_state extends
                AsyncTask<String, Void, ArrayList<StateListModel>> {

            @Override
            protected ArrayList<StateListModel> doInBackground(String... params) {
                try {
                    sharedPreferenceMain = new SharedPreference_Main(context);
                    InputStream isr = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(
                            sharedPreferenceMain.getbase_inpk_url() + "State/searchStates");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            1);
                    nameValuePairs.add(new BasicNameValuePair("SearchTerm",
                            params[0]));
                    nameValuePairs.add(new BasicNameValuePair("DeviceType", "2"));
                    nameValuePairs.add(new BasicNameValuePair("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);
                    //Log.d("Post Data", httppost.toString());
                    if (response != null)
                        System.out.println("Connection created");
                    //Log.d("Search Response", String.valueOf(response));
                    HttpEntity entity = response.getEntity();
                    isr = entity.getContent();

                    InputStreamReader isre = new InputStreamReader(isr,
                            "iso-8859-1");
                    BufferedReader reader = new BufferedReader(isre, 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("<"))
                            continue;
                        sb.append(line + "\n");
                    }
                    isr.close();

                    // parse JSON and store it in the list
                    String jsonString = sb.toString();


                    JSONObject jsonO = new JSONObject(jsonString);
                    JSONObject controls = jsonO.getJSONObject("Control");
                    ArrayList<StateListModel> stateList = null;
                    if (controls.getString("Status").equals("1")) {
                        JSONArray json_data = jsonO.getJSONArray("Data");
                        stateList = new ArrayList<StateListModel>();
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject prod = json_data.getJSONObject(i);
                            String state_name = prod.getString("StateName");
                            String state_id = prod.getString("StateId");
                            StateListModel dlm = new StateListModel();
                            dlm.setStateName(state_name);
                            dlm.setStateId(Integer.parseInt(state_id));
                            stateList.add(dlm);
                        }
                        return stateList;
                    } else {
                        Toast.makeText(AddSupplier.this, "State not found", Toast.LENGTH_LONG).show();
                        return null;
                    }


                } catch (Exception e) {
                    Log.d("HUS", "EXCEPTION " + e);
                    return null;
                }
            }
        }
    }

    public class City_Search_Adapter extends ArrayAdapter<CityListModel> implements
            Filterable {
        private ArrayList<CityListModel> City_model;
        Context context;
        SharedPreference_Main sharedPreferenceMain, sharedPreferenceMain1;
        ProgressBar pb_indicator;

        public City_Search_Adapter(Context context, int resource, ProgressBar pb_indicator
        ) {
            super(context, resource);
            this.context = context;
            City_model = new ArrayList<CityListModel>();
            this.pb_indicator = pb_indicator;
            sharedPreferenceMain = new SharedPreference_Main(context);
        }

        @Override
        public int getCount() {
            return City_model.size();
        }

        @Override
        public CityListModel getItem(int position) {
            return City_model.get(position);
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            String term = constraint.toString();
                            City_model = new Search_city().execute(term).get();
                        } catch (Exception e) {
                            Log.d("HUS", "EXCEPTION " + e);
                        }
                        filterResults.values = City_model;
                        filterResults.count = City_model.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return myFilter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.search_layout, parent,
                    false);

            // get Country
            final CityListModel city = City_model.get(position);

            TextView tv_cust_name;

            tv_cust_name = (TextView) view.findViewById(R.id.tv_cust_name);
            tv_cust_name.setText(city.getCityName());
            return view;
        }

        // download mCountry list
        private class Search_city extends
                AsyncTask<String, Void, ArrayList<CityListModel>> {

            @Override
            protected ArrayList<CityListModel> doInBackground(String... params) {
                try {
                    sharedPreferenceMain = new SharedPreference_Main(context);
                    InputStream isr = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(
                            sharedPreferenceMain.getbase_inpk_url() + "City/searchCities");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            1);
                    nameValuePairs.add(new BasicNameValuePair("SearchTerm",
                            params[0]));
                    nameValuePairs.add(new BasicNameValuePair("StateId", String.valueOf(state_id)));
                    nameValuePairs.add(new BasicNameValuePair("DeviceType", "2"));
                    nameValuePairs.add(new BasicNameValuePair("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);
                    //Log.d("Post Data", httppost.toString());
                    if (response != null)
                        System.out.println("Connection created");
                    //Log.d("Search Response", String.valueOf(response));
                    HttpEntity entity = response.getEntity();
                    isr = entity.getContent();

                    InputStreamReader isre = new InputStreamReader(isr,
                            "iso-8859-1");
                    BufferedReader reader = new BufferedReader(isre, 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("<"))
                            continue;
                        sb.append(line + "\n");
                    }
                    isr.close();

                    // parse JSON and store it in the list
                    String jsonString = sb.toString();


                    JSONObject jsonO = new JSONObject(jsonString);
                    JSONObject controls = jsonO.getJSONObject("Control");
                    ArrayList<CityListModel> cityList = null;
                    if (controls.getString("Status").equals("1")) {
                        JSONArray json_data = jsonO.getJSONArray("Data");
                        cityList = new ArrayList<CityListModel>();
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject prod = json_data.getJSONObject(i);
                            String city_name = prod.getString("CityName");
                            String city_id = prod.getString("CityId");
                            CityListModel dlm = new CityListModel();
                            dlm.setCityName(city_name);
                            dlm.setCityId(Integer.parseInt(city_id));
                            cityList.add(dlm);
                        }
                        return cityList;
                    } else {
                        Toast.makeText(AddSupplier.this, "Supplier not found", Toast.LENGTH_LONG).show();
                        return null;
                    }


                } catch (Exception e) {
                    Log.d("HUS", "EXCEPTION " + e);
                    return null;
                }
            }
        }
    }

    public class Company_Search_Adapter extends ArrayAdapter<CompanyListModel> implements
            Filterable {
        private ArrayList<CompanyListModel> Company_model;
        Context context;
        SharedPreference_Main sharedPreferenceMain, sharedPreferenceMain1;
        ProgressBar pb_indicator;

        public Company_Search_Adapter(Context context, int resource, ProgressBar pb_indicator
        ) {
            super(context, resource);
            this.context = context;
            Company_model = new ArrayList<CompanyListModel>();
            this.pb_indicator = pb_indicator;
            sharedPreferenceMain = new SharedPreference_Main(context);
        }

        @Override
        public int getCount() {
            return Company_model.size();
        }

        @Override
        public CompanyListModel getItem(int position) {
            return Company_model.get(position);
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        try {
                            String term = constraint.toString();
                            Company_model = new Search_company().execute(term).get();
                        } catch (Exception e) {
                            Log.d("HUS", "EXCEPTION " + e);
                        }
                        filterResults.values = Company_model;
                        filterResults.count = Company_model.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return myFilter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.search_layout, parent, false);

            final CompanyListModel state = Company_model.get(position);

            TextView tv_cust_name;

            tv_cust_name = (TextView) view.findViewById(R.id.tv_cust_name);
            tv_cust_name.setText(state.getCompanyName());
            return view;
        }

        // download mCountry list
        private class Search_company extends
                AsyncTask<String, Void, ArrayList<CompanyListModel>> {

            @Override
            protected ArrayList<CompanyListModel> doInBackground(String... params) {
                try {
                    sharedPreferenceMain = new SharedPreference_Main(context);
                    InputStream isr = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(
                            sharedPreferenceMain.getbase_inpk_url() + "Company/searchCompanies");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            1);
                    nameValuePairs.add(new BasicNameValuePair("SearchTerm",
                            params[0]));
                    nameValuePairs.add(new BasicNameValuePair("DeviceType", "2"));
                    nameValuePairs.add(new BasicNameValuePair("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);
                    if (response != null)
                        System.out.println("Connection created");
                    HttpEntity entity = response.getEntity();
                    isr = entity.getContent();

                    InputStreamReader isre = new InputStreamReader(isr,
                            "iso-8859-1");
                    BufferedReader reader = new BufferedReader(isre, 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("<"))
                            continue;
                        sb.append(line + "\n");
                    }
                    isr.close();

                    String jsonString = sb.toString();


                    JSONObject jsonO = new JSONObject(jsonString);
                    JSONObject controls = jsonO.getJSONObject("Control");
                    ArrayList<CompanyListModel> companyList = null;
                    if (controls.getString("Status").equals("1")) {
                        JSONArray json_data = jsonO.getJSONArray("Data");
                        companyList = new ArrayList<CompanyListModel>();
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject prod = json_data.getJSONObject(i);
                            String company_name = prod.getString("CompanyName");
                            String company_id = prod.getString("CompanyId");
                            CompanyListModel dlm = new CompanyListModel();
                            dlm.setCompanyName(company_name);
                            dlm.setCompanyId(Integer.parseInt(company_id));
                            companyList.add(dlm);
                        }
                        return companyList;
                    } else {
                        Toast.makeText(AddSupplier.this, "Company not found", Toast.LENGTH_LONG).show();
                        return null;
                    }


                } catch (Exception e) {
                    Log.d("HUS", "EXCEPTION " + e);
                    return null;
                }
            }
        }
    }


    private void getGSTCategory() { // FROM API
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "MerchantStore/getGSTCategory";
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
                        GSTCategory_Model = new ArrayList<>();
                        JSONArray json_data = jsonO.getJSONArray("Data");
                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject GSTCategory = json_data.getJSONObject(i);
                            int GSTDealerCategoryId = Integer.parseInt(GSTCategory.getString("GSTDealerCategoryId"));
                            String GSTDealerCategoryName = GSTCategory.getString("GSTDealerCategoryName");
                            GSTCategoryModel GSTM = new GSTCategoryModel();
                            GSTM.setGSTDealerCategoryId(GSTDealerCategoryId);
                            GSTM.setGSTDealerCategoryName(GSTDealerCategoryName);
                            GSTCategory_Model.add(GSTM);
                        }
                        GSTCategory_Adapter = new GSTCategoryAdapter(AddSupplier.this, GSTCategory_Model);
                        spn_gst_category.setAdapter(GSTCategory_Adapter);
                    } else {
                        Toast.makeText(AddSupplier.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Log.d("Params", params.toString());
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

    private void GetCity(final String Stateid) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "merchant/getCities";
        //http://www.aaramshop.co.in/api/index.php/
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                sharedPreference_Main.getbase_inpk_url() + tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response ", response);
                            pDialog.cancel();
                            JSONObject jsonO = new JSONObject(response);
                            if (jsonO.getString("status").equals("1")) {
                                JSONArray json_data = jsonO.getJSONArray("cities");
                                City_Model = new ArrayList<>();
                                CityModel sdm = new CityModel();
                                sdm.setSelected(true);
                                sdm.setCityid(0);
                                sdm.setCityName("Select City");
                                City_Model.add(sdm);
                                for (int i = 0; i < json_data.length(); i++) {
                                    JSONObject state = json_data.getJSONObject(i);
                                    int CityId = Integer.parseInt(state.getString("city_id"));
                                    String CityName = state.getString("city_name");
                                    CityModel sdm1 = new CityModel();
                                    sdm1.setSelected(false);
                                    sdm1.setCityid(CityId);
                                    sdm1.setCityName(CityName);
                                    City_Model.add(sdm1);
                                }
                                City_Adapter = new CityAdapter(AddSupplier.this, R.id.drop_down_head, City_Model);
                                spn_city.setAdapter(City_Adapter);

                                int city_position = 0;
                                for (int i = 0; i < City_Model.size(); i++) {
                                    if (City_Model.get(i).getCityid() == get_city_id) {
                                        City_Model.get(i).setSelected(true);
                                        city_position = i;
                                    } else {
                                        City_Model.get(i).setSelected(false);
                                    }
                                }
                                spn_city.setSelection(city_position);
                            } else {
                                Toast.makeText(AddSupplier.this, jsonO.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("state_id", Stateid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

}
