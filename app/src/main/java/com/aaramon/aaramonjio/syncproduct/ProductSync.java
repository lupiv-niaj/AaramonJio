package com.aaramon.aaramonjio.syncproduct;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
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

/**
 * Created by DELL STORE on 16-May-17.
 */

public class ProductSync extends Service implements StaticVariable {
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
            Log.d("Track Delivery Boy API Call", "Call");
            readWebPage();
        } else {
            Log.d("CAll in without internet", "Call");
            return;
        }
    }

    public void readWebPage() {
        // TODO Auto-generated method stub
        //http://www.aaramshop.co.in/api/index.php/
        final QueryClass query_check = new QueryClass();
        long lastid1 = query_check.InsertLastProductid();
        if (lastid1 == 0) {
            sharedPreference_Main.set_last_sync("0000-00-00 00:00:00");
            sharedPreference_Main.set_server_product_id("0");
        }
        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                "http://www.aaramon.com/sync-api/index.php/Product/getProduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response ", response);
                            JSONObject jsonO = new JSONObject(response);

//                            DatabaseHandlerService.InitializeDataBase(context);
                            long lastid = query_check.InsertLastProductid();
                            if (lastid == 0) {
                                sharedPreference_Main.set_last_sync("0000-00-00 00:00:00");
                                sharedPreference_Main.set_server_product_id("0");
                            }

                            if (jsonO.getString("status").equalsIgnoreCase("1")) {
                                sharedPreference_Main.set_last_sync(jsonO.getString("DATETIME"));
                                JSONArray jsonArray = jsonO.getJSONArray("Products");
                                int last_server_id = Integer.parseInt(sharedPreference_Main.get_server_product_id());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String store_company_id = jsonObject.getString("store_company_id");
                                    String server_company_id = jsonObject.getString("server_company_id");
                                    String company_name = jsonObject.getString("company_name");
                                    String store_brand_id = jsonObject.getString("store_brand_id");
                                    String server_brand_id = jsonObject.getString("server_brand_id");
                                    String brand_name = jsonObject.getString("brand_name");
                                    String brand_image = jsonObject.getString("brand_icon");

                                    String store_master_category_id = jsonObject.getString("store_master_category_id");
                                    String server_master_category_id = jsonObject.getString("server_master_category_id");
                                    String master_category_name = jsonObject.getString("master_category_name");
                                    String master_category_level = jsonObject.getString("master_category_level");
                                    String master_category_type = jsonObject.getString("master_category_type");
                                    long master_parent_id = Long.parseLong(jsonObject.getString("master_parent_id"));
                                    String master_category_image = jsonObject.getString("master_category_icon");


                                    String store_category_id = jsonObject.getString("store_category_id");
                                    String server_category_id = jsonObject.getString("server_category_id");
                                    String category_name = jsonObject.getString("category_name");
                                    String category_level = jsonObject.getString("category_level");
                                    String category_type = jsonObject.getString("category_type");
                                    String category_image = jsonObject.getString("category_icon");
                                    String store_subcategory_id = jsonObject.getString("store_subcategory_id");
                                    String server_subcategory_id = jsonObject.getString("server_subcategory_id");
                                    String subcategory_name = jsonObject.getString("subcategory_name");
                                    String subcategory_level = jsonObject.getString("subcategory_level");
                                    String subcategory_type = jsonObject.getString("subcategory_type");
                                    String store_product_id = jsonObject.getString("store_product_id");
                                    String server_merchant_store_product_id = jsonObject.getString("server_merchant_store_product_id");
                                    String server_product_id = jsonObject.getString("server_product_id");
                                    String product_name = jsonObject.getString("product_name");
                                    String is_deleted = jsonObject.getString("is_deleted");
                                    double product_price = Double.parseDouble(jsonObject.getString("product_price"));
                                    double selling_price = Double.parseDouble(jsonObject.getString("selling_price"));
                                    String ean_code = jsonObject.getString("ean_code");
                                    String product_image = jsonObject.getString("product_image");
                                    String tax_percentage = jsonObject.getString("tax_percentage");
                                    String cess = jsonObject.getString("CessTax");
                                    JSONObject tax_json = jsonObject.getJSONObject("GSTDetail");
                                    String cgst = tax_json.getString("CGSTRate");
                                    String sgst = tax_json.getString("SGSTRate");
                                    String igst = tax_json.getString("IGSTRate");
                                    String default_tax = "0.0";
                                    ean_code = ean_code.toString().trim();


                                    if (last_server_id > Integer.parseInt(server_product_id)) {
                                    } else {
                                        sharedPreference_Main.set_server_product_id(server_product_id);
                                    }

                                    long check_company = query_check.FindCompanyID(server_company_id);
                                    if (check_company == 0) {
                                        try {
                                            check_company = query_check.InsertCompany(server_company_id, company_name, "");
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }


                                    long check_brand = query_check.FindBrandID(server_brand_id);
                                    if (check_brand == 0) {
                                        try {
                                            check_brand = query_check.InsertBrand(server_brand_id, brand_name, check_company, brand_image);
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }


                                    long check_master_category = query_check.FindMasterCategoryId(server_master_category_id);
                                    if (check_master_category == 0) {
                                        try {
                                            check_master_category = query_check.InsertCategory(server_master_category_id, master_category_name, master_parent_id, master_category_level, master_category_type, master_category_image, default_tax, default_tax, default_tax);
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }


                                    long check_category = query_check.FindMasterCategoryId(server_category_id);
                                    if (check_category == 0) {
                                        try {
                                            check_category = query_check.InsertCategory(server_category_id, category_name, check_master_category, category_level, category_type, category_image, default_tax, default_tax, default_tax);
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }


                                    long check_subcategory = query_check.FindMasterCategoryId(server_subcategory_id);
                                    if (check_subcategory == 0) {
                                        try {
                                            check_subcategory = query_check.InsertCategory(server_subcategory_id, subcategory_name, check_category, subcategory_level, subcategory_type, "", cgst, sgst, igst);
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }

                                    long check_product_id = query_check.FindMasterProductID(server_merchant_store_product_id);
                                    if (check_product_id == 0) {
                                        try {
                                            check_product_id = query_check.InsertProduct(server_merchant_store_product_id, check_brand, check_subcategory, product_name, String.valueOf(product_price), ean_code, ean_code, product_image, tax_percentage, cess);
                                            long check_product_barcode = query_check.InsertProductBarcode(ean_code, check_product_id);
                                            String offer_type = "0";
                                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                            Calendar from_calender, to_calender;
                                            to_calender = Calendar.getInstance();
                                            from_calender = Calendar.getInstance();
                                            to_calender.add(Calendar.MONTH, 1);
                                            String fromdate = "", todate = "";
                                            if (product_price > selling_price) {
                                                offer_type = "1";
                                                fromdate = sdfDate.format(from_calender.getTime());
                                                todate = sdfDate.format(to_calender.getTime());
                                            } else {
                                                offer_type = "0";
                                                todate = "0000-00-00 00:00:00";
                                                fromdate = "0000-00-00 00:00:00";
                                            }
                                            long product_batch_id = query_check.InsertProductBatch(sharedPreference_Main.getStoreId(), "1", "XXX", todate, String.valueOf(product_price), String.valueOf(product_price), String.valueOf(selling_price), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }
                                }
                            } else {
                                long lastid1 = query_check.InsertLastProductid();
                                if (lastid1 == 0) {
                                    sharedPreference_Main.set_last_sync("0000-00-00 00:00:00");
                                    sharedPreference_Main.set_server_product_id("0");
                                }
                            }
                            //  stopSelf();
//                            stopSelf();
                        } catch (JSONException e) {
//                            e.printStackTrace();
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
                params.put("id", sharedPreference_Main.get_server_product_id());
                params.put("entity_type", "4");
                params.put("merchant_store_id", sharedPreference_Main.getStoreId());
                params.put("datetime", sharedPreference_Main.get_last_sync());
                Log.d("Sync Response", params.toString());
                //   params.put("key_update", "{\"products\":[{\"store_product_id\":\"\",\"server_merchant_store_product_id\":\"\"}]}");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }
}
