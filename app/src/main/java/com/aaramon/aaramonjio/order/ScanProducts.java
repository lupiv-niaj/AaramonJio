package com.aaramon.aaramonjio.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.dataaccess.SlidingTabLayout;
import com.aaramon.aaramonjio.syncproduct.QueryClass;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScanProducts extends FragmentActivity implements OnScanListener, StaticVariable {
    private boolean Scan = false;
    private BarcodePicker mBarcodePicker;
    private final int CAMERA_PERMISSION_REQUEST = 0;
    private boolean mDeniedCameraAccess = false;
    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "aANmnk0bEeSRnFMYy8LZZeZsEEgB3s3HP6tOQqJejgs";
    private boolean mPaused = true;
    Toast mToast = null;
    String barcode = "";
    ListView scan_products;
    ArrayList<ScanProductModel> scan_items;
    ScanProductAdapter adapter;
    ArrayList<CartModel> cart_model_items;
    CartAdapter cart_adapter;
    TextView txt_total_item_amount, txt_total_item, total_tax;
    SharedPreference_Main sharedPreference_main;
    QueryClass query;
    Spinner list_cart;
    RelativeLayout get_payment_cart, header_layout;
    ImageView header_arrow_image;
    FrameLayout zbarLayout;
    //View cv;
    ImageView img_up;
    int sliding = 0;
    String from = "", fromcart_id = "";
    private ViewPager viewPagerStoreProductSubCategoryName;
    EditText search_product;
    //    PagerTabStrip pagerTabStrip;
    SlidingTabLayout tabs;
    private int swipeCountProduct = 0;
    private ArrayList<StoreSubCategoryModel> storeSubCategoryModel;
    private StoreSubCategoryAdapter store_sub_category;
    ListView listviewquickaddproduct;
    TextView quickaddproduct;
    RelativeLayout rl_center_search, rl_layout_left;
    LinearLayout rl_center;
    ArrayList<QuickAddModel> quick_add_list, quick_add_search;
    QuickAddProductAdapter quickAddProductAdapter;
    ImageView img_back_account;
    Dialog login;
    ListView search_list;
    ArrayList<ProductMultipleBatchModel> rowItems;
    final Boolean[] flag = {true};
    String order_type_page = "0";
    TextView get_save_payment;
    String delivery_date = "", delivery_slot = "";

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
        setContentView(R.layout.activity_scan_products);
        login = new Dialog(ScanProducts.this);
        Bundle bun = getIntent().getExtras();
        from = bun.getString("from");
        fromcart_id = bun.getString("cartid");
        order_type_page = bun.getString("OrderTypePage");
        scan_products = (ListView) findViewById(R.id.scan_products);
        rl_layout_left = (RelativeLayout) findViewById(R.id.rl_layout_left);
        txt_total_item = (TextView) findViewById(R.id.total_items);
        txt_total_item_amount = (TextView) findViewById(R.id.total_item_amount);
        total_tax = (TextView) findViewById(R.id.total_tax);
        list_cart = (Spinner) findViewById(R.id.list_cart);
        get_payment_cart = (RelativeLayout) findViewById(R.id.get_payment_cart);
        get_save_payment = (TextView) findViewById(R.id.get_save_payment);
        img_back_account = (ImageView) findViewById(R.id.img_back_account);
        viewPagerStoreProductSubCategoryName = (ViewPager) findViewById(R.id.viewPagerStoreProductSubCategoryName);
        listviewquickaddproduct = (ListView) findViewById(R.id.listviewquickaddproduct);
        quickaddproduct = (TextView) findViewById(R.id.quickaddproduct);
        rl_center = (LinearLayout) findViewById(R.id.rl_center);
        rl_center_search = (RelativeLayout) findViewById(R.id.rl_center_search);
        search_list = (ListView) findViewById(R.id.search_listview);
        img_up = (ImageView) findViewById(R.id.img_up);
        scan_items = new ArrayList<>();
        adapter = new ScanProductAdapter(ScanProducts.this, scan_items, txt_total_item, txt_total_item_amount, total_tax);
        scan_products.setAdapter(adapter);
        ScanditLicense.setAppKey(sScanditSdkAppKey);
        zbarLayout = (FrameLayout) findViewById(R.id.frmQr);
        search_product = (EditText) findViewById(R.id.search_product_scan);
        if (order_type_page.equalsIgnoreCase("0")) {
            get_save_payment.setText("Get Payment");
        } else {
            get_save_payment.setText("Proceed");
        }
        initializeAndStartBarcodeScanning();
        query = new QueryClass();
        query.Updateean();
        scan_products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                long cartid = query.FindCartID(cart_name);
                String ean = query.FindEAN_Code(scan_items.get(position).getProduct_id());
                String offer = "";
                double product_price = 0.0, offer_price = 0.0;
                if (scan_items.get(position).getOffer_price() == 0.00) {
                    offer = String.valueOf(scan_items.get(position).getProduct_price());
                    product_price = scan_items.get(position).getProduct_price();
                    offer_price = scan_items.get(position).getProduct_price();
                } else {
                    offer = String.valueOf(scan_items.get(position).getOffer_price());
                    product_price = scan_items.get(position).getProduct_price();
                    offer_price = scan_items.get(position).getOffer_price();
                }
                mBarcodePicker.stopScanning();
                ;
                mPaused = true;
                show_dialog("edit", scan_items.get(position).getProduct_name(), ean, String.valueOf(scan_items.get(position).getProduct_price()), offer, scan_items.get(position).getProduct_id(), cartid, offer_price, scan_items.get(position).getQty());
                return true;
            }
        });
        img_back_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        search_product.getWindowToken(), 0);
                if (quickaddproduct.getText().toString().equalsIgnoreCase("Scan")) {
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    get_list_data(cartid);
                    flag[0] = true;
                    search_product.setText("");
                    quickaddproduct.setText("Quick Add");
                    rl_center.setVisibility(View.VISIBLE);
                    zbarLayout.setVisibility(View.VISIBLE);
                    rl_center_search.setVisibility(View.GONE);
                    search_product.clearFocus();
                    quickaddproduct.requestFocus();
                } else if (quickaddproduct.getText().toString().equalsIgnoreCase("Quick Add")) {
                    mBarcodePicker.stopScanning();
                    mPaused = true;
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    int count = query.GetProductCount(cartid);
                    if (count <= 0) {
                        query.DeleteCart(cartid);
                        bind_cart_spinner();
                        spinner_selection(0, String.valueOf(0));
                        list_cart.setSelection(0);
                    }
                    finish();
                }
            }
        });
        sharedPreference_main = new SharedPreference_Main(ScanProducts.this);
        bind_cart_spinner();
        if (from.equalsIgnoreCase("order")) {
            for (int i = 0; i < cart_model_items.size(); i++) {
                if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(fromcart_id))) {
                    spinner_selection(i, fromcart_id);
                    list_cart.setSelection(i);

                }
            }
        }
        try {
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        } catch (Exception e) {

        }

        search_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().length() < 3) {
                        mBarcodePicker.startScanning();
                        mPaused = false;

                        search_list.setVisibility(View.GONE);
                        rl_center_search.setVisibility(View.VISIBLE);
                    } else {
                        mBarcodePicker.stopScanning();
                        mPaused = true;
                        String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                        long cartid = query.FindCartID(cart_name);
                        if (cartid == 0) {
                            int cartno = query.FindCartCount();
                            cartno++;
                            cartid = query.InsertCart("Cart " + cartno, 0, 0, sharedPreference_main.getStoreId());
                            bind_cart_spinner();
                            for (int i = 0; i < cart_model_items.size(); i++) {
                                if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                                    spinner_selection(i, String.valueOf(cartid));
                                    list_cart.setSelection(i);
                                }
                            }
                        }
                        quick_add_search = new ArrayList<QuickAddModel>();
                        QuickAddModel quck = new QuickAddModel();
                        quck.setEnd_date("");
                        quck.setOffer_price(0.00);
                        quck.setOffer_start("");
                        quck.setProduct_id("0");
                        quck.setProduct_price(0.00);
                        quck.setProductimage("");
                        quck.setProductname("Add Quick Item");
                        quck.setSku("Add misc items quickly to bill");
                        quck.setCategory_id(0);
                        quck.setQty(0);
                        quck.setBatchNo("XXX");
                        quck.setProductBatchId(Long.parseLong("0"));
                        quick_add_search.add(0, quck);
                        String response = query.GetProductSearch(s.toString());
                        JSONArray jsonArray2 = new JSONArray(response);
                        double sgst = 0.0;
                        double igst = 0.0;
                        double cgst = 0.0;
                        double cess = 0.0;
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject = jsonArray2.getJSONObject(i);
                            String productname = jsonObject.getString(ASPROD_PRODUCT_NAME);
                            String productimage = jsonObject.getString(ASPROD_IMAGE_URL);
                            double product_price = Double.parseDouble(jsonObject.getString(ASPROD_PRODUCT_PRICE));
                            double offer_price = Double.parseDouble(jsonObject.getString(ASPB_OFFER_PRICE));
                            String product_id = jsonObject.getString(ASPROD_PRODUCT_ID);
                            String batchno = jsonObject.getString(ASPB_BATCH_NO);
                            String batchid = jsonObject.getString(ASPB_PRODUCT_BATCH_ID);
                            String offer_start = jsonObject.getString(ASPB_OFFER_START_DATE);
                            String end_date = jsonObject.getString(ASPB_OFFER_END_DATE);
                            sgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_SGST_RATE));
                            igst = Double.parseDouble(jsonObject.getString(ASCATEGORY_IGST_RATE));
                            cgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_CGST_RATE));
                            cess = Double.parseDouble(jsonObject.getString(ASPROD_CESS_RATE));
                            quck = new QuickAddModel();
                            quck.setEnd_date(end_date);
                            quck.setOffer_price(offer_price);
                            quck.setOffer_start(offer_start);
                            quck.setProduct_id(product_id);
                            quck.setProduct_price(product_price);
                            quck.setProductimage(productimage);
                            quck.setProductname(productname);
                            quck.setCategory_id(0);
                            quck.setSku("");
                            quck.setCess(cess);
                            quck.setCgst(cgst);
                            quck.setSgst(sgst);
                            quck.setIgst(igst);
                            quck.setBatchNo(batchno);
                            quck.setProductBatchId(Long.parseLong(batchid));
                            long quty = query.FindCartitemID(cartid, product_id);
                            quck.setQty(quty);
                            quick_add_search.add(i + 1, quck);
                        }
                        search_list.setVisibility(View.VISIBLE);
                        rl_center_search.setVisibility(View.GONE);
                        rl_center.setVisibility(View.GONE);
                        quickAddProductAdapter = new QuickAddProductAdapter(ScanProducts.this, quick_add_search, txt_total_item, txt_total_item_amount, cartid, total_tax);
                        search_list.setAdapter(quickAddProductAdapter);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == 0) {
                    mBarcodePicker.stopScanning();
                    mPaused = true;
                    if (quick_add_search.get(position).getCategory_id() == 0 && quick_add_search.get(position).getProduct_id().equalsIgnoreCase("0")) {
                        String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                        final long cartid = query.FindCartID(cart_name);
                        final Dialog login = new Dialog(ScanProducts.this);
                        login.setContentView(R.layout.layout_quick_add_dialog);
                        login.setCanceledOnTouchOutside(true);
                        login.setCancelable(false);
                        // Init button of login GUI
                        final MediaPlayer mp = new MediaPlayer();
                        final QueryClass queryClass = new QueryClass();
                        final CheckBox chk_product = (CheckBox) login.findViewById(R.id.food_product_radio);
                        final CheckBox chk_personal = (CheckBox) login.findViewById(R.id.personal_care_radio);
                        final CheckBox chk_home_care = (CheckBox) login.findViewById(R.id.home_care_radio);
                        final CheckBox chk_other = (CheckBox) login.findViewById(R.id.others_radio);
                        final EditText et_sellingprice = (EditText) login.findViewById(R.id.ETsellingprice);
                        final EditText et_product = (EditText) login.findViewById(R.id.ETproductName);
                        final EditText et_ean = (EditText) login.findViewById(R.id.ETean);
                        final EditText Et_productprice = (EditText) login.findViewById(R.id.ETproductprice);
                        final TextInputLayout product_price_layout = (TextInputLayout) login.findViewById(R.id.product_price_textinput);
                        final TextInputLayout product_name_textinput = (TextInputLayout) login.findViewById(R.id.product_name_textinput);
                        final TextInputLayout ean_code_textinput = (TextInputLayout) login.findViewById(R.id.ean_code_textinput);
                        final RelativeLayout more = (RelativeLayout) login.findViewById(R.id.more_layout);
                        Button send_btn = (Button) login.findViewById(R.id.ad_addproduct);
                        Button cancel_btn = (Button) login.findViewById(R.id.ad_cancel);
                        more.setVisibility(View.VISIBLE);
                        more.setOnClickListener(new View.OnClickListener()

                        {
                            @Override
                            public void onClick(View v) {
                                et_product.setVisibility(View.VISIBLE);
                                et_ean.setVisibility(View.VISIBLE);
                                Et_productprice.setVisibility(View.VISIBLE);
                                et_sellingprice.setVisibility(View.VISIBLE);
                                product_price_layout.setVisibility(View.VISIBLE);
                                ean_code_textinput.setVisibility(View.VISIBLE);
                                product_name_textinput.setVisibility(View.VISIBLE);
                                // et_ean.setEnabled(false);
                                more.setVisibility(View.GONE);
                                et_product.requestFocus();
                            }
                        });
                        send_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String producttype = "";
                                String image_url = "";

                                String tax = "";
                                if (chk_personal.isChecked()) {
                                    producttype = "Personal Care Items";
                                    tax = "12.5";
                                    image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_personal_care.png";
                                } else if (chk_product.isChecked()) {
                                    producttype = "Food Items";
                                    tax = "0";
                                    image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_food.png";
                                } else if (chk_home_care.isChecked()) {
                                    producttype = "Home Care Items";
                                    tax = "5";
                                    image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_home_care.png";
                                } else if (chk_other.isChecked()) {
                                    producttype = "Misc. Items";
                                    tax = "5";
                                    image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_others.png";
                                }
                                //cart Add
                                if (et_sellingprice.getText().toString().equalsIgnoreCase("")) {
                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                    et_sellingprice.requestFocus();
                                } else if (Double.parseDouble(et_sellingprice.getText().toString()) <= 0.00) {
                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                    et_sellingprice.requestFocus();
                                } else {
                                    String product_price = "0.00";
                                    if (Et_productprice.getText().toString().equalsIgnoreCase("")) {
                                        product_price = et_sellingprice.getText().toString();
                                    } else if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                        product_price = Et_productprice.getText().toString();
                                    } else {
                                        product_price = et_sellingprice.getText().toString();
                                    }
                                    if (et_product.getText().toString().equalsIgnoreCase("")) {

                                        try {
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


                                            double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;

                                            double price;
                                            if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                                price = Double.parseDouble(Et_productprice.getText().toString());
                                            } else {
                                                price = Double.parseDouble(et_sellingprice.getText().toString());
                                            }
                                            double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, price, Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                            double total_amt = Total_Tax_Amount[0];
                                            double tax_amt = Total_Tax_Amount[1];
                                            queryClass.InsertCartDetail(cartid, "0", producttype, Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, cgst, sgst, igst, cess, total_amt, tax_amt, "XXX", "0");
                                            login.dismiss();
                                            mBarcodePicker.startScanning();
                                            mPaused = false;
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());

                                        }
                                    } else {
                                        //  String product_price = "0.00";
                                        if (Et_productprice.getText().toString().equalsIgnoreCase("")) {
                                            product_price = et_sellingprice.getText().toString();
                                        } else if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                            product_price = Et_productprice.getText().toString();
                                        } else {
                                            product_price = et_sellingprice.getText().toString();
                                        }
                                        long check_product_id = 0;
                                        try {
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
                                            check_product_id = queryClass.InsertProduct("0", 0, 0, et_product.getText().toString(), product_price, et_ean.getText().toString(), et_ean.getText().toString(), image_url, tax, tax);
                                            long check_product_barcode = queryClass.InsertProductBarcode(et_ean.getText().toString(), check_product_id);
                                            String offer_type = "0";
                                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                            Calendar from_calender, to_calender;
                                            to_calender = Calendar.getInstance();
                                            from_calender = Calendar.getInstance();
                                            to_calender.add(Calendar.MONTH, 1);
                                            String fromdate = "", todate = "";
                                            if (Double.parseDouble(product_price) > Double.parseDouble(et_sellingprice.getText().toString())) {
                                                offer_type = "1";
                                                fromdate = sdfDate.format(from_calender.getTime());
                                                todate = sdfDate.format(to_calender.getTime());
                                            } else {
                                                offer_type = "0";
                                                todate = "0000-00-00 00:00:00";
                                                fromdate = "0000-00-00 00:00:00";
                                            }
                                            long product_batch_id = queryClass.InsertProductBatch(sharedPreference_main.getStoreId(), "1", "XXX", todate, product_price, product_price, String.valueOf(et_sellingprice.getText().toString()), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);
                                            double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;

                                            double price;
                                            if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                                price = Double.parseDouble(Et_productprice.getText().toString());
                                            } else {
                                                price = Double.parseDouble(et_sellingprice.getText().toString());
                                            }
                                            double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, price, Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                            double total_amt = Total_Tax_Amount[0];
                                            double tax_amt = Total_Tax_Amount[1];
                                            queryClass.InsertCartDetail(cartid, String.valueOf(check_product_id), et_product.getText().toString(), Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, cgst, sgst, igst, cess, total_amt, tax_amt, "XXX", String.valueOf(product_batch_id));
                                            View view = login.getCurrentFocus();
                                            if (view != null) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            }
                                            login.dismiss();
                                            mBarcodePicker.startScanning();
                                            mPaused = false;
                                            cart_adapter.notifyDataSetChanged();

                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());
                                        }
                                    }
                                }
                            }
                        });
                        chk_product.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chk_personal.setChecked(false);
                                chk_other.setChecked(false);
                                chk_home_care.setChecked(false);
                                chk_product.setChecked(true);
                            }
                        });
                        chk_personal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chk_personal.setChecked(true);
                                chk_other.setChecked(false);
                                chk_home_care.setChecked(false);
                                chk_product.setChecked(false);
                            }
                        });
                        chk_other.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chk_personal.setChecked(false);
                                chk_other.setChecked(true);
                                chk_home_care.setChecked(false);
                                chk_product.setChecked(false);
                            }
                        });
                        chk_home_care.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chk_personal.setChecked(false);
                                chk_other.setChecked(false);
                                chk_home_care.setChecked(true);
                                chk_product.setChecked(false);
                            }
                        });
                        cancel_btn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                View view = login.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                mBarcodePicker.startScanning();
                                mPaused = false;

                                login.dismiss();
                            }
                        });
                        // Make dialog box visible.
                        login.show();

                    }
                }
            }
        });
        search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_product.getText().length() == 0) {
                    if (flag[0]) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(
                                    search_product.getWindowToken(), 0);
                            flag[0] = false;
                            quickaddproduct.setText("Scan");
                            rl_center.setVisibility(View.GONE);
                            rl_center_search.setVisibility(View.VISIBLE);
                            search_product.setEnabled(true);
                            quick_add_list = null;
                            String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                            long cartid = query.FindCartID(cart_name);
                            if (cartid == 0) {
                                int cartno = query.FindCartCount();
                                cartno++;
                                cartid = query.InsertCart("Cart " + cartno, 0, 0, sharedPreference_main.getStoreId());
                                bind_cart_spinner();
                                for (int i = 0; i < cart_model_items.size(); i++) {
                                    if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                                        spinner_selection(i, String.valueOf(cartid));
                                        list_cart.setSelection(i);
                                    }
                                }
                            }
                            String SubCategory = query.GetSubCategory();
                            storeSubCategoryModel = new ArrayList<StoreSubCategoryModel>();
                            JSONArray jsonArray = new JSONArray(SubCategory);
                            storeSubCategoryModel.add(new StoreSubCategoryModel(0, "Loose Items"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                storeSubCategoryModel.add(new StoreSubCategoryModel(Integer.parseInt(jsonObject.getString("category_id")),
                                        jsonObject.getString("category_name")));
                            }
                            // storeSubCategoryModel.getSub_categories().get(index)
                            store_sub_category = new StoreSubCategoryAdapter(
                                    getSupportFragmentManager(),
                                    ScanProducts.this,
                                    storeSubCategoryModel);
                            viewPagerStoreProductSubCategoryName
                                    .setAdapter(store_sub_category);

                            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
                            tabs.setViewPager(viewPagerStoreProductSubCategoryName);
                            // Setting Custom Color for the Scroll bar indicator of the Tab View
                            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                                @Override
                                public int getIndicatorColor(int position) {
                                    return getResources().getColor(R.color.jio_main_color);
                                }
                            });
                            quick_add_list = new ArrayList<QuickAddModel>();
                            QuickAddModel quck = new QuickAddModel();
                            quck.setEnd_date("");
                            quck.setOffer_price(0.00);
                            quck.setOffer_start("");
                            quck.setProduct_id("0");
                            quck.setProduct_price(0.00);
                            quck.setProductimage("");
                            quck.setProductname("Add Quick Item");
                            quck.setSku("Add misc items quickly to bill");
                            quck.setCategory_id(0);
                            quck.setQty(0);
                            quck.setCess(0.0);
                            quck.setSgst(0.0);
                            quck.setIgst(0.0);
                            quck.setCgst(0.0);
                            quck.setBatchNo("XXX");
                            quck.setProductBatchId(Long.parseLong("0"));
                            quick_add_list.add(0, quck);
                            String response = query.GetProductEanBlank("");
                            JSONArray jsonArray2 = new JSONArray(response);
                            double sgst = 0.0;
                            double igst = 0.0;
                            double cgst = 0.0;
                            double cess = 0.0;
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                                String productname = jsonObject.getString(ASPROD_PRODUCT_NAME);
                                String productimage = jsonObject.getString(ASPROD_IMAGE_URL);
                                double product_price = Double.parseDouble(jsonObject.getString(ASPROD_PRODUCT_PRICE));
                                double offer_price = Double.parseDouble(jsonObject.getString(ASPB_OFFER_PRICE));
                                String product_id = jsonObject.getString(ASPROD_PRODUCT_ID);
                                String offer_start = jsonObject.getString(ASPB_OFFER_START_DATE);
                                String end_date = jsonObject.getString(ASPB_OFFER_END_DATE);
                                String batchno = jsonObject.getString(ASPB_BATCH_NO);
                                String batchid = jsonObject.getString(ASPB_PRODUCT_BATCH_ID);
                                sgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_SGST_RATE));
                                igst = Double.parseDouble(jsonObject.getString(ASCATEGORY_IGST_RATE));
                                cgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_CGST_RATE));
                                cess = Double.parseDouble(jsonObject.getString(ASPROD_CESS_RATE));
                                quck = new QuickAddModel();
                                quck.setEnd_date(end_date);
                                quck.setOffer_price(offer_price);
                                quck.setOffer_start(offer_start);
                                quck.setProduct_id(product_id);
                                quck.setProduct_price(product_price);
                                quck.setProductimage(productimage);
                                quck.setProductname(productname);
                                quck.setCategory_id(0);
                                quck.setSku("");
                                quck.setCess(cess);
                                quck.setCgst(cgst);
                                quck.setSgst(sgst);
                                quck.setIgst(igst);
                                quck.setBatchNo(batchno);
                                quck.setProductBatchId(Long.parseLong(batchid));
                                long quty = query.FindCartitemID(cartid, product_id);
                                quck.setQty(quty);
                                quick_add_list.add(i + 1, quck);
                            }
                            quickAddProductAdapter = new QuickAddProductAdapter(ScanProducts.this, quick_add_list, txt_total_item, txt_total_item_amount, cartid, total_tax);
                            listviewquickaddproduct.setAdapter(quickAddProductAdapter);
                        } catch (Exception e) {

                        }
                    } else {
                        flag[0] = true;
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(search_product, InputMethodManager.SHOW_IMPLICIT);
//                        String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
//                        long cartid = query.FindCartID(cart_name);
//                        int count = query.GetProductCount(cartid);
//                        if (count <= 0) {
//                            query.DeleteCart(cartid);
//                            bind_cart_spinner();
//                            list_cart.setSelection(0);
//                        }
//                        quickaddproduct.setText("Quick Add");
//                        rl_center.setVisibility(View.VISIBLE);
//                        rl_center_search.setVisibility(View.GONE);
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(
//                                search_product.getWindowToken(), 0);
//                        search_product.clearFocus();
//                        get_list_data(Long.parseLong(cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_id()));
                    }
                }
            }
        });
        quickaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quickaddproduct.getText().toString().equalsIgnoreCase("Quick Add")) {
                    mBarcodePicker.stopScanning();
                    ;
                    mPaused = true;
                    show_dialog("Add", "", "", "", "", "0", (long) 0, 0.0, 1);
                } else if (quickaddproduct.getText().toString().equalsIgnoreCase("Scan")) {
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    int count = query.GetProductCount(cartid);
                    if (count <= 0) {
                        query.DeleteCart(cartid);
                        bind_cart_spinner();
                        spinner_selection(0, String.valueOf(0));
                        list_cart.setSelection(0);
                    }

                    flag[0] = true;
                    search_product.setText("");
                    quickaddproduct.setText("Quick Add");
                    rl_center.setVisibility(View.VISIBLE);
                    zbarLayout.setVisibility(View.VISIBLE);
                    rl_center_search.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            search_product.getWindowToken(), 0);
                    search_product.clearFocus();
                    get_list_data(Long.parseLong(cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_id()));
                }
            }
        });
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("Main Activity", "onPageScrolled" + position);

            }

            @Override
            public void onPageSelected(int position) {
                //  Toast.makeText(ScanProducts.this, storeSubCategoryModel.get(position).getCategory_id() + "", Toast.LENGTH_LONG).show();
                Log.e("Main Activity", "onPageSelected" + position);
                try {
                    quick_add_list = new ArrayList<QuickAddModel>();
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    if (storeSubCategoryModel.get(position).getCategory_id() == 0) {
                        QuickAddModel quck = new QuickAddModel();
                        quck.setEnd_date("");
                        quck.setOffer_price(0.00);
                        quck.setOffer_start("");
                        quck.setProduct_id("0");
                        quck.setProduct_price(0.00);
                        quck.setProductimage("");
                        quck.setProductname("Add Quick Item");
                        quck.setSku("Add misc items quickly to bill");
                        quck.setCategory_id(storeSubCategoryModel.get(position).getCategory_id());
                        quck.setQty(0);
                        quck.setCgst(0.0);
                        quck.setIgst(0.0);
                        quck.setSgst(0.0);
                        quck.setCess(0.0);
                        quck.setBatchNo("XXX");
                        quck.setProductBatchId(Long.parseLong("0"));
                        quick_add_list.add(0, quck);
                        String response = query.GetProduct("");
                        JSONArray jsonArray = new JSONArray(response);
                        double sgst = 0.0;
                        double igst = 0.0;
                        double cgst = 0.0;
                        double cess = 0.0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String productname = jsonObject.getString(ASPROD_PRODUCT_NAME);
                            String productimage = jsonObject.getString(ASPROD_IMAGE_URL);
                            double product_price = Double.parseDouble(jsonObject.getString(ASPROD_PRODUCT_PRICE));
                            double offer_price = Double.parseDouble(jsonObject.getString(ASPB_OFFER_PRICE));
                            String product_id = jsonObject.getString(ASPROD_PRODUCT_ID);
                            String offer_start = jsonObject.getString(ASPB_OFFER_START_DATE);
                            String end_date = jsonObject.getString(ASPB_OFFER_END_DATE);
                            String batchno = jsonObject.getString(ASPB_BATCH_NO);
                            String batchid = jsonObject.getString(ASPB_PRODUCT_BATCH_ID);
                            sgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_SGST_RATE));
                            igst = Double.parseDouble(jsonObject.getString(ASCATEGORY_IGST_RATE));
                            cgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_CGST_RATE));
                            cess = Double.parseDouble(jsonObject.getString(ASPROD_CESS_RATE));
