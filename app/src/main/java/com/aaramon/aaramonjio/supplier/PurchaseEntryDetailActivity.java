package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PurchaseEntryDetailActivity extends Activity {
    SupplierService supplierService = new SupplierService();
    private int PurchaseEntryId;
    int SupplierId;
    String SupplierCode;
    String SupplierName;
    String PaymentMode;
    String PaymentTerm;
    SharedPreference_Main sharedPreference_Main;
    ArrayList<PurchaseEntryBillDetailModel> rowItems;
    SupplierService SupplierService = new SupplierService();
    PurchaseEntryModel PurchaseEntryModel = new PurchaseEntryModel();
    TextView total_amount, total_item, bill_date, bill_no, purchase_type;
    String SupplierAddress;
    String SupplierMobile;
    String SupplierOutstanding;
    private PurchaseEntryDetailAdapter adapterpe;
    ListView productlist;

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
        setContentView(R.layout.activity_purchase_entry_detail);
        sharedPreference_Main = new SharedPreference_Main(PurchaseEntryDetailActivity.this);
        // Business Logic
        Bundle bun = getIntent().getExtras();
        PurchaseEntryId = Integer.parseInt(bun.getString("PurchaseEntryId"));
        SupplierId = Integer.parseInt(bun.getString("SupplierId"));
        SupplierCode = bun.getString("SupplierCode");
        SupplierName = bun.getString("SupplierName");
//        SupplierAddress = bun.getString("SupplierAddress");
//        SupplierMobile = bun.getString("SupplierMobile");
//        SupplierOutstanding = bun.getString("SupplierOutstanding");
        //PaymentMode = bun.getString("PaymentMode");
        //PaymentTerm = bun.getString("PaymentTerm");
        total_amount = (TextView) findViewById(R.id.TotalAmountView);
        total_item = (TextView) findViewById(R.id.ItemsCountView);
        bill_date = (TextView) findViewById(R.id.BillDateValueView);
        bill_no = (TextView) findViewById(R.id.BillNoValueView);
        purchase_type = (TextView) findViewById(R.id.PurchaseTypeValueView);

        PopulateSupplierDetails();
        PopulatePurchaseEntryDetails();

        productlist = (ListView) findViewById(R.id.ProductListViewId);
        ImageView BackBtn = (ImageView) findViewById(R.id.BackButtonViewId);

        /*final PurchaseEntryDetailAdapter PurchaseEntryDetailAdapter = new PurchaseEntryDetailAdapter(PurchaseEntryDetailActivity.this, total_amount, total_item);
        PurchaseEntryModel.setPurchaseEntryId((long) PurchaseEntryId);
        PurchaseEntryDetailAdapter.PurchaseEntryModel = PurchaseEntryModel;*/

        //ProductsListView.setAdapter(PurchaseEntryDetailAdapter);

        // Save Purchase Entry
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*SavePurchaseEntryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ValidateActivityData() == true) {
                        PushPurchaseEntryToAaramShop(PurchaseEntryModel, PurchaseEntryDetailActivity.this);
                    }
                    //Intent TransferData = new Intent(getApplicationContext(), SupplierHistoryActivity.class);
                    //startActivity(TransferData);
                    //finish();

                } catch (Exception e) {
                    Toast.makeText(PurchaseEntryDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }


    public void PopulateSupplierDetails() {
        try {
            ((TextView) findViewById(R.id.SupplierNameHeaderTextViewId)).setText(SupplierName);
            ((TextView) findViewById(R.id.SupplierNameTextViewId)).setText(SupplierName);
            ((TextView) findViewById(R.id.SupplierCodeTextViewId)).setText("Supplier Code : " + SupplierCode);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void PopulatePurchaseEntryDetails() {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "Supplier/getPurchaseEntryDetail";
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
                                rowItems=new ArrayList<>();
                                JSONObject json_data =jsonO.getJSONObject("Data");
                                //String purchase = json_data.getString("PurchaseEntries");


                                JSONObject purchase_json = json_data.getJSONObject("PurchaseEntries");
                                total_item.setText(purchase_json.getString("TotalItems"));
                                total_amount.setText(purchase_json.getString("OrderAmount"));
                                bill_date.setText(purchase_json.getString("BillDate"));
                                bill_no.setText(purchase_json.getString("BillNo"));
                                purchase_type.setText(purchase_json.getString("PurchaseType"));
                                JSONArray jsonArray = purchase_json.getJSONArray("PurchaseEntryItems");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject prod = jsonArray.getJSONObject(i);
                                    String ProductName = prod.getString("ProductName");
                                    int Quantity = Integer.parseInt(prod.getString("Quantity"));
                                    double PurchasePrice = Double.parseDouble(prod.getString("PurchasePrice"));
                                    String ProductImage = prod.getString("ProductImage");
                                    if (ProductName.equalsIgnoreCase("")) {
                                        ProductName = "N/A";
                                    }

                                    PurchaseEntryBillDetailModel dlm = new PurchaseEntryBillDetailModel();
                                    dlm.setProductName(ProductName);
                                    dlm.setQty(Quantity);
                                    dlm.setPurchaseRate(PurchasePrice);
                                    dlm.setProductImage(ProductImage);
                                    /*dlm.setTotalItems(TotalItems);
                                    dlm.setOrderAmount(OrderAmount);
                                    dlm.setBillDate(BillDate);
                                    dlm.setBillNo(BillNo);
                                    dlm.setPurchaseType(PurchaseType);*/
                                    rowItems.add(dlm);
                                }
                                adapterpe = new PurchaseEntryDetailAdapter(PurchaseEntryDetailActivity.this, rowItems);
                                productlist.setAdapter(adapterpe);
                            } else {
                                Toast.makeText(PurchaseEntryDetailActivity.this, controls.getString("Message"), Toast.LENGTH_SHORT).show();
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
                params.put("SupplierId", String.valueOf(SupplierId));
                params.put("PurchaseEntryId", String.valueOf(PurchaseEntryId));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }
}
