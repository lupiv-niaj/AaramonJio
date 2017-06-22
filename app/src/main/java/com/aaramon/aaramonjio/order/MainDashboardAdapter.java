package com.aaramon.aaramonjio.order;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by dineshsolanki on 23/05/17.
 */

class MainDashboardAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MainDashboardModel> list;
    private LayoutInflater inflater;

    public MainDashboardAdapter(Context context, ArrayList<MainDashboardModel> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
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
        ImageView IVIcon;
        TextView TVText, TVTextDetail, TVPendingOrderCount;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.dashboard_list_items, null);
            holder.IVIcon = (ImageView) view.findViewById(R.id.IVIcon);
            holder.TVText = (TextView) view.findViewById(R.id.TVText);
            holder.TVTextDetail = (TextView) view.findViewById(R.id.TVTextDetail);
            holder.TVPendingOrderCount = (TextView) view.findViewById(R.id.TVPendingOrderCount);
            holder.IVIcon.setImageResource(list.get(position).getIcon());
            holder.TVText.setText(list.get(position).getText());
            holder.TVTextDetail.setText(list.get(position).getTextDetail());
//            holder.IVIcon.setColorFilter(Color.argb(61,181,234,255));


            if (list.get(position).getPendinOrderCount().equalsIgnoreCase("")) {
                holder.TVPendingOrderCount.setVisibility(View.INVISIBLE);
            } else {
                holder.TVPendingOrderCount.setVisibility(View.VISIBLE);
            }

            holder.TVPendingOrderCount.setText(list.get(position).getPendinOrderCount());
        }
        return view;
    }
}