//                            if (product_price == offer_price) {
//                                offer_price = 0.0;
//                            }
                            quck = new QuickAddModel();
                            quck.setEnd_date(end_date);
                            quck.setOffer_price(offer_price);
                            quck.setOffer_start(offer_start);
                            quck.setProduct_id(product_id);
                            quck.setProduct_price(product_price);
                            quck.setProductimage(productimage);
                            quck.setProductname(productname);
                            quck.setCategory_id(0);
                            quck.setSku("");
                            quck.setCess(cess);
                            quck.setCgst(cgst);
                            quck.setSgst(sgst);
                            quck.setIgst(igst);
                            quck.setBatchNo(batchno);
                            quck.setProductBatchId(Long.parseLong(batchid));
                            long quty = query.FindCartitemID(cartid, product_id);
                            quck.setQty(quty);
                            quick_add_list.add(i + 1, quck);
                        }
                    } else {
                        String response = query.GetProductByCategory(storeSubCategoryModel.get(position).getCategory_id());
                        JSONArray jsonArray = new JSONArray(response);
                        double sgst = 0.0;
                        double igst = 0.0;
                        double cgst = 0.0;
                        double cess = 0.0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String productname = jsonObject.getString(ASPROD_PRODUCT_NAME);
                            String productimage = jsonObject.getString(ASPROD_IMAGE_URL);
                            double product_price = Double.parseDouble(jsonObject.getString(ASPROD_PRODUCT_PRICE));
                            double offer_price = Double.parseDouble(jsonObject.getString(ASPB_OFFER_PRICE));
                            String product_id = jsonObject.getString(ASPROD_PRODUCT_ID);
                            String offer_start = jsonObject.getString(ASPB_OFFER_START_DATE);
                            String end_date = jsonObject.getString(ASPB_OFFER_END_DATE);
                            sgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_SGST_RATE));
                            igst = Double.parseDouble(jsonObject.getString(ASCATEGORY_IGST_RATE));
                            cgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_CGST_RATE));
                            cess = Double.parseDouble(jsonObject.getString(ASPROD_CESS_RATE));
                            String batchno = jsonObject.getString(ASPB_BATCH_NO);
                            String batchid = jsonObject.getString(ASPB_PRODUCT_BATCH_ID);
