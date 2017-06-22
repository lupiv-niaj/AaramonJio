package com.aaramon.aaramonjio.order;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PaymentAdapter extends BaseAdapter {
    ArrayList<PaymentModel> itemlist;
    Context context;
    String value = "";
    LayoutInflater inflater;
    String check = "";
    TextView txt_cart_amount_rcv;
    double TotalAmount;
    CheckBox hul_check;

    double hul = 0.0;
    double coupon = 0.0;

    public PaymentAdapter(Context context, ArrayList<PaymentModel> itemlist, TextView txt_cart_amount_rcv, double TotalAmount, CheckBox hul_check, double hul, double coupon) {
        // super(context, R.layout.payment_mode_layout, itemlist);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemlist = itemlist;
        this.txt_cart_amount_rcv = txt_cart_amount_rcv;
        this.TotalAmount = TotalAmount;
        this.hul_check = hul_check;
        this.hul = hul;
        this.coupon = coupon;
        inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.payment_mode_layout,
                    parent, false);
            holder.payment_mode_amt = (EditText) convertView
                    .findViewById(R.id.mode_edittext);
            holder.icon = (ImageView) convertView.findViewById(R.id.payment_icon);
            holder.txt_payment = (TextView) convertView.findViewById(R.id.payment_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        holder.txt_payment.setText(itemlist.get(position).getName());
        Picasso.with(context).load(itemlist.get(position).getImage())
                .resize(200, 200).transform(new CircleTransform())
                .centerCrop().into(holder.icon, new Callback() {
            @Override
            public void onError() {
                holder.icon
                        .setBackgroundResource(R.mipmap.no_image);
            }

            @Override
            public void onSuccess() {
            }
        });
        if (hul_check.isChecked()) {
            if (itemlist.get(holder.ref)
                    .get_id() == 3 || itemlist.get(holder.ref)
                    .get_id() == 2) {
                convertView.setEnabled(true);
                holder.txt_payment.setTextColor(context.getResources().getColor(R.color.black));
            } else {
//                holder.txt_payment.setTextColor(context.getResources().getColor(R.color.search_layout_border));
                convertView.setEnabled(false);
                convertView.setBackgroundColor(context.getResources().getColor(R.color.search_layout_border));
            }
        } else {
            convertView.setEnabled(true);
        }
        double balance_amount = getBalance();
        if (itemlist.get(holder.ref).get_selected()) {
            holder.payment_mode_amt.setVisibility(View.VISIBLE);
            if (itemlist.get(holder.ref)
                    .get_payamt() == 0.00) {
                holder.payment_mode_amt.setText("");
//                holder.payment_mode_amt.requestFocus();
            } else {
                holder.payment_mode_amt.setText(String.valueOf(itemlist.get(holder.ref)
                        .get_payamt()));
            }

        } else {
            holder.payment_mode_amt.setVisibility(View.GONE);
            if (itemlist.get(holder.ref)
                    .get_payamt() == 0.00) {
                holder.payment_mode_amt.setText("");
//                holder.payment_mode_amt.requestFocus();
            } else {
                holder.payment_mode_amt.setText(String.valueOf(itemlist.get(holder.ref)
                        .get_payamt()));
            }
        }
//        calculate();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int temp_count = 0;
//                for (int i = 0; i < itemlist.size(); i++) {
//                    if (itemlist.get(i).get_selected()) {
//                        temp_count++;
//                    }
//                }
//                if (temp_count == 1) {
//                    for (int i = 0; i < itemlist.size(); i++) {
//                        itemlist.get(i).set_selected(false);
//                        itemlist.get(i).set_payamt(0.0);
//                    }
//                    double balance_amount = getBalance();
//                    itemlist.get(holder.ref).set_payamt(balance_amount);
//                    itemlist.get(holder.ref).set_selected(true);
//                } else {
                double balance_amount = getBalance();
                if (!itemlist.get(holder.ref).get_selected()) {
                    itemlist.get(holder.ref).set_selected(true);
                    balance_amount = balance_amount - hul - coupon;
                    itemlist.get(holder.ref).set_payamt(balance_amount);
                    holder.payment_mode_amt.setText(balance_amount + "");
                    holder.payment_mode_amt.setVisibility(View.VISIBLE);
                    holder.payment_mode_amt.requestFocus();
                    holder.payment_mode_amt.setSelection(holder.payment_mode_amt.length());
                    value = value
                            + ","
                            + String.valueOf(itemlist.get(holder.ref)
                            .get_id());
                    if (value.startsWith(",")) {
                        value = value.substring(1);
                    }
                    value = value.replace(",,", ",");
                    check = "";
                } else {
                    check = "";
                    itemlist.get(holder.ref).set_selected(false);
                    itemlist.get(holder.ref).set_payamt(0.0);
                    holder.payment_mode_amt.setText("");
                    holder.payment_mode_amt.setVisibility(View.INVISIBLE);
                    value = value.replace(String.valueOf(itemlist.get(
                            holder.ref).get_id()), "");
                    value = value.replace(",,", ",");
                    if (value.startsWith(",")) {
                        value = value.substring(1);
                    }
                    if (value.endsWith(",")) {
                        value = value.substring(0, value.length() - 1);
                    }
                    calculate();
                }
//                }
                notifyDataSetChanged();
            }
        });
        holder.payment_mode_amt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String txt_value = s.toString();
                if (txt_value.equals("") || txt_value.startsWith(".")) {
                    itemlist.get(holder.ref).set_payamt(0.00);
                } else {
                    double amt_value = Double.parseDouble(txt_value);
                    if (amt_value < 0.00) {
                        itemlist.get(holder.ref).set_payamt(0.00);
                    } else {
                        itemlist.get(holder.ref).set_payamt(
                                Double.parseDouble(txt_value));
                    }

                }
                calculate();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

        });

        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint = 7;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // TODO Auto-generated method stub
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end)
                        .toString());
                if (!builder.toString().matches(
                        "(([0-9]{1})([0-9]{0,"
                                + (maxDigitsBeforeDecimalPoint - 1)
                                + "})?)?(\\.[0-9]{0,"
                                + maxDigitsAfterDecimalPoint + "})?"

                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;
            }
        };
        holder.payment_mode_amt.setFilters(new InputFilter[]{filter});
        return convertView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemlist.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return itemlist.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
        // CheckBox cb;
        EditText payment_mode_amt;
        TextView txt_payment;
        ImageView icon;
        int ref;
    }


    double getBalance() {
        double total_amount = 0.00;
        for (int i = 0; i < itemlist.size(); i++) {
            if (itemlist.get(i).get_payamt() <= 0.00) {
                total_amount = total_amount + Double.parseDouble("0.00");
            } else {
                total_amount = total_amount + itemlist.get(i).get_payamt();
            }
        }
        double bal = TotalAmount - total_amount;
        return bal;
    }

    void calculate() {
        double total_amount = 0.00;
        for (int i = 0; i < itemlist.size(); i++) {
            if (itemlist.get(i).get_payamt() <= 0.00) {
                total_amount = total_amount + Double.parseDouble("0.00");
            } else {
                total_amount = total_amount + itemlist.get(i).get_payamt();
            }
        }
        double final_total = total_amount + hul + coupon;
        double bal = TotalAmount - final_total;
//        Toast.makeText(context, "Total Amount " + TotalAmount + "\nPay Amount " + total_amount + "\nBalance" + bal + "\nHUL " + hul + "\nCoupon " + coupon + "", Toast.LENGTH_LONG).show();
        if (bal == 0.00) {
            txt_cart_amount_rcv.setTextColor(context.getResources().getColor(R.color.amount_green));
            txt_cart_amount_rcv.setText("₹" + total_amount);
        } else {

            txt_cart_amount_rcv.setTextColor(context.getResources().getColor(R.color.amount_red));
            txt_cart_amount_rcv.setText("₹" + String.valueOf(total_amount));
        }
    }
}
