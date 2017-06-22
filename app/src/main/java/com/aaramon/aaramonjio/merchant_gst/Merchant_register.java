package com.aaramon.aaramonjio.merchant_gst;

import android.app.Activity;
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
import com.aaramon.aaramonjio.order.CartAdditionalInfo;
import com.aaramon.aaramonjio.supplier.AddSupplier;
import com.aaramon.aaramonjio.supplier.CityListModel;
import com.aaramon.aaramonjio.supplier.SupplierAddUpdateSuccess;
import com.aaramon.aaramonjio.supplier.UpdateSupplier;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

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

public class Merchant_register extends Activity {
    ImageView img_back;
    String json_data;
    EditText edt_business_name, edt_gst_tinno, edt_pan_number, edt_address_line1, edt_address_line2, edt_pincode;
    // DelayAutoCompleteTextView edt_city;
    Spinner spn_gst_category, spn_business_category, spn_state, spn_city;
    TextView add_new_add;
    ArrayList<StateModel> State_Model;
    StateAdapter State_Adapter;

    ArrayList<CityModel> City_Model;
    CityAdapter City_Adapter;
    int get_city_id;

    ArrayList<GSTCategoryModel> GSTCategory_Model;
    GSTCategoryAdapter GSTCategory_Adapter;
    ArrayList<GSTBusinessModel> GSTBusiness_Model;
    GSTBusinessAdapter GSTBusiness_Adapter;
    int state_id, city_id;

