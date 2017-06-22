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


public class SupplierAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<SupplierListModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public SupplierAdapter(Context context, ArrayList<SupplierListModel> list) {
        this._context = context;
        inflater = (LayoutInflater) _context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        this.list = list;
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
        TextView supplier_name, brand_name, supplier_contact;
        ImageView supplier_icon;
    }


    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(_context);
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.activity_frequent_supplier_list, null);
            holder.supplier_icon = (ImageView) v.findViewById(R.id.supplier_icon);
            holder.supplier_name = (TextView) v.findViewById(R.id.supplier_name);
            holder.brand_name = (TextView) v.findViewById(R.id.brand_name);
            holder.supplier_contact = (TextView) v.findViewById(R.id.supplier_contact);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            holder.supplier_name.setText(list.get(position).getSupplierName() + "");
            holder.brand_name.setText(list.get(position).getSupplierCompany() + "");
            holder.supplier_contact.setText(list.get(position).getSupplierMobile() + "");
            try {
                if (list.get(position).getSupplierImage().equalsIgnoreCase("")) {
                    Picasso.with(_context).load(String.valueOf(_context.getResources().getDrawable(R.mipmap.no_image)))
                            .resize(200, 200).centerCrop()
                            .transform(new CircleTransform())
                            .into(holder.supplier_icon, new Callback() {
                                @Override
                                public void onError() {
                                    holder.supplier_icon
                                            .setBackgroundResource(R.mipmap.no_image);
                                }

                                @Override
                                public void onSuccess() {
                                }
                            });
                } else

                {
                    Picasso.with(_context).load(list.get(position).getSupplierImage())
                            .resize(200, 200).centerCrop()
                            .transform(new CircleTransform())
                            .into(holder.supplier_icon, new Callback() {
                                @Override
                                public void onError() {
                                    holder.supplier_icon
                                            .setBackgroundResource(R.mipmap.no_image);
                                }

                                @Override
                                public void onSuccess() {
                                }
                            });
                }
            } catch (Exception e) {
            }

            holder.supplier_name.setTypeface(WidgetProperties.setTextTypefaceRobotoRegular(_context));
            holder.brand_name.setTypeface(WidgetProperties.setTextTypefaceRobotoRegular(_context));
            holder.supplier_contact.setTypeface(WidgetProperties.setTextTypefaceRobotoRegular(_context));

        }
        return v;
    }
}