package com.aaramon.aaramonjio.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaramon.aaramonjio.R;

public class CustomerCategoryFragment extends Fragment {

	private static final String ARG_PAGE_NUMBER = "page_number";
	public CustomerCategoryFragment() {

	}

//	public static CustomerCategoryFragment newInstance(String page) {
//		CustomerCategoryFragment fragment = new CustomerCategoryFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PAGE_NUMBER, page);
//		fragment.setArguments(args);
//		return fragment;
//	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		alproductList = new ArrayList<ProductModel>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_customer_category, container,
				false);
//		TextView txt = (TextView) view.findViewById(R.id.page_number_label);
//		String name=getArguments().getString(ARG_PAGE_NUMBER);
//		txt.setText(name);
		return view;
	}
}
