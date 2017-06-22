package com.aaramon.aaramonjio.order;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.DelayAutoCompleteTextView;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.syncproduct.QueryClass;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_BATCH_ID;
import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_BATCH_NO;
import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_CESS_RATE;
import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_CGST_RATE;
import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_IGST_RATE;
import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_SGST_RATE;

/**
 * Created by DELL STORE on 19-May-17.
 */

public class CartAdditionalInfo extends Activity {
    String CartName, CartId, TotalItems, TotalAmount, tax_amount;
    TextView txt_amount, txt_cart_name, txt_cart_amount_rcv, proceed_order, TVApply;
    ImageView img_contact;
    EditText ETCouponCode;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS};
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 98;
    private boolean sentToSettings = false;
    Boolean permissionStatus = true;
    private final int REQUEST_CODE = 99;
    DelayAutoCompleteTextView search_customer;
    ProgressBar pb_indicator;
    SearchCustomerAdapter adapter;
    EditText shipping_address;
    RelativeLayout home_Delivery_layout, coupon_valid_layout, FMCG_layout;
    CheckBox chkbox_delivery;
    ArrayList<CustomerAddressModel> address;
    //ArrayList<SearchCustomerModel> customer_info_model;
    ListView payment_modes_list;
    SharedPreference_Main sharedPreference_main;
    int sizeofarray = 0;
    private PaymentAdapter PaymentAdapter;
    ArrayList<PaymentModel> payment_list;
    int customer_type = 0;
    int customer_position = 0;
    boolean jio_barcode = false, jio_card = false;
    double total_capture_amount = 0.00;
    double jio_barcode_amt = 0, jio_card_amt = 0;
    ;;
    String businessname = "", panno = "", gsttin = "", shopper_email = "", pincode = "";
    int stateid = 0;
    String coupon_code = "";
    double coupon_discount = 0.0, delivery_charge = 0.00;
    String delivery_date = "", delivery_slot = "";
    String customer_name = "", customer_mobile = "", server_shopper_id = "", customer_id = "0";
    ImageView img_back;
    long order_id;
    TextView TVAddAdditionalDetails, fmcg_text, fmcg_value, fmcg_verify, remove_redem, fmcgloyality_text;
    String customer_mode = "";
    ImageView expand_image, fmcg_sms_icon;
    // LinearLayout fmcg_verify_layout;
    TextInputLayout fmcg_otp_verify;
    EditText edt_otp_verify;
    double hul_redeem_value;
    CheckBox redem_check_true;
    double amount_payable = 0.0;
    String point_id = "0";

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
        setContentView(R.layout.customer_additional_details);
        Bundle bun = getIntent().getExtras();
        CartName = bun.getString("CartName");
        CartId = bun.getString("CartId");
        TotalItems = bun.getString("Items");
        TotalAmount = bun.getString("Amount");
        tax_amount = bun.getString("Tax");
        address = new ArrayList<CustomerAddressModel>();
        fmcg_otp_verify = (TextInputLayout) findViewById(R.id.fmcg_otp_verify);
        edt_otp_verify = (EditText) findViewById(R.id.edt_otp_verify);
        fmcg_verify = (TextView) findViewById(R.id.fmcg_verify);
        redem_check_true = (CheckBox) findViewById(R.id.redem_check_true);
        chkbox_delivery = (CheckBox) findViewById(R.id.chkbox_delivery);
        remove_redem = (TextView) findViewById(R.id.remove_redem);
        fmcgloyality_text = (TextView) findViewById(R.id.fmcgloyality_text);
        home_Delivery_layout = (RelativeLayout) findViewById(R.id.home_Delivery_layout);
        coupon_valid_layout = (RelativeLayout) findViewById(R.id.coupon_valid_layout);
        FMCG_layout = (RelativeLayout) findViewById(R.id.FMCG_layout);
        expand_image = (ImageView) findViewById(R.id.expand_image);
        fmcg_sms_icon = (ImageView) findViewById(R.id.fmcg_sms_icon);
        shipping_address = (EditText) findViewById(R.id.shipping_address);
        txt_amount = (TextView) findViewById(R.id.txt_cart_amount);
        TVAddAdditionalDetails = (TextView) findViewById(R.id.TVAddAdditionalDetails);
        txt_cart_amount_rcv = (TextView) findViewById(R.id.txt_cart_amount_rcv);
        txt_cart_name = (TextView) findViewById(R.id.cart_heading_title);
        proceed_order = (TextView) findViewById(R.id.proceed_order);
        img_contact = (ImageView) findViewById(R.id.img_contact);
        fmcg_text = (TextView) findViewById(R.id.fmcg_text);
        fmcg_value = (TextView) findViewById(R.id.fmcg_value);
        pb_indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        payment_modes_list = (ListView) findViewById(R.id.payment_modes_list);
        ETCouponCode = (EditText) findViewById(R.id.ETCouponCode);
        TVApply = (TextView) findViewById(R.id.TVApply);
        search_customer = (DelayAutoCompleteTextView) findViewById(R.id.customer_search);
        img_back = (ImageView) findViewById(R.id.img_back);
        search_customer.setThreshold(3);
        search_customer.setLoadingIndicator(pb_indicator);
        adapter = new SearchCustomerAdapter(CartAdditionalInfo.this,
                android.R.layout.simple_dropdown_item_1line, pb_indicator);
        search_customer.setAdapter(adapter);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_customer.getWindowToken(), 0);
        txt_cart_amount_rcv.setText("₹0.0");
        txt_amount.setText("₹" + TotalAmount);
        txt_cart_name.setText(CartName);
        sharedPreference_main = new SharedPreference_Main(CartAdditionalInfo.this);
        getpayments();
        expand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVAddAdditionalDetails.performClick();
            }
        });
        TVAddAdditionalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_customer.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(CartAdditionalInfo.this, getResources().getString(R.string.editcustomercheck), Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(CartAdditionalInfo.this, CustomerInfo.class);
                    if (customer_type == 1) {
                        if (server_shopper_id.equalsIgnoreCase("0")) {
                            customer_mode = "Add";
                            i.putExtra("shoppername", customer_name);
                            i.putExtra("shoppermobile", customer_mobile);
                            i.putExtra("shopperid", server_shopper_id);
                            i.putExtra("shopperemail", shopper_email);
                            i.putExtra("business", businessname);
                            i.putExtra("gst", gsttin);
                            i.putExtra("pan", panno);
                            i.putExtra("state", stateid);
                            i.putExtra("pincode", pincode);
                        } else {
                            customer_mode = "Edit";
                            i.putExtra("shoppername", customer_name);
                            i.putExtra("shoppermobile", customer_mobile);
                            i.putExtra("shopperid", server_shopper_id);
                            i.putExtra("shopperemail", shopper_email);
                            i.putExtra("business", businessname);
                            i.putExtra("gst", gsttin);
                            i.putExtra("pan", panno);
                            i.putExtra("state", stateid);
                            i.putExtra("pincode", pincode);
                        }
                    } else {
                        customer_mode = "Add";
                        i.putExtra("shoppername", customer_name);
                        i.putExtra("shoppermobile", customer_mobile);
                        i.putExtra("shopperid", server_shopper_id);
                        i.putExtra("shopperemail", shopper_email);
                        i.putExtra("business", businessname);
                        i.putExtra("gst", gsttin);
                        i.putExtra("pan", panno);
                        i.putExtra("state", stateid);
                        i.putExtra("pincode", pincode);
                    }

                    i.putExtra("Mode", customer_mode);
                    startActivityForResult(i, 2);
                }
            }
        });

        TVApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_customer.getText().toString().length() == 0) {
                    Toast.makeText(CartAdditionalInfo.this, getResources().getString(R.string.entercustomerforcoupon), Toast.LENGTH_LONG).show();
                } else if (ETCouponCode.getText().toString().length() == 0) {
                    Toast.makeText(CartAdditionalInfo.this, getResources().getString(R.string.entercouponcode), Toast.LENGTH_LONG).show();
                } else {
                    if (DataStatic.isInternetAvailable(CartAdditionalInfo.this)) {
                        if (TVApply.getText().toString().equalsIgnoreCase("apply")) {
                            QueryClass query = new QueryClass();
                            try {
                                JSONArray order_items = new JSONArray();
                                String response_cart_detail = query.GetCartForOrder(Long.parseLong(CartId));
                                JSONArray Cart_array = new JSONArray(response_cart_detail);
                                for (int i = 0; i < Cart_array.length(); i++) {
                                    JSONObject cart_object = Cart_array.getJSONObject(i);
                                    String serverid = cart_object.getString("server_product_id");
                                    String product_name_cart = cart_object.getString("product_name");
                                    double product_price_cart = Double.parseDouble(cart_object.getString("product_price"));
                                    double offer_price_cart = Double.parseDouble(cart_object.getString("offer_price"));
                                    int qty_cart = Integer.parseInt(cart_object.getString("quantity"));
                                    JSONObject products = new JSONObject();
                                    products.put("MerchantStoreProductId", serverid);
                                    products.put("ProductPrice", product_price_cart);
                                    products.put("Qty", qty_cart);
                                    order_items.put(products);
                                }
                                String valid_mobile = "";
                                if (customer_mobile.length() == 0) {
                                    valid_mobile = search_customer.getText().toString();
                                } else {
                                    valid_mobile = customer_mobile;
                                }

                                CouponValid(order_items.toString(), valid_mobile, ETCouponCode.getText().toString(), TotalAmount);
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }
                        } else {
                            ReCalculatePayment(4);
                        }
                    } else {
                        Toast.makeText(CartAdditionalInfo.this, getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        proceed_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jio_barcode = false;
                total_capture_amount = 0.00;
                jio_barcode_amt = 0;

                jio_card = false;
                jio_card_amt = 0;
                for (int i = 0; i < payment_list.size(); i++) {
                    total_capture_amount = total_capture_amount
                            + payment_list.get(i).get_payamt();
                    if (payment_list.get(i).get_id() == 3) {
                        double jio_amount = payment_list.get(i).get_payamt();
                        if (payment_list.get(i).get_selected() == true && jio_amount > 0) {
                            jio_barcode_amt = payment_list.get(i).get_payamt();
                            if (jio_barcode_amt > 0.00) {
                                jio_barcode = true;
                            } else {
                                jio_barcode = false;
                                Toast.makeText(CartAdditionalInfo.this,
                                        "Please enter JioMoney Amount",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else {
                            jio_barcode = false;
                        }

                    }

                    if (payment_list.get(i).get_id() == 2) {
                        if (payment_list.get(i).get_selected() == true) {

                            jio_card_amt = payment_list.get(i).get_payamt();
                            if (jio_card_amt > 0.00) {
                                jio_card = true;
                            } else {
                                jio_card = false;
                                Toast.makeText(CartAdditionalInfo.this,
                                        "Please enter JioMoney Amount",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                        } else {
                            jio_card = false;
                        }

                    }
                }

                if (redem_check_true.isChecked()) {
                    total_capture_amount = total_capture_amount + hul_redeem_value;
                }
                if (TVApply.getText().toString().equalsIgnoreCase("Remove")) {
                    total_capture_amount = total_capture_amount + coupon_discount;
                }
                if (total_capture_amount != Double.parseDouble(TotalAmount)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            CartAdditionalInfo.this);
                    builder.setMessage(getResources().getString(R.string.captureamountnotorderamount));
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            });
                    builder.show();
                } else {
                    if (jio_barcode == true && jio_card == true) {

                        Intent i = new Intent(CartAdditionalInfo.this,
                                CapturePayments.class);
                        i.putExtra("Barcode_Amount",
                                String.valueOf(jio_barcode_amt));
                        i.putExtra("order_id", order_id);
                        i.putExtra("VIA", "payment");
                        startActivityForResult(i, 103);
                    } else {
                        if (jio_barcode == true) {

                            Intent i = new Intent(CartAdditionalInfo.this,
                                    CapturePayments.class);
                            i.putExtra("Barcode_Amount",
                                    String.valueOf(jio_barcode_amt));
                            i.putExtra("order_id", order_id);
                            i.putExtra("VIA", "payment");
                            startActivityForResult(i, 101);

                        } else if (jio_card == true) {

                            Intent i = new Intent(CartAdditionalInfo.this,
                                    Jio_Money_Card.class);
                            i.putExtra("Card_Amount",
                                    String.valueOf(jio_card_amt));
                            i.putExtra("order_id", order_id);
                            i.putExtra("VIA", "payment");

                            startActivityForResult(i, 102);

                        } else {
                            //Make Json Here
                            if (DataStatic.isInternetAvailable(CartAdditionalInfo.this)) {
                                if (chkbox_delivery.isChecked()) {
                                    if (shipping_address.getText().toString().trim().equalsIgnoreCase("")) {
                                        Toast.makeText(CartAdditionalInfo.this,
                                                "Please Enter Home Delivery Address", Toast.LENGTH_SHORT)
                                                .show();
                                        return;
                                    }
                                }
                                OrderJSON();
                            } else {
                                Toast.makeText(CartAdditionalInfo.this,
                                        getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT)
                                        .show();
                            }

                        }

                    }
                }
            }
        });
        chkbox_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkbox_delivery.isChecked()) {
                    for (int i = 0; i < address.size(); i++) {
                        if (address.get(i).isChecked()) {
                            String adddress = address.get(i).getShopper_address();
                            shipping_address.setText(adddress);
                        }
                    }
                    expand_image.setVisibility(View.VISIBLE);
                    shipping_address.setVisibility(View.VISIBLE);
                    TVAddAdditionalDetails.setVisibility(View.VISIBLE);
                } else {
                    chkbox_delivery.setChecked(false);
                    expand_image.setVisibility(View.GONE);
                    shipping_address.setVisibility(View.GONE);
                    TVAddAdditionalDetails.setVisibility(View.GONE);
                    shipping_address.setText("");
                }
            }
        });

        search_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count <= 0) {
                    shipping_address.setText("");
                    chkbox_delivery.setChecked(false);
                    customer_name = "";
                    customer_mobile = "";
                    server_shopper_id = "0";
                    home_Delivery_layout.setVisibility(View.GONE);
                    FMCG_layout.setVisibility(View.GONE);
                    fmcgloyality_text.setVisibility(View.GONE);
                    fmcg_otp_verify.setVisibility(View.GONE);
                    edt_otp_verify.setVisibility(View.GONE);
                    fmcg_verify.setVisibility(View.GONE);
                    expand_image.setVisibility(View.GONE);
                    shipping_address.setVisibility(View.GONE);
                    TVAddAdditionalDetails.setVisibility(View.GONE);
                    coupon_valid_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartAdditionalInfo.this, ScanProducts.class);
                i.putExtra("from", "Order");
                i.putExtra("cartid", CartId);
                i.putExtra("OrderTypePage","0");
                startActivity(i);
                finish();
            }
        });
        search_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItem(position).getShopper_id().equalsIgnoreCase("0")) {
                    //   String myvalue = search_customer.getText().toString();
                    customer_mode = "Add";
                    search_customer.setText(adapter.getItem(position).getShopper_mobile());
                    String abc = search_customer.getText().toString();

                    Intent i = new Intent(CartAdditionalInfo.this, CustomerInfo.class);
                    if (!abc.matches("[1-9][0-9]{9,14}")) {
                        // blah! blah! blah!
                        i.putExtra("shoppername", abc);
                        i.putExtra("shoppermobile", "");
                    } else {
                        i.putExtra("shoppermobile", abc);
                        i.putExtra("shoppername", "");
                    }
                    i.putExtra("shopperid", server_shopper_id);
                    i.putExtra("Mode", customer_mode);
                    i.putExtra("shopperemail", shopper_email);
                    i.putExtra("business", businessname);
                    i.putExtra("gst", gsttin);
                    i.putExtra("pan", panno);
                    i.putExtra("state", stateid);
                    i.putExtra("pincode", pincode);
                    startActivityForResult(i, 2);
                } else {
                    search_customer.setText(adapter.getItem(position).getShopper_name() + "(" + adapter.getItem(position).getShopper_mobile() + ")");
                    customer_name = adapter.getItem(position).getShopper_name();
                    customer_mobile = adapter.getItem(position).getShopper_mobile();
                    server_shopper_id = adapter.getItem(position).getShopper_id();
                    businessname = adapter.getItem(position).getBusinessName();
                    gsttin = adapter.getItem(position).getGSTIN();
                    panno = adapter.getItem(position).getPAN();
                    stateid = adapter.getItem(position).getState_id();
                    pincode = adapter.getItem(position).getPincode();
//                    search_customer.clearFocus();
                    address.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(adapter.getItem(position).getShopper_address());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CustomerAddressModel dlm = new CustomerAddressModel();
                            if (customer_mode.equalsIgnoreCase("Add")) {
                                dlm.setChecked(true);
                            } else {
                                if (i == 0) {
                                    dlm.setChecked(true);
                                } else {
                                    dlm.setChecked(false);
                                }
                            }
                            dlm.setShopper_address_id(jsonObject.getString("shopper_address_id"));
                            dlm.setShopper_address_title(jsonObject.getString("shopper_address_title"));
                            dlm.setShopper_address(jsonObject.getString("shopper_address"));
                            dlm.setShopper_state_name(jsonObject.getString("shopper_state_name"));
                            dlm.setShopper_city_name(jsonObject.getString("shopper_city_name"));
                            dlm.setShopper_locality_name(jsonObject.getString("shopper_locality_name"));
                            dlm.setShopper_state_id(jsonObject.getString("shopper_state_id"));
                            dlm.setShopper_city_id(jsonObject.getString("shopper_city_id"));
                            dlm.setShopper_locality_id(jsonObject.getString("shopper_locality_id"));
                            dlm.setShopper_pincode(jsonObject.getString("shopper_pincode"));
                            dlm.setShopper_latitude(jsonObject.getString("shopper_latitude"));
                            dlm.setShopper_longitude(jsonObject.getString("shopper_longitude"));
                            address.add(dlm);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    customer_type = 1;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            search_customer.getWindowToken(), 0);
                    home_Delivery_layout.setVisibility(View.VISIBLE);
                    expand_image.setVisibility(View.GONE);
                    shipping_address.setVisibility(View.GONE);
                    TVAddAdditionalDetails.setVisibility(View.GONE);
                    coupon_valid_layout.setVisibility(View.VISIBLE);
                    String FMCGJson = FMCG_loyality(Long.parseLong(CartId));
                    GetFMCGLoyalityAPI(FMCGJson, customer_mobile, customer_name, server_shopper_id);
