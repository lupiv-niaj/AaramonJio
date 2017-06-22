package com.aaramon.aaramonjio.wallet;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CstomerDuesDetailsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CustomerDuesDetailModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public CstomerDuesDetailsAdapter(Context context,
                                     ArrayList<CustomerDuesDetailModel> list) {
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
        TextView tv_due_since;
        TextView tv_order_number;
        TextView tv_total_order_amount;
        TextView tv_paid_cash;
        TextView tv_balance;
        TextView tv_name;
        TextView tv_due_amount;
        TextView tv_balance_money;
        TextView tv_remind;
        EditText et_balance_money;
        int ref;
    }

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {

        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(context);
        if (v == null) {

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.customer_deus_detail_adapter, null);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.tv_due_amount = (TextView) v
                    .findViewById(R.id.tv_due_amount);
            holder.et_balance_money = (EditText) v
                    .findViewById(R.id.et_balance_money);
            holder.tv_remind = (TextView) v.findViewById(R.id.tv_remind);

            holder.tv_due_since = (TextView) v.findViewById(R.id.tv_due_since);
            holder.tv_order_number = (TextView) v
                    .findViewById(R.id.tv_order_number);
            holder.tv_total_order_amount = (TextView) v
                    .findViewById(R.id.tv_total_order_amount);
            holder.tv_paid_cash = (TextView) v.findViewById(R.id.tv_paid_cash);
            holder.tv_balance = (TextView) v.findViewById(R.id.tv_balance);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            holder.ref = position;
//            long dv = Long.valueOf(list.get(position).getTv_due_since()) * 1000;
//            Date df = new java.util.Date(dv);
//            String date = new SimpleDateFormat("dd-MM-yyyy").format(df);

            holder.tv_name.setText(list.get(position).getCust_name());
            holder.tv_due_amount.setText(list.get(position)
                    .getTv_total_order_amount());
            // holder.tv_balance_money.setText(list.get(position).getCapture_money());
            // holder.tv_balance_money.setHint("0.00");

            holder.et_balance_money.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    // TODO Auto-generated method stub

                    try {
                        String txt_value = s.toString();
                        if (txt_value.equals("") || txt_value.startsWith(".")) {
                            list.get(holder.ref).setCapture_money("0.00");

                        } else {
                            if (Double.parseDouble(list.get(holder.ref)
                                    .getTv_balance()) < Double
                                    .parseDouble(txt_value)) {
                                list.get(holder.ref).setCapture_money(
                                        txt_value);
                                Toast.makeText(context,
                                        context.getResources().getString(R.string.captureamountgrtbalance),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                list.get(holder.ref).setCapture_money(
                                        txt_value);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });

            // holder.tv_remind.setText(list.get(position).getCust_name());

            holder.tv_due_since.setText(list.get(position).getTv_due_since());
            holder.tv_order_number.setText(list.get(position)
                    .getTv_order_number());
            holder.tv_total_order_amount.setText(list.get(position)
                    .getTv_total_order_amount());
            holder.tv_paid_cash.setText(list.get(position).getTv_paid_cash());
            holder.tv_balance.setText("Rs: "
                    + list.get(position).getTv_balance());

            holder.tv_name.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_due_amount.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            // holder.tv_balance_money.setTypeface(WidgetProperties
            // .setTextTypefaceRobotoRegular(context));
            holder.tv_remind.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_due_since.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_order_number.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_total_order_amount.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_paid_cash.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));
            holder.tv_balance.setTypeface(WidgetProperties
                    .setTextTypefaceRobotoRegular(context));

//            holder.tv_remind.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    String tv_remind = list.get(position)
//                            .getCustomer_chatUserName();
//
//                    Intent intent = new Intent(context, ChatActivity.class);
//                    intent.putExtra(ChatConstants.EXTRA_IS_ANONYMOUS_USER, true);
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_USER_NAME, ""
//                            + list.get(position).getCustomer_chatUserName());
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_DISPLAY_NAME, ""
//                            + list.get(position).getCust_name());
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_IS_GROUP_CONTACT,
//                            false);
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_FIRST_NAME, ""
//                            + list.get(position).getCust_name());
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_PROFILE_PIC,
//                            list.get(position).getimage_url_320() + ""
//                                    + list.get(position).getcustomer_image());
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_COVER_PIC,
//                            list.get(position).getimage_url_320() + ""
//                                    + list.get(position).getcustomer_image());
//                    intent.putExtra(ChatConstants.EXTRA_CHAT_USER_ID, ""
//                            + list.get(position).getCustomer_chatUserName());
//                    intent.putExtra("Notify", "");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    //
//                    // Log.e("CHAT ",
//                    // "EXTRA_CHAT_USER_NAME : "
//                    // + list.get(position)
//                    // .getCustomer_chatUserName()
//                    // + "EXTRA_CHAT_DISPLAY_NAME :"
//                    // + list.get(position).getCust_name()
//                    // + "EXTRA_CHAT_FIRST_NAME : "
//                    // + list.get(position).getCust_name()
//                    // + " EXTRA_CHAT_PROFILE_PIC : "
//                    // + " EXTRA_CHAT_USER_ID : ");
//                    //
//                    context.startActivity(intent);
//
//                }
//            });

        }
        return v;
    }

}