    int getStateId, getCityId;
    String City_name;
    SharedPreference_Main sharedPreference_main;
    EditText confirm_gst_no;

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
        setContentView(R.layout.merchant_profile_setup);
        img_back = (ImageView) findViewById(R.id.img_back);
        edt_business_name = (EditText) findViewById(R.id.edt_business_name);
        edt_gst_tinno = (EditText) findViewById(R.id.edt_gst_tinno);
        edt_pan_number = (EditText) findViewById(R.id.edt_pan_number);
        spn_gst_category = (Spinner) findViewById(R.id.spn_gst_category);
        spn_business_category = (Spinner) findViewById(R.id.spn_business_category);
        edt_address_line1 = (EditText) findViewById(R.id.edt_address_line1);
        edt_address_line2 = (EditText) findViewById(R.id.edt_address_line2);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        spn_state = (Spinner) findViewById(R.id.spn_state);
        add_new_add = (TextView) findViewById(R.id.add_new_add);
        confirm_gst_no = (EditText) findViewById(R.id.edt_confirm_gst_tinno);
        sharedPreference_main = new SharedPreference_Main(Merchant_register.this);
        json_data = sharedPreference_main.get_GSTDetail();
        try {
            JSONObject jsonO = new JSONObject(json_data);
            JSONObject controls = jsonO.getJSONObject("Control");
            if (controls.getString("Status").equals("1")) {
                JSONObject json_data = jsonO.getJSONObject("Data");
                JSONObject GST_Tin_Detail = json_data.getJSONObject("GSTINDetails");
                String BusinessName = GST_Tin_Detail.getString("BusinessName");
                String AddressLineOne = GST_Tin_Detail.getString("AddressLineOne");
                String AddressLineTwo = GST_Tin_Detail.getString("AddressLineTwo");
                String Pincode = GST_Tin_Detail.getString("Pincode");
                String GSTin = GST_Tin_Detail.getString("GSTIN");
                String panNo = GST_Tin_Detail.getString("PANNo");
                // city_id = Integer.parseInt(GST_Tin_Detail.getString("CityId"));
                //City_name = GST_Tin_Detail.getString("CityName");
                GSTCategory_Model = new ArrayList<>();
                JSONArray GSTCategory_Array = GST_Tin_Detail.getJSONArray("GSTCategory");
                for (int i = 0; i < GSTCategory_Array.length(); i++) {
                    JSONObject GSTCategory = GSTCategory_Array.getJSONObject(i);
                    int GSTDealerCategoryId = Integer.parseInt(GSTCategory.getString("GSTDealerCategoryId"));
                    String GSTDealerCategoryName = GSTCategory.getString("GSTDealerCategoryName");
                    int select = Integer.parseInt(GSTCategory.getString("IsSelected"));
                    boolean IsSelected;
                    if (select == 1) {
                        IsSelected = true;
                    } else {
                        IsSelected = false;
                    }
                    GSTCategoryModel GSTM = new GSTCategoryModel();
                    GSTM.setGSTDealerCategoryId(GSTDealerCategoryId);
                    GSTM.setGSTDealerCategoryName(GSTDealerCategoryName);
                    GSTM.setIsselected(IsSelected);
                    GSTCategory_Model.add(GSTM);
                }
                GSTCategory_Adapter = new GSTCategoryAdapter(this, GSTCategory_Model);
                spn_gst_category.setAdapter(GSTCategory_Adapter);

                int Category_select_position = 0;
                for (int i = 0; i < GSTCategory_Model.size(); i++) {
                    if (GSTCategory_Model.get(i).getselected()) {
                        Category_select_position = i;

                        break;
                    }
                }
                spn_gst_category.setSelection(Category_select_position);


                GSTBusiness_Model = new ArrayList<>();
                JSONArray BusinessCategory_Array = GST_Tin_Detail.getJSONArray("BusinessCategory");
                for (int i = 0; i < BusinessCategory_Array.length(); i++) {
                    JSONObject GSTBusiness = BusinessCategory_Array.getJSONObject(i);
                    int GSTBusinessCategoryId = Integer.parseInt(GSTBusiness.getString("GSTBusinessCategoryId"));
                    String GSTBusinessCategoryName = GSTBusiness.getString("GSTBusinessCategoryName");
                    int select = Integer.parseInt(GSTBusiness.getString("IsSelected"));
                    boolean IsSelected;
                    if (select == 1) {
                        IsSelected = true;
                    } else {
                        IsSelected = false;
                    }
                    GSTBusinessModel GSTM = new GSTBusinessModel();
                    GSTM.setGSTBusinessCategoryId(GSTBusinessCategoryId);
                    GSTM.setGSTBusinessCategoryName(GSTBusinessCategoryName);
                    GSTM.setIsselected(IsSelected);
                    GSTBusiness_Model.add(GSTM);
                }
                GSTBusiness_Adapter = new GSTBusinessAdapter(this, GSTBusiness_Model);
                spn_business_category.setAdapter(GSTBusiness_Adapter);
                int Business_select_position = 0;
                for (int i = 0; i < GSTBusiness_Model.size(); i++) {
                    if (GSTBusiness_Model.get(i).getselected()) {
                        Business_select_position = i;

                        break;
                    }
                }
                spn_business_category.setSelection(Business_select_position);

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
                get_city_id = Integer.parseInt(GST_Tin_Detail.getString("CityId"));

//                City_Model = new ArrayList<>();
//                JSONArray CityDropdown = GST_Tin_Detail.getJSONArray("CityDropdown");
//                for (int i = 0; i < CityDropdown.length(); i++) {
//                    JSONObject state = CityDropdown.getJSONObject(i);
//                    int CityId = Integer.parseInt(state.getString("CityId"));
//                    String CityName = state.getString("CityName");
//                    int select = Integer.parseInt(state.getString("IsSelected"));
//                    boolean IsSelected;
//                    if (select == 1) {
//                        IsSelected = true;
//                    } else {
//                        IsSelected = false;
//                    }
//                    CityModel sdm = new CityModel();
//                    sdm.setSelected(IsSelected);
//                    sdm.setCityid(CityId);
//                    sdm.setCityName(CityName);
//                    City_Model.add(sdm);
//                }
//                City_Adapter = new CityAdapter(this, R.id.drop_down_head, City_Model);
//                spn_city.setAdapter(City_Adapter);
//                int City_select_position = 0;
//                for (int i = 0; i < City_Model.size(); i++) {
//                    if (City_Model.get(i).isSelected()) {
//                        City_select_position = i;
//                        break;
//                    }
//                }
//                spn_city.setSelection(City_select_position);
                edt_gst_tinno.setText(GSTin);
                edt_pan_number.setText(panNo);
                if (GSTin.equalsIgnoreCase("")) {
                } else {
                    spn_state.setEnabled(false);
                }
                edt_business_name.setText(BusinessName);
                edt_address_line1.setText(AddressLineOne);
                edt_address_line2.setText(AddressLineTwo);
                //  edt_city.setText(City_name);
                edt_pincode.setText(Pincode);
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


        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = City_Model.get(position).getCityid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        edt_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int cid = adapter1.getItem(i).getCityId();
//                city_id = cid;
//                City_name = adapter1.getItem(i).getCityName();
//                edt_city.setText(adapter1.getItem(i).getCityName());
//
//            }
//        });

        spn_gst_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spn_state.setEnabled(true);
                if (position == 0) {
                    edt_pan_number.setEnabled(true);
                    edt_pan_number.setText("");
                    edt_gst_tinno.setText("");
                } else if (position > 0) {
                    spn_state.setEnabled(false);
                    edt_pan_number.setEnabled(false);
                    if (edt_gst_tinno.length() < 2) {
//                        edt_pan_number.setText("");
                    }
                    // if (edt_gst_tinno.length() == 2) {
                    for (int i = 0; i < State_Model.size(); i++) {
                        DecimalFormat formatter = new DecimalFormat("00");
                        String aFormatted = formatter.format(State_Model.get(i).getStateGSTCode());
                        if (edt_gst_tinno.toString().equalsIgnoreCase(aFormatted)) {
                            State_Model.get(i).setSelected(true);
                            spn_state.setSelection(i);
                            spn_state.setEnabled(false);
                            spn_city.setSelection(0);
                        } else {
                            State_Model.get(i).setSelected(false);
                        }
                    }
                    //}
                    if (edt_gst_tinno.length() > 2 && edt_gst_tinno.length() < 14) {
                        String pannumber = edt_gst_tinno.getText().toString().substring(2, edt_gst_tinno.length());
                        edt_pan_number.setText(pannumber);
                        spn_state.setEnabled(false);
                    } else if (edt_gst_tinno.length() > 2 && edt_gst_tinno.length() >= 14) {
                        String pannumber = edt_gst_tinno.getText().toString().substring(2, 13);
                        edt_pan_number.setText(pannumber);
                        spn_state.setEnabled(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edt_gst_tinno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                int position = spn_gst_category.getSelectedItemPosition();
//                if (position > 0) {
                if (s.length() < 2) {
                    edt_pan_number.setText("");
                }
                spn_state.setEnabled(true);
                // if (s.length() == 2) {
                for (int i = 0; i < State_Model.size(); i++) {
                    DecimalFormat formatter = new DecimalFormat("00");
                    String aFormatted = formatter.format(State_Model.get(i).getStateGSTCode());
                    if (s.toString().equalsIgnoreCase(aFormatted)) {
                        State_Model.get(i).setSelected(true);
                        spn_state.setSelection(i);
                        spn_state.setEnabled(false);
                        spn_city.setSelection(0);
                        break;
                    } else {
                        State_Model.get(i).setSelected(false);
                    }
                }
                //}
                if (s.length() > 2 && s.length() < 14) {
                    String pannumber = edt_gst_tinno.getText().toString().substring(2, edt_gst_tinno.length());
                    edt_pan_number.setText(pannumber);
                    spn_state.setEnabled(false);
                } else if (s.length() > 2 && s.length() >= 14) {
                    String pannumber = edt_gst_tinno.getText().toString().substring(2, 13);
                    edt_pan_number.setText(pannumber);
                    edt_pan_number.setEnabled(false);
                    spn_state.setEnabled(false);
                }
                // }
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
                if (edt_business_name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Merchant_register.this, getResources().getString(R.string.enterbusinessname), Toast.LENGTH_LONG).show();
                    edt_business_name.requestFocus();
                    return;
                } else if (edt_address_line1.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Merchant_register.this, getResources().getString(R.string.enteraddress), Toast.LENGTH_LONG).show();
                    edt_address_line1.requestFocus();
                    return;
                } else if (edt_pincode.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Merchant_register.this, getResources().getString(R.string.enterpincode), Toast.LENGTH_LONG).show();
                    edt_pincode.requestFocus();
                    return;
                } else if (spn_city.getSelectedItemPosition() == 0) {
                    Toast.makeText(Merchant_register.this, getResources().getString(R.string.entercity), Toast.LENGTH_LONG).show();
                    spn_city.requestFocus();
                    return;
                } else {

                    if (spn_gst_category.getSelectedItemPosition() == 0) {
                        if (edt_pan_number.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.enterpannumber), Toast.LENGTH_LONG).show();
                            edt_pan_number.requestFocus();
                            return;
                        } else if (edt_pan_number.getText().length() < 10) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidpannumber), Toast.LENGTH_LONG).show();
                            edt_pan_number.requestFocus();
                            return;
                        } else if (!isValidPAN(edt_pan_number.getText().toString())) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidpannumber), Toast.LENGTH_LONG).show();
                            edt_pan_number.requestFocus();
                            return;
                        } else if (!isValidGSTIN(edt_gst_tinno.getText().toString()) && edt_gst_tinno.getText().toString().length() > 0) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            edt_gst_tinno.requestFocus();
                            return;
                        }

                    } else if (spn_gst_category.getSelectedItemPosition() > 0) {
                        if (edt_gst_tinno.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.entergstnnumber), Toast.LENGTH_LONG).show();
                            edt_gst_tinno.requestFocus();
                            return;
                        } else if (edt_gst_tinno.getText().length() < 15) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            edt_gst_tinno.requestFocus();
                            return;
                        } else if (!isValidGSTIN(edt_gst_tinno.getText().toString())) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            edt_gst_tinno.requestFocus();
                            return;
                        }
                    }

                    if (edt_gst_tinno.getText().toString().length() == 15) {
                        String gst_no = edt_gst_tinno.getText().toString();
                        gst_no = gst_no.substring(0, 2);
                        int gstno_valid = Integer.parseInt(gst_no);
                        if (gstno_valid > 37) {
                            Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidgstnnumber), Toast.LENGTH_LONG).show();
                            edt_gst_tinno.requestFocus();
                            return;
                        }
                    }
                }

                if (edt_pincode.getText().toString().length() < 6) {
                    Toast.makeText(Merchant_register.this, getResources().getString(R.string.invalidpincode), Toast.LENGTH_LONG).show();
                    edt_pincode.requestFocus();
                    return;
                }

                if (edt_gst_tinno.getText().toString().equalsIgnoreCase(confirm_gst_no.getText().toString())) {
                    try {
                        // Here we convert Java Object to JSON
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("BusinessName", edt_business_name.getText()); // Set the first name/pair
                        jsonObj.put("GSTINNo", edt_gst_tinno.getText());
                        jsonObj.put("PANNo", edt_pan_number.getText());
                        jsonObj.put("GSTINCategoryId", GSTCategory_Model.get(spn_gst_category.getSelectedItemPosition()).getGSTDealerCategoryId());
                        jsonObj.put("AddressLineOne", edt_address_line1.getText());
                        jsonObj.put("AddressLineTwo", edt_address_line2.getText());
                        jsonObj.put("Pincode", edt_pincode.getText());
                        jsonObj.put("CityId", city_id);
                        jsonObj.put("StateId", state_id);
                        int i = spn_gst_category.getSelectedItemPosition();
                        jsonObj.put("GSTCategoryId", GSTCategory_Model.get(i).getGSTDealerCategoryId());
                        sharedPreference_main.set_GSTType(String.valueOf(GSTCategory_Model.get(i).getGSTDealerCategoryId()));
                        String[] ABC = new String[1];
                        ABC[0] = String.valueOf(GSTBusiness_Model.get(spn_business_category.getSelectedItemPosition()).getGSTBusinessCategoryId());
                        jsonObj.put("BusinessCategoryId", new JSONArray().put(ABC[0]));
                        Log.d("vikash", jsonObj.toString());


                        String get_business_name = edt_business_name.getText().toString();
                        String get_gst_tin = edt_gst_tinno.getText().toString();
                        String get_gst_category_id = String.valueOf(GSTCategory_Model.get(spn_gst_category.getSelectedItemPosition()).getGSTDealerCategoryId());
                        String get_gst_category_name = GSTCategory_Model.get(spn_gst_category.getSelectedItemPosition()).getGSTDealerCategoryName();


                      String  get_gst_business_name = GSTBusiness_Model.get(spn_business_category.getSelectedItemPosition()).getGSTBusinessCategoryName();
                       String get_gst_business_id = String.valueOf(GSTBusiness_Model.get(spn_business_category.getSelectedItemPosition()).getGSTBusinessCategoryId());


                        if (get_business_name != "" && get_business_name != null) {
                            sharedPreference_main.setGSTBusinessCategoryName(get_business_name);
                        }


//                        if (gst_register != "" && gst_register != null) {
//                            sharedPreference.setGST_REG(gst_register);
//                        }

                        if (get_gst_tin != "" && get_gst_tin != null) {
                            sharedPreference_main.setGSTIN(get_gst_tin);
                        }
                        if (get_gst_category_name != "" && get_gst_category_name != null) {
                            sharedPreference_main.setGSTDealerCategoryName(get_gst_category_name);
                        }

                        if (get_gst_category_id != "" && get_gst_category_id != null) {
                            sharedPreference_main.setGSTCategoryId(get_gst_category_id);
                        }

                        if (get_gst_business_id != "" && get_gst_business_id != null) {
                            sharedPreference_main.setGSTBusinessCategoryId(get_gst_business_id);
                        }

                        if (get_gst_business_name != "" && get_gst_business_name != null) {
                            sharedPreference_main.setGSTBusinessCategoryName(get_gst_business_name);
                        }

                        String get_stateid = String.valueOf(state_id);
                        if (get_stateid != "" && get_stateid != null) {
                            sharedPreference_main.setStateId(get_stateid);
                        }
                        UpdateGSTTinDetail(jsonObj.toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(Merchant_register.this, getResources().getString(R.string.confirmGST), Toast.LENGTH_LONG).show();
                    confirm_gst_no.requestFocus();
                    return;

                }

            }
        });
    }

    private void UpdateGSTTinDetail(final String jsonList) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "MerchantStore/updateMerchantGSTINProfile";
        //http://www.aaramshop.co.in/api/index.php/
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                sharedPreference_main.getbase_inpk_url() + tag_json_obj,
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
                                Toast.makeText(Merchant_register.this, "Information successfully updated", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(Merchant_register.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
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
                params.put("GSTINDetail", jsonList);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
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
                sharedPreference_main.getbase_inpk_url() + tag_json_obj,
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
                                City_Adapter = new CityAdapter(Merchant_register.this, R.id.drop_down_head, City_Model);
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
                                Toast.makeText(Merchant_register.this, jsonO.getString("message"), Toast.LENGTH_SHORT).show();
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

    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        finish();
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
}