//                    chkbox_delivery.requestFocus();

                }
            }
        });
        img_contact.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CartAdditionalInfo.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CartAdditionalInfo.this, Manifest.permission.READ_CONTACTS)) {
                        //Show Information about why you need the permission
                        ActivityCompat.requestPermissions(CartAdditionalInfo.this, new String[]{Manifest.permission.READ_CONTACTS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                    } else if (!permissionStatus) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    } else {
                        //just request the permission
                        ActivityCompat.requestPermissions(CartAdditionalInfo.this, new String[]{Manifest.permission.READ_CONTACTS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                    permissionStatus = true;
                } else {
                    //You already have the permission, just go ahead.
                    proceedAfterPermission();
                }
            }
        });
        fmcg_sms_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLoyalityOTP(customer_mobile, server_shopper_id);
            }
        });
        fmcg_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_otp_verify.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(CartAdditionalInfo.this, getResources().getString(R.string.enterotp), Toast.LENGTH_LONG).show();
                } else {
                    VerifyLoyalityOTP(customer_mobile, server_shopper_id, edt_otp_verify.getText().toString());
                }
            }
        });
        remove_redem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReCalculatePayment(1);
            }
        });
    }

    String FMCG_loyality(long CartId) {
        JSONArray order_items = new JSONArray();
        try {
            QueryClass query = new QueryClass();
//            [{'MerchantStoreProductId':0,'ProductId':0,'EANCode':'','ProductName':'','ProductPrice':0,'SellingPrice':0,'Quantity':1}]
            String response_cart_detail = query.GetCartForFMCGProduct(CartId);
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
                JSONObject products = new JSONObject();
                products.put("MerchantStoreProductId", serverid);
                products.put("ProductId", 0);
                products.put("EANCode", ean_code);
                products.put("ProductName", product_name_cart);
                products.put("ProductPrice", product_price_cart);
                products.put("SellingPrice", offer_price_cart);
                products.put("Quantity", qty_cart);
                order_items.put(products);
            }
        } catch (Exception e) {
        }
        return order_items.toString();
    }

    void OrderJSON() {
        try {
            JSONObject order_json = new JSONObject();
            QueryClass query = new QueryClass();
            JSONArray order_items = new JSONArray();
            String response_cart_detail = query.GetCartForFMCGProduct(Long.parseLong(CartId));
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
            order_json.put("OrderAmount", Double.parseDouble(TotalAmount));
            if (Double.parseDouble(TotalAmount) < 200) {
                order_json.put("OrderType", "2");
            } else {
                order_json.put("OrderType", "1");
            }
            order_json.put("OrderTiming", order_timing);
            order_json.put("SpecialDiscount", 0);
            JSONObject coupon = new JSONObject();
            coupon.put("CouponCode", coupon_code);
            coupon.put("CouponDiscount", coupon_discount);
            order_json.put("Coupon", coupon);
            JSONArray payment_array = new JSONArray();
            for (int i = 0; i < payment_list.size(); i++) {
                if (payment_list.get(i).get_selected()) {
                    JSONObject payments = new JSONObject();
                    payments.put("PaymentModeId", payment_list.get(i).get_id());
                    payments.put("Amount", payment_list.get(i).get_payamt());
                    payment_array.put(payments);

                }
            }
            if (redem_check_true.isChecked()) {
                JSONObject payments = new JSONObject();
                payments.put("PaymentModeId", "8");
                payments.put("Amount", hul_redeem_value);
                payment_array.put(payments);
            }
            order_json.put("PointId", point_id);
            order_json.put("PaymentModes", payment_array);
            int home_delivery = 0;
            if (chkbox_delivery.isChecked()) {
                home_delivery = 1;
            } else {
                home_delivery = 0;
            }
            order_json.put("TaxableAmount", tax_amount);
            order_json.put("HomeDelivery", home_delivery);
            order_json.put("DeliveryCharge", delivery_charge);
            SimpleDateFormat sdfDate1 = new SimpleDateFormat("yyyy-MM-dd");
            Date now1 = new Date();
            delivery_date = sdfDate1.format(now1);
            order_json.put("DeliveryDate", delivery_date);
            order_json.put("DeliverySlot", delivery_slot);
            JSONObject shopper_info = new JSONObject();
            if (customer_type == 1) {
                for (int i = 0; i < address.size(); i++) {
                    if (address.get(i).isChecked()) {
                        shopper_info.put("ServerShopperId", server_shopper_id);
                        shopper_info.put("Name", customer_name);
                        shopper_info.put("Mobile", customer_mobile);
                        shopper_info.put("EmailId", "");
                        shopper_info.put("ServerAddressid", address.get(i).getShopper_address_id());
                        shopper_info.put("Address", address.get(i).getShopper_address());
                        shopper_info.put("BusinessName", businessname);
                        shopper_info.put("GSTNNo",gsttin);
                        shopper_info.put("StateId", stateid);
                        shopper_info.put("Pincode", pincode);
                    }
                }
            } else if (customer_type == 2) {
                shopper_info.put("ServerShopperId", 0);
                shopper_info.put("Name", customer_name);
                shopper_info.put("Mobile", customer_mobile);
                shopper_info.put("EmailId", "");
                shopper_info.put("ServerAddressid", 0);
                shopper_info.put("Address", "");
                shopper_info.put("BusinessName", businessname);
                shopper_info.put("GSTNNo",gsttin);
                shopper_info.put("StateId", stateid);
                shopper_info.put("Pincode", pincode);
            } else {
                shopper_info.put("ServerShopperId", 0);
                shopper_info.put("Name", "");
                shopper_info.put("Mobile", 0);
                shopper_info.put("EmailId", "");
                shopper_info.put("ServerAddressid", 0);
                shopper_info.put("Address", "");
                shopper_info.put("BusinessName", businessname);
                shopper_info.put("GSTNNo", gsttin);
                shopper_info.put("CrediLimitType", 0);
                shopper_info.put("CreditLimit", 0.0);
                shopper_info.put("StateId", stateid);
                shopper_info.put("Pincode", pincode);
            }
            order_json.put("ShopperInfo", shopper_info);

            if (DataStatic.isInternetAvailable(CartAdditionalInfo.this)) {
                PlaceOrder(order_json.toString());
            } else {
                Toast.makeText(CartAdditionalInfo.this, getResources().getString(R.string.internetnotavailable), Toast.LENGTH_SHORT).show();
            }
            Log.e("JSON", order_json.toString());
        } catch (Exception e) {
            Log.e("Order Exception", e.getMessage());
        }
    }

    void ReCalculatePayment(int action) {
        if (action == 0)//Redeem Subtract Action
        {
            double calc = 0.0;
            calc = amount_payable - hul_redeem_value;
            for (int i = 0; i < payment_list.size(); i++) {
                if (payment_list.get(i).get_id() == 3) {
                    payment_list.get(i).set_payamt(calc);
                    payment_list.get(i).set_selected(true);
                } else {
                    payment_list.get(i).set_payamt(0.0);
                    payment_list.get(i).set_selected(false);
                }
            }
//            txt_amount.setText("₹" + calc);
//            TotalAmount = String.valueOf(calc);
            amount_payable = calc;
            edt_otp_verify.setEnabled(false);
            fmcg_verify.setEnabled(false);
            redem_check_true.setChecked(true);
            edt_otp_verify.setVisibility(View.GONE);
            fmcg_otp_verify.setVisibility(View.GONE);
            fmcg_verify.setVisibility(View.GONE);
            fmcg_sms_icon.setVisibility(View.INVISIBLE);
            redem_check_true.setVisibility(View.VISIBLE);
            remove_redem.setVisibility(View.VISIBLE);
            redem_check_true.setText("₹" + hul_redeem_value + " " + fmcg_text.getText().toString() + " Applied Successfully");
            PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, hul_redeem_value, 0.0);
            payment_modes_list.setAdapter(PaymentAdapter);
            setListViewHeightBasedOnChildren(payment_modes_list);
        } else if (action == 1)//Remove Redeem Action which is taken
        {
            double calc = 0.0;
            calc = amount_payable + hul_redeem_value;
            for (int i = 0; i < payment_list.size(); i++) {
                if (payment_list.get(i).get_id() == 3) {
                    payment_list.get(i).set_payamt(calc);
                } else {
                    payment_list.get(i).set_payamt(0.0);
                }
            }
            hul_redeem_value = 0.0;
//            txt_amount.setText("₹" + calc);
//            TotalAmount = String.valueOf(calc);
            amount_payable = calc;
            edt_otp_verify.setEnabled(true);
            fmcg_verify.setEnabled(true);
            edt_otp_verify.setVisibility(View.GONE);
            fmcg_otp_verify.setVisibility(View.GONE);
            fmcg_verify.setVisibility(View.GONE);
            fmcg_sms_icon.setVisibility(View.VISIBLE);
            redem_check_true.setChecked(false);
            redem_check_true.setVisibility(View.GONE);
            remove_redem.setVisibility(View.GONE);
            redem_check_true.setText("");
            PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, 0.0, 0.0);
            payment_modes_list.setAdapter(PaymentAdapter);
            setListViewHeightBasedOnChildren(payment_modes_list);
        } else if (action == 3)//Coupon & offer Apply

        {
            coupon_code = ETCouponCode.getText().toString();
            double calc = 0.0;
//            if (redem_check_true.isChecked()) {
//                calc = amount_payable - coupon_discount - hul_redeem_value;
//            } else {
            calc = amount_payable - coupon_discount;
//            }
            for (int i = 0; i < payment_list.size(); i++) {
                if (payment_list.get(i).get_id() == 3) {
                    payment_list.get(i).set_payamt(calc);
                    payment_list.get(i).set_selected(true);
                } else {
                    payment_list.get(i).set_payamt(0.0);
                    payment_list.get(i).set_selected(false);
                }
            }

//            txt_amount.setText("₹" + calc);
//            TotalAmount = String.valueOf(calc);
            amount_payable = calc;
            TVApply.setText("REMOVE");
            if (redem_check_true.isChecked()) {
                PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, hul_redeem_value, coupon_discount);
            } else {
                PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, 0.0, coupon_discount);
            }
            payment_modes_list.setAdapter(PaymentAdapter);
            setListViewHeightBasedOnChildren(payment_modes_list);
        } else if (action == 4) {
            coupon_code = "";
            ETCouponCode.setText("");
            double calc = 0.0;
//            if (redem_check_true.isChecked()) {
            calc = amount_payable + coupon_discount;
//            } else {
//                calc = amount_payable + coupon_discount + hul_redeem_value;
//            }
            coupon_discount = 0.0;
            for (int i = 0; i < payment_list.size(); i++) {
                if (payment_list.get(i).get_id() == 3) {
                    payment_list.get(i).set_payamt(calc);
                    payment_list.get(i).set_selected(true);
                } else {
                    payment_list.get(i).set_payamt(0.0);
                    payment_list.get(i).set_selected(false);
                }
            }

//            txt_amount.setText("₹" + calc);
//            TotalAmount = String.valueOf(calc);
            amount_payable = calc;
            TVApply.setText("APPLY");
            if (redem_check_true.isChecked()) {
                PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, hul_redeem_value, 0.0);
            } else {
                PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, 0.0, 0.0);
            }
            payment_modes_list.setAdapter(PaymentAdapter);
            setListViewHeightBasedOnChildren(payment_modes_list);
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(CartAdditionalInfo.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                handleContactSelection(data);
            }
        }
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("SuccessValue");
                if (stredittext.equals("Success")) {
                    OrderJSON();
                }
            }
        }


        if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("SuccessValue");
                if (stredittext.equals("Success")) {
                    OrderJSON();
                }
            }
        }


        if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("SuccessValue");
                if (stredittext.equals("Success")) {
                    Intent i = new Intent(CartAdditionalInfo.this,
                            Jio_Money_Card.class);
                    i.putExtra("Card_Amount", String.valueOf(jio_card_amt));
                    i.putExtra("order_id", order_id);
                    i.putExtra("VIA", "payment");
                    startActivityForResult(i, 104);
                }
            }
        }
        if (requestCode == 104) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("SuccessValue");
                if (stredittext.equals("Success")) {
                    OrderJSON();
                }
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String message = data.getStringExtra("address_id");
                String address1 = data.getStringExtra("address");
                String name = data.getStringExtra("customername");
                String mobile = data.getStringExtra("customermobile");
                for (int i = 0; i < address.size(); i++) {
                    address.get(i).setChecked(false);
                }
                CustomerAddressModel dlm = new CustomerAddressModel();
                dlm.setChecked(true);
                dlm.setShopper_address_id(message);
                dlm.setShopper_address_title("");
                dlm.setShopper_address(address1);
                dlm.setShopper_state_name("");
                dlm.setShopper_city_name("");
                dlm.setShopper_locality_name("");
                dlm.setShopper_state_id("");
                dlm.setShopper_city_id("");
                dlm.setShopper_locality_id("");
                dlm.setShopper_pincode("");
                dlm.setShopper_latitude("");
                dlm.setShopper_longitude("");
                address.add(dlm);
                if (customer_mode.equalsIgnoreCase("add")) {
                    search_customer.setText(mobile + "");
                } else {
                    search_customer.setText(name + "(" + mobile + ")");
                    search_customer.setSelection(search_customer.length());
                    home_Delivery_layout.setVisibility(View.VISIBLE);
                    FMCG_layout.setVisibility(View.VISIBLE);
                    fmcgloyality_text.setVisibility(View.GONE);
                    expand_image.setVisibility(View.GONE);
                    shipping_address.setVisibility(View.GONE);
                    TVAddAdditionalDetails.setVisibility(View.GONE);
                    coupon_valid_layout.setVisibility(View.VISIBLE);
                    String FMCGJson = FMCG_loyality(Long.parseLong(CartId));
                    GetFMCGLoyalityAPI(FMCGJson, customer_mobile, customer_name, server_shopper_id);
//                    chkbox_delivery.requestFocus();
                }
            }
        }
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void handleContactSelection(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Cursor cursor = null;
                Cursor nameCursor = null;
                try {
                    cursor = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID},
                            null, null, null);

                    String phoneNumber = null;
                    String contactId = null;
                    if (cursor != null && cursor.moveToFirst()) {
                        contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    String givenName = null;///first name.
                    String familyName = null;//last name.

                    String projection[] = new String[]{ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME};
                    String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " +
                            ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                    String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId};

                    nameCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            projection, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                    if (nameCursor != null && nameCursor.moveToNext()) {
                        givenName = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                        familyName = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    }
                    search_customer.setText(givenName + " " + familyName + "(" + phoneNumber + ")");
