package com.aaramon.aaramonjio.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.syncproduct.QueryClass;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CartAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private ArrayList<CartModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;
    QueryClass query;
    Spinner spn;

    public CartAdapter(Context context, ArrayList<CartModel> list, Spinner spn) {
        this.context = context;
        this.list = list;
        query = new QueryClass();
        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        this.spn = spn;
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
        TextView TVcart, tv_date, tv_item;
        LinearLayout delete_layout;
    }

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {

        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(context);
        if (v == null) {

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.cart_spinner_layout, null);
            holder.TVcart = (TextView) v.findViewById(R.id.tv_spinner_cart);
            holder.tv_item = (TextView) v.findViewById(R.id.cart_item);
            holder.tv_date = (TextView) v.findViewById(R.id.cart_date);
            holder.delete_layout = (LinearLayout) v.findViewById(R.id.delete_layout);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            if (list.get(position).isCheck()) {
                holder.TVcart.setText(list.get(position).getCart_name() + "");
                holder.tv_item.setVisibility(View.GONE);
                holder.tv_date.setVisibility(View.GONE);
                holder.delete_layout.setVisibility(View.GONE);

            } else {
                holder.TVcart.setText(list.get(position).getCart_name() + "");
                if (list.get(position).getCart_name().equalsIgnoreCase("New Cart")) {
                    holder.tv_item.setVisibility(View.GONE);
                    holder.tv_date.setVisibility(View.GONE);
                    holder.delete_layout.setVisibility(View.GONE);

                } else {
                    holder.tv_item.setVisibility(View.VISIBLE);
                    holder.tv_date.setVisibility(View.VISIBLE);
                    holder.tv_item.setText("Items " + list.get(position).getItems() + "    ₹ " + list.get(position).getAmount() + "");
                    String date = list.get(position).getDate();
                    date = date.substring(11, 16);
                    holder.tv_date.setText(date);
                    holder.delete_layout.setVisibility(View.VISIBLE);
                }
            }
            holder.delete_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.information))
                            .setMessage(
                                    context.getResources().getString(R.string.deletecartconfirm))
                            .setPositiveButton(context.getResources().getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // continue with delete
                                            query.DeleteOrderCart(Long.parseLong(list.get(position).getCart_id()));
                                            list.remove(position);


                                            for (int i = 0; i < list.size(); i++) {
                                                if (i == 0) {
                                                    list.get(i).setCheck(true);
                                                } else {
                                                    list.get(i).setCheck(false);
                                                }
                                            }
                                            notifyDataSetChanged();
                                            spn.setSelection(0);

                                            //         Toast.makeText(Customer_transaction.this, "Allow Credit Limit Yes", Toast.LENGTH_LONG).show();
                                        }
                                    })
                            .setNegativeButton(context.getResources().getString(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // do nothing

                                        }
                                    })// .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });
//            holder.TVcart.setTypeface(WidgetProperties
//                    .setTextTypefaceRobotoRegular(context));
        }
        return v;
    }
}
