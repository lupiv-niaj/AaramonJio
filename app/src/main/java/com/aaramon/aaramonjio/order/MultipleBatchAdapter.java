package com.aaramon.aaramonjio.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;


import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class MultipleBatchAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<ProductMultipleBatchModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public MultipleBatchAdapter(Context context, ArrayList<ProductMultipleBatchModel> list) {
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
        RadioButton multiple_price_radio;
    }


    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(_context);
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.layout_multiple_batch_listview, null);
            holder.multiple_price_radio = (RadioButton) v.findViewById(R.id.multiple_price_radio);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        if (list.get(position) != null) {
            holder.multiple_price_radio.setText(list.get(position).getOfferPrice().toString());
        }
        if (list.get(position).getCheck()) {
            holder.multiple_price_radio.setChecked(true);
        } else {
            holder.multiple_price_radio.setChecked(false);
        }

        holder.multiple_price_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = Integer.parseInt(String.valueOf(list.get(position).getProductBatchId()));
                for (int i = 0; i < list.size(); i++) {
                    if (type == list.get(i).getProductBatchId()) {
                        list.get(i).setCheck(true);
                    } else {
                        list.get(i).setCheck(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
        return v;
    }
}