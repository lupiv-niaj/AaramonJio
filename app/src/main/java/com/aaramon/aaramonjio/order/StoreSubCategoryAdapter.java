package com.aaramon.aaramonjio.order;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class StoreSubCategoryAdapter extends FragmentPagerAdapter {
	ArrayList<StoreSubCategoryModel> alCategory;
	Context context;
	int productPostion;

	public StoreSubCategoryAdapter(FragmentManager paramFragmentManager,
                                   Context paramContext,
                                   ArrayList<StoreSubCategoryModel> alCategory) {
		super(paramFragmentManager);
		this.context = paramContext;
		this.alCategory = alCategory;
	}

	public int getCount() {
		return alCategory.size();
	}

	public Fragment getItem(int position) {
		Log.i("POSTION_SUB_CATEGORY", position + "");

		return new CustomerCategoryFragment();//.newInstance(alCategory.get(position).getCategory_name());
	}

	public CharSequence getPageTitle(int position) {
		return alCategory.get(position).getCategory_name();
	}

	public long getItemId(int position) {
		return position;
	}

}
