package com.aaramon.aaramonjio.supplier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;

import java.util.ArrayList;

/**
 * Created by Aaramshop on 5/22/2017.
 */

public class TaxSchedulesAdapter extends BaseAdapter
{

    Context context;
    ArrayList<TaxScheduleModel> TaxScheduleList = new ArrayList<TaxScheduleModel>();
    LayoutInflater inflater;

    public TaxSchedulesAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.TaxScheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.spinner_data_source, null);
        ((TextView) convertView.findViewById(R.id.TaxScheduleTextViewId)).setText(TaxScheduleList.get(position).getTaxScheduleDetail());
        return convertView;
    }
}
