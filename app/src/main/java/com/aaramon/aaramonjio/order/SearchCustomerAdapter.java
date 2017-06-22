package com.aaramon.aaramonjio.order;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchCustomerAdapter extends ArrayAdapter<SearchCustomerModel> implements
        Filterable {
    public ArrayList<SearchCustomerModel> Customer_model;
    Context context;
    SharedPreference_Main sharedPreferenceMain;
    ProgressBar pb_indicator;


    public SearchCustomerAdapter(Context context, int resource, ProgressBar pb_indicator) {
        super(context, resource);
        this.context = context;
        Customer_model = new ArrayList<SearchCustomerModel>();
        this.pb_indicator = pb_indicator;
        sharedPreferenceMain = new SharedPreference_Main(context);

    }

    @Override
    public int getCount() {
        return Customer_model.size();
    }

    @Override
    public SearchCustomerModel getItem(int position) {
        return Customer_model.get(position);
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
                        Customer_model = new Search_customer().execute(term).get();
                    } catch (Exception e) {
                        Log.d("HUS", "EXCEPTION " + e);
                    }
                    filterResults.values = Customer_model;
                    filterResults.count = Customer_model.size();
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
        View view = inflater.inflate(R.layout.search_customer_adapter_layout, parent,
                false);

        // get Country
        final SearchCustomerModel contry = Customer_model.get(position);

        TextView tv_cust_name;

        tv_cust_name = (TextView) view.findViewById(R.id.tv_customer);
        tv_cust_name.setText(contry.getShopper_name() + "(" + contry.getShopper_mobile() + ")");

        return view;
    }

    // download mCountry list
    private class Search_customer extends
            AsyncTask<String, Void, ArrayList<SearchCustomerModel>> {

        @Override
        protected ArrayList<SearchCustomerModel> doInBackground(String... params) {
            try {
                InputStream isr = null;
                // To getting Data
                // exception = "";
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(
                        sharedPreferenceMain.getbase_inpk_url() + "MerchantCustomers/searchMerchantCustomers");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        1);
                nameValuePairs.add(new BasicNameValuePair("search_term",
                        params[0]));
                nameValuePairs.add(new BasicNameValuePair("merchant_store_id",
                        sharedPreferenceMain.getStoreId()));
                nameValuePairs.add(new BasicNameValuePair("deviceType", "2"));
                nameValuePairs.add(new BasicNameValuePair("page_no", "0"));
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

                // parse JSON and store it in the list
                String jsonString = sb.toString();


                JSONObject jsonO = new JSONObject(jsonString);
                ArrayList<SearchCustomerModel> countryList = null;
                JSONObject controls = jsonO.getJSONObject("Control");
                countryList = new ArrayList<SearchCustomerModel>();
                if (controls.getString("Status").equals("1")) {
                    JSONArray jsonAoffers = jsonO
                            .getJSONArray("Data");
                    for (int i = 0; i < jsonAoffers.length(); i++) {
                        JSONObject jsonOoffers = jsonAoffers
                                .getJSONObject(i);
                        SearchCustomerModel alm = new SearchCustomerModel();
                        JSONArray jsonOcustomer_address_array = jsonOoffers
                                .getJSONArray("shopper_address");
                        JSONObject state = jsonOcustomer_address_array.getJSONObject(0);
                        alm.setState_id(Integer.parseInt(state.getString("shopper_state_id")));
                        alm.setPincode(state.getString("shopper_pincode"));
                        alm.setShopper_address(jsonOcustomer_address_array.toString());
                        alm.setChecked(false);
                        alm.setShopper_id(jsonOoffers
                                .getString("shopper_id"));
                        alm.setShopper_name(jsonOoffers
                                .getString("shopper_name"));
                        alm.setShopper_mobile(jsonOoffers
                                .getString("shopper_mobile"));
                        alm.setBusinessName(jsonOoffers.getString("BusinessName"));
                        alm.setGSTIN(jsonOoffers.getString("GSTIN"));
//                        alm.setPAN(jsonOoffers.getString("PAN"));

                        countryList.add(alm);
                    }
                } else {
                    SearchCustomerModel alm = new SearchCustomerModel();
                    alm.setShopper_address("");
                    alm.setChecked(false);
                    alm.setShopper_id("0");
                    alm.setShopper_name("Add New Customer");
                    alm.setShopper_mobile(params[0]);
                    countryList.add(0, alm);
                }
                return countryList;

            } catch (Exception e) {
                Log.d("HUS", "EXCEPTION " + e);
                return null;
            }
        }
    }
}

