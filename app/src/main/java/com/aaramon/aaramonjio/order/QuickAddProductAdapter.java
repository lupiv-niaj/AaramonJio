package com.aaramon.aaramonjio.order;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.syncproduct.QueryClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.aaramon.aaramonjio.order.StaticVariable.COLUMN_TOTAL_TAX_AMOUNT;

public class QuickAddProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuickAddModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;
    QueryClass queryClass;
    final MediaPlayer mp = new MediaPlayer();
    TextView txt_total_item_amount, txt_total_item, txt_tax_amount;
    long cart_id;

    public QuickAddProductAdapter(Context context, ArrayList<QuickAddModel> list, TextView txt_total_item, TextView txt_total_item_amount, long cart_id, TextView txt_tax_amt) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        queryClass = new QueryClass();
        this.txt_total_item = txt_total_item;
        this.txt_total_item_amount = txt_total_item_amount;
        this.cart_id = cart_id;
        this.txt_tax_amount = txt_tax_amt;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView TVproduct, TVprice, TVofferPrice, TVqty, TVsku;
        ImageView img_product, img_minus, img_plus, IVqucik;
    }

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {

        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(context);
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.cart_detail_layout, null);
            holder.TVproduct = (TextView) v.findViewById(R.id.TVProductName);
            holder.TVofferPrice = (TextView) v.findViewById(R.id.TVSellingPrice);
            holder.TVprice = (TextView) v.findViewById(R.id.TVProductPrice);
            holder.TVqty = (TextView) v.findViewById(R.id.TVQty);
            holder.img_product = (ImageView) v.findViewById(R.id.IVProductImage);
            holder.img_plus = (ImageView) v.findViewById(R.id.IVAdd);
            holder.img_minus = (ImageView) v.findViewById(R.id.IVMinus);
            holder.IVqucik = (ImageView) v.findViewById(R.id.IVqucik);
            holder.TVsku = (TextView) v.findViewById(R.id.TVProductSku);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            if (list.get(position).getCategory_id() == 0 && list.get(position).getProduct_id().equalsIgnoreCase("0")) {
                holder.img_minus.setVisibility(View.GONE);
                holder.img_product.setVisibility(View.GONE);
                holder.TVofferPrice.setVisibility(View.GONE);
                holder.TVprice.setVisibility(View.GONE);
                holder.TVqty.setVisibility(View.INVISIBLE);
                holder.TVsku.setVisibility(View.VISIBLE);
                holder.IVqucik.setVisibility(View.VISIBLE);
                holder.img_plus.setVisibility(View.GONE);
                holder.TVproduct.setText(list.get(position).getProductname());
                holder.TVsku.setText(list.get(position).getSku());
            } else {
                holder.img_minus.setVisibility(View.VISIBLE);
                holder.img_product.setVisibility(View.VISIBLE);
                holder.TVofferPrice.setVisibility(View.VISIBLE);
                holder.TVprice.setVisibility(View.VISIBLE);
                holder.TVqty.setVisibility(View.VISIBLE);
                holder.img_plus.setVisibility(View.VISIBLE);
                holder.IVqucik.setVisibility(View.GONE);
                holder.TVsku.setVisibility(View.GONE);
                holder.TVproduct.setText(list.get(position).getProductname());
                holder.TVqty.setText(list.get(position).getQty() + "");
                if (list.get(position).getOffer_price() == list.get(position).getProduct_price()) {
                    holder.TVprice.setText("₹" + list.get(position).getProduct_price() + "");
                    holder.TVofferPrice.setVisibility(View.GONE);
                    holder.TVprice
                            .setPaintFlags(holder.TVprice
                                    .getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    if (list.get(position).getOffer_price() == 0.00) {
                        holder.TVprice.setText("₹" + list.get(position).getProduct_price() + "");
                        holder.TVofferPrice.setVisibility(View.GONE);
                        holder.TVprice
                                .setPaintFlags(holder.TVprice
                                        .getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        holder.TVprice.setText("₹" + list.get(position).getProduct_price() + "");
                        holder.TVofferPrice.setText("₹" + list.get(position).getOffer_price() + "");
                        holder.TVprice.setPaintFlags(holder.TVprice.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.TVofferPrice.setVisibility(View.VISIBLE);
                    }
                }
                holder.TVproduct.setTypeface(WidgetProperties
                        .setTextTypefaceRobotoRegular(context));
                holder.TVprice.setTypeface(WidgetProperties
                        .setTextTypefaceRobotoRegular(context));
                holder.TVofferPrice.setTypeface(WidgetProperties
                        .setTextTypefaceRobotoRegular(context));
                holder.TVqty.setTypeface(WidgetProperties
                        .setTextTypefaceRobotoRegular(context));

                Picasso.with(context).load(list.get(position).getProductimage())
                        .resize(100, 100).centerInside()
                        .transform(new CircleTransform())
                        .into(holder.img_product, new Callback() {
                            @Override
                            public void onError() {
                                holder.img_product
                                        .setBackgroundResource(R.mipmap.no_image);
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (position == 0) {
//                            holder.IVqucik.performClick();
//                        }
//                    }
//                });
                holder.IVqucik.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog login = new Dialog(context);
                        login.setContentView(R.layout.layout_quick_add_dialog);
                        login.setCanceledOnTouchOutside(true);
                        login.setCancelable(false);
                        // Init button of login GUI
                        final QueryClass queryClass = new QueryClass();
                        final CheckBox chk_product = (CheckBox) login.findViewById(R.id.food_product_radio);
                        final CheckBox chk_personal = (CheckBox) login.findViewById(R.id.personal_care_radio);
                        final CheckBox chk_home_care = (CheckBox) login.findViewById(R.id.home_care_radio);
                        final CheckBox chk_other = (CheckBox) login.findViewById(R.id.others_radio);
                        final EditText et_sellingprice = (EditText) login.findViewById(R.id.ETsellingprice);
                        final EditText et_product = (EditText) login.findViewById(R.id.ETproductName);
                        final EditText et_ean = (EditText) login.findViewById(R.id.ETean);
                        final EditText Et_productprice = (EditText) login.findViewById(R.id.ETproductprice);
                        final TextInputLayout product_name_textinput = (TextInputLayout) login.findViewById(R.id.product_name_textinput);
                        final TextInputLayout ean_code_textinput = (TextInputLayout) login.findViewById(R.id.ean_code_textinput);

                        final RelativeLayout more = (RelativeLayout) login.findViewById(R.id.more_layout);
                        Button send_btn = (Button) login.findViewById(R.id.ad_addproduct);
                        Button cancel_btn = (Button) login.findViewById(R.id.ad_cancel);
                        final TextInputLayout product_price_layout = (TextInputLayout) login.findViewById(R.id.product_price_textinput);
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
                                product_name_textinput.setVisibility(View.VISIBLE);
                                ean_code_textinput.setVisibility(View.VISIBLE);
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
                                    Toast.makeText(context, context.getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                    et_sellingprice.requestFocus();
                                } else if (Double.parseDouble(et_sellingprice.getText().toString()) <= 0.00) {
                                    Toast.makeText(context, context.getResources().getString(R.string.entersellingprice), Toast.LENGTH_LONG).show();
                                    et_sellingprice.requestFocus();
                                } else {

                                    if (et_product.getText().toString().equalsIgnoreCase("")) {
                                        try {
                                            if (mp.isPlaying()) {
                                                mp.stop();
                                            }
                                            try {
                                                mp.reset();
                                                AssetFileDescriptor afd;
                                                afd = context.getAssets().openFd(
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

                                            double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), Double.parseDouble(et_sellingprice.getText().toString()), Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                            double total_amt = Total_Tax_Amount[0];
                                            double tax_amt = Total_Tax_Amount[1];
                                            list.get(position).setTax_amount(tax_amt);
                                            list.get(position).setTotal_amount(total_amt);

                                            queryClass.InsertCartDetail(cart_id, "0", producttype, Double.parseDouble(et_sellingprice.getText().toString()), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), total_amt, tax_amt, "XXX", "0");
                                            View view = login.getCurrentFocus();
                                            if (view != null) {
                                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            }
                                            login.dismiss();
                                        } catch (Exception e) {
                                            Log.e("Exception", e.getMessage());

                                        }
                                    } else {
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
                                                afd = context.getAssets().openFd(
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
                                            long product_batch_id = queryClass.InsertProductBatch(sharedPreference.getStoreId(), "1", "XXX", todate, product_price, product_price, String.valueOf(et_sellingprice.getText().toString()), offer_type, fromdate, todate, "0", check_product_barcode, check_product_id);
                                            double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1);
                                            double total_amt = Total_Tax_Amount[0];
                                            double tax_amt = Total_Tax_Amount[1];
                                            list.get(position).setTax_amount(tax_amt);
                                            list.get(position).setTotal_amount(total_amt);
                                            queryClass.InsertCartDetail(cart_id, "0", et_product.getText().toString(), Double.parseDouble(product_price), Double.parseDouble(et_sellingprice.getText().toString()), 1, image_url, list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), total_amt, tax_amt, "XXX", String.valueOf(product_batch_id));
                                            View view = login.getCurrentFocus();
                                            if (view != null) {
                                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            }
                                            login.dismiss();
                                            notifyDataSetChanged();
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
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                login.dismiss();
                            }
                        });
                        // Make dialog box visible.
                        login.show();

                    }
                });
                holder.img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (mp.isPlaying()) {
                            mp.stop();
                        }
                        try {
                            mp.reset();
                            AssetFileDescriptor afd;
                            afd = context.getAssets().openFd(
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
                        //Comment Here
                        try {
                            long quty = queryClass.FindCartitemID(cart_id, list.get(position).getProduct_id());
                            if (quty == 0) {
                                double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), list.get(position).getProduct_price(), list.get(position).getOffer_price(), 1);
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];
                                list.get(position).setTax_amount(tax_amt);
                                list.get(position).setTotal_amount(total_amt);
                                queryClass.InsertCartDetail(cart_id, list.get(position).getProduct_id(), list.get(position).getProductname(), list.get(position).getProduct_price(), list.get(position).getOffer_price(), 1, list.get(position).getProductimage(), list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), total_amt, tax_amt, list.get(position).getBatchNo(), list.get(position).getProduct_id());
                                list.get(position).setQty(1);
                            } else {
                                quty = quty + 1;
                                double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), list.get(position).getProduct_price(), list.get(position).getOffer_price(), Integer.parseInt(String.valueOf(quty)));
                                double total_amt = Total_Tax_Amount[0];
                                double tax_amt = Total_Tax_Amount[1];
                                list.get(position).setTax_amount(tax_amt);
                                list.get(position).setTotal_amount(total_amt);
                                queryClass.UpdateQty(cart_id, list.get(position).getProduct_id(), quty, total_amt, tax_amt);
                                list.get(position).setQty(quty);
                            }
                            double amount = 0.00;
                            long item = 0;
                            double total_amout = 0.00;
                            long total_items = 0;
                            double total_tax_amt = 0.0;
                            String response_cart_detail = queryClass.GetCartDetail(cart_id);
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

                                item = qty_cart;
                                total_amout = total_amout + (amount * item);
                                total_items = total_items + item;
                                total_tax_amt = total_tax_amt + Double.parseDouble(jsonObject1.getString(COLUMN_TOTAL_TAX_AMOUNT));
                            }
                            txt_total_item.setText(total_items + " Items");
                            txt_total_item_amount.setText("₹" + total_amout);

                            txt_tax_amount.setText("Tax ₹" + total_tax_amt);
                            if (total_items == 0) {
                                queryClass.UpdateCart(cart_id, 0, 0);
//                                queryClass.DeleteCart(cart_id);
                            } else {
                                queryClass.UpdateCart(cart_id, total_items, total_amout);
                            }
                            notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    }
                });
                holder.img_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mp.isPlaying()) {
                            mp.stop();
                        }

                        try {
                            mp.reset();
                            AssetFileDescriptor afd;
                            afd = context.getAssets()
                                    .openFd("beepunselect.wav");
                            mp.setDataSource(afd.getFileDescriptor(),
                                    afd.getStartOffset(), afd.getLength());
                            mp.prepare();
                            mp.start();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Comment Here
                        try {
                            long exists_product = queryClass.FindCartitemID(cart_id, list.get(position).getProduct_id());
                            if (exists_product == 0) {

                            } else {
                                exists_product = exists_product - 1;
                                list.get(position).setQty(exists_product);
                                if (exists_product == 0) {
                                    queryClass.DeleteCartProductFromAdd(cart_id, list.get(position).getProduct_id());
                                } else {

                                    double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), list.get(position).getProduct_price(), list.get(position).getOffer_price(), Integer.parseInt(String.valueOf(exists_product)));
                                    double total_amt = Total_Tax_Amount[0];
                                    double tax_amt = Total_Tax_Amount[1];

                                    list.get(position).setTax_amount(tax_amt);
                                    list.get(position).setTotal_amount(total_amt);
                                    queryClass.UpdateQty(cart_id, list.get(position).getProduct_id(), exists_product, total_amt, tax_amt);
                                }
                            }
                            double amount = 0.00;
                            long item = 0;
                            double total_amout = 0.00;
                            long total_items = 0;
                            double total_tax_amt = 0.0;
                            String response_cart_detail = queryClass.GetCartDetail(cart_id);
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

                                item = qty_cart;
                                total_amout = total_amout + (amount * item);
                                total_items = total_items + item;
                                total_tax_amt = total_tax_amt + Double.parseDouble(jsonObject1.getString(COLUMN_TOTAL_TAX_AMOUNT));
                            }
                            txt_total_item.setText(total_items + " Items");
                            txt_total_item_amount.setText("₹" + total_amout);
                            txt_tax_amount.setText("Tax ₹" + total_tax_amt);
                            if (total_items == 0) {
                                queryClass.UpdateCart(cart_id, 0, 0);
                                // queryClass.DeleteCart(cart_id);
                            } else {
                                queryClass.UpdateCart(cart_id, total_items, total_amout);
                            }
                            notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    }
                });
            }
        }
        return v;
    }

    double[] calculate_tax(double get_cgst, double get_sgst, double get_igst, double get_cess, double get_product_price, double get_selling_price, int qty) {
        double total_amt = 0.0;
        if (get_selling_price > 0.0) {
            total_amt = get_selling_price * qty;
        } else {
            total_amt = get_product_price * qty;
        }
        //        double tax_per = get_cgst + get_sgst + get_igst + get_cess;
        double tax_per = get_cgst + get_sgst + get_cess;
        double basic_price = total_amt / (1 + tax_per / 100);
       // basic_price = Math.round(basic_price);
        basic_price = round(basic_price, 2);
        double tax_amt = total_amt - basic_price;
        tax_amt = round(tax_amt, 2);
        return new double[]{total_amt, tax_amt};
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
