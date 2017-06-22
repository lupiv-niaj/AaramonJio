package com.aaramon.aaramonjio.controller;

public interface Constant {
	byte LOGIN_SUCCESS = 0;
	byte LOGIN_FAILED = 1;
	byte OFFERS_SUCCESS = 2;
	byte OFFERS_FAILED = 3;
	byte SIGNUP_DETAILS_SUCCESS = 4;
	byte SIGNUP_DETAILS_FAILED = 5;
	byte VERIFY_COUPON_SUCCESS = 6;
	byte VERIFY_COUPON_FAILED = 7;
	byte UPLOAD_PHOTOS_SUCCESS = 8;
	byte UPLOAD_PHOTOS_FAILED = 9;
	byte UPDATE_PROFILE_FAILED = 10;
	byte OTP_REG_FAILED = 11;
	byte OTP_REG_SUCCESS = 12;
	byte OTP_RESEND_FAILED = 13;
	byte OTP_RESEND_SUCCESS = 14;
	byte MERCH_ADD_SUCCESS = 15;
	byte MERCH_ADD_FAILED = 16;
	byte HOME_PENDING_SUCCESS = 17;
	byte HOME_PENDING_FAILED = 18;
	byte HOME_DETAIL_REPORT_FAILED = 19;
	byte HOME_DETAIL_REPORT_SUCCESS = 20;

	byte UPLOAD_PHOTOS_BUSINESS_SUCCESS = 21;
	byte UPLOAD_PHOTOS_BUSINESS_FAILED = 22;
	byte CUSTOM_OFFER_SUCCESS = 23;
	byte CUSTOM_OFFER_FAILED = 24;
	byte COMBO_OFFER_FAILED = 24;
	byte COMBO_OFFER_SUCCESS = 25;

	byte HOME_PENDING_NUM_SUCCESS = 26;
	byte HOME_PENDING_NUM_FAILED = 27;
	byte UPLOAD_PHOTOS_ACCOUNT_SUCCESS = 28;
	byte UPLOAD_PHOTOS_ACCOUNT_FAILED = 29;

	// String BASE_URL = "http://52.74.220.25:80/index.php/merchant/";
	// String BASE_URL =
	// "http://www.aaramshop.co.in:80/api/index.php/merchant/";
//	String BASE_URL = "http://www.aaramshop.co.in/api/index.php/merchant/";
//	String Baseurl="https://www.aaramshop.pk:443/api/index.php/merchant/";

	interface Message {
		String MESSAGE_MASTER_CATEGORY = "Master Category Data!";
		String MESSAGE_CATEGORY = "Category Data!";
		String MESSAGE_SUB_CATEGORY = "Sub-Category Data!";
		String MESSAGE_PRODUCT_LIST = "Product Data!";

	}

	interface Option {
		String GET_MASTER_CATEGORY = "getMasterCategories";
		String GET_CATEGORY = "getCategories";
		String GET_SUB_CATEGORY = "getSubCategories";
		String GET_PRODUCT = "getProducts";
		String SAVE_PRODUCT = "saveProducts";
	}
	interface OrderType{
		String LOCAL="0";
		String GLOBAL="1";
	}

}
