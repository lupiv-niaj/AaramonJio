package com.aaramon.aaramonjio.supplier;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by Aaramshop on 5/24/2017.
 */

public class PurchaseEntryProductsDetailAdapter extends BaseAdapter {
    Context context;
    PurchaseEntryModel PurchaseEntryModel = new PurchaseEntryModel();
    LayoutInflater inflater;
    final MediaPlayer mp = new MediaPlayer();
    SupplierService SupplierService = new SupplierService();
    TextView txt_amount, txt_item;
    SharedPreference_Main sharedPreference_main;
    String gst_category_id = "", stateid = "";
    TextView total_tax;

    public PurchaseEntryProductsDetailAdapter(Context context, TextView txt_amount, TextView txt_item, String gst_category_id, String stateid, TextView total_tax) {
        this.context = context;
        this.txt_amount = txt_amount;
        this.txt_item = txt_item;
        this.gst_category_id = gst_category_id;
        this.stateid = stateid;
        this.total_tax = total_tax;
        sharedPreference_main = new SharedPreference_Main(context);
    }

    public int getCount() {
        return PurchaseEntryModel.getProductEntryDetailList().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.purchase_entry_products_list, null);
        final TextView txtname, txtvalue, txtqty, CGSTView, IGSTView, SGSTView, CESSView, TotalView, TxtView, NewProductPurchaseValueId;
        ;
        txtname = (TextView) convertView.findViewById(R.id.ProductNameTextViewId);
        txtvalue = (TextView) convertView.findViewById(R.id.ProductPurchaseValueId);
        txtqty = (TextView) convertView.findViewById(R.id.ProductQuantityId);
        CGSTView = (TextView) convertView.findViewById(R.id.ProductCGSTId);
        IGSTView = (TextView) convertView.findViewById(R.id.ProductIGSTId);
        SGSTView = (TextView) convertView.findViewById(R.id.ProductSGSTId);
        CESSView = (TextView) convertView.findViewById(R.id.ProductCESSId);
        TotalView = (TextView) convertView.findViewById(R.id.ProductTOTALId);
        TxtView = (TextView) convertView.findViewById(R.id.ProductTAXId);
        NewProductPurchaseValueId = (TextView) convertView.findViewById(R.id.NewProductPurchaseValueId);
        CGSTView.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getCgst() + "");
        SGSTView.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getSgst() + "");
        IGSTView.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getIgst() + "");
        CESSView.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getCess() + "");
        TotalView.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getTotal_amt() + "");
        TxtView.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getTax_amt() + "");
        NewProductPurchaseValueId.setText(String.format("%1$.2f", PurchaseEntryModel.getProductEntryDetailList().get(position).getNewPurchaseRate()));
        txtname.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getProductBatchModel().getProductModel().getProductName());
        txtvalue.setText(String.format("%1$.2f", PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseRate()));
        txtqty.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getQty() + "");
        final ImageView img_minus, img_plus, ProducticonId;
        img_minus = (ImageView) convertView.findViewById(R.id.ProductminusId);
        img_plus = (ImageView) convertView.findViewById(R.id.ProductPlusId);
        ProducticonId = (ImageView) convertView.findViewById(R.id.ProducticonId);
        Picasso.with(context).load(PurchaseEntryModel.getProductEntryDetailList().get(position).getProductBatchModel().getProductModel().getImageUrl())
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
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    int qty = PurchaseEntryModel.getProductEntryDetailList().get(position).getQty();
                    qty = qty + 1;
                    PurchaseEntryModel.getProductEntryDetailList().get(position).setQty(qty);

                    calucalte_Tax(qty, PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseRate(), PurchaseEntryModel.getProductEntryDetailList().get(position).getCgst(), PurchaseEntryModel.getProductEntryDetailList().get(position).getSgst(), PurchaseEntryModel.getProductEntryDetailList().get(position).getCess(), PurchaseEntryModel.getProductEntryDetailList().get(position).getIgst(), txtvalue, TotalView, TxtView);
                    txtqty.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getQty() + "");
                    SupplierService.UpdatePurchaseEntryQty(PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseEntryDetailId(), (int) qty, Double.parseDouble(TotalView.getText().toString()), Double.parseDouble(TxtView.getText().toString()));
                    String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryModel.getPurchaseEntryId());
                    Log.d("ALKA Amount", TotalItemsAmount[0] + "- " + TotalItemsAmount[1]);
                    txt_amount.setText(TotalItemsAmount[0]);
                    txt_item.setText(TotalItemsAmount[1]);
                    total_tax.setText(TotalItemsAmount[2]);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                    if (PurchaseEntryModel.getProductEntryDetailList().get(position).getQty() == 1) {
                        // Delete Purchase Entry
                        SupplierService.DeletePurchaseEntryDetail(PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseEntryDetailId());
                        PurchaseEntryModel.getProductEntryDetailList().remove(position);
                    } else {
                        int qty = PurchaseEntryModel.getProductEntryDetailList().get(position).getQty();
                        qty = qty - 1;
                        PurchaseEntryModel.getProductEntryDetailList().get(position).setQty(qty);
                        calucalte_Tax(qty, PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseRate(), PurchaseEntryModel.getProductEntryDetailList().get(position).getCgst(), PurchaseEntryModel.getProductEntryDetailList().get(position).getSgst(), PurchaseEntryModel.getProductEntryDetailList().get(position).getCess(), PurchaseEntryModel.getProductEntryDetailList().get(position).getIgst(), txtvalue, TotalView, TxtView);
                        txtqty.setText(PurchaseEntryModel.getProductEntryDetailList().get(position).getQty() + "");
                        SupplierService.UpdatePurchaseEntryQty(PurchaseEntryModel.getProductEntryDetailList().get(position).getPurchaseEntryDetailId(), (int) qty, Double.parseDouble(TotalView.getText().toString()), Double.parseDouble(TxtView.getText().toString()));
                    }
                    String[] TotalItemsAmount = SupplierService.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryModel.getPurchaseEntryId());
                    txt_amount.setText(TotalItemsAmount[0]);
                    txt_item.setText(TotalItemsAmount[1]);
                    total_tax.setText(TotalItemsAmount[2]);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
//        ((ImageView)convertView.findViewById(R.id.ProductminusId))
        return convertView;
    }

    void calucalte_Tax(int qty, double price, double cgst, double sgst, double cess, double igst, TextView PurchasePriceView, TextView TotalView, TextView TxtView) {
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
           //     basic_price = Math.round(basic_price);
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
            Double basic = 0.0;
            if (stateid.equalsIgnoreCase(sharedPreference_main.getStateId())) {
                //cgst+sgst+cess
                double tax_per = cgst + sgst + cess;
                double tax_amt = total_amt * (tax_per / 100);
             //   tax_amt = Math.round(tax_amt);
                tax_amt = round(tax_amt, 2);
                TotalView.setText(total_amt + "");
                TxtView.setText(tax_amt + "");
            } else {
                //igst+cess
                double tax_per = igst + cess;
                double tax_amt = total_amt * (tax_per / 100);
             //   tax_amt = Math.round(tax_amt);
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
}

