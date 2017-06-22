package com.aaramon.aaramonjio.wallet;

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


public class CustomerAdvanceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CustomerAdvanceModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public CustomerAdvanceAdapter(Context context, ArrayList<CustomerAdvanceModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);


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
        TextView tv_name;
        TextView tv_amount;
        TextView tv_date;
        ImageView img_product;
        ImageView img_forward;
    }

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {

        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(context);
        if (v == null) {

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.aaram_money_adapter, null);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.tv_amount = (TextView) v.findViewById(R.id.tv_amount);
            holder.tv_date = (TextView) v.findViewById(R.id.tv_date);
            holder.img_product = (ImageView) v.findViewById(R.id.img_product);
            holder.img_forward = (ImageView) v.findViewById(R.id.image_forward);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            holder.img_forward.setVisibility(View.INVISIBLE);
            holder.tv_date.setText(list.get(position).get_order_date());
            holder.tv_name.setText(list.get(position).get_shopper_name());
            holder.tv_amount.setText("Rs. " + list.get(position).get_outstanding_amount());

            holder.tv_name.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_amount.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_date.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));

            Picasso.with(context).load(list.get(position).get_shopper_image())
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


        }
        return v;
    }

}
