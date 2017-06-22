package com.aaramon.aaramonjio.supplier;

import android.content.Context;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class PurchaseEntryDetailAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<PurchaseEntryBillDetailModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public PurchaseEntryDetailAdapter(Context context, ArrayList<PurchaseEntryBillDetailModel> list) {
        this._context = context;
        inflater = (LayoutInflater) _context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        this.list=list;
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
        TextView product_name, qty, mrp;
        ImageView product_icon;
    }


    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(_context);
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.purchase_entry_list_details, null);
            holder.product_icon = (ImageView) v.findViewById(R.id.ProducticonId);
            holder.product_name = (TextView) v.findViewById(R.id.ProductNameTextViewId);
            holder.qty = (TextView) v.findViewById(R.id.ProductQuantityId);
            holder.mrp = (TextView) v.findViewById(R.id.ProductPurchaseValueId);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            holder.product_name.setText(list.get(position).getProductName() + "");
            holder.qty.setText(list.get(position).getQty() + " Items");
            holder.mrp.setText(list.get(position).getPurchaseRate() + "");
            try {
                if(list.get(position).getProductImage().equalsIgnoreCase("")){
                    Picasso.with(_context).load(String.valueOf(_context.getResources().getDrawable(R.mipmap.no_image)))
                            .resize(200, 200).centerCrop()
                            .transform(new CircleTransform())
                            .into(holder.product_icon, new Callback() {
                                @Override
                                public void onError() {
                                    holder.product_icon
                                            .setBackgroundResource(R.mipmap.no_image);
                                }

                                @Override
                                public void onSuccess() {
                                }
                            });
                }
                else

                {
                    Picasso.with(_context).load(list.get(position).getProductImage())
                            .resize(200, 200).centerCrop()
                            .transform(new CircleTransform())
                            .into(holder.product_icon, new Callback() {
                                @Override
                                public void onError() {
                                    holder.product_icon
                                            .setBackgroundResource(R.mipmap.no_image);
                                }

                                @Override
                                public void onSuccess() {
                                }
                            });
                }
            }catch (Exception e){}

            holder.product_name.setTypeface(WidgetProperties.setTextTypefaceRobotoRegular(_context));
            holder.qty.setTypeface(WidgetProperties.setTextTypefaceRobotoRegular(_context));
            holder.mrp.setTypeface(WidgetProperties.setTextTypefaceRobotoRegular(_context));

        }
        return v;
    }
}