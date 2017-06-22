package com.aaramon.aaramonjio.merchant_gst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GSTCategoryAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private ArrayList<GSTCategoryModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public GSTCategoryAdapter(Context context, ArrayList<GSTCategoryModel> list) {
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
        TextView TV_Name;

    }

    @Override
    public View getView(final int position, View v, ViewGroup arg2) {

        final ViewHolder holder;
        sharedPreference = new SharedPreference_Main(context);
        if (v == null) {

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.drop_down_layout, null);
            holder.TV_Name = (TextView) v.findViewById(R.id.drop_down_head);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (list.get(position) != null) {
            holder.TV_Name.setText(list.get(position).getGSTDealerCategoryName());
        }
        return v;
    }
}
