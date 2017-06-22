package com.aaramon.aaramonjio.wallet;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.WidgetProperties;
import com.aaramon.aaramonjio.dataaccess.CircleTransform;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AaramMoneyAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<AaramMoneyModel> list;
	private LayoutInflater inflater;
	private SharedPreference_Main sharedPreference;
	TextView txt_str_date,txt_str_end;
	public AaramMoneyAdapter(Context context, ArrayList<AaramMoneyModel> list, TextView txt_start, TextView txt_end) {
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		txt_str_date=txt_start;
		txt_str_end=txt_end;
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
		TextView tv_name;
		TextView tv_amount;
		TextView tv_date;
		ImageView img_product;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {

		final ViewHolder holder;
		sharedPreference = new SharedPreference_Main(context);
		if (v == null) {

			holder = new ViewHolder();
			v = inflater.inflate(R.layout.aaram_money_adapter, null);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_amount = (TextView) v.findViewById(R.id.tv_amount);
			holder.tv_date = (TextView) v.findViewById(R.id.tv_date);
			holder.img_product = (ImageView) v.findViewById(R.id.img_product);

			v.setTag(holder);

		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (list.get(position) != null) {

//			long dateValue = Long.valueOf(list.get(position).getEnd_date())*1000;// its need to be in milisecond
//			Date df = new java.util.Date(dateValue);
//
//			String date = getDate(dateValue);
//			String time = getTime(dateValue);
//
			holder.tv_date.setText(list.get(position).getEnd_date());
			holder.tv_name.setText(list.get(position).getBrand_name());
			holder.tv_amount.setText("Rs. "+list.get(position).getAmount());
			
			holder.tv_name.setTypeface(WidgetProperties
					.setTextTypefaceRobotoRegular(context));
			holder.tv_amount.setTypeface(WidgetProperties
					.setTextTypefaceRobotoRegular(context));
			holder.tv_date.setTypeface(WidgetProperties
					.setTextTypefaceRobotoRegular(context));

			Picasso.with(context).load(list.get(position).getImage())
					.resize(100, 100).centerInside()
					.transform(new CircleTransform())
					.into(holder.img_product, new Callback() {
						@Override
						public void onError() {
							holder.img_product
									.setBackgroundResource(R.mipmap.no_image);
						}

						@Override
						public void onSuccess() {
						}
					});

			//Picasso.with(context).load(list.get(position).getImage()).into(holder.img_product);
			
//			v.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(context, WalletAaramShopDetails.class);
//					intent.putExtra("activityId", ""+list.get(position).getActivity_id());
//					intent.putExtra("type", "1");
//					intent.putExtra("start_date", txt_str_date.getText().toString());
//					intent.putExtra("end_date",txt_str_end.getText().toString());
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(intent);
//
//				}
//			});

		}
		return v;
	}
	private String getTime(long time) {
	    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    cal.setTimeInMillis(time);
	    Date date1 = cal.getTime();
	    int mHour = date1.getHours();
	    int mMinute = date1.getMinutes();
	    String setTime = mHour+":"+mMinute;
	    return setTime;
	}
	
	private String getDate(long time) {
	    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
	    cal.setTimeInMillis(time);
	    String date = DateFormat.format("dd-MMM-yyyy", cal).toString();
	    return date;
	}


}
