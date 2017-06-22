package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PurchaseEntryProductActivity extends Activity {
    SupplierService SupplierService = new SupplierService();
    private int PurchaseEntryId;
    private int PurchaseEntryDetailId;
    private long ProductBatchId;
    private long ProductId;
    private int SupplierId;
    private String SupplierCode;
    private String SupplierName;
    private String PaymentMode;
    private String PaymentTerm;
    TextView CGSTView, IGSTView, SGSTView, CESSView, TotalView, TxtView, ProductNameBatchNoId;
    ImageView img_back_account;
    PurchaseEntryDetailModel PurchaseEntryDetailModel = new PurchaseEntryDetailModel();
    EditText QtyEditView, PurchasePriceEditView, MRPEditView, SPEditView, BatchNoEditView, ExpiryEditView, EANCODEEditView;
    TextView ProductNameTextView, ProductPurchasePriceTextView;
    RelativeLayout PurchaseEntryView, ScanView;
    Spinner TaxSchedulerSpinner;
    String stateid, gst_category_id;
    SharedPreference_Main sharedPreference_main;

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
        setContentView(R.layout.activity_purchase_entry_product);
        // Business Logic
        sharedPreference_main = new SharedPreference_Main(PurchaseEntryProductActivity.this);
        try {
            Bundle bun = getIntent().getExtras();
            PurchaseEntryId = Integer.parseInt(bun.getString("PurchaseEntryId"));
            PurchaseEntryDetailId = Integer.parseInt(bun.getString("PurchaseEntryDetailId"));
            SupplierId = Integer.parseInt(bun.getString("SupplierId"));
            SupplierCode = bun.getString("SupplierCode");
            SupplierName = bun.getString("SupplierName");
            PaymentMode = bun.getString("PaymentMode");
            PaymentTerm = bun.getString("PaymentTerm");
            stateid = bun.getString("stateid");
            gst_category_id = bun.getString("gst_category_id");
            img_back_account = (ImageView) findViewById(R.id.img_back_account);
            QtyEditView = (EditText) findViewById(R.id.QuantityEditTextId);
            PurchasePriceEditView = (EditText) findViewById(R.id.PurchasePriceEditTextId);
            MRPEditView = (EditText) findViewById(R.id.MRPEditTextId);
            SPEditView = (EditText) findViewById(R.id.SalePriceEditTextId);
            BatchNoEditView = (EditText) findViewById(R.id.BatchEditTextId);
            ExpiryEditView = (EditText) findViewById(R.id.ExpiryEditTextId);
            EANCODEEditView = (EditText) findViewById(R.id.EanCodeEditTextId);
            ProductNameTextView = (TextView) findViewById(R.id.ProductNameTextViewId);
            ProductPurchasePriceTextView = (TextView) findViewById(R.id.ProductPurchasePricePriceId);
            TaxSchedulerSpinner = (Spinner) findViewById(R.id.TaxScheduleDataId);
            CGSTView = (TextView) findViewById(R.id.ProductCGSTId);
            IGSTView = (TextView) findViewById(R.id.ProductIGSTId);
            SGSTView = (TextView) findViewById(R.id.ProductSGSTId);
            CESSView = (TextView) findViewById(R.id.ProductCESSId);
            TotalView = (TextView) findViewById(R.id.ProductTOTALId);
            TxtView = (TextView) findViewById(R.id.ProductTAXId);
            ProductNameBatchNoId = (TextView) findViewById(R.id.ProductNameBatchNoId);
            PurchaseEntryView = (RelativeLayout) findViewById(R.id.PurchaseEntryLayoutId);
            ScanView = (RelativeLayout) findViewById(R.id.ScanLayoutId);

            TaxSchedulesAdapter TaxSchedulesAdapter = new TaxSchedulesAdapter(PurchaseEntryProductActivity.this);
            TaxSchedulesAdapter.TaxScheduleList = getTaxScheduleList(TaxScheduleModel.ACTIVETAXES);
            TaxSchedulerSpinner.setAdapter(TaxSchedulesAdapter);

            // Populate Product Details
            PopulateProductDetails();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        img_back_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Event Haandlers
        PurchaseEntryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateActivityData() == true) {
                    try {
                        // Generate Product Batch Model
                        ProductBatchModel ProductBatchModel = GenerateProductBatch();
                        // Update Product Batch
                        SupplierService.UpdateProductBatch(ProductBatchModel);
                        SupplierService.UpdateProductBarCode(ProductBatchModel);

                        // Generate Purchase Entry Detail Model
                        PurchaseEntryDetailModel PurchaseEntryDetailModel = GeneratePurchaseEntryDetail();
                        // Update Purchase Entry Detail
                        SupplierService.UpdatePurchaseEntryDetail(PurchaseEntryDetailModel);

                        Log.d("RAGHU", "UPDATED Purchase Detail & Batch");


                        // Proceed to Purchase Entry Completion Activity
                        Intent TransferData = new Intent(getApplicationContext(), PurchaseEntrySaveActivity.class);
                        TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                        TransferData.putExtra("SupplierId", SupplierId + "");
                        TransferData.putExtra("SupplierCode", SupplierCode + "");
                        TransferData.putExtra("SupplierName", SupplierName);
                        TransferData.putExtra("PaymentTerm", PaymentTerm);
                        TransferData.putExtra("stateid", stateid);
                        TransferData.putExtra("gst_category_id", gst_category_id);
                        TransferData.putExtra("PaymentMode", PaymentMode);
                        startActivity(TransferData);

                    } catch (Exception e) {
                        Toast.makeText(PurchaseEntryProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        ScanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateActivityData() == true) {
                    try {
                        // Generate Product Batch Model
                        ProductBatchModel ProductBatchModel = GenerateProductBatch();
                        // Update Product Batch
                        SupplierService.UpdateProductBatch(ProductBatchModel);
                        SupplierService.UpdateProductBarCode(ProductBatchModel);

                        // Generate Purchase Entry Detail Model
                        PurchaseEntryDetailModel PurchaseEntryDetailModel = GeneratePurchaseEntryDetail();
                        // Update Purchase Entry Detail
                        SupplierService.UpdatePurchaseEntryDetail(PurchaseEntryDetailModel);

                        Log.d("RAGHU", "UPDATED Purchase Detail & Batch");

                        // Calculate TotalItems & Amount

                        String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);

                        // Proceed to Purchase Entry Completion Activity
                        Intent TransferData = new Intent(getApplicationContext(), ScanPurchaseProducts.class);
                        TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                        TransferData.putExtra("SupplierId", SupplierId + "");
                        TransferData.putExtra("SupplierCode", SupplierCode + "");
                        TransferData.putExtra("SupplierName", SupplierName);
                        TransferData.putExtra("PaymentTerm", PaymentTerm);
                        TransferData.putExtra("PaymentMode", PaymentMode);
                        TransferData.putExtra("TotalItems", TotalItemsAmount[1]);
                        TransferData.putExtra("TotalAmount", TotalItemsAmount[0]);
                        TransferData.putExtra("TaxAmount", TotalItemsAmount[2]);
                        TransferData.putExtra("stateid", stateid);
                        TransferData.putExtra("gst_category_id", gst_category_id);
                        startActivity(TransferData);
//                        finish();
                    } catch (Exception e) {
                        Toast.makeText(PurchaseEntryProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }


    public ArrayList<TaxScheduleModel> getTaxScheduleList(int Status) {
        ArrayList<TaxScheduleModel> TaxScheduleList = new ArrayList<TaxScheduleModel>();
        try {
            TaxScheduleList = SupplierService.getTaxScheduleList(Status, Integer.parseInt(new SharedPreference_Main(PurchaseEntryProductActivity.this).getStoreId()));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return TaxScheduleList;
    }

    public void PopulateProductDetails() {
        try {
            Log.d("RAGHU", PurchaseEntryDetailId + "");
            String ProductDetails = SupplierService.GetPurchaseEntryProductDetail(PurchaseEntryDetailId);
            Log.d("RAGHU", ProductDetails);

            // Populate Fields
            JSONObject ProductObject = (JSONObject) new JSONArray(ProductDetails).get(0);
            ProductId = Long.parseLong(ProductObject.getString("product_id"));
            ProductBatchId = Long.parseLong(ProductObject.getString("product_batch_id"));
            QtyEditView.setText(ProductObject.getString("quantity"));
            PurchasePriceEditView.setText(ProductObject.getString("purchase_rate"));
            MRPEditView.setText(ProductObject.getString("mrp"));
            SPEditView.setText(ProductObject.getString("sp"));
            BatchNoEditView.setText(ProductObject.getString("batchno"));
            //ExpiryEditView.setText(ProductObject.getString("expiry"));
            ExpiryEditView.setText("2017-06-25");
            EANCODEEditView.setText(ProductObject.getString("ean_code"));
            ProductNameTextView.setText(ProductObject.getString("product_name"));
            ProductPurchasePriceTextView.setText(ProductObject.getString("purchase_rate"));


            double cess = Double.parseDouble(ProductObject.getString("cess_rate"));
            double igst = Double.parseDouble(ProductObject.getString("igst_rate"));
            double sgst = Double.parseDouble(ProductObject.getString("sgst_rate"));
            double cgst = Double.parseDouble(ProductObject.getString("cgst_rate"));
            double price = Double.parseDouble(ProductObject.getString("purchase_rate"));
            int qty = Integer.parseInt(QtyEditView.getText().toString());
            CGSTView.setText(cgst + "");
            IGSTView.setText(igst + "");
            SGSTView.setText(sgst + "");
            CESSView.setText(cess + "");
            double total_amt = 0.0;
            total_amt = price * qty;
            if (sharedPreference_main.getGSTCategoryId().equalsIgnoreCase("2") && gst_category_id.equalsIgnoreCase("2")) {
                if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                    //cgst+sgst+cess
                    double tax_per = cgst + sgst + cess;
                    double basic_price = total_amt / (1 + tax_per / 100);
                   // basic_price = Math.round(basic_price);
                    basic_price = round(basic_price, 2);
                    PurchasePriceEditView.setText(basic_price + "");
                    TotalView.setText(total_amt + "");
                    double tax_amt = total_amt - basic_price;
                    tax_amt = round(tax_amt, 2);
                    TxtView.setText(tax_amt + "");

                } else {
                    //igst+cess
                    double tax_per = igst + cess;
                    double basic_price = total_amt / (1 + tax_per / 100);
                    //basic_price = Math.round(basic_price);
                    basic_price = round(basic_price, 2);
                    PurchasePriceEditView.setText(basic_price + "");
                    TotalView.setText(total_amt + "");
                    double tax_amt = total_amt - basic_price;
                    tax_amt = round(tax_amt, 2);
                    TxtView.setText(tax_amt + "");
                }
            } else {
                if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                    //cgst+sgst+cess
                    double tax_per = cgst + sgst + cess;
                    double tax_amt = total_amt * (tax_per / 100);
                    //tax_amt = Math.round(tax_amt);
                    tax_amt = round(tax_amt, 2);
                    TotalView.setText(total_amt + "");
                    TxtView.setText(tax_amt + "");
                } else {
                    //igst+cess
                    double tax_per = igst + cess;
                    double tax_amt = total_amt * (tax_per / 100);
                    //tax_amt = Math.round(tax_amt);
                    tax_amt = round(tax_amt, 2);
                    TotalView.setText(total_amt + "");
                    TxtView.setText(tax_amt + "");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public Boolean ValidateActivityData() {
        if (
                QtyEditView.getText().toString().trim().equals("") ||
                        Integer.parseInt(QtyEditView.getText().toString().trim()) <= 0
                ) {
            Toast.makeText(PurchaseEntryProductActivity.this, "Please Provide Valid Qty", Toast.LENGTH_SHORT).show();
            QtyEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(QtyEditView, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (
                PurchasePriceEditView.getText().toString().trim().equals("") ||
                        Double.parseDouble(PurchasePriceEditView.getText().toString().trim()) <= 0
                ) {
            Toast.makeText(PurchaseEntryProductActivity.this, "Purchase Price Cannot be Zero OR Blank", Toast.LENGTH_SHORT).show();
            PurchasePriceEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(PurchasePriceEditView, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (
                MRPEditView.getText().toString().trim().equals("") ||
                        Double.parseDouble(MRPEditView.getText().toString().trim()) <= 0
                ) {
            Toast.makeText(PurchaseEntryProductActivity.this, "MRP Cannot be Zero OR Blank", Toast.LENGTH_SHORT).show();
            MRPEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(MRPEditView, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (
                SPEditView.getText().toString().trim().equals("") ||
                        Double.parseDouble(SPEditView.getText().toString().trim()) <= 0
                ) {
            Toast.makeText(PurchaseEntryProductActivity.this, "Sale Price Cannot be Zero OR Blank", Toast.LENGTH_SHORT).show();
            SPEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(SPEditView, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (
                BatchNoEditView.getText().toString().trim().equals("")
                ) {
            Toast.makeText(PurchaseEntryProductActivity.this, "Please Provide Valid Batch No", Toast.LENGTH_SHORT).show();
            BatchNoEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(BatchNoEditView, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (
                ExpiryEditView.getText().toString().trim().equals("")
                ) {
            Toast.makeText(PurchaseEntryProductActivity.this, "Please Provide Valid Expiry Date", Toast.LENGTH_SHORT).show();
            ExpiryEditView.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(ExpiryEditView, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        return true;
    }

    public ProductBatchModel GenerateProductBatch() {
        ProductBatchModel ProductBatchModel = new ProductBatchModel();
        ProductBatchModel.setProductBatchId(ProductBatchId);
        ProductBatchModel.setPurchasePrice(Double.parseDouble(PurchasePriceEditView.getText().toString()));
        ProductBatchModel.setMRP(Double.parseDouble(MRPEditView.getText().toString()));
        ProductBatchModel.setSP(Double.parseDouble(SPEditView.getText().toString()));
        ProductBatchModel.setBatchNo(BatchNoEditView.getText().toString());
        ProductBatchModel.setExpiry(ExpiryEditView.getText().toString());
        ProductBatchModel.getProductBarCodeModel().setEanCode(EANCODEEditView.getText().toString());
        ProductBatchModel.getProductModel().setProductId(ProductId);
        return ProductBatchModel;
    }

    public PurchaseEntryDetailModel GeneratePurchaseEntryDetail() {
        PurchaseEntryDetailModel PurchaseEntryDetailModel = new PurchaseEntryDetailModel();
        PurchaseEntryDetailModel.setPurchaseEntryDetailId(PurchaseEntryDetailId);
        PurchaseEntryDetailModel.setPurchaseRate(Double.parseDouble(PurchasePriceEditView.getText().toString()));
        PurchaseEntryDetailModel.setMRP(Double.parseDouble(MRPEditView.getText().toString()));
        PurchaseEntryDetailModel.setSP(Double.parseDouble(SPEditView.getText().toString()));
        PurchaseEntryDetailModel.setQty(Integer.parseInt(QtyEditView.getText().toString()));
        PurchaseEntryDetailModel.setTaxPercentage(0D);

        PurchaseEntryDetailModel.setIgst(Double.parseDouble(IGSTView.getText().toString()));
        PurchaseEntryDetailModel.setSgst(Double.parseDouble(SGSTView.getText().toString()));
        PurchaseEntryDetailModel.setCgst(Double.parseDouble(CGSTView.getText().toString()));
        PurchaseEntryDetailModel.setCess(Double.parseDouble(CESSView.getText().toString()));
        double cess = Double.parseDouble(CESSView.getText().toString());
        double igst = Double.parseDouble(IGSTView.getText().toString());
        double sgst = Double.parseDouble(SGSTView.getText().toString());
        double cgst = Double.parseDouble(CGSTView.getText().toString());
        double price = Double.parseDouble(PurchasePriceEditView.getText().toString());
        int qty = Integer.parseInt(QtyEditView.getText().toString());
        CGSTView.setText(cgst + "");
        IGSTView.setText(igst + "");
        SGSTView.setText(sgst + "");
        CESSView.setText(cess + "");
        double total_amt = 0.0;
        total_amt = price * qty;
        if (sharedPreference_main.getGSTCategoryId().equalsIgnoreCase("2") && gst_category_id.equalsIgnoreCase("2")) {
            if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                //cgst+sgst+cess
                double tax_per = cgst + sgst + cess;
                double basic_price = total_amt / (1 + tax_per / 100);
//                basic_price = Math.round(basic_price);
                basic_price = round(basic_price, 2);
                PurchasePriceEditView.setText(basic_price + "");
                TotalView.setText(total_amt + "");
                double tax_amt = total_amt - basic_price;
                tax_amt = round(tax_amt, 2);
                TxtView.setText(tax_amt + "");

            } else {
                //igst+cess
                double tax_per = igst + cess;
                double basic_price = total_amt / (1 + tax_per / 100);
//                basic_price = Math.round(basic_price);
                basic_price = round(basic_price, 2);
                PurchasePriceEditView.setText(basic_price + "");
                TotalView.setText(total_amt + "");
                double tax_amt = total_amt - basic_price;
                tax_amt = round(tax_amt, 2);
                TxtView.setText(tax_amt + "");
            }
        } else {
            if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                //cgst+sgst+cess
                double tax_per = cgst + sgst + cess;
                double tax_amt = total_amt * (tax_per / 100);
//                tax_amt = Math.round(tax_amt);
                tax_amt = round(tax_amt, 2);
                TotalView.setText(total_amt + "");
                TxtView.setText(tax_amt + "");
            } else {
                //igst+cess
                double tax_per = igst + cess;
                double tax_amt = total_amt * (tax_per / 100);
//                tax_amt = Math.round(tax_amt);
                tax_amt = round(tax_amt, 2);
                TotalView.setText(total_amt + "");
                TxtView.setText(tax_amt + "");
            }
        }






        PurchaseEntryDetailModel.setTax_amt(Double.parseDouble(TxtView.getText().toString()));
        PurchaseEntryDetailModel.setTotal_amt(Double.parseDouble(TotalView.getText().toString()));
        if (ProductNameBatchNoId.getText().equals("")) {
            PurchaseEntryDetailModel.setBatchNo("XXX");
        } else {
            PurchaseEntryDetailModel.setBatchNo(ProductNameBatchNoId.getText().toString());
        }

        return PurchaseEntryDetailModel;
    }
}
