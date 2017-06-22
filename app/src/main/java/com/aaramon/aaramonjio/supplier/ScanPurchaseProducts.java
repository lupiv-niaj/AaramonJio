package com.aaramon.aaramonjio.supplier;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.order.StaticVariable;
import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

//import com.syncproduct.QueryClass;


public class ScanPurchaseProducts extends Activity implements OnScanListener, StaticVariable {
    private boolean Scan = false;
    private BarcodePicker mBarcodePicker;
    private final int CAMERA_PERMISSION_REQUEST = 0;
    private boolean mDeniedCameraAccess = false;
    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "aANmnk0bEeSRnFMYy8LZZeZsEEgB3s3HP6tOQqJejgs";
    private boolean mPaused = true;
    Toast mToast = null;
    String barcode = "";
    MediaPlayer mp;
    FrameLayout zbarLayout;
    SupplierService SupplierService = new SupplierService();

    TextView PurchaseEntryDetaildIdView, ProductBatchIdView, ProductNameView, MRPView, SPView, PurchasePriceView, NewPurchaseEntryDetailId,
            QtyView, TotalAmountView, TotalItemsView, ProceedView, CGSTView, IGSTView, SGSTView, CESSView, TotalView, TxtView, total_tax, ProductNameBatchNoId;
    AutoCompleteTextView search_product_scan;
    RelativeLayout PurchaseEntryProductSummaryLayout;
    ImageView ProductminusId, ProductPlusId, ProducticonId;
    private int PurchaseEntryId;
    private int SupplierId;
    private String SupplierCode;
    private String SupplierName;
    private String PaymentMode;
    private String PaymentTerm;
    private String TotalItems1;
    private String TotalAmount1;
    ImageView img_back;
    String ProductDetails = "";
    ArrayAdapter<String> myAdapter;
    ListView scan_products;
    PurchaseEntryProductsDetailAdapter PurchaseEntryProductsDetailAdapter;
    PurchaseEntryModel PurchaseEntryModel;
    ImageView img_up;
    int sliding = 0;
    RelativeLayout layout_left;
    SharedPreference_Main sharedPreference_main;
    String stateid = "", gst_category_id = "";

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
        setContentView(R.layout.activity_scan_purchase_products);
        sharedPreference_main = new SharedPreference_Main(ScanPurchaseProducts.this);
        try {
            ScanditLicense.setAppKey(sScanditSdkAppKey);
            zbarLayout = (FrameLayout) findViewById(R.id.frmQr);
            mp = new MediaPlayer();
            PurchaseEntryDetaildIdView = (TextView) findViewById(R.id.PurchaseEntryDetailId);
            ProductBatchIdView = (TextView) findViewById(R.id.ProductBatchId);
            ProductNameView = (TextView) findViewById(R.id.ProductNameTextViewId);
            MRPView = (TextView) findViewById(R.id.ProductMRPValueId);
            SPView = (TextView) findViewById(R.id.ProductSPValueId);
            CGSTView = (TextView) findViewById(R.id.ProductCGSTId);
            IGSTView = (TextView) findViewById(R.id.ProductIGSTId);
            SGSTView = (TextView) findViewById(R.id.ProductSGSTId);
            CESSView = (TextView) findViewById(R.id.ProductCESSId);
            TotalView = (TextView) findViewById(R.id.ProductTOTALId);
            TxtView = (TextView) findViewById(R.id.ProductTAXId);
            ProductNameBatchNoId = (TextView) findViewById(R.id.ProductNameBatchNoId);
            total_tax = (TextView) findViewById(R.id.total_tax);
            NewPurchaseEntryDetailId = (TextView) findViewById(R.id.NewPurchaseEntryDetailId);
            img_back = (ImageView) findViewById(R.id.img_back_account);
            search_product_scan = (AutoCompleteTextView) findViewById(R.id.search_product_scan);
            PurchasePriceView = (TextView) findViewById(R.id.ProductPurchaseValuetId);
            QtyView = (TextView) findViewById(R.id.ProductQuantityId);
            PurchaseEntryProductSummaryLayout = (RelativeLayout) findViewById(R.id.PurchaseEntryProductSummaryId);
            ProceedView = (TextView) findViewById(R.id.ProceedViewId);
            TotalAmountView = (TextView) findViewById(R.id.total_item_amountview);
            TotalItemsView = (TextView) findViewById(R.id.total_itemsview);
            ProductminusId = (ImageView) findViewById(R.id.ProductminusId);
            ProductPlusId = (ImageView) findViewById(R.id.ProductPlusId);
            ProducticonId = (ImageView) findViewById(R.id.ProducticonId);
            scan_products = (ListView) findViewById(R.id.scan_products);
            img_up = (ImageView) findViewById(R.id.img_up);
            layout_left = (RelativeLayout) findViewById(R.id.layout_left);
            Bundle bun = getIntent().getExtras();
            PurchaseEntryId = Integer.parseInt(bun.getString("PurchaseEntryId"));
            SupplierId = Integer.parseInt(bun.getString("SupplierId"));
            SupplierCode = bun.getString("SupplierCode");
            SupplierName = bun.getString("SupplierName");
            PaymentMode = bun.getString("PaymentMode");
            PaymentTerm = bun.getString("PaymentTerm");
            TotalItems1 = bun.getString("TotalItems");
            TotalAmount1 = bun.getString("TotalAmount");
            stateid = bun.getString("stateid");
            gst_category_id = bun.getString("gst_category_id");
            initializeAndStartBarcodeScanning();
            PurchaseEntryModel = new PurchaseEntryModel();
            PurchaseEntryProductsDetailAdapter = new PurchaseEntryProductsDetailAdapter(ScanPurchaseProducts.this, TotalAmountView, TotalItemsView, gst_category_id, stateid, total_tax);
            PurchaseEntryModel.setPurchaseEntryId((long) PurchaseEntryId);
            PurchaseEntryProductsDetailAdapter.PurchaseEntryModel = PurchaseEntryModel;
            scan_products.setAdapter(PurchaseEntryProductsDetailAdapter);
            search_product_scan.setThreshold(3);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        search_product_scan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().length() < 3) {

                    } else {
                        myAdapter = new ArrayAdapter<String>(ScanPurchaseProducts.this, R.layout.auto_complete_layout, SupplierService.GetProductStringPurchaseSearch(search_product_scan.getText().toString()));
                        search_product_scan.setAdapter(myAdapter);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        layout_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_up.performClick();
            }
        });
        img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliding == 0) {
                    sliding++;
                    zbarLayout.setVisibility(View.GONE);
                    img_up.setImageResource(R.mipmap.ic_down_chevron);

                } else {
                    sliding--;
                    zbarLayout.setVisibility(View.VISIBLE);
                    scan_products.setVisibility(View.VISIBLE);
                    img_up.setImageResource(R.mipmap.ic_up_chevron);
                }
            }
        });
        search_product_scan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String str = search_product_scan.getText().toString();
                    String EanCode = SupplierService.GetProductEanCode(str);
                    if (mp.isPlaying()) {
                        mp.stop();
                    }

                    try {
                        mp.reset();
                        AssetFileDescriptor afd;
                        afd = getAssets().openFd(
                                "beepselect.wav");
                        mp.setDataSource(afd.getFileDescriptor(),
                                afd.getStartOffset(), afd.getLength());
                        mp.prepare();
                        mp.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ProductDetails = SupplierService.GetProductStringPurchase(EanCode);
                    if (ProductDetails.equals("[]")) {
                        Toast.makeText(ScanPurchaseProducts.this, "BarCode does not Exist !!!", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.d("SCAN1975", ProductDetails);
                        populateScannedProductDetails(ProductDetails);
                        // Check if Batch is already Added in Purchase Entry
                        long PurchaseEntryDetailId = SupplierService.ProductBatchExists(Long.parseLong(ProductBatchIdView.getText().toString()), PurchaseEntryId);
                        if (PurchaseEntryDetailId > 0) {
                            Log.d("ALKA", "Alread Exists, Update");
                            // Populate Product Details From PurchaseEntryDetail Table
                            String ProductEntryDetail = SupplierService.GetPurchaseEntryProductDetail((int) PurchaseEntryDetailId);
                            PopulateExistingScannedProductDetails(ProductEntryDetail, (int) PurchaseEntryDetailId);
                            int qty = Integer.parseInt(QtyView.getText().toString());
                            Log.d("ALKA PDI", PurchaseEntryDetaildIdView.getText().toString());
                            Log.d("ALKA QTY", qty + "");
                            SupplierService.UpdatePurchaseEntryQty(Long.parseLong(PurchaseEntryDetaildIdView.getText().toString()), qty, Double.parseDouble(TotalView.getText().toString()), Double.parseDouble(TxtView.getText().toString()));
                            String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);
                            Log.d("ALKA Amount", TotalItemsAmount[0] + "- " + TotalItemsAmount[1]);
                            TotalAmountView.setText(TotalItemsAmount[0]);
                            TotalItemsView.setText(TotalItemsAmount[1]);
                            total_tax.setText(TotalItemsAmount[2]);
                        } else {
                            // Insert in Purchase Entry Detail Table
                            // Log.d("SCAN1975", "Before GeneratePurchaseEntryDetailModel");
                            //Log.d("ALKA", "Not Exist, Insert");
                            PurchaseEntryDetailModel PurchaseEntryDetailModel = GeneratePurchaseEntryDetailModel();
                            // Log.d("SCAN1975", "After GeneratePurchaseEntryDetailModel");
                            int PurchaseEntryDetail = SupplierService.insertPurchaseEntryDetail(PurchaseEntryDetailModel);
                            PurchaseEntryDetaildIdView.setText(String.valueOf(PurchaseEntryDetail));
//                            Log.d("SCAN1975", "Purchase Entry Detail Stored.....");
//                            Log.d("SCAN1975", PurchaseEntryDetaildIdView.getText() + " - " + ProductBatchIdView.getText());

                            // Update Total Count & Total Amount View
                            TotalAmountView.setText(String.valueOf(Double.parseDouble(TotalAmountView.getText().toString()) + (PurchaseEntryDetailModel.getPurchaseRate() * PurchaseEntryDetailModel.getQty())));
                            TotalItemsView.setText(String.valueOf(Double.parseDouble(TotalItemsView.getText().toString()) + PurchaseEntryDetailModel.getQty()));
                            total_tax.setText(String.valueOf(Double.parseDouble(total_tax.getText().toString()) + PurchaseEntryDetailModel.getTax_amt()));
                        }
                        search_product_scan.setText("");
                        search_product_scan.requestFocus();
                    }
                    PopulatePurchaseEntryDetails(PurchaseEntryId);
                    PurchaseEntryProductsDetailAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        ProductminusId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (mp.isPlaying()) {
//                        mp.stop();
//                    }
//
//                    try {
//                        mp.reset();
//                        AssetFileDescriptor afd;
//                        afd = getAssets()
//                                .openFd("beepunselect.wav");
//                        mp.setDataSource(afd.getFileDescriptor(),
//                                afd.getStartOffset(), afd.getLength());
//                        mp.prepare();
//                        mp.start();
//                    } catch (IllegalStateException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    if (QtyView.getText().toString().equalsIgnoreCase("1")) {
//                        // Delete Purchase Entry
//                        SupplierService.DeletePurchaseEntryDetail(Long.parseLong(PurchaseEntryDetaildIdView.getText().toString()));
//                        PurchaseEntryDetaildIdView.setText("0");
//                        ProductBatchIdView.setText("0");
//                        QtyView.setText("0");
//                        // Set ProductSummary Layout to gone
//                        //  PurchaseEntryProductSummaryLayout.setVisibility(View.GONE);
//                    } else {
//                        int qty = Integer.parseInt(QtyView.getText().toString());
//                        QtyView.setText(--qty + "");
//                        SupplierService.UpdatePurchaseEntryQty(Long.parseLong(PurchaseEntryDetaildIdView.getText().toString()), qty);
//                    }
//
//                    String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);
//                    TotalAmountView.setText(TotalItemsAmount[0]);
//                    TotalItemsView.setText(TotalItemsAmount[1]);
//                } catch (Exception e) {
//                    Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//
//        ProductPlusId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    if (mp.isPlaying()) {
//                        mp.stop();
//                    }
//
//                    try {
//                        mp.reset();
//                        AssetFileDescriptor afd;
//                        afd = getAssets().openFd(
//                                "beepselect.wav");
//                        mp.setDataSource(afd.getFileDescriptor(),
//                                afd.getStartOffset(), afd.getLength());
//                        mp.prepare();
//                        mp.start();
//                    } catch (IllegalStateException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    int qty = Integer.parseInt(QtyView.getText().toString());
//                    QtyView.setText(String.valueOf(++qty));
//                    Log.d("ALKA PDI", PurchaseEntryDetaildIdView.getText().toString());
//                    Log.d("ALKA QTY", qty + "");
//                    SupplierService.UpdatePurchaseEntryQty(Long.parseLong(PurchaseEntryDetaildIdView.getText().toString()), qty);
//                    String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);
//                    Log.d("ALKA Amount", TotalItemsAmount[0] + "- " + TotalItemsAmount[1]);
//
//                    TotalAmountView.setText(TotalItemsAmount[0]);
//                    TotalItemsView.setText(TotalItemsAmount[1]);
//                } catch (Exception e) {
//                    Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
        PurchaseEntryProductSummaryLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    // Proceed to Purchase Entry Completion Activity
                    Intent TransferData = new Intent(getApplicationContext(), PurchaseEntryProductActivity.class);
                    TransferData.putExtra("PurchaseEntryDetailId", PurchaseEntryDetaildIdView.getText().toString());
                    TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                    TransferData.putExtra("SupplierId", SupplierId + "");
                    TransferData.putExtra("SupplierCode", SupplierCode + "");
                    TransferData.putExtra("SupplierName", SupplierName);
                    TransferData.putExtra("PaymentTerm", PaymentTerm);
                    TransferData.putExtra("PaymentMode", PaymentMode);
                    TransferData.putExtra("stateid", stateid);
                    TransferData.putExtra("gst_category_id", gst_category_id);
                    startActivity(TransferData);
                } catch (Exception e) {
                    Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });

        scan_products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // Proceed to Purchase Entry Completion Activity
                    Intent TransferData = new Intent(getApplicationContext(), PurchaseEntryProductActivity.class);
                    String value = String.valueOf(PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseEntryDetailId());
                    TransferData.putExtra("PurchaseEntryDetailId", value);
                    TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                    TransferData.putExtra("SupplierId", SupplierId + "");
                    TransferData.putExtra("SupplierCode", SupplierCode + "");
                    TransferData.putExtra("SupplierName", SupplierName);
                    TransferData.putExtra("PaymentTerm", PaymentTerm);
                    TransferData.putExtra("PaymentMode", PaymentMode);
                    TransferData.putExtra("stateid", stateid);
                    TransferData.putExtra("gst_category_id", gst_category_id);
                    startActivity(TransferData);
                } catch (Exception e) {
                    Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        ProceedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateActivityData() == true) {
                    try {
                        // Toast.makeText(ScanPurchaseProducts.this, "Set to go", Toast.LENGTH_LONG).show();
                        // Show Saved Product Details
                        String PurchaseEntryCompleteDetail = SupplierService.GetPurchaseEntryDetail(PurchaseEntryId);
                        Log.d("SCAN1975", PurchaseEntryCompleteDetail);

                        // Proceed to Purchase Entry Completion Activity
                        Intent TransferData = new Intent(getApplicationContext(), PurchaseEntrySaveActivity.class);
                        TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                        TransferData.putExtra("SupplierId", SupplierId + "");
                        TransferData.putExtra("SupplierCode", SupplierCode + "");
                        TransferData.putExtra("SupplierName", SupplierName);
                        TransferData.putExtra("PaymentTerm", PaymentTerm);
                        TransferData.putExtra("PaymentMode", PaymentMode);
                        TransferData.putExtra("stateid", stateid);
                        TransferData.putExtra("gst_category_id", gst_category_id);
                        startActivity(TransferData);

                    } catch (Exception e) {
                        Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        mBarcodePicker.stopScanning();
        finish();
    }


    public void PopulateExistingScannedProductDetails(String ProductDetails, int PurchaseEntryDetailId) throws Exception {
        JSONObject ProductObject = (JSONObject) new JSONArray(ProductDetails).get(0);
        PurchaseEntryDetaildIdView.setText(String.valueOf(PurchaseEntryDetailId));
        ProductBatchIdView.setText(ProductObject.getString("product_batch_id"));
        ProductNameView.setText(ProductObject.getString("product_name"));
        MRPView.setText(ProductObject.getString("mrp"));
        SPView.setText(ProductObject.getString("sp"));
        PurchasePriceView.setText(ProductObject.getString("purchase_rate"));
        ProductNameBatchNoId.setText(ProductObject.getString("batch_no"));
        int qty = Integer.parseInt(ProductObject.getString("quantity"));
        QtyView.setText(String.valueOf(++qty));


        double cess = Double.parseDouble(ProductObject.getString("cess_rate"));
        double igst = Double.parseDouble(ProductObject.getString("igst_rate"));
        double sgst = Double.parseDouble(ProductObject.getString("sgst_rate"));
        double cgst = Double.parseDouble(ProductObject.getString("cgst_rate"));
        double price = Double.parseDouble(ProductObject.getString("purchase_rate"));
        CGSTView.setText(cgst + "");
        IGSTView.setText(igst + "");
        SGSTView.setText(sgst + "");
        CESSView.setText(cess + "");
        double total_amt = 0.0;
        total_amt = price * qty;
        if (sharedPreference_main.getGSTCategoryId().equalsIgnoreCase("2") && gst_category_id.equalsIgnoreCase("2")) {
            //basic price
            //tax
//            Double basic = 0.0;
            if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                //cgst+sgst+cess
                double tax_per = cgst + sgst + cess;
                double basic_price = total_amt / (1 + tax_per / 100);
              //  basic_price = Math.round(basic_price);
                basic_price = round(basic_price, 2);
                PurchasePriceView.setText(basic_price + "");
                TotalView.setText(total_amt + "");
                double tax_amt = total_amt - basic_price;
                tax_amt = round(tax_amt, 2);
                TxtView.setText(tax_amt + "");

            } else {
                //igst+cess
                double tax_per = igst + cess;
                double basic_price = total_amt / (1 + tax_per / 100);
               // basic_price = Math.round(basic_price);
                basic_price = round(basic_price, 2);
                PurchasePriceView.setText(basic_price + "");
                TotalView.setText(total_amt + "");
                double tax_amt = total_amt - basic_price;
                tax_amt = round(tax_amt, 2);
                TxtView.setText(tax_amt + "");
            }
        }
        //if(suppliuer is register && merchant os unreigter  purchase price channge nhi hoga

        //supplier is unregister && retailer is register purchase price change nhi hoga //but when save reversable entry make by api

        //supplier is unregister && retailer is unregister purchase price chnage nhi hoga
        else {
            //purchase price
            //tax
//            Double basic = 0.0;
            if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                //cgst+sgst+cess
                double tax_per = cgst + sgst + cess;
                double tax_amt = total_amt * (tax_per / 100);
               // tax_amt = Math.round(tax_amt);
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
    }

    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();
        mPaused = true;
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void grantCameraPermissionsThenStartScanning() {
        if (this.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (mDeniedCameraAccess == false) {
                // it's pretty clear for why the camera is required. We don't need to give a
                // detailed reason.
                this.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }

        } else {
            // we already have the permission
            mBarcodePicker.startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mDeniedCameraAccess = false;
                if (!mPaused) {
                    mBarcodePicker.startScanning();
                }
            } else {
                mDeniedCameraAccess = true;
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
        // handle permissions for Marshmallow and onwards...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantCameraPermissionsThenStartScanning();
        } else {
            // Once the activity is in the foreground again, restart scanning.
            mBarcodePicker.startScanning();
        }
        try {
            String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);
            TotalAmountView.setText(TotalItemsAmount[0]);
            TotalItemsView.setText(TotalItemsAmount[1]);
            total_tax.setText(TotalItemsAmount[2]);
            if (Double.parseDouble(TotalItemsAmount[0]) <= 0.0) {
            } else {
                PopulatePurchaseEntryDetails(PurchaseEntryId);
                PurchaseEntryProductsDetailAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning() {
        // The scanning behavior of the barcode picker is configured through scan
        // settings. We start with empty scan settings and enable a very generous
        // set of symbologies. In your own apps, only enable the symbologies you
        // actually need.
        ScanSettings settings = ScanSettings.create();
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN13, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCA, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN8, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCE, true);

        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_QR, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_DATA_MATRIX, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE39, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE128, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_INTERLEAVED_2_OF_5, true);
        settings.setCameraFacingPreference(ScanSettings.CAMERA_FACING_BACK);
        settings.setCodeDuplicateFilter(3000);
        // Some Android 2.3+ devices do not support rotated camera feeds. On these devices, the
        // barcode picker emulates portrait mode by rotating the scan UI.
        boolean emulatePortraitMode = !BarcodePicker.canRunPortraitPicker();
        if (emulatePortraitMode) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        BarcodePicker picker = new BarcodePicker(this, settings);
//        setContentView(picker);
        zbarLayout.addView(picker);
        mBarcodePicker = picker;
        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.setOnScanListener(this);
    }

    public void PopulatePurchaseEntryDetails(int PurchaseEntryId) {
        try {
            // PurchaseEntryModel=new PurchaseEntryModel();
            PurchaseEntryModel.setProductEntryDetailList(new ArrayList<PurchaseEntryDetailModel>());
            String PurchaseEntryCompleteDetail = SupplierService.GetPurchaseEntryDetail(PurchaseEntryId);
            JSONObject PurchaseEntryObject = (JSONObject) new JSONArray(PurchaseEntryCompleteDetail).get(0);
            PurchaseEntryModel.setBillNumber(PurchaseEntryObject.getString("ret_bill_number"));
            PurchaseEntryModel.setBillDate(PurchaseEntryObject.getString("ret_bill_date"));
            PurchaseEntryModel.setPaymentMode(PurchaseEntryObject.getString("payment_type"));
            PurchaseEntryModel.setPaymentTerm(PurchaseEntryObject.getString("payment_term"));
            PurchaseEntryModel.getSupplierModel().setSupplierId(Integer.parseInt(PurchaseEntryObject.getString("supplier_id")));
            JSONArray PurchaseEntryDetailsArray = new JSONArray(PurchaseEntryCompleteDetail);
            int TotalItems = 0;
            Double TotalAmount = 0D;
            double Tax_item = 0D;
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
                PurchaseEntryDetailModel.setBatchNo(PurchaseEntryDetailsArray.getJSONObject(i).getString("batch_no"));
                PurchaseEntryDetailModel.setIgst(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("igst_rate")));
                PurchaseEntryDetailModel.setSgst(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("sgst_rate")));
                PurchaseEntryDetailModel.setCgst(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("cgst_rate")));
                PurchaseEntryDetailModel.setCess(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("cess_rate")));
                PurchaseEntryDetailModel.setTax_amt(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("total_tax_amt")));
                PurchaseEntryDetailModel.setTotal_amt(Double.parseDouble(PurchaseEntryDetailsArray.getJSONObject(i).getString("total_amt")));
                PurchaseEntryModel.getProductEntryDetailList().add(PurchaseEntryDetailModel);

                TotalItems += PurchaseEntryDetailModel.getQty();
                Tax_item += PurchaseEntryDetailModel.getTax_amt();
                TotalAmount += PurchaseEntryDetailModel.getPurchaseRate() * PurchaseEntryDetailModel.getQty();

                Log.d("MAMU", PurchaseEntryDetailModel.getProductBatchModel().getExpiry());
            }

            TotalItemsView.setText(String.format(TotalItems + ""));
            TotalAmountView.setText(String.format("%1$.2f", TotalAmount));
            TxtView.setText(String.format("%1$.2f", Tax_item));
            PurchaseEntryModel.setTotalAmount(TotalAmount);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void populateScannedProductDetails(String ProductDetails) throws Exception {
        JSONObject ProductObject = (JSONObject) new JSONArray(ProductDetails).get(0);
        PurchaseEntryDetaildIdView.setText("0");
//        ProducticonId.set
        Picasso.with(ScanPurchaseProducts.this).load(ProductObject.getString("image_url"))
                .resize(100, 100).centerInside()
                .transform(new CircleTransform())
                .into(ProducticonId, new Callback() {
                    @Override
                    public void onError() {
                        ProducticonId
                                .setBackgroundResource(R.mipmap.no_image);
                    }

                    @Override
                    public void onSuccess() {
                    }
                });


        ProductBatchIdView.setText(ProductObject.getString("product_batch_id"));
        ProductNameView.setText(ProductObject.getString("product_name"));
        MRPView.setText(ProductObject.getString("product_price"));
        SPView.setText(ProductObject.getString("offer_price"));
        PurchasePriceView.setText(ProductObject.getString("cost_price"));
        NewPurchaseEntryDetailId.setText(ProductObject.getString("cost_price"));
        QtyView.setText("1");
        double cess = Double.parseDouble(ProductObject.getString("cess_rate"));
        double igst = Double.parseDouble(ProductObject.getString("igst_rate"));
        double sgst = Double.parseDouble(ProductObject.getString("sgst_rate"));
        double cgst = Double.parseDouble(ProductObject.getString("cgst_rate"));
        double price = Double.parseDouble(ProductObject.getString("cost_price"));
        CGSTView.setText(cgst + "");
        IGSTView.setText(igst + "");
        SGSTView.setText(sgst + "");
        CESSView.setText(cess + "");
        double total_amt = 0.0;
        total_amt = price * 1;
        Log.e("Category", sharedPreference_main.getGSTCategoryId() + "," + gst_category_id);


        if (sharedPreference_main.getGSTCategoryId().equalsIgnoreCase("2") && gst_category_id.equalsIgnoreCase("2")) {
            //basic price
            //tax
            // Double basic = 0.0;
            if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                //cgst+sgst+cess
                double tax_per = cgst + sgst + cess;
                double basic_price = total_amt / (1 + tax_per / 100);
//                basic_price = Math.round(basic_price);
                basic_price = round(basic_price, 2);
                PurchasePriceView.setText(basic_price + "");
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
                PurchasePriceView.setText(basic_price + "");
                TotalView.setText(total_amt + "");
                double tax_amt = total_amt - basic_price;
                tax_amt = round(tax_amt, 2);
                TxtView.setText(tax_amt + "");
            }
        }
        //if(suppliuer is register && merchant os unreigter  purchase price channge nhi hoga

        //supplier is unregister && retailer is register purchase price change nhi hoga //but when save reversable entry make by api

        //supplier is unregister && retailer is unregister purchase price chnage nhi hoga
        else {
            //purchase price
            //tax
//            Double basic = 0.0;
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
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public PurchaseEntryDetailModel GeneratePurchaseEntryDetailModel() {
        PurchaseEntryDetailModel PurchaseEntryDetailModel = new PurchaseEntryDetailModel();
        PurchaseEntryDetailModel.setPurchaseId(PurchaseEntryId);
        Log.d("SCAN1975", PurchaseEntryDetailModel.getPurchaseId() + "");
        PurchaseEntryDetailModel.getProductBatchModel().setProductBatchId(Long.parseLong(ProductBatchIdView.getText().toString()));
        PurchaseEntryDetailModel.setPurchaseRate(Double.parseDouble(PurchasePriceView.getText().toString()));
        PurchaseEntryDetailModel.setMRP(Double.parseDouble(MRPView.getText().toString()));
        PurchaseEntryDetailModel.setSP(Double.parseDouble(SPView.getText().toString()));
        PurchaseEntryDetailModel.setQty(Integer.parseInt(QtyView.getText().toString()));
        PurchaseEntryDetailModel.setIgst(Double.parseDouble(IGSTView.getText().toString()));
        PurchaseEntryDetailModel.setSgst(Double.parseDouble(SGSTView.getText().toString()));
        PurchaseEntryDetailModel.setCgst(Double.parseDouble(CGSTView.getText().toString()));
        PurchaseEntryDetailModel.setCess(Double.parseDouble(CESSView.getText().toString()));
        PurchaseEntryDetailModel.setTax_amt(Double.parseDouble(TxtView.getText().toString()));
        PurchaseEntryDetailModel.setTotal_amt(Double.parseDouble(TotalView.getText().toString()));
        PurchaseEntryDetailModel.setNewPurchaseRate(Double.parseDouble(NewPurchaseEntryDetailId.getText().toString()));
        if (ProductNameBatchNoId.getText().equals("")) {
            PurchaseEntryDetailModel.setBatchNo("XXX");
        } else {
            PurchaseEntryDetailModel.setBatchNo(ProductNameBatchNoId.getText().toString());
        }


        PurchaseEntryDetailModel.setTaxPercentage(0.00D);
        PurchaseEntryDetailModel.setStatus(1);
        return PurchaseEntryDetailModel;
    }

    public Boolean ValidateActivityData() {
        if (Double.parseDouble(TotalAmountView.getText().toString().trim()) <= 0) {
            Toast.makeText(ScanPurchaseProducts.this, "Please Select atleast One Product", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void SetTotalItems() {
        TotalAmountView.setText(TotalAmount1);
        TotalItemsView.setText(TotalItems1);
    }

    @Override
    public void didScan(ScanSession scanSession) {
        String message = "";
        barcode = "";
        Log.d("didScan", "Start");
        for (Barcode code : scanSession.getNewlyRecognizedCodes()) {
            String data = code.getData();
            // truncate code to certain length
            String cleanData = data;
            if (data.length() > 30) {
                cleanData = data.substring(0, 25) + "[...]";
            }
            if (message.length() > 0) {
                message += "\n\n\n";
            }
            message += cleanData;
        }
        barcode = message;
        Log.d("Barcode", barcode);

        if (!(barcode.equals(""))) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MediaPlayer mp;
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.barcodescanner);
                        mp.setVolume(3, 3);
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                // TODO Auto-generated method stub
                                mp.reset();
                                mp.release();
                                mp = null;
                            }
                        });
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                        Log.d("SCAN1975", "Start.....");
                        ProductDetails = SupplierService.GetProductStringPurchase(barcode);
                        Log.d("SCAN1975", ProductDetails);
                        if (ProductDetails.equals("[]")) {
                            // PurchaseEntryProductSummaryLayout.setVisibility(View.GONE);
                            Toast.makeText(ScanPurchaseProducts.this, "BarCode does not Exist !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            //  PurchaseEntryProductSummaryLayout.setVisibility(View.VISIBLE);
                            populateScannedProductDetails(ProductDetails);
                            // Check if Batch is already Added in Purchase Entry
                            long PurchaseEntryDetailId = SupplierService.ProductBatchExists(Long.parseLong(ProductBatchIdView.getText().toString()), PurchaseEntryId);
                            if (PurchaseEntryDetailId > 0) {
                                Log.d("ALKA", "Alread Exists, Update");
                                // Populate Product Details From PurchaseEntryDetail Table
                                String ProductEntryDetail = SupplierService.GetPurchaseEntryProductDetail((int) PurchaseEntryDetailId);
                                PopulateExistingScannedProductDetails(ProductEntryDetail, (int) PurchaseEntryDetailId);
                                int qty = Integer.parseInt(QtyView.getText().toString());
                                Log.d("ALKA PDI", PurchaseEntryDetaildIdView.getText().toString());
                                Log.d("ALKA QTY", qty + "");
                                SupplierService.UpdatePurchaseEntryQty(Long.parseLong(PurchaseEntryDetaildIdView.getText().toString()), qty, Double.parseDouble(TotalView.getText().toString()), Double.parseDouble(TxtView.getText().toString()));
                                String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);
                                Log.d("ALKA Amount", TotalItemsAmount[0] + "- " + TotalItemsAmount[1]);
                                TotalAmountView.setText(TotalItemsAmount[0]);
                                TotalItemsView.setText(TotalItemsAmount[1]);
                                total_tax.setText(TotalItemsAmount[2]);
                            } else {
                                // Insert in Purchase Entry Detail Table
                                // Log.d("SCAN1975", "Before GeneratePurchaseEntryDetailModel");
                                Log.d("ALKA", "Not Exist, Insert");
                                PurchaseEntryDetailModel PurchaseEntryDetailModel = GeneratePurchaseEntryDetailModel();
                                Log.d("SCAN1975", "After GeneratePurchaseEntryDetailModel");
                                int PurchaseEntryDetail = SupplierService.insertPurchaseEntryDetail(PurchaseEntryDetailModel);
                                PurchaseEntryDetaildIdView.setText(String.valueOf(PurchaseEntryDetail));
                                Log.d("SCAN1975", "Purchase Entry Detail Stored.....");
                                Log.d("SCAN1975", PurchaseEntryDetaildIdView.getText() + " - " + ProductBatchIdView.getText());

                                // Update Total Count & Total Amount View
                                TotalAmountView.setText(String.valueOf(Double.parseDouble(TotalAmountView.getText().toString()) + (PurchaseEntryDetailModel.getPurchaseRate() * PurchaseEntryDetailModel.getQty())));
                                TotalItemsView.setText(String.valueOf(Double.parseDouble(TotalItemsView.getText().toString()) + PurchaseEntryDetailModel.getQty()));
                                total_tax.setText(String.valueOf(Double.parseDouble(total_tax.getText().toString()) + PurchaseEntryDetailModel.getTax_amt()));
                            }
                        }
                        PopulatePurchaseEntryDetails(PurchaseEntryId);
                        PurchaseEntryProductsDetailAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(ScanPurchaseProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
