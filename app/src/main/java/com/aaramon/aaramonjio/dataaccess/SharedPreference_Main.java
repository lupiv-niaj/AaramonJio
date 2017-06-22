package com.aaramon.aaramonjio.dataaccess;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference_Main {

    private Context context;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    public SharedPreference_Main(Context context) {
        this.context = context;
//		sharedPreference = context.getSharedPreferences("PREF_READ",
//				Context.MODE_PRIVATE);
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void removePreference() {
        editor = sharedPreference.edit();
        editor.clear().apply();
    }

    public void setlanguage(String language) {

        editor = sharedPreference.edit();
        editor.putString("LANG", language);
        editor.apply();
    }

    public String getlanguage() {
        String language = sharedPreference.getString("LANG", "");
        return language;
    }


    public void set_server_product_id(String language) {

        editor = sharedPreference.edit();
        editor.putString("server_product_id", language);
        editor.apply();
    }

    public String get_server_product_id() {
        String language = sharedPreference.getString("server_product_id", "");
        return language;
    }

    public void set_last_sync(String language) {
        editor = sharedPreference.edit();
        editor.putString("last_sync", language);
        editor.apply();
    }

    public String get_last_sync() {
        String language = sharedPreference.getString("last_sync", "");
        return language;
    }

    public void set_language_text(String language_text) {

        editor = sharedPreference.edit();
        editor.putString("LANG_text", language_text);
        editor.apply();
    }

    public String get_language_text() {
        String language_text = sharedPreference.getString("LANG_text", "");
        return language_text;
    }

    public void deviceId(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("deviceId", deviceId);
        editor.apply();
    }

    public String getDeviceId() {
        String deviceId = sharedPreference.getString("deviceId", "");
        return deviceId;
    }

    public void setIsRegistered(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("IsRegistered", deviceId);
        editor.apply();
    }

    public String getIsRegistered() {
        String deviceId = sharedPreference.getString("IsRegistered", "");
        return deviceId;
    }

    public void setStoreId(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("StoreId", deviceId);
        editor.apply();
    }

    public String getStoreId() {
        String deviceId = sharedPreference.getString("StoreId", "");
        return deviceId;
    }

    public void setOrderType(String OrderType) {

        editor = sharedPreference.edit();
        editor.putString("OrderType", OrderType);
        editor.apply();
    }

    public String getOrderType() {
        String deviceId = sharedPreference.getString("OrderType", "");
        return deviceId;
    }

    public void setStoreCode(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("StoreCode", deviceId);
        editor.apply();
    }

    public String getStoreCode() {
        String deviceId = sharedPreference.getString("StoreCode", "");
        return deviceId;
    }

    public void setStoreImage(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("StoreImage", deviceId);
        editor.apply();
    }

    public String getStoreImage() {
        String deviceId = sharedPreference.getString("StoreImage", "");
        return deviceId;
    }


    public String getChatUserName() {
        String chatUserName = sharedPreference.getString("chatUserName", "");
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        editor = sharedPreference.edit();
        editor.putString("chatUserName", chatUserName);
        editor.apply();
    }
//	public void setChatUsername(String deviceId) {
//
//		editor = sharedPreference.edit();
//		editor.putString("ChatUsername", deviceId);
//		editor.apply();
//	}

//	public String getChatUsername() {
//		String deviceId = sharedPreference.getString("ChatUsername", "");
//		return deviceId;
//	}

    public void setMobileVerified(int deviceId) {

        editor = sharedPreference.edit();
        editor.putInt("MobileVerified", deviceId);
        editor.apply();
    }

    public int getMobileVerified() {
        int deviceId = sharedPreference.getInt("MobileVerified", 0);
        return deviceId;
    }

//	public void setCountry(String deviceId) {
//
//		editor = sharedPreference.edit();
//		editor.putString("Country", deviceId);
//		editor.apply();
//	}
//
//	public String getCountry() {
//		String deviceId = sharedPreference.getString("Country", "");
//		return deviceId;
//	}

    public void setMobile(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("Mobile", deviceId);
        editor.apply();
    }

    public String getMobile() {
        String deviceId = sharedPreference.getString("Mobile", "");
        return deviceId;
    }

    public void setStoreName(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("StoreName", deviceId);
        editor.apply();
    }

    public String getStoreName() {
        String deviceId = sharedPreference.getString("StoreName", "");
        return deviceId;
    }

    public void setFullName(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("FullName", deviceId);
        editor.apply();
    }

    public String getFullName() {
        String deviceId = sharedPreference.getString("FullName", "");
        return deviceId;
    }

    public void setEmailId(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("EmailId", deviceId);
        editor.apply();
    }

    public String getEmailId() {
        String deviceId = sharedPreference.getString("EmailId", "");
        return deviceId;
    }

    public void setDistance(String distance) {

        editor = sharedPreference.edit();
        editor.putString("Distance", "");
        editor.apply();
    }

    public String getDistance() {
        String deviceId = sharedPreference.getString("Distance", "");
        return deviceId;
    }

    public void setHomeDelivery(String distance) {

        editor = sharedPreference.edit();
        editor.putString("HomeDelivery", "");
        editor.apply();
    }

    public String getHomeDelivery() {
        String deviceId = sharedPreference.getString("HomeDelivery", "");
        return deviceId;
    }

    public void setStoreAddress(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("StoreAddress", deviceId);
        editor.apply();
    }

    public String getStoreAddress() {
        String deviceId = sharedPreference.getString("StoreAddress", "");
        return deviceId;
    }

    public void setLocality(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("Locality", deviceId);
        editor.apply();
    }

    public String getLocality() {
        String deviceId = sharedPreference.getString("Locality", "");
        return deviceId;
    }

    public void setCity(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("City", deviceId);
        editor.apply();
    }

    public String getCity() {
        String deviceId = sharedPreference.getString("City", "");
        return deviceId;
    }

    public void setState(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("State", deviceId);
        editor.apply();
    }

    public String getState() {
        String deviceId = sharedPreference.getString("State", "");
        return deviceId;
    }

    public void setPincode(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("Pincode", deviceId);
        editor.apply();
    }

    public String getPincode() {
        String deviceId = sharedPreference.getString("Pincode", "");
        return deviceId;
    }


    public void setGST_REG(String gst_reg) {

        editor = sharedPreference.edit();
        editor.putString("GST_REG", gst_reg);
        editor.apply();
    }

    public String getGST_REG() {
        String gst_reg = sharedPreference.getString("GST_REG", "");
        return gst_reg;
    }

    public void setBusinessName(String BusinessName) {

        editor = sharedPreference.edit();
        editor.putString("BusinessName", BusinessName);
        editor.apply();
    }

    public String getBusinessName() {
        String BusinessName = sharedPreference.getString("BusinessName", "");
        return BusinessName;
    }


    public void setGSTIN(String GSTIN) {

        editor = sharedPreference.edit();
        editor.putString("GSTIN", GSTIN);
        editor.apply();
    }

    public String getGSTIN() {
        String GSTIN = sharedPreference.getString("GSTIN", "");
        return GSTIN;
    }



    public void setGSTBusinessCategoryId(String GSTBusinessCategoryId) {

        editor = sharedPreference.edit();
        editor.putString("GSTBusinessCategoryId", GSTBusinessCategoryId);
        editor.apply();
    }

    public String getGSTBusinessCategoryId() {
        String GSTBusinessCategoryId = sharedPreference.getString("GSTBusinessCategoryId", "");
        return GSTBusinessCategoryId;
    }


    public void setGSTBusinessCategoryName(String GSTBusinessCategoryName) {

        editor = sharedPreference.edit();
        editor.putString("GSTBusinessCategoryName", GSTBusinessCategoryName);
        editor.apply();
    }

    public String getGSTBusinessCategoryName() {
        String GSTBusinessCategoryName = sharedPreference.getString("GSTBusinessCategoryName", "");
        return GSTBusinessCategoryName;
    }


    public void setGSTDealerCategoryName(String GSTDealerCategoryName) {

        editor = sharedPreference.edit();
        editor.putString("GSTDealerCategoryName", GSTDealerCategoryName);
        editor.apply();
    }

    public String getGSTDealerCategoryName() {
        String GSTDealerCategoryName = sharedPreference.getString("GSTDealerCategoryName", "");
        return GSTDealerCategoryName;
    }

    public void setGSTCategoryId(String GSTCategoryId) {

        editor = sharedPreference.edit();
        editor.putString("GSTCategoryId", GSTCategoryId);
        editor.apply();
    }

    public String getGSTCategoryId() {
        String GSTCategoryId = sharedPreference.getString("GSTCategoryId", "");
        return GSTCategoryId;
    }


    public void setStateId(String StateId) {

        editor = sharedPreference.edit();
        editor.putString("StateId", StateId);
        editor.apply();
    }

    public String getStateId() {
        String StateId = sharedPreference.getString("StateId", "");
        return StateId;
    }




    public void setImageUrl320(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("setImageUrl320", deviceId);
        editor.apply();
    }

    public String getImageUrl320() {
        String deviceId = sharedPreference.getString("setImageUrl320", "");
        return deviceId;
    }

    public void setBlurBgImagePath(String deviceId) {

        editor = sharedPreference.edit();
        editor.putString("BlurBgImagePath", deviceId);
        editor.apply();
    }

    public String getBlurBgImagePath() {
        String deviceId = sharedPreference.getString("BlurBgImagePath", "");
        return deviceId;
    }

    public void setOrderId(String order_id) {

        editor = sharedPreference.edit();
        editor.putString("order_id", order_id);
        editor.apply();
    }

    public String getOrderId() {
        String order_id = sharedPreference.getString("order_id", "");
        return order_id;
    }

    public void setCheckedVal(String CheckedVal) {

        editor = sharedPreference.edit();
        editor.putString("CheckedVal", CheckedVal);
        editor.apply();
    }

    public String getCheckedVal() {
        String CheckedVal = sharedPreference.getString("CheckedVal", "");
        return CheckedVal;
    }

    public void setLocalitySignUp(String LocalitySignUp) {

        editor = sharedPreference.edit();
        editor.putString("LocalitySignUp", LocalitySignUp);
        editor.apply();
    }

    public String getLocalitySignUp() {
        String CheckedVal = sharedPreference.getString("LocalitySignUp", "");
        return CheckedVal;
    }

    public void setPaymentMode(String PaymentMode) {

        editor = sharedPreference.edit();
        editor.putString("PaymentMode", PaymentMode);
        editor.apply();
    }

    public String getPaymentMode() {
        String CheckedVal = sharedPreference.getString("PaymentMode", "");
        return CheckedVal;
    }

    public void setPaymentModeId(String PaymentModeId) {

        editor = sharedPreference.edit();
        editor.putString("PaymentModeId", PaymentModeId);
        editor.apply();
    }

    public String getPaymentModeId() {
        String CheckedVal = sharedPreference.getString("PaymentModeId", "");
        return CheckedVal;
    }

    public void setminOrderType(String minOrderType) {

        editor = sharedPreference.edit();
        editor.putString("minOrderType", minOrderType);
        editor.apply();
    }

    public String getminOrderType() {
        String CheckedVal = sharedPreference.getString("minOrderType", "");
        return CheckedVal;
    }

    public void setstrMiniOrderValue(String strMiniOrderValue) {

        editor = sharedPreference.edit();
        editor.putString("strMiniOrderValue", strMiniOrderValue);
        editor.apply();
    }

    public String getstrMiniOrderValue() {
        String CheckedVal = sharedPreference.getString("strMiniOrderValue", "");
        return CheckedVal;
    }

    public void setSku(String Sku) {

        editor = sharedPreference.edit();
        editor.putString("Sku", Sku);
        editor.apply();
    }

    public String getSku() {
        String CheckedVal = sharedPreference.getString("Sku", "");
        return CheckedVal;
    }

    public void setProduct(String Product) {

        editor = sharedPreference.edit();
        editor.putString("Product", Product);
        editor.apply();
    }

    public String getProduct() {
        String CheckedVal = sharedPreference.getString("Product", "");
        return CheckedVal;
    }

    public void setLessorAmount(String Product) {

        editor = sharedPreference.edit();
        editor.putString("setLessorAmount", Product);
        editor.apply();
    }

    public String getLessorAmount() {
        String CheckedVal = sharedPreference.getString("setLessorAmount", "");
        return CheckedVal;
    }

    public void setDiscAmt(String DiscAmt) {

        editor = sharedPreference.edit();
        editor.putString("DiscAmt", DiscAmt);
        editor.apply();
    }

    public String getDiscAmt() {
        String CheckedVal = sharedPreference.getString("DiscAmt", "");
        return CheckedVal;
    }

    public void setPendingOrders(String PendingOrders) {

        editor = sharedPreference.edit();
        editor.putString("PendingOrders", PendingOrders);
        editor.apply();
    }

    public String getPendingOrders() {
        String CheckedVal = sharedPreference.getString("PendingOrders", "");
        return CheckedVal;
    }


    public String getorder_detail_id() {
        String order_detail_id = sharedPreference.getString("order_detail_id",
                "");
        return order_detail_id;
    }

    public void setorder_detail_id(String order_detail_id) {
        editor = sharedPreference.edit();
        editor.putString("order_detail_id", order_detail_id);
        editor.apply();
    }

    public String getFbTgF() {
        String FbTgF = sharedPreference.getString("FbTgF", "");
        return FbTgF;
    }

    public void setFbTgF(String FbTgF) {
        editor = sharedPreference.edit();
        editor.putString("FbTgF", FbTgF);
        editor.apply();
    }

    public String getFbSession() {
        String FbTgF = sharedPreference.getString("Session", "");
        return FbTgF;
    }

    public void setSession(String Session) {
        editor = sharedPreference.edit();
        editor.putString("Session", Session);
        editor.apply();
    }

    public String getIs_active() {
        String FbTgF = sharedPreference.getString("is_active", "");
        return FbTgF;
    }

    public void setIs_active(String is_active) {
        editor = sharedPreference.edit();
        editor.putString("is_active", is_active);
        editor.apply();
    }

    public String getSelect_product() {
        String FbTgF = sharedPreference.getString("select_product", "");
        return FbTgF;
    }

    public void setSelect_product(String select_product) {
        editor = sharedPreference.edit();
        editor.putString("select_product", select_product);
        editor.apply();
    }

    public String getStore_working_from() {
        String FbTgF = sharedPreference.getString("store_working_from", "");
        return FbTgF;
    }

    public void setStore_working_from(String store_working_from) {
        editor = sharedPreference.edit();
        editor.putString("store_working_from", store_working_from);
        editor.apply();
    }

    public String getStore_working_to() {
        String FbTgF = sharedPreference.getString("store_working_to", "");
        return FbTgF;
    }

    public void setStore_working_to(String store_working_to) {
        editor = sharedPreference.edit();
        editor.putString("store_working_to", store_working_to);
        editor.apply();
    }

    public String getbaseurl() {
        String baseurl = sharedPreference.getString("baseurl", "");
        return baseurl;
    }

    public void setuserurl(String userurl) {
        editor = sharedPreference.edit();
        editor.putString("userbaseurl", userurl);
        editor.apply();
    }

    public String setuserurl() {
        String userurl = sharedPreference.getString("userbaseurl", "");
        return userurl;
    }

    public void setbaseurl(String baseurl) {
        editor = sharedPreference.edit();
        editor.putString("baseurl", baseurl);
        editor.apply();
    }

    public String getbase_inpk_url() {
        String inpkurl = sharedPreference.getString("inpkurl", "");
        return inpkurl;
    }


    public void set_turnover(String turnover) {
        editor = sharedPreference.edit();
        editor.putString("turnover", turnover);
        editor.apply();
    }

    public String get_turnover() {
        String turnover = sharedPreference.getString("turnover", "");
        return turnover;
    }

    public void set_GSTDetail(String gst_detail) {
        editor = sharedPreference.edit();
        editor.putString("GSTDetail", gst_detail);
        editor.apply();
    }

    public String get_GSTDetail() {
        String GSTDetail = sharedPreference.getString("GSTDetail", "");
        return GSTDetail;
    }

    public void set_GSTType(String GST_Type) {
        editor = sharedPreference.edit();
        editor.putString("GST_Type", GST_Type);
        editor.apply();
    }

    public String get_GST_Type() {
        String GST_Type = sharedPreference.getString("GST_Type", "");
        return GST_Type;
    }

    public void setbase_inpk_url(String baseurl) {
        editor = sharedPreference.edit();
        editor.putString("inpkurl", baseurl);
        editor.apply();
    }

    public String getcountry() {
        String country = sharedPreference.getString("country", "");
        return country;
    }

    public void setcountry(String country) {
        editor = sharedPreference.edit();
        editor.putString("country", country);
        editor.apply();
    }

    public String getlistsoflocality() {
        String listsoflocality = sharedPreference.getString("listsoflocality",
                "");
        return listsoflocality;
    }

    public void setlistsoflocality(String listsoflocality) {
        editor = sharedPreference.edit();
        editor.putString("listsoflocality", listsoflocality);
        editor.apply();
    }

    public String getaddressloc() {
        String addressloc = sharedPreference.getString("addressloc", "");
        return addressloc;
    }

    public void setaddressloc(String addressloc) {
        editor = sharedPreference.edit();
        editor.putString("addressloc", addressloc);
        editor.apply();
    }

    public String getdatastored() {
        String datastored = sharedPreference.getString("datastored", "");
        return datastored;
    }

    public void setdatastored(String datastored) {
        editor = sharedPreference.edit();
        editor.putString("datastored", datastored);
        editor.apply();
    }

    public String getmid() {
        String mid = sharedPreference.getString("mid", "");
        return mid;
    }

    public void setmid(String mid) {
        editor = sharedPreference.edit();
        editor.putString("mid", mid);
        editor.apply();
    }

    public String gettid() {
        String tid = sharedPreference.getString("tid", "");
        return tid;
    }

    public void settid(String tid) {
        editor = sharedPreference.edit();
        editor.putString("tid", tid);
        editor.apply();
    }

    public String getpageOffset() {
        String pageOffset = sharedPreference.getString("pageOffset", "");
        return pageOffset;
    }

    public void setpageOffset(String pageOffset) {
        editor = sharedPreference.edit();
        editor.putString("pageOffset", pageOffset);
        editor.apply();
    }

    public String getisjio() {
        String isjio = sharedPreference.getString("isjio", "");
        return isjio;
    }

    public void setisjio(String isjio) {
        editor = sharedPreference.edit();
        editor.putString("isjio", isjio);
        editor.apply();
    }

    // Log.e("merchantMobileNumber", "" +merchantMobileNumber);
    // Log.e("merchantName", "" + merchantName);
    // Log.e("merchantBusinessName", "" + merchantBusinessName);
    // Log.e("tipPermission", "" + tipPermission);
    // Log.e("tipPercent", "" + tipPercent);

    public String getmerchantMobileNumber() {
        String merchantMobileNumber = sharedPreference.getString(
                "merchantMobileNumber", "");
        return merchantMobileNumber;
    }

    public void setmerchantMobileNumber(String merchantMobileNumber) {
        editor = sharedPreference.edit();
        editor.putString("merchantMobileNumber", merchantMobileNumber);
        editor.apply();
    }

    public String getmerchantName() {
        String merchantName = sharedPreference.getString("merchantName", "");
        return merchantName;
    }

    public void setmerchantName(String merchantName) {
        editor = sharedPreference.edit();
        editor.putString("merchantName", merchantName);
        editor.apply();
    }

    public String getmerchantBusinessName() {
        String merchantBusinessName = sharedPreference.getString(
                "merchantBusinessName", "");
        return merchantBusinessName;
    }

    public void setmerchantBusinessName(String merchantBusinessName) {
        editor = sharedPreference.edit();
        editor.putString("merchantBusinessName", merchantBusinessName);
        editor.apply();
    }

    public String gettipPermission() {
        String tipPermission = sharedPreference.getString("tipPermission", "");
        return tipPermission;
    }

    public void settipPermission(String tipPermission) {
        editor = sharedPreference.edit();
        editor.putString("tipPermission", tipPermission);
        editor.apply();
    }

    public String gettipPercent() {
        String tipPercent = sharedPreference.getString("tipPercent", "");
        return tipPercent;
    }

    public void settipPercent(String tipPercent) {
        editor = sharedPreference.edit();
        editor.putString("tipPercent", tipPercent);
        editor.apply();
    }

    public String getMID() {
        String MID = sharedPreference.getString("MID", "");
        return MID;
    }

    public void setMID(String MID) {
        editor = sharedPreference.edit();
        editor.putString("MID", MID);
        editor.apply();
    }

    public String getmasterOldToken() {
        String masterOldToken = sharedPreference
                .getString("masterOldToken", "");
        return masterOldToken;
    }

    public void setmasterOldToken(String masterOldToken) {
        editor = sharedPreference.edit();
        editor.putString("masterOldToken", masterOldToken);
        editor.apply();
    }

    public String getcountpendingorderpagination() {
        String countpendingorderpagination = sharedPreference.getString(
                "countpendingorderpagination", "");
        return countpendingorderpagination;
    }

    public void setcountpendingorderpagination(
            String countpendingorderpagination) {
        editor = sharedPreference.edit();
        editor.putString("countpendingorderpagination",
                countpendingorderpagination);
        editor.apply();
    }

    public String getmerchantstatus() {
        String merchantstatus = sharedPreference
                .getString("merchantstatus", "");
        return merchantstatus;
    }

    public void setmerchantstatus(String merchantstatus) {
        editor = sharedPreference.edit();
        editor.putString("merchantstatus", merchantstatus);
        editor.apply();
    }

    public String getsuperMerchantId() {
        String superMerchantId = sharedPreference.getString("superMerchantId",
                "");
        return superMerchantId;
    }

    public void setsuperMerchantId(String superMerchantId) {
        editor = sharedPreference.edit();
        editor.putString("superMerchantId", superMerchantId);
        editor.apply();
    }

    public String getpaymentcarddeviceId() {
        String paymentcarddeviceId = sharedPreference.getString(
                "paymentcarddeviceId", "");
        return paymentcarddeviceId;
    }

    public void setpaymentcarddeviceId(String paymentcarddeviceId) {
        editor = sharedPreference.edit();
        editor.putString("paymentcarddeviceId", paymentcarddeviceId);
        editor.apply();
    }

    public void setIsseentermsandconditions(String Isseentermsandconditions) {

        editor = sharedPreference.edit();
        editor.putString("Isseentermsandconditions", Isseentermsandconditions);
        editor.apply();
    }

    public String getIsseentermsandconditions() {
        String Isseentermsandconditions = sharedPreference.getString(
                "Isseentermsandconditions", "");
        return Isseentermsandconditions;
    }

    public void setstorelatitudegloballogin(String storelatitudegloballogin) {

        editor = sharedPreference.edit();
        editor.putString("storelatitudegloballogin", storelatitudegloballogin);
        editor.apply();
    }

    public String getstorelatitudegloballogin() {
        String storelatitudegloballogin = sharedPreference.getString(
                "storelatitudegloballogin", "");
        return storelatitudegloballogin;
    }

    public void setstorelongitudegloballogin(String storelongitudegloballogin) {

        editor = sharedPreference.edit();
        editor.putString("storelongitudegloballogin", storelongitudegloballogin);
        editor.apply();
    }

    public String getstorelongitudegloballogin() {
        String storelongitudegloballogin = sharedPreference.getString(
                "storelongitudegloballogin", "");
        return storelongitudegloballogin;
    }

    public void setreportasunavailable(String reportasunavailable) {

        editor = sharedPreference.edit();
        editor.putString("reportasunavailable", reportasunavailable);
        editor.apply();
    }

    public String getreportasunavailable() {
        String reportasunavailable = sharedPreference.getString(
                "reportasunavailable", "");
        return reportasunavailable;
    }

    public void setorderidrecentorders(String orderidrecentorders) {

        editor = sharedPreference.edit();
        editor.putString("orderidrecentorders", orderidrecentorders);
        editor.apply();
    }

    public String getorderidrecentorders() {
        String orderidrecentorders = sharedPreference.getString(
                "orderidrecentorders", "");
        return orderidrecentorders;
    }

    public void setcategory_name(String category_name) {

        editor = sharedPreference.edit();
        editor.putString("category_name", category_name);
        editor.apply();
    }

    public String getcategory_name() {
        String category_name = sharedPreference.getString("category_name", "");
        return category_name;
    }

    public void setDongleId(String DongleId) {

        editor = sharedPreference.edit();
        editor.putString("DongleId", DongleId);
        editor.apply();
    }

    public String getDongleId() {
        String DongleId = sharedPreference.getString("DongleId", "");
        return DongleId;
    }

    public void setdealerSubId(String dealerSubId) {

        editor = sharedPreference.edit();
        editor.putString("dealerSubId", dealerSubId);
        editor.apply();
    }

    public String getdealerSubId() {
        String dealerSubId = sharedPreference.getString("dealerSubId", "");
        return dealerSubId;
    }

    public void setmccCode(String mccCode) {

        editor = sharedPreference.edit();
        editor.putString("mccCode", mccCode);
        editor.apply();
    }

    public String getmccCode() {
        String mccCode = sharedPreference.getString("mccCode", "");
        return mccCode;
    }

}
