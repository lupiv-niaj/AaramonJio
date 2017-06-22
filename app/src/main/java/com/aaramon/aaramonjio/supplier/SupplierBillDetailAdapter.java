package com.aaramon.aaramonjio.supplier;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;

/**
 * Created by Aaramshop on 5/16/2017.
 */

public class SupplierBillDetailAdapter extends BaseAdapter {
    Context context;
    SupplierModel SupplierModel = new SupplierModel();
    LayoutInflater inflater;

    public SupplierBillDetailAdapter() {
    }

    public SupplierBillDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.SupplierModel.SupplierBillList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SupplierModel getSupplierModel() {
        return SupplierModel;
    }

    public void setSupplierModel(SupplierModel supplierModel) {
        SupplierModel = supplierModel;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.supplier_history_bills_detail, null);
        ((TextView) convertView.findViewById(R.id.MonthTextViewId)).setText(SupplierModel.SupplierBillList.get(position).getMonth());
        ((TextView) convertView.findViewById(R.id.DateTextViewId)).setText(String.format("%02d", SupplierModel.SupplierBillList.get(position).getDay()));
        ((TextView) convertView.findViewById(R.id.BillNoTextViewId)).setText("Bill No: " + SupplierModel.SupplierBillList.get(position).getBillNo());
        ((TextView) convertView.findViewById(R.id.PurchaseTypeTextViewId)).setText("Purchase Type: " + SupplierModel.SupplierBillList.get(position).getPurchaseType());
        ((TextView) convertView.findViewById(R.id.ItemsCountTextViewId)).setText(String.valueOf(SupplierModel.SupplierBillList.get(position).getItemsCount()) + " Items");
        ((TextView) convertView.findViewById(R.id.BillAmountTextViewId)).setText(String.format("%1$.2f", SupplierModel.SupplierBillList.get(position).getBillAmount()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(context, PurchaseEntryDetailActivity.class);
                m.putExtra("PurchaseEntryId", String.valueOf(SupplierModel.getSupplierBillList().get(position).getPurchaseEntryId()));
                m.putExtra("SupplierId", String.valueOf(SupplierModel.getSupplierId()));
                m.putExtra("SupplierCode", SupplierModel.getSupplierCode());
                m.putExtra("SupplierName", SupplierModel.getSuppplierName());
//                m.putExtra("SupplierAddress",SupplierModel.getSupplierAddress());
//                m.putExtra("SupplierMobile",  SupplierModel.getSupplierMobile());
//                m.putExtra("SupplierOutstanding",  SupplierModel.getSupplierOutstanding());

                context.startActivity(m);
            }
        });
        return convertView;
    }
}