//                    search_customer.setSelection(search_customer.length());

                    customer_name = givenName + " " + familyName;
                    customer_mobile = phoneNumber;
                    server_shopper_id = "0";
                    customer_type = 2;
//                    search_customer.clearFocus();
                    home_Delivery_layout.setVisibility(View.VISIBLE);
                    expand_image.setVisibility(View.GONE);
                    shipping_address.setVisibility(View.GONE);
                    TVAddAdditionalDetails.setVisibility(View.GONE);
                    coupon_valid_layout.setVisibility(View.VISIBLE);
                    String FMCGJson = FMCG_loyality(Long.parseLong(CartId));
                    GetFMCGLoyalityAPI(FMCGJson, customer_mobile, customer_name, server_shopper_id);
//                    chkbox_delivery.requestFocus();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                    if (nameCursor != null) {
                        nameCursor.close();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CartAdditionalInfo.this, Manifest.permission.READ_CONTACTS)) {
                    //Show Information about why you need the permission
                    ActivityCompat.requestPermissions(CartAdditionalInfo.this, new String[]{Manifest.permission.READ_CONTACTS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void GetFMCGLoyalityAPI(final String product_json, final String mobile, final String name, final String shopper_id) {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = sharedPreference_main.getbase_inpk_url() + "" + "Cart/getShopperLoyalityPoints";

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pDialog.cancel();
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                //Toast.makeText(CartAdditionalInfo.this,controls.getString("me"))
                                String schemename = "", earnedpoint = "0.0", eligible = "0.0";
                                JSONArray jArray = jsonO.getJSONArray("Data");
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject jObject = jArray.getJSONObject(0);
                                    point_id = jObject.getString("PointId");
                                    schemename = jObject.getString("SchemeName");
                                    String schemeicon = jObject.getString("SchemeIcon");
                                    hul_redeem_value = Double.parseDouble(jObject.getString("BurnedPoints"));
                                    eligible = jObject.getString("EligiblePoints");
                                    earnedpoint = jObject.getString("EarnedPoints");
                                }
                                fmcg_text.setText(schemename + "");
                                fmcg_value.setText("Earn :₹" + earnedpoint + " | Eligible:₹" + eligible + "");
                                FMCG_layout.setVisibility(View.VISIBLE);
                                fmcgloyality_text.setVisibility(View.GONE);
                                if (hul_redeem_value > 0) {
                                    fmcg_sms_icon.setVisibility(View.VISIBLE);
                                } else {
                                    fmcg_sms_icon.setVisibility(View.INVISIBLE);
                                }
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
                params.put("ShopperId", shopper_id);
                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                params.put("Products", product_json);
                params.put("ShopperMobileNo", mobile);
                params.put("ShopperName", name);
                Log.d("FMCG Response", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }


    private void VerifyLoyalityOTP(final String mobile, final String shopper_id, final String otp) {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = sharedPreference_main.getbase_inpk_url() + "" + "Cart/validateLoyalityOTP";

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pDialog.cancel();
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                ReCalculatePayment(0);
                            } else {
                                Toast.makeText(CartAdditionalInfo.this, controls.getString("Message"), Toast.LENGTH_LONG).show();
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
                params.put("ShopperId", shopper_id);
                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                params.put("ShopperMobileNo", mobile);
                params.put("OTP", otp);
                Log.d("FMCG Response", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }


    private void GetLoyalityOTP(final String mobile, final String shopper_id) {
        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = sharedPreference_main.getbase_inpk_url() + "" + "Cart/sendLoyalityOTP";

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pDialog.cancel();
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                Toast.makeText(CartAdditionalInfo.this, controls.getString("Message"), Toast.LENGTH_LONG).show();
                                fmcg_otp_verify.setVisibility(View.VISIBLE);
                                edt_otp_verify.setVisibility(View.VISIBLE);
                                fmcg_verify.setVisibility(View.VISIBLE);
//                                edt_otp_verify.requestFocus();
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
                params.put("ShopperId", shopper_id);
                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                params.put("ShopperMobileNo", mobile);
                Log.d("FMCG Response", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }


    private void CouponValid(final String product_json, final String mobile, final String apply_coupon_code, final String order_amount) {

        // TODO Auto-generated method stub
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                ETCouponCode.getWindowToken(), 0);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = sharedPreference_main.getbase_inpk_url() + "" + "ShopperCoupons/validateCouponInBilling";

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                tag_json_obj,
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
                                coupon_discount = Double.parseDouble(data_object.getString("Discount"));

                                ReCalculatePayment(3);

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
                params.put("ShopperId", "0");
                params.put("MerchantStoreId", sharedPreference_main.getStoreId());
                params.put("Products", product_json);
                params.put("CouponCode", apply_coupon_code);
                params.put("OrderAmount", order_amount);
                params.put("ShopperMobileNo", mobile);
                Log.d("Billing Response", params.toString());
                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }


    private void PlaceOrder(final String order_json) {

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
                                query.DeleteOrderCart(Long.parseLong(CartId));
                                Intent i = new Intent(CartAdditionalInfo.this, OrderSuccessfully.class);
                                i.putExtra("data", response);
                                i.putExtra("items", TotalItems);
//                                i.putExtra("total_amount", TotalAmount);
//                                i.putExtra("customer_name", customer_name);
                                i.putExtra("customermobile", customer_mobile);
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


    private void getpayments() {

        // TODO Auto-generated method stub
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String tag_json_obj = "MerchantStore/getStorePaymentModes";

        StringRequest sr = new StringRequest(
                com.android.volley.Request.Method.POST,
                sharedPreference_main.getbase_inpk_url() + tag_json_obj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pDialog.cancel();
                            payment_list = new ArrayList<>();
                            JSONObject jsonO = new JSONObject(response);
                            JSONObject controls = jsonO.getJSONObject("Control");
                            if (controls.getString("Status").equals("1")) {
                                JSONArray jsonAoffers = jsonO
                                        .getJSONArray("Data");
                                for (int i = 0; i < jsonAoffers.length(); i++) {
                                    JSONObject jsonOoffers = jsonAoffers
                                            .getJSONObject(i);
                                    int id = Integer.parseInt(jsonOoffers.getString("PaymentModeId"));
                                    String name = jsonOoffers.getString("PaymentModeName");
                                    String icon = jsonOoffers.getString("Icon");
                                    if (id == 3) {
                                        payment_list.add(new PaymentModel(name, id, true, Double.parseDouble(TotalAmount), icon));
                                    } else {
                                        payment_list.add(new PaymentModel(name, id, false, 0.0, icon));
                                    }

                                }
                                amount_payable = Double.parseDouble(TotalAmount);
                                PaymentAdapter = new PaymentAdapter(getApplicationContext(), payment_list, txt_cart_amount_rcv, Double.parseDouble(TotalAmount), redem_check_true, 0.0, 0.0);
                                payment_modes_list.setAdapter(PaymentAdapter);
                                setListViewHeightBasedOnChildren(payment_modes_list);
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
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CartAdditionalInfo.this, ScanProducts.class);
        i.putExtra("from", "Order");
        i.putExtra("cartid", CartId);
        i.putExtra("OrderTypePage","0");
        startActivity(i);
        finish();
    }
}
