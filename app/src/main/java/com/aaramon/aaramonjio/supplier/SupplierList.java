package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.DelayAutoCompleteTextView;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.order.MainDashboard;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierList extends Activity {
    private TextView add_supplier, NoSuppliers;
    private ImageView img_back;
    DelayAutoCompleteTextView search_supplier;
    Dialog EditSupplier;
    SharedPreference_Main sharedPreference_Main;
    ArrayList<SupplierListModel> rowItems;
    private ListView supplierlist;
    private SupplierAdapter adaptersupplier;
    ProgressBar pb_indicator;
    Supplier_Search_Adapter adapter;

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
        setContentView(R.layout.activity_supplier_list);

        sharedPreference_Main = new SharedPreference_Main(SupplierList.this);
        img_back = (ImageView) findViewById(R.id.img_back_account);
        NoSuppliers = (TextView) findViewById(R.id.NoSuppliers);
        search_supplier = (DelayAutoCompleteTextView) findViewById(R.id.search_supplier);
        add_supplier = (TextView) findViewById(R.id.quickaddsupplier);
        supplierlist = (ListView) findViewById(R.id.supplierListView);
        search_supplier.setThreshold(3);
        search_supplier.setLoadingIndicator(pb_indicator);
        adapter = new Supplier_Search_Adapter(SupplierList.this,
                android.R.layout.simple_dropdown_item_1line, pb_indicator);
        search_supplier.setAdapter(adapter);
        add_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddSupplier.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        supplierlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sid = String.valueOf(rowItems.get(i).getSupplierId());
                String scode = String.valueOf(rowItems.get(i).getSupplierCode());
                String stateid = rowItems.get(i).getStateId();
                String gst_category_id = rowItems.get(i).getGSTCategoryId();

                Intent m = new Intent(getApplicationContext(), SupplierHistoryActivity.class);
                m.putExtra("supplier_id", sid);
                m.putExtra("SupplierCode", scode);
                m.putExtra("stateid", stateid);
                m.putExtra("category_id", gst_category_id);
                startActivity(m);

            }
        });

    }

    protected void onResume() {
        super.onResume();
        if (DataStatic.isInternetAvailable(SupplierList.this)) {
            rowItems = new ArrayList<SupplierListModel>();
            getsupplier();
        } else {
            Toast.makeText(SupplierList.this,
                    getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void getsupplier() {

        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "Supplier/getStoreSuppliers";
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

                                for (int i = 0; i < json_data.length(); i++) {
                                    JSONObject prod = json_data.getJSONObject(i);
                                    int supplier_id = Integer.parseInt(prod.getString("SupplierId"));
                                    String supplier_code = prod.getString("SupplierCode");
                                    String supplier_name = prod.getString("SupplierName");
                                    String supplier_image = prod.getString("SupplierImage");
                                    String supplier_company = prod.getString("SupplierCompany");
                                    int GSTRegister = Integer.parseInt(prod.getString("GSTINRegistered"));
                                    String GSTNNo = prod.getString("GSTNNo");
                                    String GSTCategoryId = prod.getString("GSTCategoryId");
                                    String StateId = prod.getString("StateId");
                                    String CityId = prod.getString("CityId");
                                    String StateCode = prod.getString("StateCode");
                                    String StateName = prod.getString("StateName");
                                    String CityName = prod.getString("CityName");

                                    if (supplier_company.equalsIgnoreCase("")) {
                                        supplier_company = "N/A";
                                    }

                                    String supplier_mobile = prod.getString("SupplierMobile");

                                    SupplierListModel dlm = new SupplierListModel();
                                    dlm.setSupplierId(supplier_id);
                                    dlm.setSupplierCode(supplier_code);
                                    dlm.setSupplierName(supplier_name);
                                    dlm.setSupplierImage(supplier_image);
                                    dlm.setSupplierCompany(supplier_company);
                                    dlm.setSupplierMobile(supplier_mobile);
                                    dlm.setGSTREgister(GSTRegister);
                                    dlm.setGSTNNo(GSTNNo);
                                    dlm.setGSTCategoryId(GSTCategoryId);
                                    dlm.setStateId(StateId);
                                    dlm.setCityId(CityId);
                                    dlm.setStateCode(StateCode);
                                    dlm.setStateName(StateName);
                                    dlm.setCityName(CityName);
                                    rowItems.add(dlm);
                                }
                                adaptersupplier = new SupplierAdapter(SupplierList.this, rowItems);
                                supplierlist.setAdapter(adaptersupplier);

                                if (rowItems.size() > 0) {
                                    NoSuppliers.setVisibility(View.INVISIBLE);
                                    supplierlist.setVisibility(View.VISIBLE);
                                } else {
                                    NoSuppliers.setVisibility(View.VISIBLE);
                                    supplierlist.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                Toast.makeText(SupplierList.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
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
                params.put("PageNo", "0");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    public class Supplier_Search_Adapter extends ArrayAdapter<SupplierListModel> implements
            Filterable {
        private ArrayList<SupplierListModel> Supplier_model;
        Context context;
        SharedPreference_Main sharedPreferenceMain, sharedPreferenceMain1;
        ProgressBar pb_indicator;

        public Supplier_Search_Adapter(Context context, int resource, ProgressBar pb_indicator
        ) {
            super(context, resource);
            this.context = context;
            Supplier_model = new ArrayList<SupplierListModel>();
            this.pb_indicator = pb_indicator;
            sharedPreferenceMain = new SharedPreference_Main(context);
        }

        @Override
        public int getCount() {
            return Supplier_model.size();
        }

        @Override
        public SupplierListModel getItem(int position) {
            return Supplier_model.get(position);
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
                            Supplier_model = new Search_supplier().execute(term).get();
                        } catch (Exception e) {
                            Log.d("HUS", "EXCEPTION " + e);
                        }
                        filterResults.values = Supplier_model;
                        filterResults.count = Supplier_model.size();
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
            View view = inflater.inflate(R.layout.show_supplier_adapter, parent,
                    false);

            // get Country
            final SupplierListModel supplier = Supplier_model.get(position);

            TextView tv_cust_name;

            tv_cust_name = (TextView) view.findViewById(R.id.tv_cust_name);
            tv_cust_name.setText(supplier.getSupplierName());
            //Log.d("SID", String.valueOf(supplier.getSupplierId()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_supplier.setText("");
                    Intent intent = new Intent(SupplierList.this,
                            SupplierHistoryActivity.class);
                    intent.putExtra("supplier_id", String.valueOf(supplier.getSupplierId()));
                    startActivity(intent);
                    //finish();
                }
            });
            return view;
        }

        // download mCountry list
        private class Search_supplier extends
                AsyncTask<String, Void, ArrayList<SupplierListModel>> {

            @Override
            protected ArrayList<SupplierListModel> doInBackground(String... params) {
                try {
                    sharedPreferenceMain = new SharedPreference_Main(context);
                    InputStream isr = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(
                            sharedPreferenceMain.getbase_inpk_url() + "Supplier/searchStoreSuppliers");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            1);
                    nameValuePairs.add(new BasicNameValuePair("SearchTerm",
                            params[0]));
                    nameValuePairs.add(new BasicNameValuePair("MerchantStoreId",
                            sharedPreferenceMain.getStoreId()));
                    nameValuePairs.add(new BasicNameValuePair("DeviceType", "2"));
                    nameValuePairs.add(new BasicNameValuePair("DeviceId", sharedPreferenceMain.getDeviceId()));
                    nameValuePairs.add(new BasicNameValuePair("PageNo", "0"));
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
                    ArrayList<SupplierListModel> supplierList = null;
                    if (controls.getString("Status").equals("1")) {
                        JSONArray json_data = jsonO.getJSONArray("Data");
                        supplierList = new ArrayList<SupplierListModel>();

                        for (int i = 0; i < json_data.length(); i++) {
                            JSONObject prod = json_data.getJSONObject(i);
                            String supplier_name = prod.getString("SupplierName");
                            String supplier_id = prod.getString("SupplierId");
                            SupplierListModel dlm = new SupplierListModel();
                            dlm.setSupplierName(supplier_name);
                            dlm.setSupplierId(Integer.parseInt(supplier_id));

                            supplierList.add(dlm);
                        }
                        return supplierList;
                    } else {
                        Toast.makeText(SupplierList.this, "Supplier not found", Toast.LENGTH_LONG).show();
                        return null;
                    }


                } catch (Exception e) {
                    Log.d("HUS", "EXCEPTION " + e);
                    return null;
                }
            }
        }
    }

}
