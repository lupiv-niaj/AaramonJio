package com.aaramon.aaramonjio.reports;


import android.content.Context;
import android.text.format.DateFormat;
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
import com.aaramon.aaramonjio.supplier.UtilityService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class SummaryAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<DailySummaryreportListModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public SummaryAdapter(Context context, ArrayList<DailySummaryreportListModel> list) {
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
        TextView MonthTextViewId, DateTextViewId, BillNoTextViewId, PurchaseTypeTextViewId, ItemsCountTextViewId, BillAmountTextViewId;
    }


    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(_context);
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.daily_sale_details, null);
            holder.MonthTextViewId = (TextView) v.findViewById(R.id.MonthTextViewId);
            holder.DateTextViewId = (TextView) v.findViewById(R.id.DateTextViewId);
            holder.BillNoTextViewId = (TextView) v.findViewById(R.id.BillNoTextViewId);
            holder.PurchaseTypeTextViewId = (TextView) v.findViewById(R.id.PurchaseTypeTextViewId);
            holder.ItemsCountTextViewId = (TextView) v.findViewById(R.id.ItemsCountTextViewId);
            holder.BillAmountTextViewId = (TextView) v.findViewById(R.id.BillAmountTextViewId);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            Date BillDate = null;
            try {
                BillDate = UtilityService.GetDateTimeFromTimeStamp(Long.parseLong(list.get(position).getOrderTiming()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.MonthTextViewId.setText(DateFormat.format("MMM", BillDate) + "");
            holder.DateTextViewId.setText(DateFormat.format("dd", BillDate) + "");
            holder.BillNoTextViewId.setText("Bill No: " + list.get(position).getInvoiceNo() + "");
            holder.PurchaseTypeTextViewId.setText(list.get(position).getPaymentType() + "");
            holder.ItemsCountTextViewId.setText(list.get(position).getTotalItems() + " Items");
            holder.BillAmountTextViewId.setText("₹" +list.get(position).getTotalAmount() + "");

        }
        return v;
    }
}