//                            if (product_price == offer_price) {
//                                offer_price = 0.0;
//                            }

                            QuickAddModel quck = new QuickAddModel();
                            quck.setEnd_date(end_date);
                            quck.setOffer_price(offer_price);
                            quck.setOffer_start(offer_start);
                            quck.setProduct_id(product_id);
                            quck.setProduct_price(product_price);
                            quck.setProductimage(productimage);
                            quck.setProductname(productname);
                            quck.setSku("");
                            quck.setCess(cess);
                            quck.setCgst(cgst);
                            quck.setSgst(sgst);
                            quck.setIgst(igst);
                            quck.setBatchNo(batchno);
                            quck.setProductBatchId(Long.parseLong(batchid));
                            quck.setCategory_id(storeSubCategoryModel.get(position).getCategory_id());
                            long quty = query.FindCartitemID(cartid, product_id);
                            quck.setQty(quty);
                            quick_add_list.add(quck);
                        }
                    }
                    quickAddProductAdapter = new QuickAddProductAdapter(ScanProducts.this, quick_add_list, txt_total_item, txt_total_item_amount, cartid, total_tax);
                    listviewquickaddproduct.setAdapter(quickAddProductAdapter);
                } catch (Exception e) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("Main Activity", "onPageScrollStateChanged" + state);
            }
        });
        rl_layout_left.setOnClickListener(new View.OnClickListener() {
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
                    search_product.setText("");
                    if (rl_center_search.isShown()) {
                        rl_center_search.setVisibility(View.GONE);
                        search_product.setText("");
                        quickaddproduct.setText("Quick Add");
                        rl_center.setVisibility(View.VISIBLE);
//                        scan_products.setVisibility(View.VISIBLE);
                    }
                    search_product.clearFocus();
                    zbarLayout.setVisibility(View.GONE);
                    img_up.setImageResource(R.mipmap.ic_down_chevron);

                } else {
                    sliding--;
                    search_product.setText("");
                    if (rl_center_search.isShown()) {
                        rl_center_search.setVisibility(View.GONE);
                        search_product.setText("");
                        quickaddproduct.setText("Quick Add");
                        rl_center.setVisibility(View.VISIBLE);
//
                    }
                    search_product.clearFocus();
                    zbarLayout.setVisibility(View.VISIBLE);
                    scan_products.setVisibility(View.VISIBLE);
                    img_up.setImageResource(R.mipmap.ic_up_chevron);
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    get_list_data(cartid);
                }
            }
        });

        list_cart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    ((TextView) parent.getChildAt(0)).setText(cart_model_items.get(position).getCart_name());
