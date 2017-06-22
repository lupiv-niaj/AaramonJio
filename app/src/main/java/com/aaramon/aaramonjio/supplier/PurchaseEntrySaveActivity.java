package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.aaramon.aaramonjio.order.StaticVariable.TABLE_AS_TEMP_PURCHASE;
import static com.aaramon.aaramonjio.order.StaticVariable.TABLE_AS_TEMP_PURCHASE_DETAIL;


public class PurchaseEntrySaveActivity extends Activity {
    SupplierService supplierService = new SupplierService();
    private int PurchaseEntryId;
    private int SupplierId;
    String SupplierCode;
    String SupplierName;
    String PaymentMode;
    String PaymentTerm;

    SupplierService SupplierService = new SupplierService();
    PurchaseEntryModel PurchaseEntryModel = new PurchaseEntryModel();
    TextView total_amount, total_item;
    String stateid = "", gst_category_id = "";
TextView TaxAmountView;
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
        setContentView(R.layout.activity_purchase_entry_save);

        // Business Logic
        Bundle bun = getIntent().getExtras();
        PurchaseEntryId = Integer.parseInt(bun.getString("PurchaseEntryId"));
        SupplierId = Integer.parseInt(bun.getString("SupplierId"));
        SupplierCode = bun.getString("SupplierCode");
        SupplierName = bun.getString("SupplierName");
        PaymentMode = bun.getString("PaymentMode");
        PaymentTerm = bun.getString("PaymentTerm");
        stateid = bun.getString("stateid");
        gst_category_id = bun.getString("gst_category_id");
        total_amount = (TextView) findViewById(R.id.TotalAmountView);
        total_item = (TextView) findViewById(R.id.ItemsCountView);
        TaxAmountView=(TextView)findViewById(R.id.TaxAmountView);
        PopulateSupplierDetails();
        PopulatePurchaseEntryDetails();

        ListView ProductsListView = (ListView) findViewById(R.id.ProductListViewId);
        TextView SavePurchaseEntryTextView = (TextView) findViewById(R.id.SavePurchaseEntryTextViewId);
        ImageView BackBtn = (ImageView) findViewById(R.id.BackButtonViewId);

        final PurchaseEntryProductsDetailAdapter PurchaseEntryProductsDetailAdapter = new PurchaseEntryProductsDetailAdapter(PurchaseEntrySaveActivity.this, total_amount, total_item,gst_category_id,stateid,TaxAmountView);
        PurchaseEntryModel.setPurchaseEntryId((long) PurchaseEntryId);
        PurchaseEntryProductsDetailAdapter.PurchaseEntryModel = PurchaseEntryModel;

        ProductsListView.setAdapter(PurchaseEntryProductsDetailAdapter);

