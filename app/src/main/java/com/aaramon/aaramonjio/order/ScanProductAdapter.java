package com.aaramon.aaramonjio.order;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.syncproduct.QueryClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ScanProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ScanProductModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;
    QueryClass queryClass;
    final MediaPlayer mp = new MediaPlayer();
    TextView txt_total_item_amount, txt_total_item, txt_tax_amount;

    public ScanProductAdapter(Context context, ArrayList<ScanProductModel> list, TextView txt_total_item, TextView txt_total_item_amount, TextView txt_tax_amt) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        queryClass = new QueryClass();
        this.txt_total_item = txt_total_item;
        this.txt_total_item_amount = txt_total_item_amount;
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
        TextView TVproduct, TVprice, TVofferPrice, TVqty, tv_detail;
        ImageView img_product, img_minus, img_plus;
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
            holder.tv_detail = (TextView) v.findViewById(R.id.tv_detail);
            holder.img_minus = (ImageView) v.findViewById(R.id.IVMinus);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {

//			long dateValue = Long.valueOf(list.get(position).getEnd_date())*1000;// its need to be in milisecond
//			Date df = new java.util.Date(dateValue);
//
//			String date = getDate(dateValue);
//			String time = getTime(dateValue);
//
            holder.TVproduct.setText(list.get(position).getProduct_name());
            holder.TVqty.setText(list.get(position).getQty() + "");
            double tax_per = list.get(position).getCgst() + list.get(position).getIgst() + list.get(position).getSgst() + list.get(position).getCess();
            holder.tv_detail.setText("Tax % " + tax_per + "," + list.get(position).getTax_amount() + "," + list.get(position).getTotal_amount());
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

            Picasso.with(context).load(list.get(position).getProduct_image())
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
                    Long cart_id = Long.parseLong(list.get(position).getCart_id());
                    //long exists_product = queryClass.FindCartitemID(Long.parseLong(list.get(position).getCart_id()), list.get(position).getProduct_id());
                    int exists_product = list.get(position).getQty();
                    exists_product = exists_product + 1;
                    list.get(position).setQty(exists_product);
                    double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), list.get(position).getProduct_price(), list.get(position).getOffer_price(), Integer.parseInt(String.valueOf(exists_product)));
                    double total_amt = Total_Tax_Amount[0];
                    double tax_amt = Total_Tax_Amount[1];
                    list.get(position).setTax_amount(tax_amt);
                    list.get(position).setTotal_amount(total_amt);
                    queryClass.UpdateQty(Long.parseLong(list.get(position).getCart_id()), list.get(position).getProduct_id(), exists_product, total_amt, tax_amt);
                    double amount = 0.00;
                    int item = 0;
                    double total_amout = 0.00;
                    int total_items = 0;
                    double total_tax_amt = 0.0;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOffer_price() > 0.00) {
                            amount = list.get(i).getOffer_price();
                        } else {
                            amount = list.get(i).getProduct_price();
                        }
                        item = list.get(i).getQty();
                        total_amout = total_amout + (amount * item);
                        total_items = total_items + item;
                        total_tax_amt = total_tax_amt + list.get(i).getTax_amount();
                    }
                    txt_total_item.setText(total_items + " Items");
                    txt_total_item_amount.setText("₹" + total_amout);
                    txt_tax_amount.setText("Tax ₹" + total_tax_amt);
                    if (total_items == 0) {
                        queryClass.UpdateCart(cart_id, 0, 0);
                        // queryClass.DeleteCart(Long.parseLong(list.get(position).getCart_id()));
                    } else {
                        queryClass.UpdateCart(cart_id, total_items, total_amout);
                    }
                    notifyDataSetChanged();
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
                    // long exists_product = queryClass.FindCartitemID(Long.parseLong(list.get(position).getCart_id()), list.get(position).getProduct_id());
                    Long cart_id = Long.parseLong(list.get(position).getCart_id());
                    int exists_product = list.get(position).getQty();
                    exists_product = exists_product - 1;
                    if (exists_product <= 0) {
                        queryClass.DeleteCartProduct(Integer.parseInt(list.get(position).getCart_item_id()));
                        list.remove(position);
                    } else {
                        list.get(position).setQty(exists_product);
                        double[] Total_Tax_Amount = calculate_tax(list.get(position).getCgst(), list.get(position).getSgst(), list.get(position).getIgst(), list.get(position).getCess(), list.get(position).getProduct_price(), list.get(position).getOffer_price(), Integer.parseInt(String.valueOf(exists_product)));
                        double total_amt = Total_Tax_Amount[0];
                        double tax_amt = Total_Tax_Amount[1];

                        list.get(position).setTax_amount(tax_amt);
                        list.get(position).setTotal_amount(total_amt);
                        queryClass.UpdateQty(Long.parseLong(list.get(position).getCart_id()), list.get(position).getProduct_id(), exists_product, total_amt, tax_amt);
                    }
                    double amount = 0.00;
                    int item = 0;
                    double total_amout = 0.00;
                    int total_items = 0;
                    double total_tax_amt = 0.0;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOffer_price() > 0.00) {
                            amount = list.get(i).getOffer_price();
                        } else {
                            amount = list.get(i).getProduct_price();
                        }
                        item = list.get(i).getQty();
                        total_items = total_items + item;
                        total_amout = total_amout + (amount * item);
                        total_tax_amt = total_tax_amt + list.get(i).getTax_amount();
                    }
                    txt_total_item.setText(total_items + " Items");
                    txt_total_item_amount.setText("₹" + total_amout);
                    txt_tax_amount.setText("Tax ₹" + total_tax_amt);
                    if (total_items == 0) {
                        queryClass.UpdateCart(cart_id, 0, 0);
                        // queryClass.DeleteCart(Long.parseLong(list.get(position).getCart_id()));
                    } else {
                        queryClass.UpdateCart(cart_id, total_items, total_amout);
                    }

                    notifyDataSetChanged();
                }
            });
            //Picasso.with(context).load(list.get(position).getImage()).into(holder.img_product);
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