//                    ((TextView) parent.getChildAt(0)).setBackgroundColor(R.color.primary);
                    scan_items.clear();
                    txt_total_item.setText("0 Items");
                    txt_total_item_amount.setText("₹0.00");
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    query.UpdateCart(cartid, 0, 0.0);
//                    query.DeleteCart(cartid);
                    spinner_selection(0, String.valueOf(0));
                    adapter.notifyDataSetChanged();
                } else {
//                    ((TextView) parent.getChildAt(0)).setTextColor(R.color.white);
//                    ((TextView) parent.getChildAt(0)).setBackgroundColor(R.color.primary);
//                    ((TextView) parent.getChildAt(0)).setText(cart_model_items.get(position).getCart_name());
                    spinner_selection(position, String.valueOf(cart_model_items.get(position).getCart_id()));
                    scan_items.clear();
                    adapter.notifyDataSetChanged();
                    long cartid = Long.parseLong(cart_model_items.get(position).getCart_id());
                    get_list_data(cartid);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        get_payment_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_type_page.equalsIgnoreCase("0")) {
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    int count = query.GetProductCount(cartid);
                    if (count <= 0) {
                        Toast.makeText(ScanProducts.this, "Please Scan/Enter Some Product in Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            double amount = 0.00;
                            long item = 0;
                            double total_amout = 0.00;
                            long total_items = 0;
                            double tax_amount = 0.0;
                            String response_cart_detail = query.GetCartDetail(cartid);
                            JSONArray jsonArray1 = new JSONArray(response_cart_detail);
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                double product_price_cart = Double.parseDouble(jsonObject1.getString("product_price"));
                                double offer_price_cart = Double.parseDouble(jsonObject1.getString("offer_price"));
                                int qty_cart = Integer.parseInt(jsonObject1.getString("quantity"));

                                if (offer_price_cart > 0.00) {
                                    amount = offer_price_cart;
                                } else {
                                    amount = product_price_cart;
                                }

                                tax_amount = tax_amount + Double.parseDouble(jsonObject1.getString(COLUMN_TOTAL_TAX_AMOUNT));
                                item = qty_cart;
                                total_amout = total_amout + (amount * item);
                                total_items = total_items + item;
                            }
                            txt_total_item.setText(total_items + " Items");
                            txt_total_item_amount.setText("₹" + total_amout);

                            Intent i = new Intent(ScanProducts.this, CartAdditionalInfo.class);
                            String cart_name_select = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                            String cart_id = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_id();
                            if (total_items == 0) {
                                query.UpdateCart(Long.parseLong(cart_id), 0, 0);
//                            query.DeleteCart(Long.parseLong(cart_id));
                            } else {

                                query.UpdateCart(Long.parseLong(cart_id), total_items, total_amout);
                            }
                            i.putExtra("CartName", cart_name_select);
                            i.putExtra("CartId", cart_id);
                            i.putExtra("Items", total_items + "");
                            i.putExtra("Amount", total_amout + "");
                            i.putExtra("Tax", tax_amount + "");
                            startActivity(i);
                            finish();
                        } catch (Exception e) {
                            Log.e("Get Payment Exception", e.getMessage());
                        }
                    }
                } else {
                    String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                    long cartid = query.FindCartID(cart_name);
                    int count = query.GetProductCount(cartid);
                    if (count <= 0) {
                        Toast.makeText(ScanProducts.this, "Please Scan/Enter Some Product in Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            double amount = 0.00;
                            long item = 0;
                            double total_amout = 0.00;
                            long total_items = 0;
                            double tax_amount = 0.0;
                            String response_cart_detail = query.GetCartDetail(cartid);
                            JSONArray jsonArray1 = new JSONArray(response_cart_detail);
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                double product_price_cart = Double.parseDouble(jsonObject1.getString("product_price"));
                                double offer_price_cart = Double.parseDouble(jsonObject1.getString("offer_price"));
                                int qty_cart = Integer.parseInt(jsonObject1.getString("quantity"));

                                if (offer_price_cart > 0.00) {
                                    amount = offer_price_cart;
                                } else {
                                    amount = product_price_cart;
                                }

                                tax_amount = tax_amount + Double.parseDouble(jsonObject1.getString(COLUMN_TOTAL_TAX_AMOUNT));
                                item = qty_cart;
                                total_amout = total_amout + (amount * item);
                                total_items = total_items + item;
                            }
                            txt_total_item.setText(total_items + " Items");
                            txt_total_item_amount.setText("₹" + total_amout);
                            if (DataStatic.isInternetAvailable(ScanProducts.this)) {
                                OrderJSON(total_amout, total_items, tax_amount, cartid);
                            } else {
                                Toast.makeText(ScanProducts.this,
                                        getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (Exception e) {
                            Log.e("Get Payment Exception", e.getMessage());
                        }

                    }
                }
            }
        });
    }

    void bind_cart_spinner() {
        try {
            String get_cart = query.GetActiveCart();
            cart_model_items = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(get_cart);
            cart_model_items.add(0, new CartModel("New Cart", "0", "0", "0", "0000-00-00 00:00:00", true));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cart_model_items.add(new CartModel(jsonObject.getString(COLUMN_CART_NAME), jsonObject.getString(COLUMN_TEMP_CART_ID), jsonObject.getString(COLUMN_CART_ITEM), jsonObject.getString(COLUMN_CART_VALUE), jsonObject.getString(COLUMN_ADDED_DATE), false));
            }
            cart_adapter = new CartAdapter(this, cart_model_items, list_cart);
            list_cart.setAdapter(cart_adapter);
        } catch (Exception e) {
            Log.e("GetActiveCart", e.getMessage());

        }
    }

    void spinner_selection(int position, String cartid) {
        if (position == 0) {
            for (int ij = 0; ij < cart_model_items.size(); ij++) {
                if (ij == 0) {
                    cart_model_items.get(ij).setCheck(true);
                } else {
                    cart_model_items.get(ij).setCheck(false);
                }
            }
        } else {
            for (int ij = 0; ij < cart_model_items.size(); ij++) {
                if (cart_model_items.get(ij).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                    cart_model_items.get(ij).setCheck(true);
                } else {
                    cart_model_items.get(ij).setCheck(false);
                }
            }
        }
        cart_adapter.notifyDataSetChanged();
    }

    void get_list_data(long cart_id) {
        try {
//            try {
//                scan_products.removeHeaderView(cv);
//            } catch (Exception e) {
//
//            }
////            if (scan_items.size() > 1) {
//            scan_products.addHeaderView(cv);
////            }
            scan_items = new ArrayList<>();
            adapter = new ScanProductAdapter(ScanProducts.this, scan_items, txt_total_item, txt_total_item_amount, total_tax);
            scan_products.setAdapter(adapter);
            String response_cart_detail = query.GetCartDetail(cart_id);
            JSONArray jsonArray1 = new JSONArray(response_cart_detail);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                String cart_item_id_cart = jsonObject1.getString("temp_cart_item_id");
                String card_id = jsonObject1.getString("temp_cart_id");
                String product_id_cart = jsonObject1.getString("product_id");
                String product_name_cart = jsonObject1.getString("product_name");
                double product_price_cart = Double.parseDouble(jsonObject1.getString("product_price"));
                double offer_price_cart = Double.parseDouble(jsonObject1.getString("offer_price"));
                String image_url_cart = jsonObject1.getString("image_url");
                int qty_cart = Integer.parseInt(jsonObject1.getString("quantity"));
                double total_amount = Double.parseDouble(jsonObject1.getString(COLUMN_TOTAL_AMOUNT));
                double total_tax_amount = Double.parseDouble(jsonObject1.getString(COLUMN_TOTAL_TAX_AMOUNT));
                String batchno = jsonObject1.getString(COLUMN_BATCH_NO);
                String batchid = jsonObject1.getString(COLUMN_BATCH_ID);
                double sgst = Double.parseDouble(jsonObject1.getString(COLUMN_SGST_RATE));
                double igst = Double.parseDouble(jsonObject1.getString(COLUMN_IGST_RATE));
                double cgst = Double.parseDouble(jsonObject1.getString(COLUMN_CGST_RATE));
                double cess = Double.parseDouble(jsonObject1.getString(COLUMN_CESS_RATE));


                double offerprice = 0;
                if (offer_price_cart < product_price_cart) {
                    offerprice = offer_price_cart;
                } else {
                    offerprice = 0.00;
                }
                scan_items.add(new ScanProductModel(product_name_cart, product_price_cart, offerprice, image_url_cart, qty_cart, product_id_cart, card_id, cart_item_id_cart, total_amount, total_tax_amount, cgst, igst, sgst, cess, batchid, batchno));
            }
//            if (scan_items.size() == 0) {
//                scan_products.removeHeaderView(cv);
//            }
            double amount = 0.00;
            int item = 0;
            double total_amout = 0.00;
            int total_items = 0;
            double total_tax_amt = 0.0;
            for (int i = 0; i < scan_items.size(); i++) {
                if (scan_items.get(i).getOffer_price() > 0.00) {
                    amount = scan_items.get(i).getOffer_price();
                } else {
                    amount = scan_items.get(i).getProduct_price();
                }
                item = scan_items.get(i).getQty();
                total_amout = total_amout + (amount * item);
                total_items = total_items + item;
                total_tax_amt = total_tax_amt + scan_items.get(i).getTax_amount();
            }
            txt_total_item.setText(total_items + " Items");
            txt_total_item_amount.setText("₹" + total_amout);
            total_tax.setText("Tax ₹" + total_tax_amt);
            if (total_items == 0) {
                query.UpdateCart(cart_id, 0, 0);
//                query.DeleteCart(cart_id);
            } else {
                query.UpdateCart(cart_id, total_items, total_amout);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("Error Response", e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        img_back_account.performClick();
    }

    @Override
    public void didScan(ScanSession scanSession) {
        String message = "";

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


//        final String finalMessage = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (barcode.length() > 0) {
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
                        String response = query.GetProduct(barcode.toString().trim());
                        rowItems = new ArrayList<ProductMultipleBatchModel>();
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() == 0) {
                            if (DataStatic.isInternetAvailable(ScanProducts.this)) {
                                getEANCodeProduct(barcode);
                            } else {
                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT).show();
                            }

//                            show_dialog("Add", "", barcode, "", "", "0", (long) 0, 0.0);

                        } else if (jsonArray.length() > 1) {
                            String productname = null;
                            String productimage = null;
                            String ProductId = null;
                            double sgst = 0.0;
                            double igst = 0.0;
                            double cgst = 0.0;
                            double cess = 0.0;
                            for (int m = 0; m < jsonArray.length(); m++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(m);
                                productname = jsonObject.getString(ASPROD_PRODUCT_NAME);
                                productimage = jsonObject.getString(ASPROD_IMAGE_URL);
                                double product_price = Double.parseDouble(jsonObject.getString(ASPROD_PRODUCT_PRICE));
                                double offer_price = Double.parseDouble(jsonObject.getString(ASPB_OFFER_PRICE));
                                ProductId = jsonObject.getString(ASPROD_PRODUCT_ID);
                                String offer_start = jsonObject.getString(ASPB_OFFER_START_DATE);
                                String end_date = jsonObject.getString(ASPB_OFFER_END_DATE);
                                Long ProductBatchId = jsonObject.getLong(ASPB_PRODUCT_BATCH_ID);
                                String Batch_no = jsonObject.getString(ASPB_BATCH_NO);
                                sgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_SGST_RATE));
                                igst = Double.parseDouble(jsonObject.getString(ASCATEGORY_IGST_RATE));
                                cgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_CGST_RATE));
                                cess = Double.parseDouble(jsonObject.getString(ASPROD_CESS_RATE));
                                double amount = 0.00;
                                int item = 0;
                                double total_amout = 0.00;
                                int total_items = 0;
                                ProductMultipleBatchModel pmb = new ProductMultipleBatchModel();
                                pmb.setProductName(productname);
                                pmb.setProductImage(productimage);
                                pmb.setProductPrice(product_price);
                                pmb.setOfferPrice(offer_price);
                                pmb.setProductId(ProductId);
                                pmb.setOfferStartDate(offer_start);
                                pmb.setOfferEndDate(end_date);
                                pmb.setProductBatchId(ProductBatchId);
                                pmb.setBatchNo(Batch_no);
                                pmb.setProductBatchId(ProductBatchId);
                                pmb.setCess(cess);
                                pmb.setCgst(cgst);
                                pmb.setSgst(sgst);
                                pmb.setIgst(igst);
                                if (m == 0) {
                                    pmb.setCheck(true);
                                } else {
                                    pmb.setCheck(false);
                                }
                                rowItems.add(pmb);
                            }
                            String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                            mBarcodePicker.stopScanning();
                            mPaused = true;
                            BatchDialog(cart_name, productname, productimage, ProductId, cgst, sgst, igst, cess);
                        } else {
//                            String response = query.GetProduct(barcode.toString().trim());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String productname = jsonObject.getString(ASPROD_PRODUCT_NAME);
                            String productimage = jsonObject.getString(ASPROD_IMAGE_URL);
                            double product_price = Double.parseDouble(jsonObject.getString(ASPROD_PRODUCT_PRICE));
                            double offer_price = Double.parseDouble(jsonObject.getString(ASPB_OFFER_PRICE));
                            String product_id = jsonObject.getString(ASPROD_PRODUCT_ID);
                            String offer_start = jsonObject.getString(ASPB_OFFER_START_DATE);
                            String end_date = jsonObject.getString(ASPB_OFFER_END_DATE);
                            Long ProductBatchId = jsonObject.getLong(ASPB_PRODUCT_BATCH_ID);
                            String Batch_no = jsonObject.getString(ASPB_BATCH_NO);
                            double sgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_SGST_RATE));
                            double igst = Double.parseDouble(jsonObject.getString(ASCATEGORY_IGST_RATE));
                            double cgst = Double.parseDouble(jsonObject.getString(ASCATEGORY_CGST_RATE));
                            double cess = Double.parseDouble(jsonObject.getString(ASPROD_CESS_RATE));
                            double amount = 0.00;
                            int item = 0;
                            double total_amout = 0.00;
                            int total_items = 0;
                            for (int i = 0; i < scan_items.size(); i++) {
                                if (scan_items.get(i).getOffer_price() > 0.00) {
                                    amount = scan_items.get(i).getOffer_price();
                                } else {
                                    amount = scan_items.get(i).getProduct_price();
                                }
                                item = scan_items.get(i).getQty();
                                total_amout = total_amout + (amount * item);
                                total_items = total_items + item;
                            }
                            String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                            long cartid = query.FindCartID(cart_name);
                            if (cartid == 0) {
                                int cartno = query.FindCartCount();
                                cartno++;
                                cartid = query.InsertCart("Cart " + cartno, total_amout, total_items, sharedPreference_main.getStoreId());
                                bind_cart_spinner();
                                for (int i = 0; i < cart_model_items.size(); i++) {
                                    if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                                        spinner_selection(i, String.valueOf(cartid));
                                        list_cart.setSelection(i);
                                    }
                                }
                            }
                            long exists_product = query.FindCartitemID(cartid, product_id);
                            if (exists_product == 0) {
                                double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, product_price, offer_price, 1);
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];

                                query.InsertCartDetail(cartid, product_id, productname, product_price, offer_price, 1, productimage, cgst, sgst, igst, cess, total_amt, tax_amt, Batch_no, String.valueOf(ProductBatchId));
                            } else {
                                exists_product = exists_product + 1;
                                double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, product_price, offer_price, Integer.parseInt(String.valueOf(exists_product)));
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];
                                query.UpdateQty(cartid, product_id, exists_product, total_amt, tax_amt);
                            }
                            get_list_data(cartid);
                            barcode = "";
                        }
                    } catch (Exception e) {
                        Log.e("Response Error", e.getMessage());
                    }
                }
            }
        });
    }

    void BatchDialog(final String cartname, String productname, String productimage, String ProductId, final double cgst, final double igst, final double sgst, final double cess) {
        final Dialog multipleBatch = new Dialog(ScanProducts.this);
        multipleBatch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        multipleBatch.setContentView(R.layout.layout_multiple_batch_dialog);
        final ListView lv_price = (ListView) multipleBatch.findViewById(R.id.lv_price);
        final MultipleBatchAdapter batch_adapter = new MultipleBatchAdapter(ScanProducts.this, rowItems);
        lv_price.setAdapter(batch_adapter);
        multipleBatch.setCanceledOnTouchOutside(true);
        multipleBatch.setCancelable(false);
        multipleBatch.show();
        final QueryClass queryClass = new QueryClass();
        final ImageView ProducticonId = (ImageView) multipleBatch.findViewById(R.id.ProducticonId);
        final TextView ProductNameTextViewId = (TextView) multipleBatch.findViewById(R.id.ProductNameTextViewId);
        ProductNameTextViewId.setText(productname.toString());
        final TextView ProductMRPValueId = (TextView) multipleBatch.findViewById(R.id.ProductMRPValueId);
        Picasso.with(ScanProducts.this).load(productimage)
                .resize(200, 200).centerCrop()
                .transform(new CircleTransform())
                .into(ProducticonId, new Callback() {
                    @Override
                    public void onError() {
                        ProducticonId.setBackgroundResource(R.mipmap.no_image);
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
        final Button send_btn = (Button) multipleBatch.findViewById(R.id.ad_addproduct);
        final Button cancel_btn = (Button) multipleBatch.findViewById(R.id.ad_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipleBatch.dismiss();
                mPaused = false;
                mBarcodePicker.startScanning();

            }
        });

        final String finalProductId = ProductId;
        final String finalProductname = productname;
        final String finalProductimage = productimage;
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long cartid = queryClass.FindCartID(cartname);
                if (cartid == 0) {
                    int cartno = queryClass.FindCartCount();
                    cartno++;
                    try {
                        cartid = query.InsertCart("Cart " + cartno, 0.00, 0, sharedPreference_main.getStoreId());
                    } catch (Exception e) {
                        Log.e("Exception Throw", e.getMessage());
                        e.printStackTrace();
                    }
                    bind_cart_spinner();
                    for (int i = 0; i < cart_model_items.size(); i++) {
                        if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                            spinner_selection(i, String.valueOf(cartid));
                            list_cart.setSelection(i);
                        }
                    }
                }
                double SelectedProductPrice = 0;
                double SelectedOfferPrice = 0;
                String batchno = "";
                String batchid = "";
                for (int i = 0; i < rowItems.size(); i++) {
                    if (rowItems.get(i).getCheck()) {
                        SelectedProductPrice = rowItems.get(i).getProductPrice();
                        SelectedOfferPrice = rowItems.get(i).getOfferPrice();
                        batchno = rowItems.get(i).getBatchNo();
                        batchid = String.valueOf(rowItems.get(i).getProductBatchId());
                    }
                }
                long exists_product = queryClass.FindCartitemOfferID(cartid, finalProductId, SelectedOfferPrice);
                if (exists_product == 0) {
                    //Selected checkbox price
                    try {
                        double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, SelectedProductPrice, SelectedOfferPrice, 1);
                        double total_amt = Total_Tax_Amount[0];
                        double tax_amt = Total_Tax_Amount[1];
                        queryClass.InsertCartDetail(cartid, finalProductId, finalProductname, SelectedProductPrice, SelectedOfferPrice, 1, finalProductimage, cgst, sgst, igst, cess, total_amt, tax_amt, batchno, batchid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    exists_product = exists_product + 1;
                    double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, SelectedProductPrice, SelectedOfferPrice, Integer.parseInt(String.valueOf(exists_product)));
                    double total_amt = Total_Tax_Amount[0];
                    double tax_amt = Total_Tax_Amount[1];
                    queryClass.UpdateBatchQty(cartid, finalProductId, exists_product, SelectedOfferPrice, total_amt, tax_amt);
                }
                get_list_data(cartid);
                barcode = "";
                multipleBatch.dismiss();
                mPaused = false;
                mBarcodePicker.startScanning();
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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

    void show_dialog(final String mode_type, String product_name, String eancode, String product_price, String offer_price, final String product_id, final Long cart_id, final double old_offer_price, final int qty) {
        //  login=new Dialog(this);
        // Set GUI of login screen
        login = new Dialog(this);
        login.requestWindowFeature(Window.FEATURE_NO_TITLE);
        login.setContentView(R.layout.layout_quick_add_dialog);
        login.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        login.setCanceledOnTouchOutside(true);

        login.setCancelable(false);
        // Init button of login GUI
        final QueryClass queryClass = new QueryClass();
        final TextInputLayout product_price_layout = (TextInputLayout) login.findViewById(R.id.product_price_textinput);
        final TextView header_text = (TextView) login.findViewById(R.id.text_quick_add);
        final CheckBox chk_product = (CheckBox) login.findViewById(R.id.food_product_radio);
        final CheckBox chk_personal = (CheckBox) login.findViewById(R.id.personal_care_radio);
        final CheckBox chk_home_care = (CheckBox) login.findViewById(R.id.home_care_radio);
        final CheckBox chk_other = (CheckBox) login.findViewById(R.id.others_radio);
        final EditText et_product = (EditText) login.findViewById(R.id.ETproductName);
        final EditText et_ean = (EditText) login.findViewById(R.id.ETean);
        final EditText et_sellingprice = (EditText) login.findViewById(R.id.ETsellingprice);
        final EditText Et_productprice = (EditText) login.findViewById(R.id.ETproductprice);
        final TextInputLayout product_name_textinput = (TextInputLayout) login.findViewById(R.id.product_name_textinput);
        final TextInputLayout ean_code_textinput = (TextInputLayout) login.findViewById(R.id.ean_code_textinput);
        final MediaPlayer mp = new MediaPlayer();
        final RelativeLayout more = (RelativeLayout) login.findViewById(R.id.more_layout);
        Button send_btn = (Button) login.findViewById(R.id.ad_addproduct);
        Button cancel_btn = (Button) login.findViewById(R.id.ad_cancel);

        et_sellingprice.setSelection(et_sellingprice.length());
        Et_productprice.setSelection(et_product.length());
        et_product.setText(product_name);
        et_ean.setText(eancode);
        Et_productprice.setText(product_price);
        et_sellingprice.setText(offer_price);

        if (mode_type.equalsIgnoreCase("edit")) {
            header_text.setText(getResources().getString(R.string.headertextedit));
            chk_home_care.setVisibility(View.GONE);
            chk_other.setVisibility(View.GONE);
            chk_personal.setVisibility(View.GONE);
            chk_product.setVisibility(View.GONE);
            et_ean.setVisibility(View.VISIBLE);
            et_product.setVisibility(View.VISIBLE);
            et_sellingprice.setVisibility(View.VISIBLE);
            product_name_textinput.setVisibility(View.VISIBLE);
            ean_code_textinput.setVisibility(View.VISIBLE);
            Et_productprice.setVisibility(View.VISIBLE);
            product_price_layout.setVisibility(View.VISIBLE);
            et_ean.setEnabled(false);
            more.setVisibility(View.GONE);
            send_btn.setText("Update");
            et_product.requestFocus();
//            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            mgr.showSoftInput(et_product, InputMethodManager.SHOW_IMPLICIT);
        } else {
            header_text.setText(getResources().getString(R.string.headertextadd));
            chk_home_care.setVisibility(View.VISIBLE);
            chk_other.setVisibility(View.VISIBLE);
            chk_personal.setVisibility(View.VISIBLE);
            chk_product.setVisibility(View.VISIBLE);
            send_btn.setText("Add");
            if (eancode.equalsIgnoreCase("")) {
                et_ean.setVisibility(View.GONE);
                et_ean.setEnabled(true);
                et_product.setVisibility(View.GONE);
                et_sellingprice.setVisibility(View.VISIBLE);
                Et_productprice.setVisibility(View.GONE);
                product_price_layout.setVisibility(View.GONE);
                more.setVisibility(View.VISIBLE);
                et_sellingprice.requestFocus();
            } else {
                et_ean.setVisibility(View.VISIBLE);
                et_product.setVisibility(View.VISIBLE);
                et_sellingprice.setVisibility(View.VISIBLE);
                Et_productprice.setVisibility(View.VISIBLE);
                product_price_layout.setVisibility(View.VISIBLE);
                et_ean.setEnabled(false);
                more.setVisibility(View.GONE);
                et_ean.setText(eancode);
                et_product.requestFocus();
//                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                mgr.showSoftInput(et_product, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        Et_productprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(Et_productprice, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        et_ean.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(et_ean, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        et_product.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(et_product, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        et_sellingprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(et_sellingprice, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode_type.equalsIgnoreCase("edit")) {
                    //Edit product info
                    //cart Detail
                    if (et_product.getText().toString().length() == 0) {
                        Toast.makeText(ScanProducts.this, getResources().getString(R.string.enterproductname), Toast.LENGTH_LONG).show();
                    } else if (Et_productprice.getText().toString().length() == 0) {
                        Toast.makeText(ScanProducts.this, getResources().getString(R.string.enterproductprice), Toast.LENGTH_LONG).show();
                    } else if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.0) {
                        Toast.makeText(ScanProducts.this, getResources().getString(R.string.enterproductprice), Toast.LENGTH_LONG).show();
                    } else {
                        Double product_price = Double.parseDouble(Et_productprice.getText().toString());
                        Double offer_price = Double.parseDouble(et_sellingprice.getText().toString());
                        if (offer_price > product_price) {
                            Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricemrp), Toast.LENGTH_LONG).show();
                        } else {
                            String offertype = "0";
                            if (offer_price < product_price) {
                                offertype = "1";
                            } else {
                                offertype = "0";
                            }
                            if (product_id.equalsIgnoreCase("0") && et_ean.getText().toString().equalsIgnoreCase("")) {

                            } else {
                                queryClass.UpdateProductInfo(et_product.getText().toString(), product_price, product_id, et_ean.getText().toString(), offertype, String.valueOf(offer_price));
                            }
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

                            double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;
                            double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, product_price, offer_price, qty);
                            double total_amt = Total_Tax_Amount[0];
                            double tax_amt = Total_Tax_Amount[1];
                            queryClass.UpdateProductInfoInCart(et_product.getText().toString(), product_price, product_id, String.valueOf(offer_price), cart_id, old_offer_price, total_amt, tax_amt);
                            View view = login.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            login.dismiss();
                            mBarcodePicker.startScanning();
                            mPaused = false;
                            String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                            Long cartid = query.FindCartID(cart_name);
                            get_list_data(cartid);
                        }
                    }
                } else {
                    String producttype = "";
                    String image_url = "";
                    String tax = "";
                    String cat_id = "0";
                    if (chk_personal.isChecked()) {
                        producttype = "Personal Care Items";
                        tax = "12.5";
                        cat_id = "100001";
                        image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_personal_care.png";
                    } else if (chk_product.isChecked()) {
                        producttype = "Food Items";
                        tax = "0";
                        cat_id = "100002";
                        image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_food.png";
                    } else if (chk_home_care.isChecked()) {
                        producttype = "Home Care Items";
                        tax = "5";
                        cat_id = "100003";
                        image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_home_care.png";
                    } else if (chk_other.isChecked()) {
                        producttype = "Misc. Items";
                        tax = "5";
                        cat_id = "100004";
                        image_url = "http://www.aaramshop.co.in/api/uploaded_files/tax-category/ic_others.png";
                    }
                    long cartid = 0;
                    try {
                        String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                        cartid = query.FindCartID(cart_name);
                        if (cartid == 0) {
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
                            int cartno = queryClass.FindCartCount();
                            cartno++;
                            cartid = query.InsertCart("Cart " + cartno, 0, 0, sharedPreference_main.getStoreId());
                            bind_cart_spinner();
                            for (int i = 0; i < cart_model_items.size(); i++) {
                                if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                                    spinner_selection(i, String.valueOf(cartid));
                                    list_cart.setSelection(i);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    if (et_ean.getText().toString().equalsIgnoreCase("")) {
                        if (et_product.getText().toString().equalsIgnoreCase("")) {
                            //cart Add
                            try {
                                if (et_sellingprice.getText().toString().equalsIgnoreCase("")) {
                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                } else {
                                    if (Double.parseDouble(et_sellingprice.getText().toString()) <= 0.00) {
//                                        if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
                                        Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricecheck), Toast.LENGTH_LONG).show();
                                    } else {
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


                                        double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;
                                        double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, Double.parseDouble(et_sellingprice.getText().toString()), Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                        double total_amt = Total_Tax_Amount[0];
                                        double tax_amt = Total_Tax_Amount[1];

                                        query.InsertCartDetail(cartid, cat_id, producttype, Double.parseDouble(et_sellingprice.getText().toString()), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, cgst, sgst, igst, cess, total_amt, tax_amt, "XXX", "0");
                                        View view = login.getCurrentFocus();
                                        if (view != null) {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        }
                                        login.dismiss();
                                        mBarcodePicker.startScanning();
                                        mPaused = false;
                                        get_list_data(cartid);
                                    }
                                }
//                                else{
//                                    query.InsertCartDetail(cartid, "0", producttype, Double.parseDouble(Et_productprice.getText().toString()), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url);
//                                    login.dismiss();
//                                    get_list_data(cartid);
//                                }
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }

                        } else {
                            //Add to product without ean code

                            if (et_sellingprice.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                if (Double.parseDouble(et_sellingprice.getText().toString()) <= 0.00) {
//                                        if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricecheck), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }


                            String product_price = "0.00";
                            if (Et_productprice.getText().toString().equalsIgnoreCase("")) {
                                product_price = et_sellingprice.getText().toString();
                            } else if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                product_price = Et_productprice.getText().toString();
                            } else {
                                product_price = et_sellingprice.getText().toString();
                            }

//                        if (Et_productprice.getText().toString().equalsIgnoreCase("") && et_sellingprice.getText().toString().equalsIgnoreCase("")) {
//                            Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
//                            return;
//                        } else {
//                            if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
//                                if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
//                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricecheck), Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                            } else {
//                                if (Double.parseDouble(Et_productprice.getText().toString()) < Double.parseDouble(et_sellingprice.getText().toString())) {
//                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricemrp), Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                            }
//                        }
                            long check_product_id = 0;
                            try {
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
                                check_product_id = queryClass.InsertProduct("0", 0, 0, et_product.getText().toString(), product_price, et_ean.getText().toString(), et_ean.getText().toString(), image_url, tax, tax);
                                long check_product_barcode = queryClass.InsertProductBarcode(et_ean.getText().toString(), check_product_id);
                                String offer_type = "0";
                                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                Calendar from_calender, to_calender;
                                to_calender = Calendar.getInstance();
                                from_calender = Calendar.getInstance();
                                to_calender.add(Calendar.MONTH, 1);
                                String fromdate = "", todate = "";
                                if (Double.parseDouble(product_price) > Double.parseDouble(et_sellingprice.getText().toString())) {
                                    offer_type = "1";
                                    fromdate = sdfDate.format(from_calender.getTime());
                                    todate = sdfDate.format(to_calender.getTime());
                                } else {
                                    offer_type = "0";
                                    todate = "0000-00-00 00:00:00";
                                    fromdate = "0000-00-00 00:00:00";
                                }
                                long product_batch_id = queryClass.InsertProductBatch(sharedPreference_main.getStoreId(), "1", "XXX", todate, product_price, String.valueOf(et_sellingprice.getText().toString()), String.valueOf(et_sellingprice.getText().toString()), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);
                                double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;
                                double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];
                                query.InsertCartDetail(cartid, String.valueOf(check_product_id), et_product.getText().toString(), Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, cgst, sgst, igst, cess, total_amt, tax_amt, "XXX", String.valueOf(product_batch_id));

                                View view = login.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                login.dismiss();
                                mBarcodePicker.startScanning();
                                mPaused = false;
                                get_list_data(cartid);

                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }
                    } else {
                        if (et_product.getText().toString().equalsIgnoreCase("")) {
                            //item add with product type
//                            if (Et_productprice.getText().toString().equalsIgnoreCase("") && et_sellingprice.getText().toString().equalsIgnoreCase("")) {
//                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricecheck), Toast.LENGTH_LONG).show();
//                                return;
//                            } else {
//                                if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
//                                    if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
//                                        Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricecheck), Toast.LENGTH_LONG).show();
//                                        return;
//                                        return;
//                                    }
//                                } else {
//                                    if (Double.parseDouble(Et_productprice.getText().toString()) < Double.parseDouble(et_sellingprice.getText().toString())) {
//                                        Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricemrp), Toast.LENGTH_LONG).show();
//                                        return;
//                                    }
//                                }
//                            }
                            if (et_sellingprice.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                if (Double.parseDouble(et_sellingprice.getText().toString()) <= 0.00) {
//                                        if (Double.parseDouble(Et_productprice.getText().toString()) <= 0.00) {
                                    Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricecheck), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }


                            String product_price = "0.00";
                            if (Et_productprice.getText().toString().equalsIgnoreCase("")) {
                                product_price = et_sellingprice.getText().toString();
                            } else if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                product_price = Et_productprice.getText().toString();
                            } else {
                                product_price = et_sellingprice.getText().toString();
                            }
                            long check_product_id = 0;
                            try {
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
                                check_product_id = queryClass.InsertProduct("0", 0, 0, producttype, product_price, et_ean.getText().toString(), et_ean.getText().toString(), image_url, tax, tax);
                                long check_product_barcode = queryClass.InsertProductBarcode(et_ean.getText().toString(), check_product_id);
                                String offer_type = "0";
                                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                Calendar from_calender, to_calender;
                                to_calender = Calendar.getInstance();
                                from_calender = Calendar.getInstance();
                                to_calender.add(Calendar.MONTH, 1);
                                String fromdate = "", todate = "";
                                if (Double.parseDouble(product_price) > Double.parseDouble(et_sellingprice.getText().toString())) {
                                    offer_type = "1";
                                    fromdate = sdfDate.format(from_calender.getTime());
                                    todate = sdfDate.format(to_calender.getTime());
                                } else {
                                    offer_type = "0";
                                    todate = "0000-00-00 00:00:00";
                                    fromdate = "0000-00-00 00:00:00";
                                }
                                long product_batch_id = queryClass.InsertProductBatch(sharedPreference_main.getStoreId(), "1", "XXX", todate, product_price, String.valueOf(et_sellingprice.getText().toString()), String.valueOf(et_sellingprice.getText().toString()), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);

                                double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;
                                double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];

                                query.InsertCartDetail(cartid, String.valueOf(check_product_id), producttype, Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, cgst, sgst, igst, cess, total_amt, tax_amt, "XXX", String.valueOf(product_batch_id));
                                View view = login.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                login.dismiss();
                                mBarcodePicker.startScanning();
                                mPaused = false;
                                get_list_data(cartid);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }

                        } else {
                            //add product with proper info
                            if (Et_productprice.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.enterproductprice), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (et_sellingprice.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (Double.parseDouble(Et_productprice.getText().toString()) < Double.parseDouble(et_sellingprice.getText().toString())) {
                                Toast.makeText(ScanProducts.this, getResources().getString(R.string.sellingpricemrp), Toast.LENGTH_LONG).show();
                                return;
                            }

                            String product_price = "0.00";
                            if (Et_productprice.getText().toString().equalsIgnoreCase("")) {
                                product_price = et_sellingprice.getText().toString();
                            } else if (Double.parseDouble(Et_productprice.getText().toString()) > 0.00) {
                                product_price = Et_productprice.getText().toString();
                            } else {
                                product_price = et_sellingprice.getText().toString();
                            }
                            long check_product_id = 0;
                            try {
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
                                check_product_id = queryClass.InsertProduct("0", 0, 0, et_product.getText().toString(), product_price, et_ean.getText().toString(), et_ean.getText().toString(), image_url, tax, tax);
                                long check_product_barcode = queryClass.InsertProductBarcode(et_ean.getText().toString(), check_product_id);
                                String offer_type = "0";
                                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                Calendar from_calender, to_calender;
                                to_calender = Calendar.getInstance();
                                from_calender = Calendar.getInstance();
                                to_calender.add(Calendar.MONTH, 1);
                                String fromdate = "", todate = "";
                                if (Double.parseDouble(product_price) > Double.parseDouble(et_sellingprice.getText().toString())) {
                                    offer_type = "1";
                                    fromdate = sdfDate.format(from_calender.getTime());
                                    todate = sdfDate.format(to_calender.getTime());
                                } else {
                                    offer_type = "0";
                                    todate = "0000-00-00 00:00:00";
                                    fromdate = "0000-00-00 00:00:00";
                                }
                                long product_batch_id = queryClass.InsertProductBatch(sharedPreference_main.getStoreId(), "1", "XXX", todate, product_price, product_price, String.valueOf(et_sellingprice.getText().toString()), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);
                                double cgst = 0.0, sgst = 0.0, igst = 0.0, cess = 0.0;
                                double[] Total_Tax_Amount = calculate_tax(cgst, sgst, igst, cess, Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];
                                query.InsertCartDetail(cartid, String.valueOf(check_product_id), et_product.getText().toString(), Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, cgst, sgst, igst, cess, total_amt, tax_amt, "XXX", String.valueOf(product_batch_id));
                                View view = login.getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                login.dismiss();
                                mPaused = false;
                                mBarcodePicker.startScanning();
                                get_list_data(cartid);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }
                    }
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                et_product.setVisibility(View.VISIBLE);
                et_ean.setVisibility(View.VISIBLE);
                Et_productprice.setVisibility(View.VISIBLE);
                et_sellingprice.setVisibility(View.VISIBLE);
                product_name_textinput.setVisibility(View.VISIBLE);
                ean_code_textinput.setVisibility(View.VISIBLE);
                product_price_layout.setVisibility(View.VISIBLE);
                // et_ean.setEnabled(false);
                more.setVisibility(View.GONE);
                et_product.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(et_product, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        chk_product.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                chk_personal.setChecked(false);
                chk_other.setChecked(false);
                chk_home_care.setChecked(false);
                chk_product.setChecked(true);
            }
        });
        chk_personal.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                chk_personal.setChecked(true);
                chk_other.setChecked(false);
                chk_home_care.setChecked(false);
                chk_product.setChecked(false);
            }
        });
        chk_other.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                chk_personal.setChecked(false);
                chk_other.setChecked(true);
                chk_home_care.setChecked(false);
                chk_product.setChecked(false);
            }
        });
        chk_home_care.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                chk_personal.setChecked(false);
                chk_other.setChecked(false);
                chk_home_care.setChecked(true);
                chk_product.setChecked(false);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = login.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                login.dismiss();
                mPaused = false;
                mBarcodePicker.startScanning();
            }
        });
        // Make dialog box visible.
        login.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    private void getEANCodeProduct(final String ean_code) { // FROM API
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "MerchantStoreProducts/getMerchantStoreProductByEANCode";
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, sharedPreference_main.getbase_inpk_url() + tag_json_obj, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response ", response);
                    pDialog.cancel();
                    JSONObject jsonO = new JSONObject(response);
                    JSONObject controls = jsonO.getJSONObject("Control");
                    if (controls.getString("Status").equals("1")) {
                        JSONObject jsonObject = jsonO.getJSONObject("Data");
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
                        long check_company = query.FindCompanyID(server_company_id);
                        ean_code = ean_code.toString().trim();
                        if (check_company == 0) {
                            try {
                                check_company = query.InsertCompany(server_company_id, company_name, "");
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }


                        long check_brand = query.FindBrandID(server_brand_id);
                        if (check_brand == 0) {
                            try {
                                check_brand = query.InsertBrand(server_brand_id, brand_name, check_company, brand_image);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }


                        long check_master_category = query.FindMasterCategoryId(server_master_category_id);
                        if (check_master_category == 0) {
                            try {
                                check_master_category = query.InsertCategory(server_master_category_id, master_category_name, master_parent_id, master_category_level, master_category_type, master_category_image, default_tax, default_tax, default_tax);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }


                        long check_category = query.FindMasterCategoryId(server_category_id);
                        if (check_category == 0) {
                            try {
                                check_category = query.InsertCategory(server_category_id, category_name, check_master_category, category_level, category_type, category_image, default_tax, default_tax, default_tax);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }


                        long check_subcategory = query.FindMasterCategoryId(server_subcategory_id);
                        if (check_subcategory == 0) {
                            try {
                                check_subcategory = query.InsertCategory(server_subcategory_id, subcategory_name, check_category, subcategory_level, subcategory_type, "", cgst, sgst, igst);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }

                        long check_product_id = query.FindMasterProductID(server_merchant_store_product_id);
                        if (check_product_id == 0) {
                            try {
                                check_product_id = query.InsertProduct(server_merchant_store_product_id, check_brand, check_subcategory, product_name, String.valueOf(product_price), ean_code, ean_code, product_image, tax_percentage, cess);
                                long check_product_barcode = query.InsertProductBarcode(ean_code, check_product_id);
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
                                long product_batch_id = query.InsertProductBatch(sharedPreference_main.getStoreId(), "1", "XXX", todate, String.valueOf(product_price), String.valueOf(product_price), String.valueOf(selling_price), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);
                                String response_local = query.GetProduct(barcode.toString().trim());
                                JSONArray jsonArray_local = new JSONArray(response_local);
                                JSONObject jsonObject_local = jsonArray_local.getJSONObject(0);
                                String productname_local = jsonObject_local.getString(ASPROD_PRODUCT_NAME);
                                String productimage_local = jsonObject_local.getString(ASPROD_IMAGE_URL);
                                double product_price_local = Double.parseDouble(jsonObject_local.getString(ASPROD_PRODUCT_PRICE));
                                double offer_price_local = Double.parseDouble(jsonObject_local.getString(ASPB_OFFER_PRICE));
                                String product_id_local = jsonObject_local.getString(ASPROD_PRODUCT_ID);
                                String offer_start_local = jsonObject_local.getString(ASPB_OFFER_START_DATE);
                                String end_date_local = jsonObject_local.getString(ASPB_OFFER_END_DATE);
                                String batchno = jsonObject_local.getString(ASPB_BATCH_NO);
                                String batchid = jsonObject_local.getString(ASPB_PRODUCT_BATCH_ID);
                                double sgst_new = Double.parseDouble(jsonObject_local.getString(ASCATEGORY_SGST_RATE));
                                double igst_new = Double.parseDouble(jsonObject_local.getString(ASCATEGORY_IGST_RATE));
                                double cgst_new = Double.parseDouble(jsonObject_local.getString(ASCATEGORY_CGST_RATE));
                                double cess_new = Double.parseDouble(jsonObject_local.getString(ASPROD_CESS_RATE));
                                double amount = 0.00;
                                int item = 0;
                                double total_amout = 0.00;
                                int total_items = 0;
                                for (int i = 0; i < scan_items.size(); i++) {
                                    if (scan_items.get(i).getOffer_price() > 0.00) {
                                        amount = scan_items.get(i).getOffer_price();
                                    } else {
                                        amount = scan_items.get(i).getProduct_price();
                                    }
                                    item = scan_items.get(i).getQty();
                                    total_amout = total_amout + (amount * item);
                                    total_items = total_items + item;
                                }
                                String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                                long cartid = query.FindCartID(cart_name);
                                if (cartid == 0) {
                                    int cartno = query.FindCartCount();
                                    cartno++;
                                    cartid = query.InsertCart("Cart " + cartno, total_amout, total_items, sharedPreference_main.getStoreId());
                                    bind_cart_spinner();
                                    for (int i = 0; i < cart_model_items.size(); i++) {
                                        if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                                            spinner_selection(i, String.valueOf(cartid));
                                            list_cart.setSelection(i);
                                        }
                                    }
                                }
                                long exists_product = query.FindCartitemID(cartid, product_id_local);
                                if (exists_product == 0) {
                                    double[] Total_Tax_Amount = calculate_tax(cgst_new, sgst_new, igst_new, cess_new, product_price_local, offer_price_local, 1);
                                    double total_amt = Total_Tax_Amount[0];
                                    double tax_amt = Total_Tax_Amount[1];
                                    query.InsertCartDetail(cartid, product_id_local, productname_local, product_price_local, offer_price_local, 1, productimage_local, cgst_new, sgst_new, igst_new, Double.parseDouble(cess), total_amt, tax_amt, batchno, batchid);
                                } else {
                                    exists_product = exists_product + 1;

                                    double[] Total_Tax_Amount = calculate_tax(Double.parseDouble(cgst), Double.parseDouble(sgst), Double.parseDouble(igst), Double.parseDouble(cess), product_price_local, offer_price_local, Integer.parseInt(String.valueOf(exists_product)));
                                    double total_amt = Total_Tax_Amount[0];
                                    double tax_amt = Total_Tax_Amount[1];

                                    query.UpdateQty(cartid, product_id_local, exists_product, total_amt, tax_amt);
                                }
                                get_list_data(cartid);
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        } else {
                            try {
                                String response_local = query.GetProduct(barcode.toString().trim());
                                JSONArray jsonArray_local = new JSONArray(response_local);
                                JSONObject jsonObject_local = jsonArray_local.getJSONObject(0);
                                String productname_local = jsonObject_local.getString(ASPROD_PRODUCT_NAME);
                                String productimage_local = jsonObject_local.getString(ASPROD_IMAGE_URL);
                                double product_price_local = Double.parseDouble(jsonObject_local.getString(ASPROD_PRODUCT_PRICE));
                                double offer_price_local = Double.parseDouble(jsonObject_local.getString(ASPB_OFFER_PRICE));
                                String product_id_local = jsonObject_local.getString(ASPROD_PRODUCT_ID);
                                String offer_start_local = jsonObject_local.getString(ASPB_OFFER_START_DATE);
                                String end_date_local = jsonObject_local.getString(ASPB_OFFER_END_DATE);
                                String batchno = jsonObject_local.getString(ASPB_BATCH_NO);
                                String batchid = jsonObject_local.getString(ASPB_PRODUCT_BATCH_ID);

                                double sgst_new = Double.parseDouble(jsonObject_local.getString(ASCATEGORY_SGST_RATE));
                                double igst_new = Double.parseDouble(jsonObject_local.getString(ASCATEGORY_IGST_RATE));
                                double cgst_new = Double.parseDouble(jsonObject_local.getString(ASCATEGORY_CGST_RATE));
                                double cess_new = Double.parseDouble(jsonObject_local.getString(ASPROD_CESS_RATE));
                                double amount = 0.00;
                                int item = 0;
                                double total_amout = 0.00;
                                int total_items = 0;
                                for (int i = 0; i < scan_items.size(); i++) {
                                    if (scan_items.get(i).getOffer_price() > 0.00) {
                                        amount = scan_items.get(i).getOffer_price();
                                    } else {
                                        amount = scan_items.get(i).getProduct_price();
                                    }
                                    item = scan_items.get(i).getQty();
                                    total_amout = total_amout + (amount * item);
                                    total_items = total_items + item;
                                }
                                String cart_name = cart_model_items.get(list_cart.getSelectedItemPosition()).getCart_name();
                                long cartid = query.FindCartID(cart_name);
                                if (cartid == 0) {
                                    int cartno = query.FindCartCount();
                                    cartno++;
                                    cartid = query.InsertCart("Cart " + cartno, total_amout, total_items, sharedPreference_main.getStoreId());
                                    bind_cart_spinner();
                                    for (int i = 0; i < cart_model_items.size(); i++) {
                                        if (cart_model_items.get(i).getCart_id().equalsIgnoreCase(String.valueOf(cartid))) {
                                            spinner_selection(i, String.valueOf(cartid));
                                            list_cart.setSelection(i);
                                        }
                                    }
                                }
                                long exists_product = query.FindCartitemID(cartid, product_id_local);
                                if (exists_product == 0) {
                                    double[] Total_Tax_Amount = calculate_tax(cgst_new, sgst_new, igst_new, cess_new, product_price_local, offer_price_local, 1);
                                    double total_amt = Total_Tax_Amount[0];
                                    double tax_amt = Total_Tax_Amount[1];
                                    query.InsertCartDetail(cartid, product_id_local, productname_local, product_price_local, offer_price_local, 1, productimage_local, cgst_new, sgst_new, igst_new, Double.parseDouble(cess), total_amt, tax_amt, batchno, batchid);
                                } else {
                                    exists_product = exists_product + 1;

                                    double[] Total_Tax_Amount = calculate_tax(Double.parseDouble(cgst), Double.parseDouble(sgst), Double.parseDouble(igst), Double.parseDouble(cess), product_price_local, offer_price_local, Integer.parseInt(String.valueOf(exists_product)));
                                    double total_amt = Total_Tax_Amount[0];
                                    double tax_amt = Total_Tax_Amount[1];

                                    query.UpdateQty(cartid, product_id_local, exists_product, total_amt, tax_amt);
                                }
                                get_list_data(cartid);
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        mBarcodePicker.stopScanning();
                        ;
                        mPaused = true;
                        show_dialog("Add", "", ean_code, "", "", "0", (long) 0, 0.0, 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.cancel();
                pDialog.cancel();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final Calendar from_calender = Calendar.getInstance();

                Map<String, String> params = new HashMap<String, String>();
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceId", sharedPreference_main.getDeviceId());
                params.put("DeviceType", "2");
                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                params.put("EANCode", ean_code);
                Log.d("Params", params.toString());
                return params;

            }
        };
        AppController.getInstance().

                addToRequestQueue(sr);

    }

    double[] calculate_tax(double get_cgst, double get_sgst, double get_igst, double get_cess, double get_product_price, double get_selling_price, int qty) {
        double price = 0.0;
        double total_amt = 0.0;
        if (get_selling_price > 0.0) {
            total_amt = get_selling_price * qty;
        } else {
            total_amt = get_product_price * qty;
        }
//        double tax_per = get_cgst + get_sgst + get_igst + get_cess;
        double tax_per = get_cgst + get_sgst + get_cess;
        double basic_price = total_amt / (1 + tax_per / 100);
//        basic_price = Math.round(basic_price);
        basic_price = round(basic_price, 2);
        double tax_amt = total_amt - basic_price;
        tax_amt = round(tax_amt, 2);
        return new double[]{total_amt, tax_amt};
    }

    void OrderJSON(double total, long total_item, double tax, long cartid) {
        try {
            JSONObject order_json = new JSONObject();
            QueryClass query = new QueryClass();
            JSONArray order_items = new JSONArray();
            String response_cart_detail = query.GetCartForFMCGProduct(cartid);
            JSONArray Cart_array = new JSONArray(response_cart_detail);
            for (int i = 0; i < Cart_array.length(); i++) {
                JSONObject cart_object = Cart_array.getJSONObject(i);
                String serverid = "";
                if (cart_object.has("server_product_id")) {
                    serverid = cart_object.getString("server_product_id");
                } else {
                    serverid = "0";
                }
                String product_name_cart = cart_object.getString("product_name");
                String ean_code = "";
                if (cart_object.has("ean_code")) {
                    ean_code = cart_object.getString("ean_code");
                } else {
                    ean_code = "0";
                }
                double product_price_cart = Double.parseDouble(cart_object.getString("product_price"));
                double offer_price_cart = Double.parseDouble(cart_object.getString("offer_price"));
                int qty_cart = Integer.parseInt(cart_object.getString("quantity"));
                double cgst = Double.parseDouble(cart_object.getString(COLUMN_CGST_RATE));
                double sgst = Double.parseDouble(cart_object.getString(COLUMN_SGST_RATE));
                double igst = Double.parseDouble(cart_object.getString(COLUMN_IGST_RATE));
                double cess = Double.parseDouble(cart_object.getString(COLUMN_CESS_RATE));
                String batchno = cart_object.getString(COLUMN_BATCH_NO);
                String batchid = cart_object.getString(COLUMN_BATCH_ID);
                JSONObject products = new JSONObject();
                products.put("ServerMerchantStoreProductId", serverid);
                products.put("ServerProductId", 0);
                products.put("EANCode", ean_code);
                products.put("ProductName", product_name_cart);
                products.put("ProductPrice", product_price_cart);
                products.put("OfferPrice", offer_price_cart);
                products.put("Quantity", qty_cart);
                products.put("CGSTRate", cgst);
                products.put("IGSTRate", igst);
                products.put("SGSTRate", sgst);
                products.put("CESSRate", cess);
                products.put("BatchNo", batchno);
                products.put("BatchId", batchid);
                order_items.put(products);
            }
            order_json.put("OrderItems", order_items);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String order_timing = sdfDate.format(now);
            order_json.put("OrderId", "0");
            order_json.put("OrderAmount", total);
            order_json.put("OrderType", "3");
            order_json.put("OrderTiming", order_timing);
            order_json.put("SpecialDiscount", 0);
            JSONObject coupon = new JSONObject();
            coupon.put("CouponCode", "");
            coupon.put("CouponDiscount", "0.0");
            order_json.put("Coupon", coupon);
            JSONArray payment_array = new JSONArray();
            JSONObject payments = new JSONObject();
            payments.put("PaymentModeId", "3");
            payments.put("Amount", total);
            payment_array.put(payments);
            order_json.put("PointId", "0");
            order_json.put("PaymentModes", payment_array);
            int home_delivery = 0;
            order_json.put("TaxableAmount", tax);
            order_json.put("HomeDelivery", home_delivery);
            order_json.put("DeliveryCharge", "0");
            SimpleDateFormat sdfDate1 = new SimpleDateFormat("yyyy-MM-dd");
            Date now1 = new Date();
            delivery_date = sdfDate1.format(now1);
            order_json.put("DeliveryDate", delivery_date);
            order_json.put("DeliverySlot", delivery_slot);
            JSONObject shopper_info = new JSONObject();
            shopper_info.put("ServerShopperId", 0);
            shopper_info.put("Name", "");
            shopper_info.put("Mobile", 0);
            shopper_info.put("EmailId", "");
            shopper_info.put("ServerAddressid", 0);
            shopper_info.put("Address", "");
            shopper_info.put("BusinessName", "");
            shopper_info.put("GSTNNo", "");
            shopper_info.put("CrediLimitType", 0);
            shopper_info.put("CreditLimit", 0.0);
            shopper_info.put("StateId", "");
            shopper_info.put("Pincode", "");
            order_json.put("ShopperInfo", shopper_info);
            if (DataStatic.isInternetAvailable(ScanProducts.this)) {
                PlaceOrder(order_json.toString(), cartid, total_item);
            } else {
                Toast.makeText(ScanProducts.this, getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT).show();
            }
            Log.e("JSON", order_json.toString());
        } catch (Exception e) {
            Log.e("Order Exception", e.getMessage());
        }
    }

    private void PlaceOrder(final String order_json, final long CartId, final long total_item) {

        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "Checkout/addSales";

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                sharedPreference_main.getbase_inpk_url() + tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pDialog.cancel();
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                //Toast.makeText(CartAdditionalInfo.this,controls.getString("me"))
                                JSONObject data_object = jsonO.getJSONObject("Data");
                                QueryClass query = new QueryClass();
                                query.DeleteOrderCart(CartId);
                                Intent i = new Intent(ScanProducts.this, OrderSuccessfully.class);
                                i.putExtra("data", response);
                                i.putExtra("items", total_item+"");
//                                i.putExtra("total_amount", TotalAmount);
//                                i.putExtra("customer_name", customer_name);
                                i.putExtra("customermobile", "");
                                startActivity(i);
                                finish();
                            } else {
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
                params.put("BillDetail", order_json);
                Log.d("Billing Response", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }

}