        // Save Purchase Entry
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SavePurchaseEntryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ValidateActivityData() == true) {
                        PushPurchaseEntryToAaramShop(PurchaseEntryModel, PurchaseEntrySaveActivity.this);
                    }
                    //Intent TransferData = new Intent(getApplicationContext(), SupplierHistoryActivity.class);
                    //startActivity(TransferData);
                    //finish();

                } catch (Exception e) {
                    Toast.makeText(PurchaseEntrySaveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public Boolean ValidateActivityData() {
        if (Double.parseDouble(total_amount.getText().toString().trim()) <= 0) {
            Toast.makeText(PurchaseEntrySaveActivity.this, "Please Select atleast One Product", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void PushPurchaseEntryToAaramShop(final PurchaseEntryModel PEM, final Context context) {
        PurchaseEntryModel = PEM;

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String APIUrl = "http://www.aaramshop.co.in:80/api/index.php/Supplier/addPurchaseEntry";
        StringRequest Request = new StringRequest(
                com.android.volley.Request.Method.POST,
                APIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("VIJAYKUMAR", response);
                            pDialog.cancel();
                            JSONObject ResponseObject = new JSONObject(response);
                            JSONObject Control = ResponseObject.getJSONObject("Control");

                            if (Control.getString("Status").trim().equals("1")) {
                                // Update All Product Batches
//                                Log.d("VIJAYKUMAR", "UPDATING ALL STOCKS");
//                                Log.d("VIJAYKUMAR", PurchaseEntryModel.getProductEntryDetailList().size()+"");
                                supplierService.ChangeProductBatchesStock(PurchaseEntryModel.getProductEntryDetailList());
                                // Delete  Purchase Entry from Temp Tables
                                Log.d("VIJAYKUMAR", "CLEARING TEMP Purchase Enteries Tables");
                                supplierService.ClearTableData(TABLE_AS_TEMP_PURCHASE);
                                supplierService.ClearTableData(TABLE_AS_TEMP_PURCHASE_DETAIL);
                                // Call Actual  Acivity
                                Toast.makeText(context, "Purchase Entry Created & Upload Successfully", Toast.LENGTH_LONG).show();
                                Intent TransferData = new Intent(PurchaseEntrySaveActivity.this, SupplierAddUpdateSuccess.class);
                                TransferData.putExtra("SupplierCompany", "");
                                TransferData.putExtra("message","Purchase Entry Successfully Added");
//                                TransferData.putExtra("SupplierCode",SupplierCode);
                                //TransferData.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(TransferData);
                                //  finish();

                            } else {
                                Toast.makeText(context, Control.getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception API", e.getMessage());
                            //throw new Exception(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                //pDialog.cancel();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreference_Main sharedPreference_Main = new SharedPreference_Main(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceId", sharedPreference_Main.getDeviceId());
                params.put("DeviceType", "2");
                params.put("MerchantStoreId", (new SharedPreference_Main(context).getStoreId()));
                params.put("SupplierId", String.valueOf(PurchaseEntryModel.getSupplierModel().getSupplierId()));
                try {
                    String PurchaseEntry = supplierService.GeneratePurchaseEntryJson(PurchaseEntryModel);
                    params.put("PurchaseEntry", PurchaseEntry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(Request);
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
        try {
            String PurchaseEntryCompleteDetail = SupplierService.GetPurchaseEntryDetail(PurchaseEntryId);
            JSONObject PurchaseEntryObject = (JSONObject) new JSONArray(PurchaseEntryCompleteDetail).get(0);
            PurchaseEntryModel.setBillNumber(PurchaseEntryObject.getString("ret_bill_number"));
            PurchaseEntryModel.setBillDate(PurchaseEntryObject.getString("ret_bill_date"));
            PurchaseEntryModel.setPaymentMode(PurchaseEntryObject.getString("payment_type"));
            PurchaseEntryModel.setPaymentTerm(PurchaseEntryObject.getString("payment_term"));
            PurchaseEntryModel.getSupplierModel().setSupplierId(Integer.parseInt(PurchaseEntryObject.getString("supplier_id")));
            ((TextView) findViewById(R.id.BillNoValueView)).setText(PurchaseEntryModel.getBillNumber());
            ((TextView) findViewById(R.id.BillDateValueView)).setText(PurchaseEntryModel.getBillDate());
            ((TextView) findViewById(R.id.PurchaseTypeValueView)).setText(PurchaseEntryModel.getPaymentMode());

            JSONArray PurchaseEntryDetailsArray = new JSONArray(PurchaseEntryCompleteDetail);
            int TotalItems = 0;
            Double TotalAmount = 0D;
            double tax_amount=0D;
            for (int i = 0; i < PurchaseEntryDetailsArray.length(); i++) {
                PurchaseEntryDetailModel PurchaseEntryDetailModel = new PurchaseEntryDetailModel();
                PurchaseEntryDetailModel.setPurchaseEntryDetailId(PurchaseEntryDetailsArray.getJSONObject(i).getLong("purchase_detail_id"));
                PurchaseEntryDetailModel.getProductBatchModel().setProductBatchId(PurchaseEntryDetailsArray.getJSONObject(i).getLong("product_batch_id"));
                PurchaseEntryDetailModel.getProductBatchModel().setBatchNo(PurchaseEntryDetailsArray.getJSONObject(i).getString("batchno"));
//                PurchaseEntryDetailModel.getProductBatchModel().setExpiry(PurchaseEntryDetailsArray.getJSONObject(i).getString("expiry"));
                PurchaseEntryDetailModel.getProductBatchModel().setExpiry("2017-06-25");
                PurchaseEntryDetailModel.getProductBatchModel().getProductModel().setImageUrl(PurchaseEntryDetailsArray.getJSONObject(i).getString("image_url"));
                PurchaseEntryDetailModel.getProductBatchModel().getProductModel().setProductName(PurchaseEntryDetailsArray.getJSONObject(i).getString("product_name"));
                PurchaseEntryDetailModel.getProductBatchModel().getProductModel().setServerProductId(PurchaseEntryDetailsArray.getJSONObject(i).getLong("server_product_id"));
                PurchaseEntryDetailModel.setPurchaseRate(PurchaseEntryDetailsArray.getJSONObject(i).getDouble("purchase_rate"));
                PurchaseEntryDetailModel.setNewPurchaseRate(PurchaseEntryDetailsArray.getJSONObject(i).getDouble("new_purchase"));
                PurchaseEntryDetailModel.setMRP(PurchaseEntryDetailsArray.getJSONObject(i).getDouble("mrp"));
                PurchaseEntryDetailModel.setSP(PurchaseEntryDetailsArray.getJSONObject(i).getDouble("sp"));
                PurchaseEntryDetailModel.setQty(PurchaseEntryDetailsArray.getJSONObject(i).getInt("quantity"));
                PurchaseEntryDetailModel.setIgst(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("igst_rate")));
                PurchaseEntryDetailModel.setSgst(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("sgst_rate")));
                PurchaseEntryDetailModel.setCgst(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("cgst_rate")));
                PurchaseEntryDetailModel.setCess(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("cess_rate")));
                PurchaseEntryDetailModel.setTax_amt(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("total_tax_amt")));
                PurchaseEntryDetailModel.setTotal_amt(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("total_amt")));
                PurchaseEntryModel.getProductEntryDetailList().add(PurchaseEntryDetailModel);
                TotalItems += PurchaseEntryDetailModel.getQty();
                TotalAmount += PurchaseEntryDetailModel.getPurchaseRate() * PurchaseEntryDetailModel.getQty();
                tax_amount+=PurchaseEntryDetailModel.getTax_amt();
                Log.d("MAMU", PurchaseEntryDetailModel.getProductBatchModel().getExpiry());
            }

            total_item.setText(String.format(TotalItems + " Items"));
            total_amount.setText(String.format("%1$.2f", TotalAmount));
            TaxAmountView.setText(String.format("%1$.2f",tax_amount));
            PurchaseEntryModel.setTotalAmount(TotalAmount);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
