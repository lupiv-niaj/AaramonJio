package com.aaramon.aaramonjio.merchant_gst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CityAdapter extends ArrayAdapter<CityModel> {
    private Context context;
    private ArrayList<CityModel> list;
    private LayoutInflater inflater;
    private SharedPreference_Main sharedPreference;

    public CityAdapter(Context context, int textViewResourceId, ArrayList<CityModel> list) {
        super(context, textViewResourceId, list);
        this.context = context;
        this.list = list;

        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    private class ViewHolder {
        TextView TV_Name;
    }

    public View getCustomView(final int position, View v, ViewGroup arg2) {

        ViewHolder holder;
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
            holder.TV_Name.setText(list.get(position).getCityName());


        }
        return v;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

}
